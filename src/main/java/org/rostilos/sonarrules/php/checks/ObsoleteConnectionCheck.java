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
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.expression.MemberAccessTree;

import java.util.Arrays;
import java.util.List;


@Rule(
        key = ObsoleteConnectionCheck.KEY,
        name = "Obsolete connection methods should not be used",
        description = "Obsolete connection methods should not be used. Use getConnection method instead.",
        tags = {"convention", "obsolete", "magento2"}
)
public class ObsoleteConnectionCheck extends PHPVisitorCheck {
    public static final String KEY = "ObsoleteConnection";
    private static final List<String> OBSOLETE_METHODS = Arrays.asList(
            "_getReadConnection",
            "_getWriteConnection",
            "_getReadAdapter",
            "_getWriteAdapter",
            "getReadConnection",
            "getWriteConnection",
            "getReadAdapter",
            "getWriteAdapter"
    );

    @Override
    public void visitMemberAccess(MemberAccessTree tree) {
        String memberName = tree.member().toString();

        if (OBSOLETE_METHODS.contains(memberName)) {
            context().newIssue(this, tree.member(),
                    String.format("Contains obsolete method: %s. Please use getConnection method instead.", memberName));
        }

        super.visitMemberAccess(tree);
    }

    @Override
    public void visitMethodDeclaration(MethodDeclarationTree tree) {
        String methodName = tree.name().text();

        if (OBSOLETE_METHODS.contains(methodName)) {
            context().newIssue(this, tree.name(),
                    String.format("Contains obsolete method: %s. Please use getConnection method instead.", methodName));
        }

        super.visitMethodDeclaration(tree);
    }
}