package assignment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * LoginTest class tests the login functionality of the-internet.herokuapp.com
 * 
 * Test scenarios:
 * 1. Successful login and logout with valid credentials
 * 2. Failed login with invalid credentials with screenshot capture
 */
public class LoginTest {

    // ============================================================
    // CONSTANTS
    // ============================================================
    
    private static final String LOGIN_URL = "https://the-internet.herokuapp.com/login";
    private static final String VALID_USERNAME = "tomsmith";
    private static final String VALID_PASSWORD = "SuperSecretPassword!";
    private static final String INVALID_USERNAME = "wronguser";
    private static final String INVALID_PASSWORD = "wrongpassword";
    
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final String SCREENSHOTS_DIR = "screenshots";
    
    // Success messages
    private static final String LOGIN_SUCCESS_MSG = "You logged into a secure area!";
    private static final String LOGOUT_SUCCESS_MSG = "You logged out of the secure area!";
    private static final String LOGIN_ERROR_MSG = "Your username is invalid!";

    // ============================================================
    // LOCATORS
    // ============================================================
    
    private static final By USERNAME_FIELD = By.id("username");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By LOGOUT_BUTTON = By.cssSelector("a[href='/logout']");
    private static final By FLASH_MESSAGE = By.id("flash");

    // ============================================================
    // INSTANCE VARIABLES
    // ============================================================
    
    private WebDriver driver;
    private WebDriverWait wait;

    // ============================================================
    // SETUP AND TEARDOWN
    // ============================================================

    @BeforeEach
    public void setUp() {
        // Initialize WebDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
        
        // Create screenshots directory if it doesn't exist
        try {
            Path screenshotDir = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
                System.out.println("Created screenshots directory: " + screenshotDir.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Could not create screenshots directory: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    /**
     * Navigates to the login page.
     */
    private void navigateToLoginPage() {
        driver.get(LOGIN_URL);
        System.out.println("Navigated to: " + LOGIN_URL);
    }

    /**
     * Logs in with the specified credentials.
     */
    private void login(String username, String password) {
        WebElement usernameField = wait.until(
            ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD)
        );
        usernameField.clear();
        usernameField.sendKeys(username);
        
        WebElement passwordField = driver.findElement(PASSWORD_FIELD);
        passwordField.clear();
        passwordField.sendKeys(password);
        
        driver.findElement(LOGIN_BUTTON).click();
        System.out.println("Login attempted with username: " + username);
    }

    /**
     * Gets the flash message text.
     */
    private String getFlashMessage() {
        WebElement flashElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(FLASH_MESSAGE)
        );
        return flashElement.getText();
    }

    /**
     * Logs out of the application.
     */
    private void logout() {
        wait.until(
            ExpectedConditions.elementToBeClickable(LOGOUT_BUTTON)
        ).click();
        System.out.println("Logged out successfully.");
    }

    /**
     * Takes a screenshot with a timestamp - WORKING VERSION
     */
    private String takeScreenshot(String testName) {
        try {
            // Create screenshots directory if it doesn't exist
            Path screenshotDir = Paths.get(SCREENSHOTS_DIR);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            );
            String filename = testName + "_" + timestamp + ".png";
            Path screenshotPath = screenshotDir.resolve(filename);

            // Take screenshot - THIS IS THE KEY PART/
            // //NBNBNNBNBNBNBNBNBNBNBNBNB
            TakesScreenshot screenshotTaker = (TakesScreenshot) driver;
            File screenshot = screenshotTaker.getScreenshotAs(OutputType.FILE);
            
            // Save screenshot
            Files.copy(screenshot.toPath(), screenshotPath, StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println(" Screenshot saved: " + screenshotPath.toAbsolutePath());
            return screenshotPath.toString();
            
        } catch (IOException e) {
            System.err.println(" Failed to save screenshot: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println(" Unexpected error while taking screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Pauses execution for visual confirmation.
     */
    private void pauseForVisual(String action) {
        try {
            System.out.println("Pausing to view: " + action);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ============================================================
    // TEST METHODS
    // ============================================================

    /**
     * Test: Login with valid credentials and then logout.
     */
    @Test
    public void loginAndLogoutWithValidCredentials() {
        System.out.println("==========================================");
        System.out.println("TEST: Login and Logout with Valid Credentials");
        System.out.println("==========================================");

        try {
            // Step 1: Navigate to login page
            navigateToLoginPage();
            pauseForVisual("Login page");
            
            // Take screenshot of login page
            takeScreenshot("01-login-page");

            // Step 2: Login with valid credentials
            login(VALID_USERNAME, VALID_PASSWORD);
            pauseForVisual("After login attempt");

            // Step 3: Verify successful login
            String loginMessage = getFlashMessage();
            System.out.println("Login message: " + loginMessage);
            
            // Take screenshot after successful login
            takeScreenshot("02-after-successful-login");
            
            assertTrue(
                loginMessage.contains(LOGIN_SUCCESS_MSG),
                "Expected login success message containing: '" + LOGIN_SUCCESS_MSG + 
                "' but got: '" + loginMessage + "'"
            );
            System.out.println(" Login successful!");

            // Step 4: Logout(functionality to be implemented soon)
            logout();
            pauseForVisual("After logout");

            // Step 5: Verify successful logout
            String logoutMessage = getFlashMessage();
            System.out.println("Logout message: " + logoutMessage);
            
            // Take screenshot after logout
            takeScreenshot("03-after-logout");
            
            assertTrue(
                logoutMessage.contains(LOGOUT_SUCCESS_MSG),
                "Expected logout success message containing: '" + LOGOUT_SUCCESS_MSG + 
                "' but got: '" + logoutMessage + "'"
            );
            System.out.println(" Logout successful!");

            System.out.println(" Test passed: Valid login and logout completed successfully.");

        } catch (Exception e) {
            System.err.println(" Test failed: " + e.getMessage());
            takeScreenshot("04-valid-login-failure");
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Test: Login with invalid credentials and capture screenshot.
     */
    @Test
    public void loginWithIncorrectCredentials() {
        System.out.println("==========================================");
        System.out.println("TEST: Login with Incorrect Credentials");
        System.out.println("==========================================");

        try {
            // Step 1: Navigate to login page
            navigateToLoginPage();
            pauseForVisual("Login page");
            
            // Take screenshot of login page
            takeScreenshot("05-login-page-before-invalid");

            // Step 2: Login with invalid credentials
            login(INVALID_USERNAME, INVALID_PASSWORD);
            pauseForVisual("After invalid login attempt");

            // Step 3: Verify error message
            String errorMessage = getFlashMessage();
            System.out.println("Error message: " + errorMessage);
            
            // Take screenshot of error
            takeScreenshot("06-invalid-login-error");
            
            assertTrue(
                errorMessage.contains(LOGIN_ERROR_MSG),
                "Expected error message containing: '" + LOGIN_ERROR_MSG + 
                "' but got: '" + errorMessage + "'"
            );
            System.out.println(" Error message displayed correctly!");

            System.out.println(" Test passed: Invalid login handled correctly with screenshot captured.");

        } catch (Exception e) {
            System.err.println(" Test failed: " + e.getMessage());
            takeScreenshot("07-invalid-login-failure");
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Additional Test: Login with empty credentials.
     */
    @Test
    public void loginWithEmptyCredentials() {
        System.out.println("==========================================");
        System.out.println("TEST: Login with Empty Credentials");
        System.out.println("==========================================");

        try {
            navigateToLoginPage();
            takeScreenshot("08-empty-credentials-before");
            
            login("", "");
            pauseForVisual("After empty credentials");

            String errorMessage = getFlashMessage();
            System.out.println("Error message: " + errorMessage);
            
            takeScreenshot("09-empty-credentials-error");
            
            assertTrue(
                errorMessage.contains("Your username is invalid!"),
                "Expected error message for empty credentials"
            );
            
            System.out.println(" Test passed: Empty credentials handled correctly.");

        } catch (Exception e) {
            System.err.println(" Test failed: " + e.getMessage());
            takeScreenshot("10-empty-credentials-failure");
            fail("Test failed: " + e.getMessage());
        }
    }

    /**
     * Additional Test: Login with valid username but invalid password.
     */
    @Test
    public void loginWithValidUsernameInvalidPassword() {
        System.out.println("==========================================");
        System.out.println("TEST: Login with Valid Username, Invalid Password");
        System.out.println("==========================================");

        try {
            navigateToLoginPage();
            takeScreenshot("11-invalid-password-before");
            
            login(VALID_USERNAME, "wrongpassword");
            pauseForVisual("After invalid password");

            String errorMessage = getFlashMessage();
            System.out.println("Error message: " + errorMessage);
            
            takeScreenshot("12-invalid-password-error");
            
            assertTrue(
                errorMessage.contains("Your password is invalid!"),
                "Expected password error message"
            );
            
            System.out.println(" Test passed: Invalid password handled correctly.");

        } catch (Exception e) {
            System.err.println(" Test failed: " + e.getMessage());
            takeScreenshot("13-invalid-password-failure");
            fail("Test failed: " + e.getMessage());
        }
    }
}