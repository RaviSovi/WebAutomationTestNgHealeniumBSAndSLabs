package com.project.utilities;

import com.project.pageobjects.TMLTestPage;

public class Pages {
    private static ThreadLocal<TMLTestPage> tmltestpage = ThreadLocal.withInitial(TMLTestPage::new);

    public static TMLTestPage getTMLTestPage() {
        return tmltestpage.get();
    }

    /** Clean up method to remove ThreadLocals after each test **/
    public static void unloadPages() {
        tmltestpage.remove();
    }
}
