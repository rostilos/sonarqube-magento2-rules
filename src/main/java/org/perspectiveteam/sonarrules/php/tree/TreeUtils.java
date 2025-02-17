package org.perspectiveteam.sonarrules.php.tree;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.sonar.php.tree.impl.PHPTree;
import org.sonar.php.utils.collections.ListUtils;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxTrivia;

public class TreeUtils {
   private TreeUtils() {
   }

   public static boolean isDescendant(Tree tree, Tree potentialParent) {
      Tree parent;
      for(parent = tree; parent != null && !potentialParent.equals(parent); parent = parent.getParent()) {
      }

      return potentialParent.equals(parent);
   }

   @CheckForNull
   public static Tree findAncestorWithKind(Tree tree, Collection<Tree.Kind> kinds) {
      Tree parent;
      for(parent = tree; parent != null && !kinds.contains(parent.getKind()); parent = parent.getParent()) {
      }

      return parent;
   }

   @CheckForNull
   public static Tree findAncestorWithKind(Tree tree, Tree.Kind... kinds) {
      return findAncestorWithKind(tree, (Collection)Arrays.asList(kinds));
   }

   public static Stream<Tree> descendants(@Nullable Tree root) {
      if (root != null && !((PHPTree)root).isLeaf()) {
         Spliterator<Tree> spliterator = Spliterators.spliteratorUnknownSize(((PHPTree)root).childrenIterator(), 16);
         Stream<Tree> stream = StreamSupport.stream(spliterator, false);
         return stream.flatMap((tree) -> {
            return Stream.concat(Stream.of(tree), descendants(tree));
         });
      } else {
         return Stream.empty();
      }
   }

   public static <T extends Tree> Stream<T> descendants(@Nullable Tree root, Class<T> clazz) {
      Stream var10000 = descendants(root);
      Objects.requireNonNull(clazz);
      var10000 = var10000.filter(clazz::isInstance);
      Objects.requireNonNull(clazz);
      return var10000.map(clazz::cast);
   }

   public static Optional<Tree> firstDescendant(@Nullable Tree root, Predicate<Tree> predicate) {
      return descendants(root).filter(predicate).findFirst();
   }

   public static <T extends Tree> Optional<Tree> firstDescendant(Tree root, Class<T> clazz) {
      Objects.requireNonNull(clazz);
      return firstDescendant(root, clazz::isInstance);
   }

   public static boolean hasAnnotation(Tree declaration, String annotation) {
      if (!annotation.startsWith("@")) {
         annotation = "@" + annotation;
      }

      List<SyntaxTrivia> trivias = ((PHPTree)declaration).getFirstToken().trivias();
      return !trivias.isEmpty() ? StringUtils.containsIgnoreCase(((SyntaxTrivia)ListUtils.getLast(trivias)).text(), annotation) : false;
   }
}
