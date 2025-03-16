package org.perspectiveteam.sonarrules.php.checks;

import org.junit.jupiter.api.Test;
import org.perspectiveteam.sonarrules.plugins.php.CheckVerifier;

import java.io.File;

public class FunctionArgumentsShouldNotBeModifiedCheckTest {
    @Test
    void test() throws Exception {
        CheckVerifier.verify(new FunctionArgumentsShouldNotBeModifiedCheck(), "FunctionArgumentsShouldNotBeModifiedCheck.php");
    }
}
