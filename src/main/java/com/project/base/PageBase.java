package com.project.base;

import com.project.config.ReadProperties;
import com.project.utilities.TestDataLoader;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;

public class PageBase extends ReadProperties {
    public static String reportFileName;
    public static String Execution = "";

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private static final String SAUCELABS_USERNAME = getProperty("slabs.username");
    private static final String SAUCELABS_ACCESS_KEY = getProperty("slabs.accesskey");
    public static final String SAUCELABS_URL = getProperty("slabs.eu-cental.url");

    private static final String BROWSERSTACK_USERNAME = getProperty("bs.username");
    private static final String BROWSERSTACK_ACCESS_KEY = getProperty("bs.accesskey");
    public static final String BROWSERSTACK_URL = getProperty("bs.url");

    private static String getProperty(String key) {
        try {
            return DrivergetProperty(key);
        } catch (Exception e) {
            System.err.println("Failed to load property: " + key);
            return "";
        }
    }

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--start-maximized",
                "--disable-infobars",
                "--disable-extensions",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--remote-allow-origins=*",
                "--incognito",
                "--disable-popup-blocking",
                "--disable-blink-features=AutomationControlled",
                "--disable-notifications"
        );
        return options;
    }

    public static EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--inprivate", "--start-maximized");
        return options;
    }

    public static HashMap<String, Object> getBrowserStackOptions(String testName, String buildName, String className) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("os", "Windows");
        options.put("osVersion", "10");
        options.put("browserVersion", "latest");
        options.put("projectName", "Company Web Automation");
        options.put("buildName", buildName);
        options.put("sessionName", className + "." + testName);
        options.put("userName", BROWSERSTACK_USERNAME);
        options.put("accessKey", BROWSERSTACK_ACCESS_KEY);
        options.put("seleniumVersion", "4.33.0");
        options.put("selfHeal", "true");
        return options;
    }

    public static HashMap<String, Object> getSauceLabsOptions(String testName, String buildName, String className) {
        HashMap<String, Object> options = new HashMap<>();
        options.put("username", SAUCELABS_USERNAME);
        options.put("accessKey", SAUCELABS_ACCESS_KEY);
        options.put("platformName", "Windows 11");
        options.put("browserVersion", "latest");
        options.put("build", buildName);
        options.put("name", className + "." + testName);
        return options;
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) throw new IllegalStateException("WebDriver is not initialized.");
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void removeDriver() {
        System.out.println("Removing Thread " + Thread.currentThread().getId());
        driver.remove();
    }

    public static void quitDriver() {
        try {
            getDriver().quit();
        } catch (Exception e) {
            System.err.println("Error while quitting driver: " + e.getMessage());
        }
    }

    public static void updateCloudExecutionStatus(WebDriver driver, ITestResult result, String platform) {
        String status;
        String reason = "";

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> {
                status = "passed";
                reason = "Passed";
            }
            case ITestResult.FAILURE -> {
                status = "failed";
                reason = result.getThrowable() != null ? result.getThrowable().toString() : "Failed";
            }
            default -> {
                status = "skipped";
                reason = result.getThrowable() != null ? result.getThrowable().toString() : "Skipped";
            }
        }

        try {
            if (platform.equalsIgnoreCase("browserstack")) {
                JSONObject executorObject = new JSONObject();
                JSONObject argumentsObject = new JSONObject();
                argumentsObject.put("status", status);
                argumentsObject.put("reason", reason);
                executorObject.put("action", "setSessionStatus");
                executorObject.put("arguments", argumentsObject);
                ((JavascriptExecutor) driver).executeScript(String.format("browserstack_executor: %s", executorObject));
            } else if (platform.equalsIgnoreCase("saucelabs")) {
                ((JavascriptExecutor) driver).executeScript("sauce:job-result=" + status);
            }
        } catch (Exception e) {
            System.out.println("Unable to update execution status: " + e.getMessage());
        }
    }

    public static boolean isHealeniumEnabled() throws Exception {
        return Boolean.parseBoolean(DrivergetProperty("use.healenium"));
    }

    @BeforeSuite(alwaysRun = true)
    public void loadTestData() {
        TestDataLoader.loadAllExcelData("src\\test\\resources\\WebAutomation\\TestData\\");
    }
}