package reporting;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestReportGenerator {

    private static final String REPORT_DIRECTORY = "target/surefire-reports";
    private static final String OUTPUT_FILE = "target/automation-test-report.html";

    public static void main(String[] args) {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("        GENERATING AUTOMATION TEST REPORT");
        System.out.println("======================================================");

        try {

            File reportDirectory = new File(REPORT_DIRECTORY);

            if (!reportDirectory.exists()) {
                System.err.println("Surefire report directory was not found:");
                System.err.println(reportDirectory.getAbsolutePath());
                return;
            }

            List<TestResult> results = readSurefireResults(reportDirectory);

            if (results.isEmpty()) {
                System.err.println("No Surefire test results were found.");
                return;
            }

            generateHtmlReport(results);

            File reportFile = new File(OUTPUT_FILE);

            System.out.println();
            System.out.println("Report generated successfully:");
            System.out.println(reportFile.getAbsolutePath());

            openReport(reportFile);

        } catch (Exception e) {

            System.err.println("Failed to generate test report.");
            e.printStackTrace();
        }
    }

    // =========================================================
    // READ SUREFIRE XML RESULTS
    // =========================================================

    private static List<TestResult> readSurefireResults(File directory)
            throws Exception {

        List<TestResult> results = new ArrayList<>();

        File[] xmlFiles = directory.listFiles(
                (dir, name) ->
                        name.startsWith("TEST-") && name.endsWith(".xml")
        );

        if (xmlFiles == null) {
            return results;
        }

        Arrays.sort(xmlFiles, Comparator.comparing(File::getName));

        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();

        DocumentBuilder builder =
                factory.newDocumentBuilder();

        for (File xmlFile : xmlFiles) {

            Document document = builder.parse(xmlFile);

            NodeList testCases =
                    document.getElementsByTagName("testcase");

            for (int i = 0; i < testCases.getLength(); i++) {

                Element testCase =
                        (Element) testCases.item(i);

                String className =
                        testCase.getAttribute("classname");

                String testName =
                        testCase.getAttribute("name");

                String time =
                        testCase.getAttribute("time");

                String status = "PASSED";
                String message = "";

                NodeList failures =
                        testCase.getElementsByTagName("failure");

                NodeList errors =
                        testCase.getElementsByTagName("error");

                NodeList skipped =
                        testCase.getElementsByTagName("skipped");

                if (failures.getLength() > 0) {

                    status = "FAILED";

                    Element failure =
                            (Element) failures.item(0);

                    message =
                            getFailureMessage(failure);

                } else if (errors.getLength() > 0) {

                    status = "ERROR";

                    Element error =
                            (Element) errors.item(0);

                    message =
                            getFailureMessage(error);

                } else if (skipped.getLength() > 0) {

                    status = "SKIPPED";
                }

                results.add(
                        new TestResult(
                                className,
                                testName,
                                time,
                                status,
                                message
                        )
                );
            }
        }

        return results;
    }

    // =========================================================
    // EXTRACT FAILURE / ERROR MESSAGE
    // =========================================================

    private static String getFailureMessage(Element element) {

        String message = element.getAttribute("message");

        String text = element.getTextContent();

        if (message == null || message.isBlank()) {
            message = text;
        }

        if (message == null) {
            return "";
        }

        return message.trim();
    }

    // =========================================================
    // GENERATE HTML
    // =========================================================

    private static void generateHtmlReport(
            List<TestResult> results) throws IOException {

        int total = results.size();

        int passed = countStatus(results, "PASSED");
        int failed = countStatus(results, "FAILED");
        int errors = countStatus(results, "ERROR");
        int skipped = countStatus(results, "SKIPPED");

        double successRate =
                total == 0
                        ? 0
                        : ((double) passed / total) * 100;

        boolean successful =
                failed == 0 && errors == 0;

        LocalDateTime executionTime =
                LocalDateTime.now();

        String formattedDate =
                executionTime.format(
                        DateTimeFormatter.ofPattern(
                                "dd MMMM yyyy"
                        )
                );

        String formattedTime =
                executionTime.format(
                        DateTimeFormatter.ofPattern(
                                "HH:mm:ss"
                        )
                );

        String statusText =
                successful
                        ? "ALL TESTS PASSED"
                        : "TEST EXECUTION FAILED";

        String statusClass =
                successful
                        ? "success"
                        : "danger";

        StringBuilder html =
                new StringBuilder();

        // =====================================================
        // HTML HEADER
        // =====================================================

        html.append("""
                <!DOCTYPE html>
                <html lang="en">

                <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Java Selenium Automation Test Report</title>

                    <style>

                        * {
                            box-sizing: border-box;
                            margin: 0;
                            padding: 0;
                        }

                        body {
                            font-family:
                                Arial, Helvetica, sans-serif;
                            background: #f4f6f8;
                            color: #263238;
                            line-height: 1.6;
                        }

                        .container {
                            width: 92%;
                            max-width: 1200px;
                            margin: auto;
                        }

                        header {
                            background:
                                linear-gradient(
                                    135deg,
                                    #111827,
                                    #374151
                                );

                            color: white;
                            padding: 35px 0;
                        }

                        .header-content {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            gap: 20px;
                        }

                        h1 {
                            font-size: 30px;
                            margin-bottom: 5px;
                        }

                        .subtitle {
                            color: #d1d5db;
                        }

                        .status-banner {
                            padding: 12px 20px;
                            border-radius: 25px;
                            font-weight: bold;
                        }

                        .success {
                            background: #166534;
                            border: 1px solid #22c55e;
                        }

                        .danger {
                            background: #991b1b;
                            border: 1px solid #ef4444;
                        }

                        .info {
                            background: white;
                            border-bottom: 1px solid #e5e7eb;
                        }

                        .info-grid {
                            display: grid;
                            grid-template-columns:
                                repeat(4, 1fr);
                            gap: 20px;
                            padding: 20px 0;
                        }

                        .info-item span {
                            display: block;
                            color: #6b7280;
                            font-size: 11px;
                            text-transform: uppercase;
                        }

                        .info-item strong {
                            font-size: 14px;
                        }

                        main {
                            padding: 35px 0;
                        }

                        .section {
                            margin-bottom: 35px;
                        }

                        .section-title {
                            font-size: 22px;
                            margin-bottom: 8px;
                        }

                        .section-description {
                            color: #6b7280;
                            margin-bottom: 20px;
                        }

                        .cards {
                            display: grid;
                            grid-template-columns:
                                repeat(5, 1fr);
                            gap: 18px;
                        }

                        .card {
                            background: white;
                            padding: 22px;
                            border-radius: 10px;
                            border: 1px solid #e5e7eb;
                            box-shadow:
                                0 2px 8px
                                rgba(0,0,0,.07);
                        }

                        .card-label {
                            color: #6b7280;
                            font-size: 11px;
                            text-transform: uppercase;
                            font-weight: bold;
                        }

                        .card-value {
                            font-size: 28px;
                            font-weight: bold;
                            margin-top: 8px;
                        }

                        .green {
                            color: #15803d;
                        }

                        .red {
                            color: #dc2626;
                        }

                        .orange {
                            color: #d97706;
                        }

                        .table-wrapper {
                            background: white;
                            border-radius: 10px;
                            overflow-x: auto;
                            border: 1px solid #e5e7eb;
                            box-shadow:
                                0 2px 8px
                                rgba(0,0,0,.07);
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }

                        th {
                            background: #f9fafb;
                            padding: 14px;
                            text-align: left;
                            font-size: 12px;
                            text-transform: uppercase;
                            color: #374151;
                        }

                        td {
                            padding: 14px;
                            border-top:
                                1px solid #f0f0f0;
                            font-size: 14px;
                        }

                        tr:hover {
                            background: #f9fafb;
                        }

                        .badge {
                            display: inline-block;
                            padding: 5px 11px;
                            border-radius: 15px;
                            font-size: 11px;
                            font-weight: bold;
                        }

                        .badge-passed {
                            background: #dcfce7;
                            color: #166534;
                        }

                        .badge-failed {
                            background: #fee2e2;
                            color: #991b1b;
                        }

                        .badge-error {
                            background: #fee2e2;
                            color: #991b1b;
                        }

                        .badge-skipped {
                            background: #fef3c7;
                            color: #92400e;
                        }

                        .failure-box {
                            margin-top: 8px;
                            padding: 10px;
                            background: #fff7f7;
                            border-left:
                                4px solid #dc2626;
                            color: #7f1d1d;
                            font-family: Consolas, monospace;
                            font-size: 12px;
                            white-space: pre-wrap;
                            word-break: break-word;
                        }

                        footer {
                            background: #111827;
                            color: #d1d5db;
                            padding: 25px;
                            text-align: center;
                            font-size: 13px;
                        }

                        @media(max-width:900px) {

                            .cards {
                                grid-template-columns:
                                    repeat(2, 1fr);
                            }

                            .info-grid {
                                grid-template-columns:
                                    repeat(2, 1fr);
                            }

                            .header-content {
                                flex-direction: column;
                                align-items: flex-start;
                            }
                        }

                        @media(max-width:600px) {

                            .cards,
                            .info-grid {
                                grid-template-columns: 1fr;
                            }
                        }

                    </style>

                </head>

                <body>
                """);

        // =====================================================
        // HEADER
        // =====================================================

        html.append("""
                <header>

                    <div class="container header-content">

                        <div>

                            <h1>
                                Java Selenium Automation
                                Test Report
                            </h1>

                            <p class="subtitle">
                                Automated Functional Testing
                                &amp; Web UI Validation
                            </p>

                        </div>

                        <div class="status-banner %s">
                            %s
                        </div>

                    </div>

                </header>
                """.formatted(statusClass, statusText));

        // =====================================================
        // INFORMATION
        // =====================================================

        html.append("""
                <section class="info">

                    <div class="container info-grid">

                        <div class="info-item">
                            <span>Developer</span>
                            <strong>
                                Boitumelo Khauoe
                            </strong>
                        </div>

                        <div class="info-item">
                            <span>Project</span>
                            <strong>
                                java-automation
                            </strong>
                        </div>

                        <div class="info-item">
                            <span>Framework</span>
                            <strong>
                                Selenium WebDriver + JUnit 5
                            </strong>
                        </div>

                        <div class="info-item">
                            <span>Execution</span>
                            <strong>
                                %s %s
                            </strong>
                        </div>

                    </div>

                </section>
                """.formatted(
                formattedDate,
                formattedTime
        ));

        // =====================================================
        // MAIN
        // =====================================================

        html.append("""
                <main>

                    <div class="container">

                        <section class="section">

                            <h2 class="section-title">
                                Test Execution Summary
                            </h2>

                            <p class="section-description">
                                This report was generated directly
                                from the Maven Surefire results
                                produced by the current test run.
                            </p>

                            <div class="cards">
                """);

        appendCard(
                html,
                "Total Tests",
                String.valueOf(total),
                ""
        );

        appendCard(
                html,
                "Passed",
                String.valueOf(passed),
                "green"
        );

        appendCard(
                html,
                "Failures",
                String.valueOf(failed),
                failed > 0 ? "red" : ""
        );

        appendCard(
                html,
                "Errors",
                String.valueOf(errors),
                errors > 0 ? "red" : ""
        );

        appendCard(
                html,
                "Success Rate",
                String.format(
                        Locale.US,
                        "%.2f%%",
                        successRate
                ),
                successful ? "green" : "orange"
        );

        html.append("""
                            </div>

                        </section>
                """);

        // =====================================================
        // TEST RESULTS
        // =====================================================

        html.append("""
                        <section class="section">

                            <h2 class="section-title">
                                Test Results
                            </h2>

                            <p class="section-description">
                                Individual test results captured
                                from the current Maven execution.
                            </p>

                            <div class="table-wrapper">

                                <table>

                                    <thead>

                                        <tr>
                                            <th>Test Class</th>
                                            <th>Test Case</th>
                                            <th>Status</th>
                                            <th>Execution Time</th>
                                        </tr>

                                    </thead>

                                    <tbody>
                """);

        for (TestResult result : results) {

            String badgeClass =
                    switch (result.status) {
                        case "PASSED" -> "badge-passed";
                        case "FAILED" -> "badge-failed";
                        case "ERROR" -> "badge-error";
                        default -> "badge-skipped";
                    };

            html.append("""
                    <tr>

                        <td>%s</td>

                        <td>
                            <strong>%s</strong>

                            %s
                        </td>

                        <td>
                            <span class="badge %s">
                                %s
                            </span>
                        </td>

                        <td>%s s</td>

                    </tr>
                    """.formatted(
                    escapeHtml(
                            simpleClassName(result.className)
                    ),
                    escapeHtml(result.testName),
                    result.message.isBlank()
                            ? ""
                            : """
                              <div class="failure-box">%s</div>
                              """.formatted(
                                    escapeHtml(
                                            result.message
                                    )
                            ),
                    badgeClass,
                    result.status,
                    escapeHtml(result.time)
            ));
        }

        html.append("""
                                    </tbody>

                                </table>

                            </div>

                        </section>
                """);

        // =====================================================
        // CONCLUSION
        // =====================================================

        html.append("""
                        <section class="section">

                            <h2 class="section-title">
                                Test Execution Conclusion
                            </h2>

                            <div class="card">

                                <h3>
                """);

        if (successful) {

            html.append("""
                                    Automation Run Successful
                                </h3>

                                <p>
                                    All %d automated tests passed
                                    successfully.
                                </p>
                    """.formatted(total));

        } else {

            html.append("""
                                    Automation Run Requires Attention
                                </h3>

                                <p>
                                    The test suite completed, but
                                    %d test(s) failed and %d
                                    test(s) encountered errors.
                                    Review the failed and errored
                                    test cases above.
                                </p>
                    """.formatted(
                    failed,
                    errors
            ));
        }

        html.append("""
                            </div>

                        </section>

                    </div>

                </main>

                <footer>

                    <p>
                        <strong>
                            Java Selenium Automation Test Report
                        </strong>
                    </p>

                    <p>
                        Generated automatically from Maven
                        Surefire test results
                    </p>

                    <p>
                        Developer & Automation Tester :
                        <strong>
                            Boitumelo Khauoe
                        </strong>
                    </p>

                </footer>

                </body>
                </html>
                """);

        File output =
                new File(OUTPUT_FILE);

        File parent =
                output.getParentFile();

        if (parent != null) {
            parent.mkdirs();
        }

        try (FileWriter writer =
                     new FileWriter(output)) {

            writer.write(html.toString());
        }
    }

    // =========================================================
    // CARD HELPER
    // =========================================================

    private static void appendCard(
            StringBuilder html,
            String label,
            String value,
            String cssClass) {

        html.append("""
                <div class="card">

                    <div class="card-label">
                        %s
                    </div>

                    <div class="card-value %s">
                        %s
                    </div>

                </div>
                """.formatted(
                label,
                cssClass,
                value
        ));
    }

    // =========================================================
    // COUNT STATUS
    // =========================================================

    private static int countStatus(
            List<TestResult> results,
            String status) {

        int count = 0;

        for (TestResult result : results) {

            if (result.status.equals(status)) {
                count++;
            }
        }

        return count;
    }

    // =========================================================
    // SIMPLE CLASS NAME
    // =========================================================

    private static String simpleClassName(
            String className) {

        if (className == null) {
            return "";
        }

        int index =
                className.lastIndexOf('.');

        return index >= 0
                ? className.substring(index + 1)
                : className;
    }

    // =========================================================
    // HTML ESCAPING
    // =========================================================

    private static String escapeHtml(
            String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // =========================================================
    // OPEN REPORT AUTOMATICALLY
    // =========================================================

    private static void openReport(
            File reportFile) {

        try {

            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop()
                        .browse(
                                reportFile
                                        .toURI()
                        );

                System.out.println(
                        "Report opened automatically."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Report was generated but could not " +
                    "be opened automatically."
            );
        }
    }

    // =========================================================
    // TEST RESULT MODEL
    // =========================================================

    private static class TestResult {

        String className;
        String testName;
        String time;
        String status;
        String message;

        TestResult(
                String className,
                String testName,
                String time,
                String status,
                String message) {

            this.className = className;
            this.testName = testName;
            this.time = time;
            this.status = status;
            this.message = message;
        }
    }
}