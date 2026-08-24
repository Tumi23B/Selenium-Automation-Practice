package exercises;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Exercise06FileDownloadTest {

    @Test
    public void testFileDownload() throws InterruptedException {

        // ==========================================
        // Create downloads folder
        // ==========================================

        String downloadPath =
                System.getProperty("user.dir")
                        + File.separator
                        + "downloads";

        File downloadDirectory =
                new File(downloadPath);

        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs();
        }

        // ==========================================
        // Configure Chrome Downloads
        // ==========================================

        ChromeOptions options =
                new ChromeOptions();

        options.addArguments("--start-maximized");

        options.setExperimentalOption(
                "prefs",
                java.util.Map.of(
                        "download.default_directory",
                        downloadDirectory.getAbsolutePath(),
                        "download.prompt_for_download",
                        false,
                        "download.directory_upgrade",
                        true
                )
        );

        // ==========================================
        // Launch Chrome
        // ==========================================

        WebDriver driver =
                new ChromeDriver(options);

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(10));

        try {

            // ==========================================
            // Open download page
            // ==========================================

            driver.get(
                    "https://the-internet.herokuapp.com/download"
            );

            // ==========================================
            // Find a file
            // ==========================================

            WebElement fileLink =
                    driver.findElement(
                            By.cssSelector("#content a")
                    );

            String filename =
                    fileLink.getText();

            System.out.println(
                    "Downloading: " + filename
            );

            // ==========================================
            // Click file
            // ==========================================

            fileLink.click();

            // ==========================================
            // Wait for download
            // ==========================================

            File downloadedFile =
                    new File(
                            downloadDirectory,
                            filename
                    );

            int attempts = 0;

            while (
                    !downloadedFile.exists()
                            && attempts < 20
            ) {

                Thread.sleep(500);

                attempts++;
            }

            // ==========================================
            // Check file exists
            // ==========================================

            assertTrue(
                    downloadedFile.exists(),
                    "Downloaded file does not exist."
            );

            // ==========================================
            // Check file size
            // ==========================================

            assertTrue(
                    downloadedFile.length() > 0,
                    "Downloaded file is empty."
            );

            // ==========================================
            // Print filename
            // ==========================================

            System.out.println(
                    "Filename: "
                            + downloadedFile.getName()
            );

            System.out.println(
                    "File size: "
                            + downloadedFile.length()
                            + " bytes"
            );

            // PASS
            System.out.println(
                    "PASS - File downloaded successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "FAIL - File download failed."
            );

            throw e;

        } finally {

            driver.quit();
        }
    }
}