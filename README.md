Selenium Automation Practice

A Java-based Selenium WebDriver automation project containing practical web automation exercises and functional test scenarios developed while learning and applying automated software testing techniques.

The project demonstrates browser automation, functional validation, assertions, explicit waits, screenshot capture, test reporting, and Maven-based test execution.

🚀 Technologies Used
Java 25
Selenium WebDriver 4.35.0
JUnit 5
Apache Maven
Google Chrome
ChromeDriver
Git & GitHub
HTML Test Reporting
📌 Project Overview

This project contains Selenium WebDriver automation exercises as well as functional automation scenarios.

The automation suite covers:

Web navigation
Login validation
Positive and negative login scenarios
Logout validation
UI element interaction
Checkbox interaction
Dropdown interaction
File upload
File download
Shopping cart functionality
Product selection
Product removal
Checkout
Order completion validation
Automated screenshots
HTML test reporting
Automatic generation of the HTML test report after test execution
🧪 Automated Test Scenarios
1. Login Automation

The LoginTest class automates the login functionality of:

The Internet

https://the-internet.herokuapp.com/login

The test suite covers:

Successful login with valid credentials
Successful logout
Login with incorrect credentials
Login with empty credentials
Login with a valid username and invalid password
Validation of success and error messages
Screenshot capture during test execution
2. SauceDemo End-to-End Automation

The SauceDemoTest class performs an end-to-end shopping workflow using the free SauceDemo website:

https://www.saucedemo.com/

The automated scenario covers:

Login
Open SauceDemo
Enter username
Enter password
Click Login
Verify the Products page
Add Products

The test adds three products:

Sauce Labs Backpack
Sauce Labs Bike Light
Sauce Labs Bolt T-Shirt

The automation verifies that all three products have been added successfully.

Remove Product

The automation:

Opens the shopping cart
Verifies the three products
Removes the Sauce Labs Backpack
Verifies that two products remain
Confirms that the correct products remain in the cart
Checkout

The automation:

Clicks Checkout
Enters customer information
Enters postal code
Continues to the order overview
Verifies the products displayed during checkout
Order Completion

The automation:

Clicks Finish
Verifies the order confirmation
Confirms that the message:

Thank you for your order!

is displayed.

📚 Selenium Practice Exercises

The project also contains practical Selenium exercises covering common WebDriver functionality.

Exercise	Description
Exercise 01	Open and validate an example website
Exercise 02	Wikipedia Selenium search
Exercise 03	Checkbox interaction
Exercise 04	Dropdown interaction
Exercise 05	File upload
Exercise 06	File download

These exercises were created to build practical experience with Selenium WebDriver and browser automation.

📊 Automated Test Reporting

After the Maven test suite has finished executing, the project automatically generates an HTML automation test report using the results from the completed tests.

The report is generated at:

reporting/TestReportGenerator.java

The report is also opened automatically after the tests have completed, allowing the test results to be reviewed immediately.

📁 Project Structure
JavaAutomation/
│
├── pom.xml
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── reporting/
│   │           └── TestReportGenerator.java
│   │
│   └── test/
│       └── java/
│           ├── assignment/
│           │   ├── LoginTest.java
│           │   └── SauceDemoTest.java
│           │
│           └── exercises/
│               ├── Exercise01ExampleTest.java
│               ├── Exercise02WikipediaTest.java
│               ├── Exercise03CheckboxTest.java
│               ├── Exercise04DropdownTest.java
│               ├── Exercise05FileUploadTest.java
│               └── Exercise06FileDownloadTest.java
│
├── screenshots/
│
└── target/
    └── automation-test-report.html
