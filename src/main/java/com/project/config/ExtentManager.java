package com.project.config;

import java.io.File;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.project.base.DriverFactory;

public class ExtentManager extends DriverFactory {

    public static ExtentReports extent;
    public static String fileSeparator = System.getProperty("file.separator");
    public static String reportFilePath = System.getProperty("user.dir") + fileSeparator + "TestReports";
    public static String reportFileLocation = reportFilePath + fileSeparator + reportFileName;

    /**
     * Get an extent report instance
     **/
    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }

    /**
     * Create the report path
     **/
    public static String getReportPath(String path) {
        File testDirectory = new File(path);

        if (testDirectory.exists() && testDirectory.isDirectory()) {
            String[] files = testDirectory.list();
            if (files != null) {
                for (String fileName : files) {
                    File file = new File(testDirectory, fileName);
                    if (file.delete()) {
                        System.out.println("Deleted file: " + file.getName());
                    } else {
                        System.err.println("Failed to delete file: " + file.getName());
                    }
                }
            }
        }

        if (!testDirectory.exists()) {
            if (testDirectory.mkdirs()) {
                System.out.println("Directory created: '" + path + "'");
            } else {
                System.err.println("Failed to create directory: '" + path + "'");
                return System.getProperty("user.dir"); // fallback path
            }
        } else {
            System.out.println("Directory already exists: '" + path + "'");
        }

        // Return desired report file location (assumed defined somewhere)
        return reportFileLocation;
    }

    /**
     * Create an extent report instance
     **/
    public static ExtentReports createInstance() {
        String fileName = getReportPath(reportFilePath);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle(reportFileName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName(reportFileName);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("OS", "Windows");
        extent.setSystemInfo("AUT", "QA");

        return extent;
    }
}
