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
import org.sonar.check.Priority;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = InterfaceNameCheck.KEY,
        name = "Interface names should end with 'Interface' suffix",
        description = "Interfaces should have names that end with the 'Interface' suffix to clearly identify their purpose.",
        priority = Priority.MINOR,
        tags = {"convention", "magento2"}
)
public class InterfaceNameCheck extends PHPVisitorCheck {

    public static final String KEY = "InterfaceName";
    private static final String MESSAGE = "Interface should have name that ends with \"Interface\" suffix.";
    private static final String INTERFACE_SUFFIX = "Interface";

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        if (tree.is(Tree.Kind.INTERFACE_DECLARATION)) {
            String interfaceName = tree.name().text();

            if (!interfaceName.endsWith(INTERFACE_SUFFIX)) {
                context().newIssue(this, tree.name(), MESSAGE);
            }
        }

        super.visitClassDeclaration(tree);
    }
}