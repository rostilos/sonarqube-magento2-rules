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
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;
import org.sonar.plugins.php.api.tree.statement.InlineHTMLTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.regex.Pattern;

@Rule(
        key = PhtmlTemplateCheck.KEY,
        name = "Phtml templates should follow best practices",
        description = "Template files should not access protected/private Block members and should not use deprecated 'text/javascript' type attribute.",
        priority = org.sonar.check.Priority.MINOR,
        tags = {"obsolete", "magento2", "template"}
)
public class PhtmlTemplateCheck extends PHPVisitorCheck {

    public static final String KEY = "PhtmlTemplate";
    private static final String BLOCK_VARIABLE = "$block";
    private static final Pattern TEXT_JAVASCRIPT_PATTERN =
            Pattern.compile("type=[\"']text/javascript[\"']", Pattern.CASE_INSENSITIVE);


    @Override
    public void visitMemberAccess(MemberAccessTree tree) {
        if (isIncludedFile()) {
            checkBlockVariableAccess(tree);
        }
        super.visitMemberAccess(tree);
    }

    @Override
    public void visitInlineHTML(InlineHTMLTree tree) {
        super.visitInlineHTML(tree);
        if (isIncludedFile()) {
            String htmlContent = tree.inlineHTMLToken().text();
            String[] lines = htmlContent.split("\n");
            int startLine = tree.inlineHTMLToken().line();
            int lineCounter = 0;
            for (String line : lines) {
                if (TEXT_JAVASCRIPT_PATTERN.matcher(line).find()) {
                    context().newLineIssue(this, startLine + lineCounter, "Please do not use \"text/javascript\" type attribute.");
                }
                lineCounter++;
            }
        }
    }

    private void checkBlockVariableAccess(MemberAccessTree tree) {
        if (tree.object().is(Tree.Kind.VARIABLE_IDENTIFIER)) {
            VariableIdentifierTree variable = (VariableIdentifierTree) tree.object();

            if (BLOCK_VARIABLE.equals(variable.variableExpression().text())) {
                String memberName = tree.member().toString();

                if (memberName.startsWith("_")) {
                    context().newIssue(this, tree,
                            "Access to protected and private members of Block class is obsolete in phtml templates. Use only public members.");
                }
            }
        }
    }

    private boolean isIncludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}