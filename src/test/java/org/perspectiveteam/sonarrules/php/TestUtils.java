package org.perspectiveteam.sonarrules.php;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.sonar.plugins.php.api.tests.PhpTestFile;
import org.sonar.plugins.php.api.visitors.PhpFile;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class TestUtils {

    private TestUtils() {
    }

    public static PhpFile getCheckFile(String filename) {
        return getFile(new File("src/test/resources/checks/" + filename));
    }

    public static PhpFile getFile(File file, String contents) {
        try {
            Files.write(file.toPath(), contents.getBytes(UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write test file: " + file.getAbsolutePath());
        }
        return getFile(file);
    }

    public static PhpFile getFile(File file) {
        return new PhpTestFile(file);
    }

}
