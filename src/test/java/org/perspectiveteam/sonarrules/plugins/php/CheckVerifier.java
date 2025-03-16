package org.perspectiveteam.sonarrules.plugins.php;

import org.sonar.plugins.php.api.tests.PHPCheckVerifier;
import org.sonar.plugins.php.api.visitors.PHPCheck;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

public class CheckVerifier extends PHPCheckVerifier {
    private CheckVerifier(boolean readExpectedIssuesFromComments, boolean frameworkDetectionEnabled) {
        super(readExpectedIssuesFromComments, frameworkDetectionEnabled);
    }

    public static void verify(PHPCheck check, String... relativePaths) {
        PHPCheckVerifier.verify(check, Arrays.stream(relativePaths).map(CheckVerifier::checkFile).toArray(File[]::new));
    }

    public static void verifyNoIssue(PHPCheck check, String relativePath) {
        PHPCheckVerifier.verifyNoIssue(checkFile(relativePath), check);
    }

    public static void verifyNoIssueIgnoringExpected(PHPCheck check, String relativePath) {
        new CheckVerifier(false, true).createVerifier(Collections.singletonList(checkFile(relativePath)), check).assertNoIssues();
    }

    private static File checkFile(String relativePath) {
        return new File("src/test/resources/checks/" + relativePath);
    }
}



