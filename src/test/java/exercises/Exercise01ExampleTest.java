package exercises;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Exercise01ExampleTest {

    @Test
    public void openExampleWebsite() {

        // Launch Chrome
        WebDriver driver = new ChromeDriver();

        // Open Example.com
        driver.get("https://example.com");

        // Verify the page title is correct 
        String actualTitle = driver.getTitle();

        assertEquals("Example Domain", actualTitle);

        // Wait 5 seconds so we can see the page
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Close the browser
        driver.quit();
    }
}