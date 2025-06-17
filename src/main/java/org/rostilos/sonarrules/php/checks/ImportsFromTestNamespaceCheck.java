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
import org.sonar.plugins.php.api.tree.statement.UseStatementTree;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.statement.UseClauseTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;


@Rule(
        key = ImportsFromTestNamespaceCheck.KEY,
        name = "Application modules should not use classes from test modules",
        description = "Detects imports from test namespaces like 'Magento\\Tests'",
        priority = org.sonar.check.Priority.MAJOR,
        tags = {"convention", "magento2"}
)
public class ImportsFromTestNamespaceCheck extends PHPVisitorCheck {
    public static final String KEY = "ImportsFromTestNamespace";
    private static final String PROHIBITED_NAMESPACE = "Magento\\Tests";
    private static final String MAGENTO_NAMESPACE = "Magento";
    private static final String TESTS_SEGMENT = "Tests";
    private static final String MESSAGE = "Application modules should not use classes from test modules.";

    @Override
    public void visitUseStatement(UseStatementTree tree) {
        super.visitUseStatement(tree);
        for (UseClauseTree useClause : tree.clauses()) {
            NamespaceNameTree namespaceName = useClause.namespaceName();
            String fullyQualifiedName = namespaceName.fullyQualifiedName();

            if (fullyQualifiedName.contains(PROHIBITED_NAMESPACE) || fullyQualifiedName.contains("\\" + PROHIBITED_NAMESPACE)) {
                context().newIssue(this, namespaceName, MESSAGE);
                continue;
            }

            if (tree.prefix() != null && !tree.prefix().toString().isEmpty()) {
                String prefix = tree.prefix().toString();
                if (!prefix.replace("\\", "").equals(MAGENTO_NAMESPACE)) {
                    return;
                }
                if (fullyQualifiedName.startsWith(TESTS_SEGMENT) ||
                        fullyQualifiedName.contains("\\" + TESTS_SEGMENT)) {
                    context().newIssue(this, namespaceName, MESSAGE);
                }
            }
        }
    }
}
