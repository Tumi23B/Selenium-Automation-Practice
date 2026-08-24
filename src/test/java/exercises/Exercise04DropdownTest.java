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

        WebDriver driver = new ChromeDriver();

        try {

            driver.get("https://the-internet.herokuapp.com/dropdown");

            WebElement dropdownElement =
                    driver.findElement(By.id("dropdown"));

            Select dropdown =
                    new Select(dropdownElement);

            // ==========================================
            // Open dropdown
            // ==========================================

            System.out.println("Opening dropdown...");

            dropdownElement.click();

            Thread.sleep(2000);

            // ==========================================
            // Select Option 1
            // ==========================================

            dropdown.selectByVisibleText("Option 1");

            String selectedOption =
                    dropdown.getFirstSelectedOption().getText();

            assertEquals("Option 1", selectedOption);

            System.out.println(
                    "PASS - Option 1 selected successfully."
            );

            Thread.sleep(2000);

            // ==========================================
            // Open dropdown again
            // ==========================================

            System.out.println("Opening dropdown again...");

            dropdownElement.click();

            Thread.sleep(2000);

            // ==========================================
            // Select Option 2
            // ==========================================

            dropdown.selectByValue("2");

            selectedOption =
                    dropdown.getFirstSelectedOption().getText();

            assertEquals("Option 2", selectedOption);

            System.out.println(
                    "PASS - Option 2 selected successfully."
            );

            // ==========================================
            // Select using index
            // ==========================================

            dropdown.selectByIndex(1);

            selectedOption =
                    dropdown.getFirstSelectedOption().getText();

            System.out.println(
                    "Selected value: " + selectedOption
            );

            System.out.println(
                    "PASS - Dropdown test completed successfully."
            );

            // Keep browser open
            Thread.sleep(6000);

        } catch (Exception e) {

            System.out.println(
                    "FAIL - Dropdown test failed."
            );

            e.printStackTrace();

            throw new RuntimeException(
                    "Dropdown test failed.",
                    e
            );

        } finally {

            driver.quit();
        }
    }
}