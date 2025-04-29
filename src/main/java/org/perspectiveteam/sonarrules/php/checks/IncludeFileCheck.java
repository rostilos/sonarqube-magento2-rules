package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.CallArgumentTree;
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.php.api.PHPKeyword;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Rule(
        key = IncludeFileCheck.KEY,
        name = "Include and require statements should be used properly",
        description = "Detects possible improper usage of include and require functions",
        priority = org.sonar.check.Priority.CRITICAL,
        tags = {"security", "magento2"}
)
public class IncludeFileCheck extends PHPVisitorCheck {
    public static final String KEY = "IncludeFile";
    private static final String MESSAGE_BASE = "Include statement detected. File manipulations are discouraged.";
    private static final Pattern URL_PATTERN = Pattern.compile("(https?|ftp)://.*", Pattern.CASE_INSENSITIVE);
    private static final Set<String> INCLUDE_FUNCTIONS = Set.of(
        PHPKeyword.INCLUDE_ONCE.getValue(),
        PHPKeyword.INCLUDE.getValue(),
        PHPKeyword.REQUIRE.getValue(),
        PHPKeyword.REQUIRE_ONCE.getValue()
    );

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        super.visitFunctionCall(tree);
        String functionName = tree.callee().toString().toLowerCase();

        if (INCLUDE_FUNCTIONS.contains(functionName) && !tree.arguments().isEmpty()) {
            CallArgumentTree argument = tree.callArguments().get(0);
            StringBuilder message = new StringBuilder(MESSAGE_BASE);

            //TODO: tweak this additional messages
            if (argument.is(Tree.Kind.REGULAR_STRING_LITERAL)) {
                String includePath = ((LiteralTree) argument).value().replaceAll("^[\"']|[\"']$", "");
                if (URL_PATTERN.matcher(includePath).matches()) {
                    message.append(" Passing URLs is forbidden.");
                }

                if (isControllerInPath(includePath) && isInControllerClass()) {
                    return;
                }
            }

            if (argument.is(Tree.Kind.CONCATENATION)) {
                message.append(" Concatenating is forbidden.");
            }

            if (hasVariable(argument)) {
                message.append(" Variables inside are insecure.");
            }

            context().newIssue(this, tree, message.toString());
        }
    }

    private boolean hasVariable(Tree expression) {
        if (expression.is(Tree.Kind.VARIABLE_IDENTIFIER)) {
            return true;
        } else if (expression.is(Tree.Kind.CONCATENATION)) {
            BinaryExpressionTree binaryExpression = (BinaryExpressionTree) expression;
            return hasVariable(binaryExpression.leftOperand()) || hasVariable(binaryExpression.rightOperand());
        }
        return false;
    }

    private boolean isControllerInPath(String path) {
        return path.toLowerCase().contains("controller");
    }

    private boolean isInControllerClass() {
        return context().getPhpFile().filename().toLowerCase().contains("controller");
    }
}
