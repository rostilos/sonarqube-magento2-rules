package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.statement.*;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.perspectiveteam.sonarrules.php.utils.CheckUtils;

import java.util.List;

@Rule(
        key = ConstructorDependencyCheck.KEY,
        name = ConstructorDependencyCheck.MESSAGE,
        description = "Class constructor must only contain dependency assignment operations and/or argument validation operations. Complex logic should be moved to separate methods.",
        priority = Priority.MAJOR,
        tags = {"magento2", "clean-code", "design"}
)

public class ConstructorDependencyCheck extends PHPVisitorCheck {
    public static final String KEY = "ConstructorDependency";
    public static final String MESSAGE = "Constructor limited to dependency assignment and validation.";

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        if (CheckUtils.isConstructorMethodPromotion(tree)) {
            if (tree.body().is(Tree.Kind.BLOCK)) {
                List<StatementTree> statements = ((BlockTree) tree.body()).statements();
                statements.forEach(statement -> {
                    if (!isAllowedStatement(statement)) {
                        context().newIssue(this, statement, MESSAGE);
                    }
                });
            }
        }
        super.visitMethodDeclaration(tree);
    }

    /**
     * Only assignments at MemberAccess and Throw level are allowed ( as a result of argument validation )
     */
    private boolean isAllowedStatement(Tree statement) {
        if (statement instanceof ForEachStatementTree) {
            List<StatementTree> childStatements = ((ForEachStatementTree) statement).statements();
            return isNestedStatementsValid(childStatements);
        }

        if (statement instanceof IfStatementTree) {
            List<StatementTree> childStatements = ((IfStatementTree) statement).statements();
            return isNestedStatementsValid(childStatements);
        }

        if (statement instanceof ExpressionStatementTree) {
            // Allow `$this->dependency = $dependency;`
            ExpressionStatementTree expressionStatement = (ExpressionStatementTree) statement;
            if (expressionStatement.expression() instanceof AssignmentExpressionTree) {
                AssignmentExpressionTree assignment = (AssignmentExpressionTree) expressionStatement.expression();
                return assignment.variable() instanceof MemberAccessTree;
            }

            // Allow parent::__construct()
            if (expressionStatement.expression() instanceof FunctionCallTree) {
                FunctionCallTree functionCall = (FunctionCallTree) expressionStatement.expression();
                return isParent(functionCall);
            }
        }
        // Allow Throw Statements ( as part of argument validation )
        return statement instanceof ThrowStatementTree;
    }

    /**
     * Check for nested statements
     * ( for example, if the composite argument loop contains validity checks on the argument ) and throw an exception
     */
    private boolean isNestedStatementsValid(List<StatementTree> statements) {
        for (StatementTree statement : statements) {
            if (statement.is(Tree.Kind.BLOCK)) {
                List<StatementTree> nestedStatements = ((BlockTree) statement).statements();
                return isNestedStatementsValid(nestedStatements);
            } else {
                return isAllowedStatement(statement);
            }
        }
        return true;
    }

    private static boolean isParent(FunctionCallTree functionCall) {
        if (functionCall.callee() instanceof MemberAccessTree) {
            MemberAccessTree callee = (MemberAccessTree) functionCall.callee();
            return callee.object().toString().equals("parent");
        }
        return false;
    }

}
