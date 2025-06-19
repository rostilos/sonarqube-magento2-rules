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

    public static final String KEY = "StrictTypesDeclaration";
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