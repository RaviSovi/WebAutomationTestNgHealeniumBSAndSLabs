package com.project.testscripts;

import com.project.base.DriverFactory;
import com.project.config.ER;
import com.project.utilities.TestDataLoader;
import com.project.utilities.Utils;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class BasicTest2 extends DriverFactory {

    @BeforeClass(alwaysRun = true)
    public void setTestContext() {
        // Set which Excel file and sheet this test will use
        //TestDataLoader.setContext("testdata_web", "Configuration");
    }

    @Test
    public static void testCase3() throws Exception {
        try {
            ER.Info("Test case 3 started");
            getDriver().get("https://www.amazon.com");
//			String browser = TestDataLoader.get("browser");
//			String app_url = TestDataLoader.get("app_URL");
//
//			System.out.println("browser: " + browser);
//			System.out.println("app_url: " + app_url);
            String title = getDriver().getTitle();
            System.out.println("current tile is " + title);
            //Utils.pauseExecution(5);
            ER.Info("Scrolling to element....");
            Utils.clickOnElement(getByArgument("amazoncom.continueshoppingbutton.primary"));
            //Utils.pauseExecution(5);
            ER.Pass("Scrolled to element successfully..");
            String flipkartTitle = getDriver().getTitle();
            ER.Pass("Title of gmail page is ::::" + flipkartTitle);

        } catch (Exception e) {
            ER.Fail("Failed test case with reason:\n" + e.getMessage());
            e.getMessage();

        }
    }

    @Test
    public static void testCase4() throws Exception {
        try {
            ER.Info("Test case 4 started");
            getDriver().get("https://www.facebook.com");
            String title = getDriver().getTitle();
            System.out.println("current tile is " + title);
            Utils.pauseExecution(5);
            ER.Info("Clicked on button successfully....");
            getDriver().findElement(By.xpath("//a[contains(text(), 'Forgot')]")).click();
            String instaTitle = getDriver().getTitle();
            ER.Pass("Title of gmail page is ::::" + instaTitle);
        } catch (Exception e) {
            ER.Fail("Failed test case with reason:\n" + e.getMessage());
            e.getMessage();

        }
    }
}
