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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.expression.LiteralTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = LiteralNamespaceCheck.KEY,
        name = LiteralNamespaceCheck.MESSAGE,
        description = "Use ::class constant instead of literal namespace strings",
        priority = Priority.MAJOR,
        tags = {"magento", "best-practices"}
)

public class LiteralNamespaceCheck extends PHPVisitorCheck {

    public static final String KEY = "LiteralNamespace";
    public static final String MESSAGE = "Avoid Literal Namespace Strings.";
    private static final Pattern LITERAL_NAMESPACE_PATTERN = Pattern.compile("^\\\\{0,2}+[A-Z][A-Za-z]++(\\\\{1,2}+[A-Z][A-Za-z]++){2,}+(?!\\\\+)$");

    @Override
    public void visitLiteral(LiteralTree tree) {
        if (tree.is(Tree.Kind.REGULAR_STRING_LITERAL)) {
            String literalValue = tree.value();

            literalValue = literalValue.substring(1, literalValue.length() - 1);

            if (isNamespaceLiteral(literalValue)) {
                context().newIssue(this, tree, "Use ::class notation instead.");
            }
        }
        super.visitLiteral(tree);
    }

    private boolean isNamespaceLiteral(String value) {
        Matcher matcher = LITERAL_NAMESPACE_PATTERN.matcher(value);
        return matcher.matches()
                && !value.contains(" ")
                && !value.contains("://")
                && !value.contains("@")
                && !value.startsWith("\\");
    }
}
