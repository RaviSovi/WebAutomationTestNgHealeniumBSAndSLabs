package com.project.base;

import com.epam.healenium.SelfHealingDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.net.URL;
import java.time.Duration;

public class DriverFactory extends PageBase {

    @BeforeMethod(alwaysRun = true)
    @Parameters({"execution", "browser"})
    public void setupDriver(String execution, String browser, Method method, ITestContext context) throws Exception {
        Execution = execution;
        String className = context.getAllTestMethods()[0].getRealClass().getSimpleName();
        WebDriver webDriver = null;
        System.out.println("Test cases are running on: '"+Execution+"' and '"+browser+"' Browser.");

        switch (execution.toLowerCase()) {
            case "local" -> {
                if (browser.equalsIgnoreCase("chrome")) {
                    WebDriverManager.chromedriver().setup();
                    webDriver = new ChromeDriver(getChromeOptions());
                } else if (browser.equalsIgnoreCase("edge")) {
                    webDriver = new EdgeDriver(getEdgeOptions());
                }
            }
            case "browserstack" -> {
                var options = browser.equalsIgnoreCase("chrome") ? getChromeOptions() : getEdgeOptions();
                options.setCapability("bstack:options", getBrowserStackOptions(method.getName(), context.getSuite().getName(), className));
                if (isHealeniumEnabled()) {
                    webDriver = new RemoteWebDriver(new URL("http://localhost:8085/wd/hub"), options);
                } else {
                    webDriver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), options);
                }
            }
            case "saucelabs" -> {
                var options = browser.equalsIgnoreCase("chrome") ? getChromeOptions() : getEdgeOptions();
                options.setCapability("sauce:options", getSauceLabsOptions(method.getName(), context.getSuite().getName(), className));
                webDriver = new RemoteWebDriver(new URL(SAUCELABS_URL), options);
            }
        }

        if (isHealeniumEnabled()) {
            System.out.println("Launching with Healenium SelfHealingDriver");
            setDriver(SelfHealingDriver.create(webDriver));
        } else {
            System.out.println("Launching with regular Selenium WebDriver");
            setDriver(webDriver);
        }

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        getDriver().manage().deleteAllCookies();
    }

    @AfterMethod(alwaysRun = true)
    @Parameters({"execution"})
    public void tearDown(String platform, ITestResult result) {
        updateCloudExecutionStatus(getDriver(), result, platform);
        quitDriver();
        removeDriver();
    }

    @BeforeSuite(alwaysRun = true)
    @Parameters({"execution"})
    public void initExtentReportName(String execution, ITestContext context) {
        reportFileName = context.getSuite().getName() + " " + execution + ".html";
    }
}