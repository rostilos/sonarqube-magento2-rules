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
import org.sonar.plugins.php.api.tree.expression.ExpressionTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.statement.ExpressionStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = ShortEchoSyntaxCheck.KEY,
        name = "Short echo tag syntax must be used.",
        description = "Magento2 recommends using the short echo syntax in .phtml files",
        priority = Priority.MINOR,
        tags = {"magento2", "best-practices"}
)

public class ShortEchoSyntaxCheck extends PHPVisitorCheck {
    public static final String KEY = "ShortEchoSyntax";
    public static final String MESSAGE = "Short echo tag syntax must be used; expected \"<?=\" but found \"<?php echo\"";

    @Override
    public void visitExpressionStatement(ExpressionStatementTree tree) {
        super.visitExpressionStatement(tree);
        if (!isIncludedFile() || !tree.expression().is(Tree.Kind.FUNCTION_CALL)) {
            return;
        }
        if (tree.getParent() == null || tree.getParent().getKind() != Tree.Kind.SCRIPT) {
            return;
        }

        ScriptTree parent = (ScriptTree) tree.getParent();
        ExpressionTree callee = ((FunctionCallTree) tree.expression()).callee();
        String calleeName = callee.toString();
        boolean isOneLineEcho = isOneLineEcho(parent, calleeName);

        if (isOneLineEcho) {
            context().newIssue(this, tree, MESSAGE);
        }
    }

    private boolean isOneLineEcho(ScriptTree parent, String calleeName) {
        return calleeName.equals("echo") && parent.fileOpeningTagToken().text().equals("<?php");
    }

    private boolean isIncludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}
