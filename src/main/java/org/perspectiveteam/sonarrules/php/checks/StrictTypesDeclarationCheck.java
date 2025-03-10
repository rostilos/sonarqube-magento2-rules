package org.perspectiveteam.sonarrules.php.checks;

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
        tags = {"convention"}
)

public class StrictTypesDeclarationCheck extends PHPVisitorCheck {

    public static final String KEY = "M1.3.1";
    public static final String MESSAGE = "All new PHP files MUST start with 'declare(strict_types=1);'. All updated files SHOULD include it.";

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

    /**
     * Return line on which the issue should be reported.
     * <p/>
     * The node contains everything before the first opening include HTML if present
     * this allows to ensure reporting the issue on the correct line.
     */
    private static int getLineToReport(ScriptTree tree) {
        return tree.fileOpeningTagToken().endLine();
    }
}