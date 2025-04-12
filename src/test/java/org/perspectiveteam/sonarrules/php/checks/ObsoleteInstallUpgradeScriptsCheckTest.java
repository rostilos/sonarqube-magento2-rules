package org.perspectiveteam.sonarrules.php.checks;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.perspectiveteam.sonarrules.php.TestUtils;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.visitors.FileIssue;

import org.sonar.plugins.php.api.visitors.PhpIssue;

class ObsoleteInstallUpgradeScriptsCheckTest {

    private final ObsoleteInstallUpgradeScriptsCheck check = new ObsoleteInstallUpgradeScriptsCheck();
    private static final String TEST_DIR = "InstallUpgrade/";

    @Test
    void okTest() {
        checkNoIssue("valid-file.php");
        checkNoIssue("some-sql-folder/file.php");
        checkNoIssue("some-data-folder/file.php");
    }

    @Test
    void koTest() {
        checkIssue(
                "install-sample.php",
                "Install scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "InstallSchema.php",
                "InstallSchema scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "InstallData.php",
                "InstallData scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        );
        checkIssue(
                "data-install-.php",
                "Install scripts are obsolete. Please create class InstallData in module's Setup folder"
        );
        checkIssue(
                "upgrade-.php",
                "Upgrade scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "UpgradeSchema.php",
                "UpgradeSchema scripts are obsolete. Please use declarative schema approach in module's etc/db_schema.xml file"
        );
        checkIssue(
                "UpgradeData.php",
                "UpgradeData scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        );
        checkIssue(
                "data-upgrade-.php",
                "Upgrade scripts are obsolete. Please use data patches approach in module's Setup/Patch/Data dir"
        );
        checkIssue(
                "recurring.php",
                "Recurring scripts are obsolete. Please create class Recurring in module's Setup folder"
        );
        checkIssue(
                "sql/file.php",
                "file.php is in an invalid directory sql:\n" +
                        "- Create a data patch within module's Setup/Patch/Data folder for data upgrades.\n" +
                        "- Use declarative schema approach in module's etc/db_schema.xml file for schema changes."
        );
        checkIssue(
                "data/file.php",
                "file.php is in an invalid directory data:\n" +
                        "- Create a data patch within module's Setup/Patch/Data folder for data upgrades.\n" +
                        "- Use declarative schema approach in module's etc/db_schema.xml file for schema changes."
        );
    }

    private void checkNoIssue(String fileName) {
        check(fileName, Collections.emptyList());
    }

    private void checkIssue(String fileName, String expectedIssueMessage) {
        check(fileName, Collections.singletonList(new FileIssue(check, expectedIssueMessage)));
    }

    private void check(String fileName, List<PhpIssue> expectedIssues) {
        PHPCheckTest.check(check, TestUtils.getCheckFile(TEST_DIR + fileName), expectedIssues);
    }

}
