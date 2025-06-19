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

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.*;
import org.sonar.plugins.php.api.tree.expression.*;
import org.sonar.plugins.php.api.tree.statement.BlockTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.tree.statement.StatementTree;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;

import java.util.Collections;
import java.util.List;

import org.rostilos.sonarrules.php.utils.CheckUtils;

@Rule(
        key = StatelessPluginCheck.KEY,
        name = "Plugins must be stateless",
        description = "Plugins in Magento 2 should not have properties with state as they can be instantiated multiple times during a request",
        priority = Priority.CRITICAL,
        tags = {"magento2", "bug"}
)
public class StatelessPluginCheck extends PHPSubscriptionCheck {
    public static final String KEY = "StatelessPlugin";
    private static final String MESSAGE = "Plugins must be stateless. Found potential stateful behavior: %s";

    private static final Tree.Kind[] INCREMENT_DECREMENT = {
            Tree.Kind.PREFIX_INCREMENT,
            Tree.Kind.PREFIX_DECREMENT,
            Tree.Kind.POSTFIX_INCREMENT,
            Tree.Kind.POSTFIX_DECREMENT
    };

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.CLASS_DECLARATION);
    }

    @Override
    public void visitNode(Tree tree) {
        ClassDeclarationTree classTree = (ClassDeclarationTree) tree;

        if (!CheckUtils.isPluginClass(classTree)) {
            return;
        }

        checkForStaticProperties(classTree);
        checkForPropertyModifications(classTree);
    }

    private void checkForStaticProperties(ClassDeclarationTree classTree) {
        classTree.members().stream()
                .filter(member -> member.is(Tree.Kind.CLASS_PROPERTY_DECLARATION))
                .map(ClassPropertyDeclarationTree.class::cast)
                .filter(property -> property.modifierTokens().stream()
                        .anyMatch(modifier -> modifier.text().equals("static")))
                .forEach(property ->
                        property.declarations().forEach(declaration -> {
                            String message = String.format(MESSAGE, "static property utilization");
                            context().newIssue(this, declaration, message);
                        })
                );
    }

    private void checkForPropertyModifications(ClassDeclarationTree classTree) {
        classTree.members().stream()
                .filter(member -> member.is(Tree.Kind.METHOD_DECLARATION))
                .map(MethodDeclarationTree.class::cast)
                .filter(method -> !CheckUtils.isConstructorMethodPromotion(method))
                .forEach(method -> {
                    if (method.body().is(Tree.Kind.BLOCK)) {
                        BlockTree methodBody = (BlockTree) method.body();
                        methodBody.statements().forEach(this::checkContainsPropertyModification);
                    }
                });
    }

    private void checkContainsPropertyModification(StatementTree statement) {
        if (!statement.is(Tree.Kind.EXPRESSION_STATEMENT)) {
            return;
        }

        ExpressionTree expression = ((ExpressionStatementTree) statement).expression();

        if (isIncrementDecrementOnThisProperty(expression)) {
            String message = String.format(MESSAGE, "object property modification");
            context().newIssue(this, expression, message);
            return;
        }

        if (isAssignmentToObjectProperty(expression)) {
            String message = String.format(MESSAGE, "object property modification");
            context().newIssue(this, expression, message);
        }
    }

    private boolean isIncrementDecrementOnThisProperty(ExpressionTree expression) {
        if (!isIncrementOrDecrement(expression)) {
            return false;
        }

        UnaryExpressionTree unaryExpression = (UnaryExpressionTree) expression;
        if (!unaryExpression.expression().is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            return false;
        }

        MemberAccessTree memberAccess = (MemberAccessTree) unaryExpression.expression();
        return memberAccess.object().toString().equals("$this");
    }

    private boolean isIncrementOrDecrement(Tree tree) {
        for (Tree.Kind kind : INCREMENT_DECREMENT) {
            if (tree.is(kind)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAssignmentToObjectProperty(ExpressionTree expression) {
        if (!expression.is(Tree.Kind.ASSIGNMENT)) {
            return false;
        }

        AssignmentExpressionTree assignment = (AssignmentExpressionTree) expression;
        ExpressionTree target = assignment.variable();

        if (!target.is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            return false;
        }

        MemberAccessTree memberAccess = (MemberAccessTree) target;
        return memberAccess.object().is(Tree.Kind.VARIABLE_IDENTIFIER);
    }
}