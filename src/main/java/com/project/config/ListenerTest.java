package com.project.config;

import com.project.base.DriverFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;

public class ListenerTest extends DriverFactory implements ITestListener {
	
	@Override
	public void onFinish(ITestContext context)
	{
		System.out.println("**** Test Suite "+context.getName()+" ending ****");
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();
	}
	
	@Override
	public void onStart(ITestContext context)
	{
		System.out.println("**** Test Suite "+context.getName()+" started ****");
	}
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result)
	{
		System.out.println("**** Test failed but within percentage % "+result.getMethod().getMethodName());
	}
	
	@Override
	public void onTestFailure(ITestResult result)
	{
		System.out.println("**** Test "+result.getMethod().getMethodName()+" Test failed.");
		ExtentTestManager.getTest().log(Status.FAIL, result.getMethod().getMethodName()+" Test failed.");
    }
	
	@Override
	public void onTestSkipped(ITestResult result)
	{
		System.out.println("**** Test "+result.getMethod().getMethodName()+" Test skipped.");
		ExtentTestManager.getTest().log(Status.SKIP, result.getMethod().getMethodName()+" Test skipped.");
	}
	
	@Override
	public void onTestStart(ITestResult result)
	{
		System.out.println("**** Running test method "+result.getMethod().getMethodName()+" ....");
		ExtentTestManager.startTest(result.getMethod().getMethodName());
	}
	
	@Override
	public void onTestSuccess(ITestResult result)
	{
		System.out.println("**** Executed "+result.getMethod().getMethodName()+" test successfully ....");
		ExtentTestManager.getTest().log(Status.PASS, result.getMethod().getMethodName()+" Test passed.");
	}
}
