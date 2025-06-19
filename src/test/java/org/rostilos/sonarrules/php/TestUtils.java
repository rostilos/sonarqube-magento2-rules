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

package org.rostilos.sonarrules.php;

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
