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
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = AbstractBlockCheck.KEY,
        name = "Redundant parameters in Block methods should not be used",
        description = "The 3rd parameter for getChildHtml() and 4th parameter for getChildChildHtml() methods are not needed anymore.",
        priority = org.sonar.check.Priority.MAJOR,
        tags = {"obsolete", "magento2"}
)
public class AbstractBlockCheck extends PHPVisitorCheck {

    public static final String KEY = "AbstractBlock";
    private static final String CHILD_HTML_METHOD = "getChildHtml";
    private static final String CHILD_CHILD_HTML_METHOD = "getChildChildHtml";

    @Override
    public void visitMemberAccess(MemberAccessTree tree) {
        String methodName = tree.member().toString();

        if (CHILD_HTML_METHOD.equals(methodName) || CHILD_CHILD_HTML_METHOD.equals(methodName)) {
            Tree parent = tree.getParent();

            assert parent != null;
            if (parent.is(Tree.Kind.FUNCTION_CALL)) {
                FunctionCallTree functionCall = (FunctionCallTree) parent;
                int paramCount = functionCall.callArguments().size();

                if (CHILD_HTML_METHOD.equals(methodName) && paramCount >= 3) {
                    context().newIssue(this, tree, "3rd parameter is not needed anymore for getChildHtml()");
                }

                if (CHILD_CHILD_HTML_METHOD.equals(methodName) && paramCount >= 4) {
                    context().newIssue(this, tree, "4th parameter is not needed anymore for getChildChildHtml()");
                }
            }
        }

        super.visitMemberAccess(tree);
    }
}