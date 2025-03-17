package org.perspectiveteam.sonarrules.php.checks;

import org.junit.jupiter.api.Test;
import org.perspectiveteam.sonarrules.plugins.php.CheckVerifier;

public class StrictTypesDeclarationCheckTest {
    @Test
    void test() throws Exception {
        CheckVerifier.verify(new StrictTypesDeclarationCheck(), "StrictTypesDeclarationCheck.php");
    }
}