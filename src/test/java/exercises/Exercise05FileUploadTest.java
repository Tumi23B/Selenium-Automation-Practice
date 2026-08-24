package exercises;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Exercise05FileUploadTest {

    @Test
    public void testFileUpload() throws IOException {

        // ==========================================
        // Create student.txt
        // ==========================================

        File file = new File("student.txt");

        if (!file.exists()) {
            file.createNewFile();
        }

        // Launch Chrome
        WebDriver driver = new ChromeDriver();

        try {

            // Open website
            driver.get(
                    "https://the-internet.herokuapp.com/upload"
            );

            // Locate file upload field
            WebElement uploadField =
                    driver.findElement(By.id("file-upload"));

            // Upload student.txt
            uploadField.sendKeys(
                    file.getAbsolutePath()
            );

            // Click Upload
            driver.findElement(By.id("file-submit"))
                    .click();

            // Verify student.txt appears
            String uploadedFile =
                    driver.findElement(By.id("uploaded-files"))
                            .getText();

            assertTrue(
                    uploadedFile.contains("student.txt")
            );

            // PASS
            System.out.println(
                    "PASS - student.txt uploaded successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "FAIL - File upload failed."
            );

            throw e;

        } finally {

            driver.quit();
        }
    }
}