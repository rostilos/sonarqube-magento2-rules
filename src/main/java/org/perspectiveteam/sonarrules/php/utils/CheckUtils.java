package org.perspectiveteam.sonarrules.php.utils;

import org.sonar.plugins.php.api.tree.declaration.*;

public final class CheckUtils {
  public static boolean isConstructorMethodPromotion(MethodDeclarationTree tree) {
    return tree.name().text().equalsIgnoreCase("__construct");
  }
}
