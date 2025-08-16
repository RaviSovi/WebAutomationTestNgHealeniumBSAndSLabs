package com.project.pageobjects;

import com.project.base.DriverFactory;
import com.project.config.ER;
import com.project.utilities.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TMLTestPage {
    private WebDriver driver;

    public TMLTestPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private static ThreadLocal<TMLTestPage> tmltestpage = ThreadLocal.withInitial(() -> {
        return new TMLTestPage(DriverFactory.getDriver());
    });

    public static TMLTestPage getTMLTestPage() {
        return tmltestpage.get();
    }

    public static void removeTMLTestPage() {
        tmltestpage.remove();
    }


    @FindBy(xpath = "(//a[@class='button-regular button-2 cta-btn ga-class'][normalize-space()='Book Now'])[5]")
    private WebElement buynowlink;

    @FindAll({@FindBy(xpath = "//*[@data-optionid='Creative_+_S_CAMO']"),
            @FindBy(xpath = "//*[@data-optionid='Creative_+_S_CAMO']")})
    private WebElement ceativescamoradiobutton;

    @FindAll({@FindBy(css = ".fa.icon-Burger"),
            @FindBy(xpath = "//i[@class='']")})
    private WebElement hamburgermenu;

    public void clickOnBuyNowLink() throws Exception {
        try {
            Utils.clickOnElement(buynowlink);
        } catch (Exception e) {
            ER.Fail("Unable to click on buy now link using web element: " + buynowlink);
            throw new RuntimeException(e);
        }
    }

    public void clickOnCreativeSCAMORadiobutton() throws Exception {
        try {
            Utils.clickOnElement(ceativescamoradiobutton);
        } catch (Exception e) {
            ER.Fail("Unable to click on buy now link using web element: " + ceativescamoradiobutton);
            throw new RuntimeException(e);
        }
    }

    public void clickOnHamBurgerMenu() throws Exception {
        try {
            Utils.clickOnElement(hamburgermenu);
        } catch (Exception e) {
            ER.Fail("Unable to click on buy now link using web element: " + hamburgermenu);
            throw new RuntimeException(e);
        }
    }
}
