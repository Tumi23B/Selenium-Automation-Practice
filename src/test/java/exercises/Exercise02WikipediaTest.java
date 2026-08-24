package exercises;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Exercise02WikipediaTest {

    @Test
    public void searchForSelenium() throws InterruptedException {

        // Launch Chrome
        WebDriver driver = new ChromeDriver();

        // Open Wikipedia
        driver.get("https://www.wikipedia.org/");

        // Find the search box
        driver.findElement(By.id("searchInput")).sendKeys("Selenium");

        // Submit the search
        driver.findElement(By.id("searchInput")).submit();

        // Wait 1 second so we can see the result
       // Thread.sleep(1000);

        // Get the page heading
        String heading = driver.findElement(By.id("firstHeading")).getText();

        // Verify the heading contains "Selenium"
        assertTrue(heading.contains("Selenium"));

        // Close the browser
        driver.quit();
    }
}