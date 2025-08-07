package com.project.testscripts;

import com.project.base.DriverFactory;
import com.project.config.ER;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class BasicTest extends DriverFactory {

	@Test
	public static void testCase1() throws Exception {
		try {
			ER.Info("Test case 1 started");
			getDriver().get("https://www.flipkart.com");
			String title = getDriver().getTitle();
			System.out.println("current tile is " + title);
			Thread.sleep(5000);
			ER.Info("Scrolling to element....");
			//WebElement el= getDriver().findElement(By.xpath("//*[text()='Flipkart: The One-stop Shopping Destination']"));
			//Utils.scrollToElement("//*[text()='Flipkart: The One-stop Shopping Destination']");
			Thread.sleep(5000);
			ER.Pass("Scrolled to element successfully..");
			String flipkartTitle = getDriver().getTitle();
			ER.Pass("Title of gmail page is ::::" + flipkartTitle);

		} catch (Exception e) {
			ER.Fail("Failed test case with reason:\n"+e.getMessage());
			e.getMessage();

		}
	}

	@Test
	public static void testCase2() throws Exception {
		try {
			ER.Info("Test case 2 started");
			getDriver().get("https://www.facebook.com");
			String title = getDriver().getTitle();
			System.out.println("current tile is " + title);
			Thread.sleep(5000);
			ER.Info("Clicked on button successfully....");
			getDriver().findElement(By.xpath("//a[contains(text(), 'Forgot')]")).click();
			String instaTitle = getDriver().getTitle();
			ER.Pass("Title of gmail page is ::::" + instaTitle);
		} catch (Exception e) {
			ER.Fail("Failed test case with reason:\n"+e.getMessage());
			e.getMessage();

		}
	}
}
