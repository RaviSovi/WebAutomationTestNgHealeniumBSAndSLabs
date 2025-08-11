package com.project.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TestDataLoader {

    // Nested structure: fileName -> sheetName -> key -> value
    private static final Map<String, Map<String, Map<String, String>>> dataCache = new ConcurrentHashMap<>();

    // ThreadLocal context for current file & sheet for parallel safety
    private static final ThreadLocal<String> currentFile = new ThreadLocal<>();
    private static final ThreadLocal<String> currentSheet = new ThreadLocal<>();

    private TestDataLoader() {
        // Prevent instantiation
    }

    /**
     * Loads all Excel files and sheets from the given folder into memory.
     * Call this ONCE in @BeforeSuite.
     */
    public static void loadAllExcelData(String folderPath) {
        try {
            File folder = new File(folderPath);
            File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xlsx"));

            if (files == null) {
                throw new RuntimeException("No Excel files found in: " + folderPath);
            }

            for (File file : files) {
                String fileNameWithoutExt = file.getName().replaceFirst("[.][^.]+$", ""); // remove extension
                Map<String, Map<String, String>> sheetMap = new ConcurrentHashMap<>();

                try (FileInputStream fis = new FileInputStream(file);
                     Workbook workbook = new XSSFWorkbook(fis)) {

                    for (Sheet sheet : workbook) {
                        Map<String, String> keyValueMap = new ConcurrentHashMap<>();

                        for (Row row : sheet) {
                            Cell keyCell = row.getCell(0);
                            Cell valueCell = row.getCell(1);

                            if (keyCell != null && valueCell != null) {
                                keyValueMap.put(keyCell.toString().trim(), valueCell.toString().trim());
                            }
                        }
                        sheetMap.put(sheet.getSheetName(), keyValueMap);
                    }
                }
                dataCache.put(fileNameWithoutExt, sheetMap);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading Excel data: " + e.getMessage(), e);
        }
    }

    /**
     * Sets the current Excel context for this thread.
     */
    public static void setContext(String fileName, String sheetName) {
        currentFile.set(fileName);
        currentSheet.set(sheetName);
    }

    /**
     * Gets the value for a given key from the current thread's file/sheet context.
     */
    public static String get(String key) {
        String fileName = currentFile.get();
        String sheetName = currentSheet.get();

        if (fileName == null || sheetName == null) {
            throw new IllegalStateException("TestDataLoader context not set. Call setContext(fileName, sheetName) first.");
        }

        return Optional.ofNullable(dataCache.get(fileName))
                .map(sheets -> sheets.get(sheetName))
                .map(kv -> kv.get(key))
                .orElseThrow(() -> new RuntimeException("Key '" + key + "' not found in " + fileName + " -> " + sheetName));
    }

    /**
     * Gets the entire map for the current context.
     */
    public static Map<String, String> getAll() {
        String fileName = currentFile.get();
        String sheetName = currentSheet.get();

        if (fileName == null || sheetName == null) {
            throw new IllegalStateException("TestDataLoader context not set. Call setContext(fileName, sheetName) first.");
        }
        return Collections.unmodifiableMap(dataCache.get(fileName).get(sheetName));
    }
}
