package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.FunctionDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ParameterTree;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;
import org.sonar.plugins.php.api.tree.statement.ForEachStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Rule(
        key = org.perspectiveteam.sonarrules.php.checks.FunctionArgumentsShouldNotBeModifiedCheck.KEY,
        name = org.perspectiveteam.sonarrules.php.checks.FunctionArgumentsShouldNotBeModifiedCheck.MESSAGE,
        description = "Function arguments should not be modified within the function body. Use local variables instead.",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention", "pitfall", "maintainability"}
)
public class FunctionArgumentsShouldNotBeModifiedCheck extends PHPVisitorCheck {

    public static final String KEY = "M1.1";
    public static final String MESSAGE = "Function arguments SHOULD NOT be modified.";

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
            if (tree.variable().is(Tree.Kind.VARIABLE_IDENTIFIER)) {
                VariableIdentifierTree variable = (VariableIdentifierTree) tree.variable();
                String variableName = variable.text();

                if (!variableName.isEmpty() && parameterNames.contains(variableName)) {
                    issues.add(new IssueInfo(tree,
                            "Function argument \"" + variableName + "\" should not be modified. Use a local variable instead."));

                }
            }
            super.visitAssignmentExpression(tree);
        }

        @Override
        public void visitForEachStatement(ForEachStatementTree tree) {
            if (tree.value() != null && tree.value().is(Tree.Kind.REFERENCE_VARIABLE)) {
                if (tree.value().is(Tree.Kind.VARIABLE_IDENTIFIER)) {
                    VariableIdentifierTree variable = (VariableIdentifierTree) tree.value();
                    String variableName = variable.text();

                    if (parameterNames.contains(variableName)) {
                        issues.add(new IssueInfo(tree,
                                "Function argument \"" + variableName + "\" should not be modified through foreach reference. Use a local variable instead."));
                    }
                }
            }
            super.visitForEachStatement(tree);
        }
    }
}



