package com.project.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.epam.healenium.SelfHealingDriver;
import com.project.base.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.MediaEntityModelProvider;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class CaptureScreenshot extends DriverFactory {
    private static final String SCREENSHOT_DIR = "Screenshots";

    /**
     * This method will capture screenshot during execution and save it at destination path.
     **/
    public static String capture() throws Exception {
        WebDriver webDriver = getDriver();
        if (webDriver instanceof SelfHealingDriver) {
            webDriver = ((SelfHealingDriver) webDriver).getDelegate();
        }

        File srcFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        Path screenshotDir = Paths.get(SCREENSHOT_DIR);
        if (!Files.exists(screenshotDir)) {
            Files.createDirectories(screenshotDir);
        }
        File destFile = screenshotDir.resolve(System.currentTimeMillis() + ".png").toFile();
        FileUtils.copyFile(srcFile, destFile);
        return destFile.getAbsolutePath();
    }

    /**
     * This method will create media entity with file path and title
     **/
    @SuppressWarnings("finally")
    public static MediaEntityModelProvider createMediaEntity(String filePath, String title) {
        MediaEntityModelProvider mediaEntityModelProvider = null;
        try {
            mediaEntityModelProvider = MediaEntityBuilder.createScreenCaptureFromPath(filePath, title).build();
        } finally {
            return mediaEntityModelProvider;
        }
    }
}
