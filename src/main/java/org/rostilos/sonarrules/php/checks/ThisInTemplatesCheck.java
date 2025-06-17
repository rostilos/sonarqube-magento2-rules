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
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.expression.VariableIdentifierTree;

import java.util.Arrays;
import java.util.List;

@Rule(
        key = ThisInTemplatesCheck.KEY,
        name = "The use of $this in templates is forbidden. Using $helper is discouraged.",
        description = "The use of $this in templates is forbidden. Using $helper is discouraged.",
        priority = Priority.MAJOR,
        tags = {"magento2", "convention"}
)

public class ThisInTemplatesCheck extends PHPVisitorCheck {
    public static final String KEY = "ThisInTemplate";
    private static final List<String> DISCOURAGED_HELPER_VARS = Arrays.asList("$helper", "$_helper");


    @Override
    public void visitVariableIdentifier(VariableIdentifierTree tree) {
        super.visitVariableIdentifier(tree);

        String variableName = tree.variableExpression().text();

        if (!isIncludedFile()) {
            return;
        }
        if ("$this".equals(variableName)) {
            context().newIssue(this, tree, "The use of $this in templates is deprecated. Use $block instead.");
        } else if (DISCOURAGED_HELPER_VARS.contains(variableName)) {
            context().newIssue(this, tree, "The use of helpers in templates is discouraged. Use ViewModel instead.");
        }

    }

    private boolean isIncludedFile() {
        return context().getPhpFile().filename().endsWith(".phtml");
    }
}