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
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;
import org.sonar.plugins.php.api.tree.expression.NameIdentifierTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.rostilos.sonarrules.php.utils.CheckUtils;

@Rule(
        key = EventsInConstructorsCheck.KEY,
        name = EventsInConstructorsCheck.MESSAGE,
        description = "Events MUST NOT be triggered in constructors. Triggering events during object construction can lead to unpredictable behavior since the object may not be fully initialized.",
        priority = Priority.CRITICAL,
        tags = {"magento2", "bug", "event-handling"}
)

public class EventsInConstructorsCheck extends PHPVisitorCheck {

    public static final String KEY = "EventsInConstructors";
    public static final String FORBIDDEN_METHOD = "dispatch";
    public static final String MESSAGE = "No events in constructors.";

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        boolean isCalledInConstructor = isCalledInConstructor(tree);
        if (isCalledInConstructor && isEventDispatch(tree)) {
            context().newIssue(this, tree.callee(), MESSAGE);
        }
        super.visitFunctionCall(tree);
    }

    private boolean isCalledInConstructor(Tree tree) {
        if (!tree.is(Tree.Kind.METHOD_DECLARATION)) {
            if (tree.getParent() == null) {
                return false;
            }
            return isCalledInConstructor(tree.getParent());
        } else {
            return CheckUtils.isConstructorMethodPromotion((MethodDeclarationTree) tree);
        }

    }

    private boolean isEventDispatch(FunctionCallTree functionCallTree) {
        if (functionCallTree.callee().is(Tree.Kind.OBJECT_MEMBER_ACCESS)) {
            MemberAccessTree callee = (MemberAccessTree) functionCallTree.callee();
            NameIdentifierTree member = (NameIdentifierTree) callee.member();
            return member.text().equals(FORBIDDEN_METHOD);
        }
        return false;
    }
}
