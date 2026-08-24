package exercises;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class Exercise03CheckboxTest {

    @Test
    public void testCheckboxes() {

        // Launch Chrome
        WebDriver driver = new ChromeDriver();

        try {

            // Open the page
            driver.get("https://the-internet.herokuapp.com/checkboxes");

            // Find both checkboxes
            WebElement checkbox1 =
                    driver.findElements(By.cssSelector("input[type='checkbox']")).get(0);

            WebElement checkbox2 =
                    driver.findElements(By.cssSelector("input[type='checkbox']")).get(1);

            // Determine which checkbox is selected
            System.out.println(
                    "Checkbox 1 selected: " + checkbox1.isSelected()
            );

            System.out.println(
                    "Checkbox 2 selected: " + checkbox2.isSelected()
            );

            // Tick the unchecked checkbox
            if (!checkbox1.isSelected()) {
                checkbox1.click();
            }

            if (!checkbox2.isSelected()) {
                checkbox2.click();
            }

            // Untick the checked checkbox
            if (checkbox1.isSelected()) {
                checkbox1.click();
            }

            if (checkbox2.isSelected()) {
                checkbox2.click();
            }

            // Verify both are unchecked
            assertFalse(
                    checkbox1.isSelected(),
                    "Checkbox 1 should be unchecked"
            );

            assertFalse(
                    checkbox2.isSelected(),
                    "Checkbox 2 should be unchecked"
            );

            // PASS
            System.out.println("PASS - Both checkboxes are unchecked.");

        } catch (Exception e) {

            System.out.println("FAIL - Checkbox test failed.");

            throw e;

        } finally {

            // Close browser
            driver.quit();
        }
    }
}