package org.rostilos.sonarrules.php.plugins;

import org.sonar.plugins.php.api.tests.PHPCheckVerifier;
import org.sonar.plugins.php.api.visitors.PHPCheck;

import java.io.File;
import java.util.Arrays;

public final class CheckVerifier extends PHPCheckVerifier {

    CheckVerifier(boolean readExpectedIssuesFromComments, boolean frameworkDetectionEnabled) {
        super(readExpectedIssuesFromComments, frameworkDetectionEnabled);
    }

    public static void verify(PHPCheck check, String... relativePaths) {
        PHPCheckVerifier.verify(check, Arrays.stream(relativePaths).map(CheckVerifier::checkFile).toArray(File[]::new));
    }

    private static File checkFile(String relativePath) {
        return new File("src/test/resources/checks/" + relativePath);
    }

}
