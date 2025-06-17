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
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = FinalImplementationCheck.KEY,
        name = "Final keyword should not be used in Magento code",
        description = "Final keyword is prohibited in Magento. It decreases extensibility and is not compatible with plugins and proxies.",
        tags = {"convention", "magento2", "extensibility"}
)
public class FinalImplementationCheck extends PHPVisitorCheck {

    public static final String KEY = "FinalImplementation";
    private static final String MESSAGE = "Final keyword is prohibited in Magento.";

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        for (SyntaxToken modifier : tree.modifiersToken()) {
            if ("final".equals(modifier.text())) {
                context().newIssue(this, modifier, MESSAGE);
            }
        }

        super.visitClassDeclaration(tree);
    }

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        for (SyntaxToken modifier : tree.modifiers()) {
            if ("final".equals(modifier.text())) {
                context().newIssue(this, modifier, MESSAGE);
            }
        }

        super.visitMethodDeclaration(tree);
    }
}