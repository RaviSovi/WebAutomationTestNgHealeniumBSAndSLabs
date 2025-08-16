package com.project.utilities;

import org.openqa.selenium.StaleElementReferenceException;

import java.util.HashMap;
import java.util.Map;

public class PageManager {
    /** ThreadLocal Map for per-thread page storage **/
    private static ThreadLocal<Map<Class<?>, Object>> pages = ThreadLocal.withInitial(HashMap::new);

    /**
     * Get page object for the given class.
     * If not present, creates it.
     * If stale, reinitializes it automatically.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPage(Class<T> pageClass) {
        Map<Class<?>, Object> pageMap = pages.get();

        try {
            // If page not in map, create it
            if (!pageMap.containsKey(pageClass)) {
                pageMap.put(pageClass, createPageInstance(pageClass));
            }
            return (T) pageMap.get(pageClass);

        } catch (StaleElementReferenceException staleEx) {
            System.out.println("[PageManager] Detected stale elements in " + pageClass.getSimpleName() + ". Reinitializing...");
            pageMap.remove(pageClass);
            pageMap.put(pageClass, createPageInstance(pageClass));
            return (T) pageMap.get(pageClass);
        }
    }

    /** Factory method to create page instance **/
    private static <T> T createPageInstance(Class<T> pageClass) {
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not create page: " + pageClass.getName(), e);
        }
    }

    /** Remove all page objects for the current thread **/
    public static void unload() {
        pages.remove();
    }
}
