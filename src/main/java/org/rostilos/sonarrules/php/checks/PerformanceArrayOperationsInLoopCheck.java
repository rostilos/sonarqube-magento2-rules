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
import org.sonar.plugins.php.api.tree.expression.FunctionCallTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


@Rule(
        key = PerformanceArrayOperationsInLoopCheck.KEY,
        name = "Resource-intensive array operations should not be used in loops",
        description = "Resource-intensive array operations like array_merge() in loops can cause significant performance issues.",
        priority = Priority.MAJOR,
        tags = {"performance", "magento2"}
)
public class PerformanceArrayOperationsInLoopCheck extends PHPVisitorCheck {

    public static final String KEY = "PerformanceArrayOperationsInLoop";
    private static final Set<String> INEFFICIENT_ARRAY_FUNCTIONS = new HashSet<>(Arrays.asList(
            "array_merge",
            "array_merge_recursive",
            "array_replace",
            "array_replace_recursive",
            "array_diff",
            "array_diff_assoc",
            "array_diff_key",
            "array_diff_uassoc",
            "array_diff_ukey",
            "array_intersect",
            "array_intersect_assoc",
            "array_intersect_key",
            "array_intersect_uassoc",
            "array_intersect_ukey",
            "array_udiff",
            "array_udiff_assoc",
            "array_udiff_uassoc",
            "array_uintersect",
            "array_uintersect_assoc",
            "array_uintersect_uassoc"
    ));

    private static final String MESSAGE_TEMPLATE = "%s() is used in a loop and is a resource-intensive operation.";

    @Override
    public void visitFunctionCall(FunctionCallTree tree) {
        String functionName = tree.callee().toString();

        if (INEFFICIENT_ARRAY_FUNCTIONS.contains(functionName) && isInsideLoop(tree)) {
            context().newIssue(this, tree, String.format(MESSAGE_TEMPLATE, functionName));
        }

        super.visitFunctionCall(tree);
    }

    private boolean isInsideLoop(Tree tree) {
        Tree parent = tree.getParent();

        while (parent != null) {
            if (parent.is(Tree.Kind.FOR_STATEMENT) ||
                    parent.is(Tree.Kind.FOREACH_STATEMENT) ||
                    parent.is(Tree.Kind.WHILE_STATEMENT) ||
                    parent.is(Tree.Kind.DO_WHILE_STATEMENT)) {
                return true;
            }
            parent = parent.getParent();
        }

        return false;
    }
}
