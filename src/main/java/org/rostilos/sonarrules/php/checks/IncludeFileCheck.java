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

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.CallArgumentTree;
import org.sonar.php.api.PHPKeyword;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.tree.expression.BinaryExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

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

        if (INCLUDE_FUNCTIONS.contains(functionName) && !tree.callArguments().isEmpty()) {
            CallArgumentTree argument = tree.callArguments().get(0);
            StringBuilder message = new StringBuilder(MESSAGE_BASE);

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
