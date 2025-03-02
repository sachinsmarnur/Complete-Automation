package reusableMethods;

import com.microsoft.playwright.*;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.*;
import constants.TimeConstants;
import customException.BrowserNotAvailable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlaywrightActions {

    protected static ThreadLocal<BrowserContext> browserContext = new ThreadLocal<>();
    protected static ThreadLocal<Browser> browser = new ThreadLocal<>();
    protected static ThreadLocal<Page> page = new ThreadLocal<>();
    protected static ThreadLocal<Playwright> playwright = new ThreadLocal<>();

    private static final Logger log = LogManager.getLogger(PlaywrightActions.class);

    private static Page mainPage;

    public static void openBrowser(String browserName, String url) {
        boolean scenarioRunMode = false;

        if (System.getProperty("runMode") != null)
            scenarioRunMode = System.getProperty("runMode").equalsIgnoreCase("headless");

        playwright.set(Playwright.create(new Playwright.CreateOptions()));

        switch (browserName) {
            case "chrome":
                browser.set(playwright.get().chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(scenarioRunMode)));
                break;
            case "firefox":
                browser.set(playwright.get().firefox().launch(new BrowserType.LaunchOptions().setHeadless(scenarioRunMode)));
                break;
            case "safari":
                browser.set(playwright.get().webkit().launch(new BrowserType.LaunchOptions().setHeadless(scenarioRunMode)));
                break;
            default:
                throw new BrowserNotAvailable("Please make sure that you have entered the correct Browser Name : " + browserName);
        }
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        browserContext.set(browser.get().newContext(new Browser.NewContextOptions().setViewportSize((int) dimension.getWidth(), (int) dimension.getHeight())));
        page.set(browserContext.get().newPage());
        page.get().setDefaultTimeout(TimeConstants.PW_TIMEOUT);
        page.get().navigate(url);
        mainPage = page.get();
    }

    public static void closeBrowser() {
        log.info("Closing the browser context.");
        browserContext.get().pages().forEach(Page::close);

        if (browser != null) {
            log.info("Closing the browser context.");
            browser.get().contexts().forEach(BrowserContext::close);
        }

        if (playwright.get() != null) {
            log.info("Closing Playwright.");
            playwright.get().close();
        }
    }

    public void closeTab() {
        page.get().close();
        log.info("Tabs after close: {}", browserContext.get().pages().size());
    }

    public static String getBrowserVersion() {
        return browser.get().version();
    }

    public byte[] takeScreenshot() {
        return page.get().screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    public Locator getLocator(String locator) {
        return page.get().locator(locator);
    }

    public void enterText(String locator, String value) {
        getLocator(locator).fill(value);
    }

    public void typeText(String locator, String value) {
        getLocator(locator).type(value);
    }

    public void clearText(String locator) {
        getLocator(locator).clear();
    }

    public void clearAndEnterText(String locator, String value) {
        clearText(locator);
        enterText(locator, value);
    }

    public void clickOnElement(String locator) {
        getLocator(locator).click();
    }

    public String getTextOfElement(String locator) {
        return getLocator(locator).textContent();
    }

    public String getEnteredText(String locator) {
        return getLocator(locator).inputValue();
    }

    public String getEnteredTextValue(String locator) {
        return getLocator(locator).getAttribute("value");
    }

    public void clickForciblyOnElement(String locator) {
        getLocator(locator).click(new Locator.ClickOptions().setForce(true));
    }

    public void rightClickOnElement(String locator) {
        getLocator(locator).click(new Locator.ClickOptions().setButton(MouseButton.RIGHT));
    }

    public void scrollIntoView(String locator) {
        getLocator(locator).scrollIntoViewIfNeeded();
    }

    public void scrollTillElementAndClick(String locator) {
        getLocator(locator).scrollIntoViewIfNeeded();
        getLocator(locator).click();
    }

    public void clickOnElementWithWait(String locator, int delayInMillis) {
        try {
            getLocator(locator).click(new Locator.ClickOptions().setDelay(delayInMillis));
            log.info("Clicked on element with locator: {} after waiting for {} milliseconds.", locator, delayInMillis);
        } catch (Exception e) {
            log.error("Failed to click on element with locator: {}", locator, e);
        }
    }

    public void doubleClickOnElement(String locator) {
        getLocator(locator).dblclick();
    }

    public void dragAndDrop(String sourceLocator, String targetLocator) {
        getLocator(sourceLocator).dragTo(getLocator(targetLocator));
    }

    public void hoverOnElement(String locator) {
        getLocator(locator).hover();
    }

    public void hoverOnElementAndClick(String locator) {
        getLocator(locator).hover();
        getLocator(locator).click();
    }

    public void selectByValue(String locator, String value) {
        getLocator(locator).selectOption(value);
    }

    public void selectByIndex(String locator, int index) {
        getLocator(locator).selectOption(String.valueOf(index));
    }

    public void selectByVisibleText(String locator, String text) {
        getLocator(locator).selectOption(new SelectOption().setLabel(text));
    }

    public void waitForSelector(String locator) {
        page.get().waitForSelector(locator);
    }

    public void waitForSelector(String locator, int timeout) {
        page.get().waitForSelector(locator, new Page.WaitForSelectorOptions().setTimeout(timeout));
    }

    public void clickOnListOfElements(String locator) {
        waitForSelector(locator);
        Locator locatorList = getLocator(locator);
        for (int index = 0; index < locatorList.count(); index++) {
            locatorList.nth(index).click();
        }
    }

    public void checkOnCheckbox(String locator) {
        getLocator(locator).check();
    }

    public void unCheckbox(String locator) {
        getLocator(locator).uncheck();
    }

    public boolean isElementVisible(String locator) {
        return getLocator(locator).isVisible();
    }

    public boolean isElementHidden(String locator) {
        return getLocator(locator).isHidden();
    }

    public boolean isElementChecked(String locator) {
        return getLocator(locator).isChecked();
    }

    public boolean isElementEditable(String locator) {
        return getLocator(locator).isEditable();
    }

    public boolean isElementEnabled(String locator) {
        return getLocator(locator).isEnabled();
    }

    public boolean isElementDisabled(String locator) {
        return getLocator(locator).isDisabled();
    }

    public void mouseHoverOnElement(String locator) {
        getLocator(locator).hover();
    }

    public String getTitleOfPage() {
        return page.get().title();
    }

    public String getCurrentUrl() {
        return page.get().url();
    }

    public void navigateTo(String url) {
        page.get().navigate(url);
    }

    public void navigateBack() {
        page.get().goBack();
    }

    public void navigateForward() {
        page.get().goForward();
    }

    public void switchToWindow() {
        page.set(page.get().waitForPopup(new Page.WaitForPopupOptions().setTimeout(TimeConstants.PAGE_TIMEOUT), () -> {
        }));
    }

    public Frame switchToFrame(String frame) {
        return page.get().frame(frame);
    }

    public List<String> getTextOfElements(String locator) {
        List<String> locatorList = new ArrayList<>();
        try {
            waitForSelector(locator);
            for (int index = 0; index < getLocator(locator).count(); index++) {
                locatorList.add(getLocator(locator).nth(index).textContent());

            }
        } catch (TimeoutError e) {
            log.error("Failed to get text of elements with locator: {}", locator, e);
        }
        return locatorList;
    }

    public String getAttributeOfLocator(String locator, String attribute) {
        return getLocator(locator).getAttribute(attribute);
    }

    public List<String> getAttributeOfLocators(String locator, String attribute) {
        List<String> attributeList = new ArrayList<>();
        try {
            waitForSelector(locator);
            for (int index = 0; index < getLocator(locator).count(); index++) {
                attributeList.add(getLocator(locator).nth(index).getAttribute(attribute));
            }
        } catch (TimeoutError e) {
            log.error("Failed to get attribute of elements with locator: {}", locator, e);
        }
        return attributeList;
    }

    public void waitForElementToBeVisible(String locator) {
        getLocator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForElementToBeHidden(String locator) {
        getLocator(locator).waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
    }

    public void waitUntilNetworkStable() {
        page.get().waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void reloadPage() {
        page.get().reload();
    }

    public void reloadAndWaitUntilNetworkStable() {
        page.get().reload(new Page.ReloadOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));
    }

    public void resumeScriptAfter(int timeInSeconds) {
        DateTime dateTime = DateTime.now().plusSeconds(timeInSeconds);
        while (dateTime.isAfterNow()) {
            log.trace("Waiting for Script to resume");
        }
    }

    public void validateLocatorHasCSS(String locator, String cssProperty, String cssValue) {
        waitForSelector(locator);
        for (int index = 0; index < getLocator(locator).count(); index++) {
            PlaywrightAssertions.assertThat(getLocator(locator).nth(index)).hasCSS(cssProperty, cssValue);
        }
    }
}
