package com.project.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ReadProperties {
    public static Properties pro;
    public static File f;

    public static Properties loadProperties() throws Exception {
        if (pro == null) {
            pro = new Properties();

            f = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\WebAutomation\\PropertyFiles\\");
            System.out.println("**** Web Automation locator file executed.");
            File[] file = f.listFiles();
            for (int i = 0; i < file.length; i++) {
                pro.load(new FileInputStream(file[i]));
            }
        }
        return pro;
    }

    public static String DrivergetProperty(String prop) throws Exception {
        pro = loadProperties();
        return pro.getProperty(prop);
    }

    public static By getByArgument(String property) throws Exception {
        By by = null;
        String byValue = DrivergetProperty(property).split(";")[1];
        String byType = DrivergetProperty(property).split(";")[0];

        switch (byType.toUpperCase()) {
            case "ID":
                by = By.id(byValue);
                break;

            case "CLASSNAME":
                by = By.className(byValue);
                break;

            case "XPATH":
                by = By.xpath(byValue);
                break;
        }
        return by;
    }

    public static By getByArgumentFromString(String property) throws Exception {
        By by = null;
        String byValue = property.split(";")[1];
        String byType = property.split(";")[0];

        switch (byType.toUpperCase()) {
            case "ID":
                by = By.id(byValue);
                break;

            case "CLASSNAME":
                by = By.className(byValue);
                break;

            case "XPATH":
                by = By.xpath(byValue);
                break;
        }
        return by;
    }

    @SuppressWarnings("unused")
    public static void storeElementAttributes(WebElement element, String key) throws Exception {

        // Initialize properties
        Properties properties = new Properties();
        File propertiesFile = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\WebAutomation\\PropertyFiles\\locators.properties");

        // Load existing properties if the file exists
        if (propertiesFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                properties.load(fis);
            }
        } else {
            // Create the file if it doesn't exist
            propertiesFile.createNewFile();
        }

        // Build XPath dynamically based on element attributes
        StringBuilder xpathBuilder = new StringBuilder("//*");
        xpathBuilder.append("[");
        for (String attribute : new String[]{"id", "class", "name", "type", "value", "href"}) {
            String attributeValue = element.getAttribute(attribute);
            if (attributeValue != null && !attributeValue.isEmpty()) {
                xpathBuilder.append("@").append(attribute).append("='").append(attributeValue).append("' or ");
            }
        }

        // Remove the trailing " or "
        if (xpathBuilder.toString().endsWith(" or ")) {
            xpathBuilder.setLength(xpathBuilder.length() - 4);
        }
        xpathBuilder.append("]");

        // Store the dynamically generated XPath in the properties object
        properties.setProperty(key, xpathBuilder.toString());

        // Write the properties to the file manually to avoid escaping
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(propertiesFile))) {
            for (String propertyKey : properties.stringPropertyNames()) {
                writer.write(propertyKey + "=" + properties.getProperty(propertyKey));
                writer.newLine();
            }
        }

        System.out.println("Element attributes stored with key: " + key);
    }
}
