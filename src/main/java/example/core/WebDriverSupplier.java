package example.core;

import static java.lang.Boolean.parseBoolean;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public final class WebDriverSupplier {

    private WebDriverSupplier() {
        // only static methods
    }

    public static WebDriver createChromeDriver(ChromeOptions options) {
        // FIXME Find solution how the level can be set for Chromedriver output.
        ChromeDriverService.Builder builder = new ChromeDriverService.Builder();
        String chromeDriverExePath = System.getProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY);
        assertThat(chromeDriverExePath, notNullValue());
        builder.usingDriverExecutable(new File(chromeDriverExePath));
        ChromeDriverService service = builder.build();
        return new ChromeDriver(service, options);
    }

    public static WebDriver createFirefoxDriver(FirefoxProfile profile, DesiredCapabilities capabilities) {
        return new FirefoxDriver(new FirefoxBinary(), profile, capabilities);
    }

    public static HtmlUnitDriver createHtmlUnitDriver() {
        HtmlUnitDriver driver = new HtmlUnitDriver();
        boolean javascriptEnabled = parseBoolean(System.getProperty("webdriver.htmlunit.javascriptEnabled", "true"));
        driver.setJavascriptEnabled(javascriptEnabled);
        return driver;
    }

    public static WebDriver createInternetExplorerDriver(DesiredCapabilities capabilities) {
        InternetExplorerDriverService.Builder builder = new InternetExplorerDriverService.Builder();
        InternetExplorerDriverService service = builder.build();
        return new InternetExplorerDriver(service, capabilities);
    }
    
}
