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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.NamespaceNameTree;
import org.sonar.plugins.php.api.tree.statement.NamespaceStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
        key = ReservedWordsCheck.KEY,
        name = "PHP reserved words should not be used as class names or in namespaces",
        description = "PHP reserved words cannot be used to name a class, interface, trait, or in namespaces.",
        tags = {"convention", "php"}
)
public class ReservedWordsCheck extends PHPVisitorCheck {
    public static final String KEY = "ReservedWords";
    private static final Map<String, String> RESERVED_WORDS;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("int", "7");
        map.put("float", "7");
        map.put("bool", "7");
        map.put("string", "7");
        map.put("true", "7");
        map.put("false", "7");
        map.put("null", "7");
        map.put("void", "7.1");
        map.put("iterable", "7.1");
        map.put("resource", "7");
        map.put("object", "7");
        map.put("mixed", "7");
        map.put("numeric", "7");
        map.put("match", "8");
        RESERVED_WORDS = Collections.unmodifiableMap(map);
    }

    @Override
    public void visitNamespaceStatement(NamespaceStatementTree tree) {
        NamespaceNameTree namespaceName = tree.namespaceName();

        if (namespaceName != null) {
            String[] parts = namespaceName.fullName().split("\\\\");

            for (String part : parts) {
                String lowerCasePart = part.toLowerCase();
                if (RESERVED_WORDS.containsKey(lowerCasePart)) {
                    context().newIssue(this, namespaceName,
                            String.format("Cannot use \"%s\" in namespace as it is reserved since PHP %s",
                                    part, RESERVED_WORDS.get(lowerCasePart)));
                }
            }
        }

        super.visitNamespaceStatement(tree);
    }

    @Override
    public void visitClassDeclaration(ClassDeclarationTree tree) {
        String className = tree.name().text();
        String lowerCaseClassName = className.toLowerCase();

        if (RESERVED_WORDS.containsKey(lowerCaseClassName)) {
            context().newIssue(this, tree.name(),
                    String.format("Cannot use \"%s\" as class name as it is reserved since PHP %s",
                            className, RESERVED_WORDS.get(lowerCaseClassName)));
        }

        super.visitClassDeclaration(tree);
    }
}
