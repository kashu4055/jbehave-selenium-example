package example.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import example.core.MyWebDriverProvider;
import example.core.StoryConfigurator;

public abstract class PageObject {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    private final MyWebDriverProvider driverProvider = StoryConfigurator.getInstance().getDriverProvider();
    
    protected final MyWebDriverProvider getDriverProvider() {
        return this.driverProvider;
    }

    protected final WebDriver getDriver() {
        return getDriverProvider().get();
    }
    
    protected Logger logger() {
        return this.logger;
    }
    
}
