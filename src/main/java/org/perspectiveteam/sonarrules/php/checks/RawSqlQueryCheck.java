package org.perspectiveteam.sonarrules.php.checks;

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
        checkForRawSql(tree);
        super.visitLiteral(tree);
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpressionTree tree) {
        if (tree.value().is(Tree.Kind.REGULAR_STRING_LITERAL,
                Tree.Kind.EXPANDABLE_STRING_LITERAL,
                Tree.Kind.HEREDOC_LITERAL)) {
            checkForRawSql(tree.value());
        }
        super.visitAssignmentExpression(tree);
    }

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        if (tree.callee().is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            String functionName = ((MemberAccessTree) tree.callee()).member().toString();

            if (QUERY_FUNCTIONS.contains(functionName.toLowerCase())) {
                tree.arguments().forEach(argument -> {
                    if (argument.is(Tree.Kind.REGULAR_STRING_LITERAL,
                            Tree.Kind.EXPANDABLE_STRING_LITERAL,
                            Tree.Kind.HEREDOC_LITERAL)) {
                        checkForRawSql(argument);
                    }
                });
            }
        }


        super.visitFunctionCall(tree);
    }

    private void checkForRawSql(ExpressionTree tree) {
        String value = tree.toString();
        value = value.replaceAll("^['\"]|['\"]$", "");
        String sqlPattern = "^(" + String.join("|", SQL_STATEMENTS) + ")\\s";

        if (Pattern.compile(sqlPattern, Pattern.CASE_INSENSITIVE).matcher(value.trim()).find()) {
            context().newIssue(this, tree, String.format(MESSAGE, value.trim()));
        }
    }
}