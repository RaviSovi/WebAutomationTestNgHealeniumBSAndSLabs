package com.project.utilities;

import com.project.base.DriverFactory;
import com.project.config.CaptureScreenshot;
import com.project.config.ER;
import com.project.config.ReadProperties;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class Utils extends DriverFactory {

    /** Capture screenshot using this method **/
    public static void captureSS() throws Exception {
        String path = CaptureScreenshot.capture();
        ER.Info("taking screenshot", CaptureScreenshot.createMediaEntity(path, "Screenshot"));
    }

    /** Use this method to wait until web page loaded successfully **/
    public static void waitForPageLoad() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        if (js.executeScript("return document.readyState").toString().equals("complete")) {
            System.out.println("Page Loaded properly....");
        }
    }

    /** Use this method to wait until web element available in DOM **/
    public static boolean waitorPresenceofElementLocated(By locator) throws Exception {
        boolean status = false;
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            status = true;
        } catch (Exception e) {
            System.out.println("****** Element is not available in DOM : " + locator + " ****** \n" + e.getMessage());
            ER.Fail("****** Element is not available in DOM with locator (Waited 60 seconds): " + locator + " ****** \n" + e.getMessage());
        }
        return status;
    }

    /** Use this method to wait until web element visible on web page **/
    public static void waitForVisibilityofElementLocated(By locator) throws Exception {
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            ER.Fail("****** Element is not visible with locator (Waited 60 seconds): " + locator + " ****** \n" + e.getMessage());
            e.getMessage();
        }
    }

    /** Scroll to element until its in view. - USING LOCATOR **/
    public static void scrollToElement(By locator) throws Exception {
        /*WebElement el = getDriver().findElement(locator);
        js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({ block: 'center', inline: 'center'});", el);*/

        pauseExecution(5);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        WebElement el = getDriver().findElement(locator);
        try {
            for (int i = 0; i < 20; i++) { // limit attempts
                js.executeScript(
                        "let el = arguments[0];" +
                                "let container = el.parentElement;" +
                                "while (container) {" +
                                "   let style = window.getComputedStyle(container);" +
                                "   if (/(auto|scroll)/.test(style.overflowY) && container.scrollHeight > container.clientHeight) {" +
                                "       container.scrollTop = el.offsetTop - (container.clientHeight / 2) + (el.offsetHeight / 2);" +
                                "       return true;" + // scrolled in container
                                "   }" +
                                "   container = container.parentElement;" +
                                "}" +


                                "window.scrollTo({ " + "top: arguments[0].getBoundingClientRect().top + window.scrollY - (window.innerHeight / 2) + (arguments[0].offsetHeight / 2)," + "behavior: 'instant'" + "});", el);
                // Recalculate after scroll
                Long viewportHeight = ((Number) js.executeScript("return window.innerHeight")).longValue();
                Long elementCenter = ((Number) js.executeScript("return arguments[0].getBoundingClientRect().top + arguments[0].offsetHeight / 2;", el)).longValue();
                if (Math.abs((viewportHeight / 2) - elementCenter) <= 2) {
                    break;
                }
            }
        } catch (Exception e) {
            ER.Fail("****** Unable to scroll to element with locator:" + locator + " ****** \n" + e.getMessage());
            throw new RuntimeException(e);
        }
        //  pauseExecution(10);
    }

    /*** Use this method to click on element using javascript - USING LOCATOR ***/
    public static void jsClick(By locator) throws Exception {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            WebElement el = getDriver().findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", el);
            System.out.println("locator clicked");
            waitForPageLoad();
        } catch (Exception e) {
            ER.Fail("****** Unable to JS click on element with locator:" + locator + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to click on element using click - USING LOCATOR ***/
    public static void clickOnElement(By locator) throws Exception {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            WebElement we = getDriver().findElement(locator);
            we.click();
            System.out.println("locator clicked");
            waitForPageLoad();
        } catch (Exception e) {
            ER.Fail("****** Unable to click on element with locator:" + locator + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to use control click - USING LOCATOR ***/
    public static void controlClick(By locator) throws Exception {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            Actions act = new Actions(getDriver());
            act.keyDown(Keys.CONTROL).click(getDriver().findElement(locator)).build().perform();
            waitForPageLoad();
        } catch (Exception e) {
            ER.Fail("****** Unable to control click on element with locator:" + locator + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to send Keys to text box by javascript - USING LOCATOR ***/
    public static void sendKeysByJSExec(By locator, String inputText) throws Exception {
        try {
            waitForPageLoad();
            waitorPresenceofElementLocated(locator);
            scrollToElement(locator);
            waitForVisibilityofElementLocated(locator);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].value=" + inputText + ";", getDriver().findElement(locator));
        } catch (Exception e) {
            ER.Fail("****** Unable to send Keys to element using JS with locator:" + locator + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to locator array by attribute from Excel. ***/
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

    /*** Use this method to click on element by locator text ***/
    public static void clickOnElementByText(String locatortext) {
        try {
            By locator = getElementByText(locatortext);
            System.out.println(locator);
            jsClick(locator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** Use this method to click on element by locator text ***/
    public static void sendKeysUsingLocatorText(String locatortext, String inputString) {
        try {
            By locator = getElementByText(locatortext);
            System.out.println(locator);
            sendKeysByJSExec(locator, inputString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** Use this method to navigate back on web page ***/
    public static void navigateBack() throws Exception {
        try {
            getDriver().navigate().back();
        } catch (Exception e) {
            ER.Fail("****** Unable to navigate back to page ****** \n" + e.getMessage());
            throw new RuntimeException(e);
        }
        waitForPageLoad();
    }

    /*** Use this method to switch window by index ***/
    public static void switchToWindowByIndex(int index) throws Exception {
        try {
            ArrayList<String> tabwindow = new ArrayList<String>(getDriver().getWindowHandles());
            getDriver().switchTo().window(tabwindow.get(index));
        } catch (Exception e) {
            ER.Fail("****** Unable to switch to window using Index ****** " + index + "\n" + e.getMessage());
            throw new RuntimeException(e);
        }
        waitForPageLoad();
    }

    public static WebElement findElementWithFallback(WebDriver driver, String locatorBaseKey) throws Exception {
        // Primary locator
        String primaryLocator = DrivergetProperty(locatorBaseKey + ".primary");
        try {
            return driver.findElement(getByArgument(primaryLocator));
        } catch (Exception e) {
            System.out.println("[WARN] Primary locator failed for: " + locatorBaseKey + ". Trying backups...");
        }

        // Backups with their own types
        int backupIndex = 1;
        while (true) {
            String backupLocator = DrivergetProperty(locatorBaseKey + ".backup" + backupIndex);

            if (backupLocator == null || backupLocator.trim().isEmpty()) {
                break; // No more backups
            }
            try {
                return driver.findElement(getByArgument(backupLocator));
            } catch (Exception e) {
                backupIndex++;
            }
        }
        throw new RuntimeException("No locator found for key: " + locatorBaseKey);
    }

    public static void pauseExecution(int seconds) {
        System.out.println("Paused Execution for: " + seconds + " Seconds.");
        Actions act = new Actions(getDriver());
        act.pause(Duration.ofSeconds(seconds)).build().perform();
    }

    //************************** Below methods are only need to use when you have web element *********************************//

    /*** Use this method to wait until web element is clickable on web page - USING WEB ELEMENT ***/
    public static void waitForElementToBeClickable(WebElement webElement) throws Exception {
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception e) {
            ER.Fail("****** Element is not clickable with web element (Waited 60 seconds): " + webElement + " ****** \n" + e.getMessage());
            e.getMessage();
        }
    }

    /*** Use this method to wait until web element visible on web page - USING WEB ELEMENT ***/
    public static void waitForVisibilityOfElement(WebElement webElement) throws Exception {
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
            wait.until(ExpectedConditions.visibilityOf(webElement));
        } catch (Exception e) {
            ER.Fail("****** Element is not visible with web element (Waited 60 seconds): " + webElement + " ****** \n" + e.getMessage());
            e.getMessage();
        }
    }

    /*** Scroll to element until its in view. - USING WEB ELEMENT ***/
    public static void scrollToElement(WebElement webElement) throws Exception {
        /*WebElement el = getDriver().findElement(locator);
        js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView({ block: 'center', inline: 'center'});", webElement);*/

        // pauseExecution(2);

        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        try {
            for (int i = 0; i < 20; i++) { // limit attempts
                js.executeScript(
                        "let el = arguments[0];" +
                                "let container = el.parentElement;" +
                                "while (container) {" +
                                "   let style = window.getComputedStyle(container);" +
                                "   if (/(auto|scroll)/.test(style.overflowY) && container.scrollHeight > container.clientHeight) {" +
                                "       container.scrollTop = el.offsetTop - (container.clientHeight / 2) + (el.offsetHeight / 2);" +
                                "       return true;" + // scrolled in container
                                "   }" +
                                "   container = container.parentElement;" +
                                "}" +


                                "window.scrollTo({ " + "top: arguments[0].getBoundingClientRect().top + window.scrollY - (window.innerHeight / 2) + (arguments[0].offsetHeight / 2)," + "behavior: 'instant'" + "});", webElement);
                // Recalculate after scroll
                Long viewportHeight = ((Number) js.executeScript("return window.innerHeight")).longValue();
                Long elementCenter = ((Number) js.executeScript("return arguments[0].getBoundingClientRect().top + arguments[0].offsetHeight / 2;", webElement)).longValue();
                if (Math.abs((viewportHeight / 2) - elementCenter) <= 2) {
                    break;
                }
            }
        } catch (Exception e) {
            ER.Fail("****** Unable to scroll to element with web element :" + webElement + " ****** \n" + e.getMessage());
            throw new RuntimeException(e);
        }
        // pauseExecution(2);
    }

    /*** Use this method to click on element using javascript - USING WEB ELEMENT ***/
    public static void jsClick(WebElement webElement) throws Exception {
        try {
            waitForPageLoad();
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();", webElement);
            System.out.println("locator clicked");
            waitForPageLoad();
        } catch (Exception e) {
            ER.Fail("****** Unable to JS click on element with web element :" + webElement + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to click on element using click - USING WEB ELEMENT ***/
    public static void clickOnElement(WebElement webElement) throws Exception {
        try {
            waitForPageLoad();
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            webElement.click();
            System.out.println("Web Element clicked");
            waitForPageLoad();
        } catch (Exception e) {
            ER.Fail("****** Unable to click on web element :" + webElement + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to use control click - USING WEB ELEMENT ***/
    public static void controlClick(WebElement webElement) throws Exception {
        try {
            waitForPageLoad();
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            Actions act = new Actions(getDriver());
            act.keyDown(Keys.CONTROL).click(webElement).build().perform();
            waitForPageLoad();
        } catch (Exception e) {
            ER.Fail("****** Unable to control click on web element :" + webElement + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to clear text from text box - USING WEB ELEMENT ***/
    public static void clearText(WebElement webElement) throws Exception {
        try {
            waitForPageLoad();
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            webElement.clear();
        } catch (Exception e) {
            ER.Fail("****** Unable to clear text from web element : " + webElement + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to send Keys to text box by javascript - USING WEB ELEMENT ***/
    public static void sendKeysByJSExec(WebElement webElement, String inputText) throws Exception {
        try {
            waitForPageLoad();
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            clearText(webElement);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].value=" + inputText + ";", webElement);
        } catch (Exception e) {
            ER.Fail("****** Unable to send Keys : " + inputText + " to web element  : " + webElement + " using JS ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /*** Use this method to send Keys to text box - USING WEB ELEMENT ***/
    public static void sendKeys(WebElement webElement, String inputText) throws Exception {
        try {
            waitForPageLoad();
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            clearText(webElement);
            webElement.sendKeys(inputText);
        } catch (Exception e) {
            ER.Fail("****** Unable to send Keys :" + inputText + " to web element  :" + webElement + " ****** \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /** This method will verify whether the element is displayed. **/
    public static void isDisplayed(WebElement webElement) {
        String text = null;
        try {
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            if (webElement.isDisplayed())
            {
                text=webElement.getText();
                ER.Pass(webElement+" is displayed on web page with text : "+text);
            }
        } catch (Exception e) {
           ER.Warning(webElement+" is not displayed on web page with text : "+text+"\n Exception Details: " + e.getMessage());
        }
    }

    /** This method will verify whether the element is not displayed **/
    public static void isNotDisplayed(WebElement webElement) {
        String text = null;
        try {
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            if (!webElement.isDisplayed())
            {
                text=webElement.getText();
                ER.Pass(webElement+" is not displayed on web page with text : "+text);
            }
        } catch (Exception e) {
            ER.Warning(webElement+" is displayed on web page with text : "+text+"\n Exception Details: " + e.getMessage());
        }
    }

    /** This method will verify whether the element is selected. **/
    public static boolean isSelected(WebElement webElement) throws Exception {
        boolean status = false;
        try {
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            if (webElement.isSelected())
                status = true;
        } catch (Exception e) {
            ER.Fail(webElement+" is not selected \n Exception Details: " + e.getMessage());
        }
        return status;
    }

    /** This method will verify whether the element is not selected. **/
    public static boolean isNotSelected(WebElement webElement) throws Exception {
        boolean status = false;
        try {
            if (!webElement.isSelected())
                status = true;
        } catch (Exception e) {
            ER.Fail(webElement+" is selected \n Exception Details: " + e.getMessage());
        }
        return status;
    }

    /** This method will get the text of expected element. **/
    public static String getText(WebElement webElement) {
        String text = null;
        try {
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            text = webElement.getText().trim();
        } catch (Exception e) {
            ER.Info("Exception Details: " + e.getMessage());
        }
        if (null == text) {
            ER.Warning("Unable to get text for element : "+webElement);
            return text;
        } else {
            return text;
        }
    }

    /** This method will get the value of expected element. **/
    public static String getValue(WebElement webElement) {
        String text = null;
        try {
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            text = webElement.getAttribute("value").trim();
        } catch (Exception e) {
            ER.Info("Exception Details: " + e.getMessage());
        }
        if (null == text) {
            ER.Warning("Unable to get value for element : "+webElement);
            return text;
        } else {
            return text;
        }
    }

    /** This method will get the name of expected element for iOS. **/
    public static String getAttribute(WebElement webElement) {
        String text = null;
        try {
            scrollToElement(webElement);
            waitForVisibilityOfElement(webElement);
            text = webElement.getAttribute("label").trim();
        } catch (Exception e) {
            ER.Info("Exception Details: " + e.getMessage());
        }
        if (null == text) {
            ER.Warning("Unable to get Attribute for element : "+webElement);
            return text;
        } else {
            return text;
        }
    }

    /** This method will verify that expected and actual text. **/
    public static void verifyText(WebElement webElement, String expectedText) {
        String actualText = null;
        try {
            actualText = getText(webElement);
            if (actualText.equals(expectedText)) {
                ER.Pass("Text of Web Element : '"+webElement+"' is matching, expected text : "+expectedText+" actual text : "+actualText);
            } else {
                ER.Warning("Text of Web Element : '"+webElement+"' is not matching, expected text : "+expectedText+" actual text : "+actualText);
            }
        } catch (Exception e) {
            ER.Info("Exception Details: " + e.getMessage());
        }
    }

    /** This method will verify that expected and actual text are not matching. **/
    public static void verifyTextNotMatching(WebElement webElement, String expectedText) {
        String actualText = null;
        try {
            actualText = getText(webElement);
            if (!actualText.equals(expectedText)) {
                ER.Pass("Text of Web Element : '"+webElement+"' is not matching, expected text : "+expectedText+" actual text : "+actualText);
            } else {
                ER.Warning("Text of Web Element : '"+webElement+"' is matching, expected text : "+expectedText+" actual text : "+actualText);
            }
        } catch (Exception e) {
            ER.Info("Exception Details: " + e.getMessage());
        }
    }

    /** This method will verify the length of expected and actual text. **/
    public static void verifyTextLength(WebElement webElement, int expectedTextLength) {
        int actualTextLength = 0;
        try {
            actualTextLength = getText(webElement).length();
            if (actualTextLength == (expectedTextLength)) {
                ER.Pass("Text length of Web Element : '"+webElement+"' is matching, expected text length : "+expectedTextLength+" actual text length : "+actualTextLength);
            } else {
                ER.Warning("Text length of Web Element : '"+webElement+"' is not matching, expected text length : "+expectedTextLength+" actual text length : "+actualTextLength);
            }
        } catch (Exception e) {
            ER.Info("Exception Details: " + e.getMessage());
        }
    }
}
