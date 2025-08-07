package com.project.utilities;

import com.project.base.DriverFactory;
import com.project.config.CaptureScreenshot;
import com.project.config.ER;
import com.project.config.ReadProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class Utils extends DriverFactory {
    private static JavascriptExecutor js;
    private static Actions act;

    /** Capture screenshot using this method **/
    public static void captureSS() throws Exception {
        String path = CaptureScreenshot.capture();
        ER.Info("taking screenshot", CaptureScreenshot.createMediaEntity(path, "Screenshot"));
    }

    /** Use this method to wait until web page loaded successfully **/
    public static void waitForPageLoad() {
        js = (JavascriptExecutor) getDriver();
        if (js.executeScript("return document.readyState").toString().equals("complete")) {
            System.out.println("Page Loaded properly....");
        }
    }

    /** Use this method to wait until web element available in DOM **/
    public static boolean waitorPresenceofElementLocated(By locator) {
        boolean status = false;
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            status = true;
        } catch (Exception e) {
            System.out.println("****** Element is not available in DOM : " + locator + " ******");
        }
        return status;
    }

    /** Use this method to wait until web element visible on web page **/
    public static void waitForVisibilityofElementLocated(By locator) {
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /** Scroll to element until its in view. **/
    public static void scrollToElement(By locator) {
        WebElement el = getDriver().findElement(locator);
        js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({ block: 'center', inline: 'center', behavior: 'smooth' });", el);
    }

    /** Use this method to click on element using java script **/
    public static void jsClick(By locator) {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            WebElement el = getDriver().findElement(locator);
            js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", el);
            System.out.println("locator clicked");
            waitForPageLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Use this method to click on element using click **/
    public static void clickOnElement(By locator) {
        try {
                waitForPageLoad();
                waitorPresenceofElementLocated(locator);
                scrollToElement(locator);
                waitForVisibilityofElementLocated(locator);
                WebElement we = getDriver().findElement(locator);
                we.click();
                waitForPageLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Use this method to use control click **/
    public static void controlClick(By locator) {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            act = new Actions(getDriver());
            act.keyDown(Keys.CONTROL).click(getDriver().findElement(locator)).build().perform();
            waitForPageLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendKeysByJSExec(By locator, String inputText) {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].value=" + inputText + ";", getDriver().findElement(locator));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Use this method to locator array by attribute from excel. **/
    public static String[] getLocatorArrayByAttribute() {
        String locatorString = "a;i;button;span;input;div;p";
        System.out.println(locatorString);
        String[] locatorarr = locatorString.split(";");
        return locatorarr;
    }

    public static By getElementByText(String locatortext) {
        By locatorXpath = null;
        try {
            String locator = null;
            String locatorString = null;
            waitForPageLoad();
            for (int i = 0; i < getLocatorArrayByAttribute().length; i++) {
                String attribute = getLocatorArrayByAttribute()[i];
                locatorString = "//" + attribute + "[normalize-space(text())='" + locatortext + "']";
                locator = "xpath;" + locatorString;
                System.out.println(locator);
                if (waitorPresenceofElementLocated(ReadProperties.getByArgumentFromString(locator))) {
                    locatorXpath = ReadProperties.getByArgumentFromString(locator);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locatorXpath;
    }

    /** Use this method to click on element by locator text **/
    public static void clickOnElementByText(String locatortext) {
        try {
            By locator = getElementByText(locatortext);
            System.out.println(locator);
            jsClick(locator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Use this method to click on element by locator text **/
    public static void sendKeysUsingLocatorText(String locatortext, String inputString) {
        try {
            By locator = getElementByText(locatortext);
            System.out.println(locator);
            sendKeysByJSExec(locator, inputString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Use this method to navigate back on web page **/
    public static void navigateBack() {
        getDriver().navigate().back();
        waitForPageLoad();
    }

    /** Use this method to switch window by index **/
    public static void swithcToWindowByIndex(int index) {
        ArrayList<String> tabwindow = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabwindow.get(index));
        waitForPageLoad();
    }
}
