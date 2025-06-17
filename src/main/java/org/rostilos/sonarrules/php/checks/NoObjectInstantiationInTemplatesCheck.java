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
import org.sonar.plugins.php.api.tree.expression.NewExpressionTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = NoObjectInstantiationInTemplatesCheck.KEY,
        name = NoObjectInstantiationInTemplatesCheck.MESSAGE,
        description = "Templates must not instantiate objects. All objects must be passed from the Block objects.",
        priority = Priority.CRITICAL,
        tags = {"magento2", "performance", "convention", "security"}
)

public class NoObjectInstantiationInTemplatesCheck extends PHPVisitorCheck {

    public static final String KEY = "NoObjectInstantiationInTemplates";
    public static final String MESSAGE = "Templates must not instantiate objects.";

    @Override
    public void visitNewExpression(NewExpressionTree tree) {
        if (context().getPhpFile().filename().endsWith(".phtml")) {
            context().newIssue(this, tree, MESSAGE);
        }
        super.visitNewExpression(tree);
    }
}