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

package org.rostilos.sonarrules.php.utils;

import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.declaration.*;

import java.util.List;
import java.util.Set;

public final class CheckUtils {
  private CheckUtils() {
    throw new IllegalStateException("Utility class");
  }
  private static final List<String> PLUGIN_METHOD_PREFIXES = List.of("before", "around", "after");
  public static final Set<String> MAGIC_METHODS = Set.of(
          "__construct", "__destruct", "__call", "__callStatic", "__get",
          "__set", "__isset", "__unset", "__sleep", "__wakeup", "__toString", "__invoke",
          "__set_state", "__clone", "__debugInfo", "_construct");

  public static boolean isConstructorMethodPromotion(MethodDeclarationTree tree) {
    return tree.name().text().equalsIgnoreCase("__construct");
  }

  public static boolean isPluginClass(ClassDeclarationTree classTree) {
    for (ClassMemberTree member : classTree.members()) {
      if (member.is(Tree.Kind.METHOD_DECLARATION)) {
        MethodDeclarationTree methodTree = (MethodDeclarationTree) member;
        String methodName = methodTree.name().text();

        for (String prefix : PLUGIN_METHOD_PREFIXES) {
          if (methodName.startsWith(prefix) && methodName.length() > prefix.length()) {
            return Character.isUpperCase(methodName.charAt(prefix.length()));
          }
        }
      }
    }

    return false;
  }
}
