package com.project.testscripts;

import com.project.base.DriverFactory;
import com.project.config.ER;
import com.project.pageobjects.TMLTestPage;
import com.project.utilities.PageManager;
import com.project.utilities.Pages;
import com.project.utilities.TestDataLoader;
import com.project.utilities.Utils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TMLTest extends DriverFactory {

    @BeforeClass(alwaysRun = true)
    public void setTestContext() {
        // Set which Excel file and sheet this test will use
        //TestDataLoader.setContext("testdata_web", "LoginTestData");
    }

    @Test
    public void testCase1() throws Exception {
        try {
            ER.Info("Test case 1 started");
            System.out.println("Started Thread " + Thread.currentThread().getId());
            getDriver().get("https://cars.tatamotors.com");
//            String username = TestDataLoader.get("username");
//            String password = TestDataLoader.get("password");
//
//            System.out.println("Username: " + username);
//            System.out.println("Password: " + password);
            String title = getDriver().getTitle();
            System.out.println("current title is " + title);
            ER.Info("Click on Buy now for Tata Punch....");
            Pages.getTMLTestPage().clickOnBuyNowLink();
            Utils.captureSS();
            Pages.getTMLTestPage().clickOnCreativeSCAMORadiobutton();
            Utils.captureSS();
            ER.Pass("Creative S CAMO Radio button selected successfully..");
            String flipkartTitle = getDriver().getTitle();
            ER.Pass("Title of current page is ::::" + flipkartTitle);
        } catch (Exception e) {
            ER.Fail("Failed test case with reason:\n" + e.getMessage());
            e.getMessage();

        }
    }

    @Test
    public void testCase2() throws Exception {
        try {
            //TMLTestPage tmltestpage = new TMLTestPage(getDriver());
            ER.Info("Test case 2 started");
            System.out.println("Started Thread " + Thread.currentThread().getId());
            getDriver().get("https://cars.tatamotors.com");
            String title = getDriver().getTitle();
            System.out.println("current page title is " + title);
            //Utils.pauseExecution(1);
            ER.Info("Clicking on hamburger menu..");
            PageManager.getPage(TMLTestPage.class).clickOnHamBurgerMenu();
            String instaTitle = getDriver().getTitle();
            ER.Pass("Title of current page is ::::" + instaTitle);
            Utils.pauseExecution(1);
        } catch (Exception e) {
            ER.Fail("Failed test case with reason:\n" + e.getMessage());
            e.getMessage();
        }
    }
}
