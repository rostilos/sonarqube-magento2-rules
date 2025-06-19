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
import org.sonar.plugins.php.api.tree.expression.*;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.Tree;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Rule(
        key = "RawSqlQuery",
        name = "Raw SQL queries should not be used",
        description = "Using raw SQL queries is a security risk and should be avoided.",
        priority = org.sonar.check.Priority.MAJOR,
        tags = {"security", "sql-injection", "magento2"}
)
public class RawSqlQueryCheck extends PHPVisitorCheck {
    public static final String KEY = "RawSqlQuery";
    private static final String MESSAGE = "Possible raw SQL statement %s detected.";

    private static final List<String> SQL_STATEMENTS = Arrays.asList(
            "SELECT", "UPDATE", "INSERT", "CREATE", "DELETE", "ALTER", "DROP", "TRUNCATE"
    );

    private static final List<String> QUERY_FUNCTIONS = Arrays.asList(
            "query"
    );

    @Override
    public void visitLiteral(LiteralTree tree) {
        super.visitLiteral(tree);
        if(isExcludedFile()){
            return;
        }
        checkForRawSql(tree, true);
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpressionTree tree) {
        super.visitAssignmentExpression(tree);
        if(isExcludedFile()){
            return;
        }
        if (tree.value().is(Tree.Kind.REGULAR_STRING_LITERAL,
                Tree.Kind.EXPANDABLE_STRING_LITERAL,
                Tree.Kind.HEREDOC_LITERAL)) {
            checkForRawSql(tree.value(), true);
        }
    }

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        super.visitFunctionCall(tree);
        if(isExcludedFile()){
            return;
        }
        if (tree.callee().is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            String functionName = ((MemberAccessTree) tree.callee()).member().toString();

            if (QUERY_FUNCTIONS.contains(functionName.toLowerCase())) {
                tree.arguments().forEach(argument -> {
                    if (argument.is(Tree.Kind.REGULAR_STRING_LITERAL,
                            Tree.Kind.EXPANDABLE_STRING_LITERAL,
                            Tree.Kind.HEREDOC_LITERAL)) {
                        checkForRawSql(argument, false);
                    }
                });
            }
        }
    }

    private void checkForRawSql(ExpressionTree tree, boolean caseSensitive) {
        String value = tree.toString();
        value = value.replaceAll("(^['\"])|(['\"]$)", "");
        String sqlPattern = "^(" + String.join("|", SQL_STATEMENTS) + ")\\s";

        if(caseSensitive){
            if (Pattern.compile(sqlPattern).matcher(value.trim()).find()) {
                context().newIssue(this, tree, String.format(MESSAGE, value.trim()));
            }
        }else{
            if (Pattern.compile(sqlPattern, Pattern.CASE_INSENSITIVE).matcher(value.trim()).find()) {
                context().newIssue(this, tree, String.format(MESSAGE, value.trim()));
            }
        }
    }

    private boolean isExcludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}