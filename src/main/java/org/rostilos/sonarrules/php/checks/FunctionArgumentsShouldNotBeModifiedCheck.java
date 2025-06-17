/*
 * Copyright (C) 2025 Rostislav Suleimanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rostilos.sonarrules.php.checks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.FunctionDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ParameterTree;
import org.sonar.plugins.php.api.tree.expression.ArrayAccessTree;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;
import org.sonar.plugins.php.api.tree.statement.ForEachStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = FunctionArgumentsShouldNotBeModifiedCheck.KEY,
        name = FunctionArgumentsShouldNotBeModifiedCheck.MESSAGE,
        description = "Function arguments should not be modified within the function body. Use local variables instead.",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention", "pitfall", "maintainability"}
)
public class FunctionArgumentsShouldNotBeModifiedCheck extends PHPVisitorCheck {

    public static final String KEY = "FunctionArgumentsShouldNotBeModified";
    public static final String MESSAGE = "Function arguments should not be modified.";

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {

        checkFunctionParameters(tree.parameters().parameters(), tree.body());

        super.visitMethodDeclaration(tree);
    }

    @Override
    public void visitFunctionDeclaration(FunctionDeclarationTree tree) {
        checkFunctionParameters(tree.parameters().parameters(), tree.body());
        super.visitFunctionDeclaration(tree);
    }

    private void checkFunctionParameters(Iterable<ParameterTree> parameters, Tree functionBody) {
        Set<String> parameterNames = new HashSet<>();
        for (ParameterTree parameter : parameters) {
            parameterNames.add(parameter.variableIdentifier().text());
        }

        if (parameterNames.isEmpty()) {
            return;
        }

        ParameterModificationCollector collector = new ParameterModificationCollector(parameterNames);
        functionBody.accept(collector);
        for (IssueInfo issue : collector.getIssues()) {
            context().newIssue(this, issue.tree, issue.message);
        }
    }

    private static class IssueInfo {
        final Tree tree;
        final String message;

        IssueInfo(Tree tree, String message) {
            this.tree = tree;
            this.message = message;
        }
    }

    private static class ParameterModificationCollector extends PHPVisitorCheck {
        private final Set<String> parameterNames;
        private final List<IssueInfo> issues = new ArrayList<>();

        ParameterModificationCollector(Set<String> parameterNames) {
            this.parameterNames = parameterNames;
        }

        List<IssueInfo> getIssues() {
            return issues;
        }

        @Override
        public void visitAssignmentExpression(AssignmentExpressionTree tree) {
            String variableName = "";

            switch (tree.variable().getKind()) {
                case VARIABLE_IDENTIFIER:
                    VariableIdentifierTree variable = (VariableIdentifierTree) tree.variable();
                    variableName = variable.text();
                    break;
                case ARRAY_ACCESS:
                    ArrayAccessTree arrayAccessVariable = (ArrayAccessTree) tree.variable();
                    if (arrayAccessVariable.object().is(Tree.Kind.VARIABLE_IDENTIFIER)) {
                        variableName = ((VariableIdentifierTree) arrayAccessVariable.object()).text();
                    }
                    break;
                default:
                    break;
            }
            if (!variableName.isEmpty() && parameterNames.contains(variableName)) {
                issues.add(new IssueInfo(tree,
                        "Function argument \"" + variableName + "\" should not be modified. Use a local variable instead."));

            }
            super.visitAssignmentExpression(tree);
        }

        @Override
        public void visitForEachStatement(ForEachStatementTree tree) {
            if (tree.value() != null && tree.value().is(Tree.Kind.REFERENCE_VARIABLE)) {
                String iterableVariableName = tree.expression().toString();
                if (parameterNames.contains(iterableVariableName)) {
                    issues.add(new IssueInfo(tree,
                            "Potential modification of the function argument by reference variable. Use a local variable instead."));
                }
            }
            super.visitForEachStatement(tree);
        }
    }
}



