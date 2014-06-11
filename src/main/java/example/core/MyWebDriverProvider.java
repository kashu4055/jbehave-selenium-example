package example.core;

import static org.jbehave.web.selenium.PropertyWebDriverProvider.Browser.CHROME;
import static org.jbehave.web.selenium.PropertyWebDriverProvider.Browser.FIREFOX;
import static org.jbehave.web.selenium.PropertyWebDriverProvider.Browser.HTMLUNIT;
import static org.jbehave.web.selenium.PropertyWebDriverProvider.Browser.IE;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jbehave.web.selenium.DelegatingWebDriverProvider;
import org.jbehave.web.selenium.PropertyWebDriverProvider.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import example.SeleniumStories;

public final class MyWebDriverProvider extends DelegatingWebDriverProvider {

    private static final Logger LOGGER = Logger.getLogger(MyWebDriverProvider.class);

    @Override
    public void end() {
        super.delegate.get().quit();
        super.delegate.set(null);
    }

    @Override
    public void initialize() {
        Browser browser = SeleniumStories.getBrowser();
        WebDriver driver = createDriver(browser);
        this.delegate.set(driver);
    }

    private WebDriver createDriver(Browser browser) {
        WebDriver driver;
        LOGGER.info(String.format("Starting browser '%s' ...", browser));
        if (CHROME == browser) {
            driver = createChromeDriver();
        } else if (FIREFOX == browser) {
            driver = createFirefoxDriver();
        } else if (HTMLUNIT == browser) {
            driver = createHtmlUnitDriver();
        } else if (IE == browser) {
            driver = createInternetExplorerDriver();
        } else {
            LOGGER.warn(String.format("Using HtmlUnitDriver as browser since passed type '%s' did not match any value of '%s'...", browser,
                    ArrayUtils.toString(Browser.values())));
            driver = createHtmlUnitDriver();
        }
        return driver;
    }

    protected WebDriver createChromeDriver() {
        return WebDriverSupplier.createChromeDriver(new ChromeOptions());
    }

    protected WebDriver createFirefoxDriver() {
        return WebDriverSupplier.createFirefoxDriver(new FirefoxProfile(), DesiredCapabilities.firefox());
    }

    protected WebDriver createHtmlUnitDriver() {
        return WebDriverSupplier.createHtmlUnitDriver();
    }

    protected WebDriver createInternetExplorerDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        // fix focus behavior of ie:
        capabilities.setCapability("nativeEvents", false);
        return WebDriverSupplier.createInternetExplorerDriver(capabilities);
    }

    @Override
    public boolean saveScreenshotTo(String path) {
        return saveScreenshotTo(path, Level.ERROR);
    }

    public boolean saveScreenshotTo(String path, Level logLevel) {
        boolean result = false;
        String relativePath = path.replaceFirst(".*(\\\\|/)target(\\\\|/)jbehave", "..");
        String message = String.format("<a href=\"%s\">Screenshot saved to: %s</a>", relativePath, relativePath);
        LOGGER.log(logLevel, message);
        result = super.saveScreenshotTo(path);
        return result;
    }

}
