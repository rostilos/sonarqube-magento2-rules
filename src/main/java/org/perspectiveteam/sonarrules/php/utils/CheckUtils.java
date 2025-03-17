package org.perspectiveteam.sonarrules.php.utils;

import org.sonar.plugins.php.api.tree.declaration.*;

import java.util.List;
import java.util.Set;

public final class CheckUtils {
  private static final List<String> PLUGIN_METHOD_PREFIXES = List.of("before", "around", "after");
  public static final Set<String> MAGIC_METHODS = Set.of(
          "__construct", "__destruct", "__call", "__callStatic", "__get",
          "__set", "__isset", "__unset", "__sleep", "__wakeup", "__toString", "__invoke",
          "__set_state", "__clone", "__debugInfo");

  public static boolean isConstructorMethodPromotion(MethodDeclarationTree tree) {
    return tree.name().text().equalsIgnoreCase("__construct");
  }

  public static boolean isPluginClass(ClassDeclarationTree classTree) {
    for (org.sonar.plugins.php.api.tree.Tree member : classTree.members()) {
      if (member.is(ClassTree.Kind.METHOD_DECLARATION)) {
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
