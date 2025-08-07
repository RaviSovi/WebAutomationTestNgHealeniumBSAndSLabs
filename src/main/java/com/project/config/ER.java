package com.project.config;

import java.io.IOException;
import java.util.logging.Logger;
import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.project.base.DriverFactory;

public class ER extends DriverFactory {
    private static final Logger logger = Logger.getLogger(ER.class.getName());
    private static final String SEPARATOR = "********************************************************************************";

    private static void logConsole(String msg) {
        logger.info(SEPARATOR);
        logger.info(msg);
        logger.info(SEPARATOR);
    }

    /**
     * This method will log the status of the test step along with message in extent report and console
     **/
    public static void Pass(String msg) {
        ExtentTestManager.getTest().log(Status.PASS, msg);
        logConsole(msg);
    }

    /**
     * This method will log the status of the test step along with message in extent report and console
     **/
    public static void Info(String msg) {
        ExtentTestManager.getTest().log(Status.INFO, msg);
        logConsole(msg);
    }

    /**
     * This method will log the status of the test step along with message in extent report and console
     **/
    public static void Info(String msg, MediaEntityModelProvider provider) {
        ExtentTestManager.getTest().log(Status.INFO, msg, provider);
        logConsole(msg);
    }

    /**
     * This method will log the status of the test step along with message in extent report and console
     **/
    public static void Warning(String msg) {
        ExtentTestManager.getTest().log(Status.WARNING, msg);
        logConsole(msg);
    }

    /**
     * This method will log the status of the test step along with message in extent report and console
     **/
    public static void Fail(String msg) throws IOException, Exception {
        ExtentTestManager.getTest().log(Status.FAIL, msg + ExtentTestManager.getTest().addScreenCaptureFromPath(CaptureScreenshot.capture()));
        logConsole(msg);
        throw new Exception(msg);
    }
}
