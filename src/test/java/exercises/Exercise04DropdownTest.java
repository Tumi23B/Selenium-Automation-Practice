package exercises;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Exercise04DropdownTest {

    @Test
    public void testDropdown() {

        // Launch Chrome
        WebDriver driver = new ChromeDriver();

        try {

            // Open the page
            driver.get("https://the-internet.herokuapp.com/dropdown");

            // Locate dropdown
            WebElement dropdownElement =
                    driver.findElement(By.id("dropdown"));

            Select dropdown =
                    new Select(dropdownElement);

            // ==========================================
            // Select Option 1 using visible text
            // ==========================================

            dropdown.selectByVisibleText("Option 1");

            // Verify selected option
            String selectedOption =
                    dropdown.getFirstSelectedOption().getText();

            assertEquals("Option 1", selectedOption);

            System.out.println(
                    "Option 1 selected successfully."
            );

            // ==========================================
            // Select Option 2 using value
            // ==========================================

            dropdown.selectByValue("2");

            // Verify selected option
            selectedOption =
                    dropdown.getFirstSelectedOption().getText();

            assertEquals("Option 2", selectedOption);

            System.out.println(
                    "Option 2 selected successfully."
            );

            // ==========================================
            // Select an option using index
            // ==========================================

            dropdown.selectByIndex(1);

            // Print selected value
            selectedOption =
                    dropdown.getFirstSelectedOption().getText();

            System.out.println(
                    "Selected value: " + selectedOption
            );

            // PASS
            System.out.println("PASS - Dropdown test completed.");

        } catch (Exception e) {

            System.out.println("FAIL - Dropdown test failed.");

            throw e;

        } finally {

            driver.quit();
        }
    }
}