package reusableMethods;

import com.microsoft.playwright.*;
import constants.TimeConstants;
import customException.BrowserNotAvailable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class PlaywrightActions {

    protected static ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();
    protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();

    private static final Logger log = LogManager.getLogger(PlaywrightActions.class);

    private static Page mainPage;

    public static void openBrowser(String browserName, String url) {
        boolean scenarioRunMode = false;

        if (System.getProperty("runMode") == null)
            scenarioRunMode = System.getProperty("runMode").equalsIgnoreCase("headless");

        playwright.set(Playwright.create(new Playwright.CreateOptions()));

        switch (browserName) {
            case "chrome":
                browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(scenarioRunMode)));
                break;
            case "firefox":
                browser.set(playwright.get().firefox().launch(new BrowserType.LaunchOptions().setHeadless(scenarioRunMode)));
            case "safari":
                browser.set(playwright.get().webkit().launch(new BrowserType.LaunchOptions().setHeadless(scenarioRunMode)));
            default:
                throw new BrowserNotAvailable("Please make sure that you have entered the correct Browser Name : " + browserName);
        }
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        browserContext.set(browser.get().newContext(new Browser.NewContextOptions().setViewportSize((int) dimension.getWidth(), (int) dimension.getHeight())));
        page.set(browserContext.get().newPage());
        page.get().setDefaultTimeout(TimeConstants.PW_TIMEOUT);
        page.get().navigate(url);
        mainPage =page.get();
    }
}
