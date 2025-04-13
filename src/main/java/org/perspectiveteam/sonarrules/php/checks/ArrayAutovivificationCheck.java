package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.check.Priority;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.ArrayAccessTree;
import org.sonar.plugins.php.api.tree.expression.AssignmentExpressionTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Rule(
        key = ArrayAutovivificationCheck.KEY,
        name = "Automatic conversion of false to array is deprecated",
        description = "PHP 8.1+ has deprecated the automatic conversion of false to array. This can lead to unexpected behavior.",
        priority = Priority.MAJOR,
        tags = {"php8", "compatibility", "deprecated", "magento2"}
)
public class ArrayAutovivificationCheck extends PHPVisitorCheck {

    public static final String KEY = "ArrayAutovivification";
    private static final String MESSAGE = "Deprecated: Automatic conversion of false to array is deprecated.";

    private final Map<String, Set<String>> falseVariables = new HashMap<>();
    private String currentFunction = "global";

    @Override
    public void visitExpressionStatement(ExpressionStatementTree tree) {
        if (tree.expression().is(Tree.Kind.ASSIGNMENT)) {
            AssignmentExpressionTree assignment = (AssignmentExpressionTree) tree.expression();

            if (assignment.variable().is(Tree.Kind.VARIABLE_IDENTIFIER) &&
                    isFalseLiteral(assignment.value())) {
                VariableIdentifierTree variable = (VariableIdentifierTree) assignment.variable();
                String variableName = variable.text();

                falseVariables
                        .computeIfAbsent(currentFunction, k -> new HashSet<>())
                        .add(variableName);
            }
        }

        super.visitExpressionStatement(tree);
    }

    @Override
    public void visitArrayAccess(ArrayAccessTree tree) {
        if (tree.object().is(Tree.Kind.VARIABLE_IDENTIFIER)) {
            VariableIdentifierTree variable = (VariableIdentifierTree) tree.object();
            String variableName = variable.text();

            if (falseVariables.getOrDefault(currentFunction, Collections.emptySet()).contains(variableName)) {
                context().newIssue(this, tree, MESSAGE);
            }
        }

        super.visitArrayAccess(tree);
    }

    @Override
    public void visitFunctionExpression(org.sonar.plugins.php.api.tree.expression.FunctionExpressionTree tree) {
        String previousFunction = currentFunction;
        currentFunction = "anonymous_" + tree.functionToken().line();

        super.visitFunctionExpression(tree);

        currentFunction = previousFunction;
    }

    @Override
    public void visitMethodDeclaration(org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree tree) {
        String previousFunction = currentFunction;
        currentFunction = tree.name().text();

        super.visitMethodDeclaration(tree);

        currentFunction = previousFunction;
    }

    @Override
    public void visitFunctionDeclaration(org.sonar.plugins.php.api.tree.declaration.FunctionDeclarationTree tree) {
        String previousFunction = currentFunction;
        currentFunction = tree.name().text();

        super.visitFunctionDeclaration(tree);

        currentFunction = previousFunction;
    }

    private boolean isFalseLiteral(ExpressionTree tree) {
        if (tree.is(Tree.Kind.BOOLEAN_LITERAL)) {
            LiteralTree literal = (LiteralTree) tree;
            return "false".equals(literal.value());
        }
        return false;
    }
}
