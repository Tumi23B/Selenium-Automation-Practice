package assignment;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceDemoTest {

    @Test
    public void completePurchase() {

        // ==========================================
        // Launch Chrome
        // ==========================================

        WebDriver driver = new ChromeDriver();

        try {

            // ==========================================
            // Open Sauce Demo
            // ==========================================

            driver.get(
                    "https://www.saucedemo.com/"
            );

            // ==========================================
            // Login
            // ==========================================

            driver.findElement(
                    By.id("user-name")
            ).sendKeys("standard_user");

            driver.findElement(
                    By.id("password")
            ).sendKeys("secret_sauce");

            driver.findElement(
                    By.id("login-button")
            ).click();

            // ==========================================
            // Verify Login
            // ==========================================

            assertTrue(
                    driver.findElement(
                            By.className("title")
                    ).getText().contains("Products")
            );

            System.out.println(
                    "Login successful."
            );

            // ==========================================
            // Add 3 Products
            // ==========================================

            driver.findElement(
                    By.id("add-to-cart-sauce-labs-backpack")
            ).click();

            driver.findElement(
                    By.id("add-to-cart-sauce-labs-bike-light")
            ).click();

            driver.findElement(
                    By.id("add-to-cart-sauce-labs-bolt-t-shirt")
            ).click();

            // ==========================================
            // Verify Cart Contains 3 Products
            // ==========================================

            String cartCount =
                    driver.findElement(
                            By.className("shopping_cart_badge")
                    ).getText();

            assertEquals("3", cartCount);

            System.out.println(
                    "3 products added successfully."
            );

            // ==========================================
            // Remove 1 Product
            // ==========================================

            driver.findElement(
                    By.id("remove-sauce-labs-backpack")
            ).click();

            // ==========================================
            // Verify Cart Contains 2 Products
            // ==========================================

            cartCount =
                    driver.findElement(
                            By.className("shopping_cart_badge")
                    ).getText();

            assertEquals("2", cartCount);

            System.out.println(
                    "1 product removed successfully."
            );

            // ==========================================
            // Open Cart
            // ==========================================

            driver.findElement(
                    By.className("shopping_cart_link")
            ).click();

            // ==========================================
            // Checkout
            // ==========================================

            driver.findElement(
                    By.id("checkout")
            ).click();

            // ==========================================
            // Customer Details
            // ==========================================

            driver.findElement(
                    By.id("first-name")
            ).sendKeys("Boitumelo");

            driver.findElement(
                    By.id("last-name")
            ).sendKeys("Khauoe");

            driver.findElement(
                    By.id("postal-code")
            ).sendKeys("1448");

            // ==========================================
            // Continue
            // ==========================================

            driver.findElement(
                    By.id("continue")
            ).click();

            // ==========================================
            // Finish Order
            // ==========================================

            driver.findElement(
                    By.id("finish")
            ).click();

            // ==========================================
            // Verify Order Completion
            // ==========================================

            String confirmation =
                    driver.findElement(
                            By.className("complete-header")
                    ).getText();

            assertEquals(
                    "Thank you for your order!",
                    confirmation
            );

            // ==========================================
            // PASS
            // ==========================================

            System.out.println(
                    "PASS - Sauce Demo order completed successfully."
            );

        } catch (Exception e) {

            System.out.println(
                    "FAIL - Sauce Demo test failed."
            );

            throw e;

        } finally {

            // Close browser
            driver.quit();
        }
    }
}