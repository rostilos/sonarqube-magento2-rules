package org.perspectiveteam.sonarrules.php.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.ScriptTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.statement.DeclareStatementTree;
import org.sonar.plugins.php.api.tree.statement.StatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.List;

@Rule(
        key = StrictTypesDeclarationCheck.KEY,
        name = StrictTypesDeclarationCheck.MESSAGE,
        description = "All new PHP files must start with 'declare(strict_types=1);' to enforce strict type checking. All updated files SHOULD include it.",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention", "php7", "type-safety", "quality"}
)

public class StrictTypesDeclarationCheck extends PHPVisitorCheck {

    public static final String KEY = "M1.3.1";
    public static final String MESSAGE = "PHP files must declare strict types.";

    @Override
    public void visitScript(ScriptTree tree) {
        List<StatementTree> statements = tree.statements();

        if (!statements.isEmpty()) {
            Tree firstStatement = statements.get(0);

            if (firstStatement instanceof DeclareStatementTree) {
                String code = firstStatement.toString();

                if (!code.contains("declare(strict_types=1);")) {
                    context().newLineIssue(this, getLineToReport(tree), MESSAGE);
                }
            } else {
                context().newLineIssue(this, getLineToReport(tree), MESSAGE);
            }
        } else {
            context().newLineIssue(this, getLineToReport(tree), MESSAGE);
        }

        super.visitScript(tree);
    }

    private static int getLineToReport(ScriptTree tree) {
        return tree.fileOpeningTagToken().endLine();
    }
}