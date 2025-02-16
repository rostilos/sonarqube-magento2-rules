package org.perpectiveteam.sonarrules.php;

import java.util.Collection;
import java.util.stream.Stream;

import org.sonar.check.Rule;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.ClassDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ClassTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.ParameterTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(
  key = EventsInConstructorsCheck.KEY,
  name = EventsInConstructorsCheck.MESSAGE,
  tags = {"convention"}
)

public class EventsInConstructorsCheck extends PHPVisitorCheck {

  public static final String KEY = "S3";
  public static final String MESSAGE = "Events MUST NOT be triggered in constructors.";

  @Override
  public void visitClassDeclaration(ClassDeclarationTree tree) {
    super.visitClassDeclaration(tree);

    if (tree.is(Kind.CLASS_DECLARATION)) {
      visitClass(tree);
    }
  }

  private void visitClass(ClassTree tree) {
    var isEventCaleedInConstructor = isEventCaleedInConstructor(tree);

    if (isEventCaleedInConstructor) {
      var message = String.format(MESSAGE);
      context().newIssue(this, tree.classToken(), message);
    }
  }
  
  private boolean isEventCaleedInConstructor(ClassTree tree) {
    var constructors = getConstructors(tree);
    return (boolean) constructors.map(constructor -> constructor.parameters().parameters())
      .flatMap(Collection::stream)
      .filter(ParameterTree::isPropertyPromotion)
      .anyMatch(parameter -> parameter.toString().contains("->dispatch")); 
  }

  private static Stream<MethodDeclarationTree> getConstructors(ClassTree tree) {
    return tree.members().stream()
      .filter(member -> member.is(Kind.METHOD_DECLARATION))
      .map(MethodDeclarationTree.class::cast)
      .filter(member -> "__construct".equals((member.name()).text()));
  }
}
