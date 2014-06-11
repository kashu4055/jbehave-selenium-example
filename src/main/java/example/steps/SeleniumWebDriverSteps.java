package example.steps;

import java.awt.Toolkit;

import org.apache.log4j.Logger;
import org.jbehave.web.selenium.PerStoryWebDriverSteps;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;

import example.core.StoryConfigurator;

public final class SeleniumWebDriverSteps extends PerStoryWebDriverSteps {
    
    private static final Logger LOGGER = Logger.getLogger(SeleniumWebDriverSteps.class);
    
    public SeleniumWebDriverSteps() {
        super(StoryConfigurator.getInstance().getDriverProvider());
    }
    
    @Override
    public void beforeStory() throws Exception {
        super.beforeStory();
        WebDriver driver = getDriverProvider().get();
        adjustWindow(driver);
    }
    
    @Override
    public void afterStory() throws Exception {
        super.afterStory();
    }

    private void adjustWindow(WebDriver driver) {
        Window window = driver.manage().window();
        Point location = new Point(0, 0);
        LOGGER.info(String.format("Moving browser window to x=%s and y=%s ...", location.getX(), location.getY()));
        window.setPosition(location);
        Dimension dimension = getDefaultSize();
        LOGGER.info(String.format("Resizing browser window to height=%s and width=%s ...", dimension.getHeight(), dimension.getWidth()));
        window.setSize(dimension);
    }

    private Dimension getDefaultSize() {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double defaultWidth = screenSize.getWidth() * 0.75;
        double defaultHeight = screenSize.getHeight() * 0.97;
        return new Dimension((int) defaultWidth, (int) defaultHeight);
    }
    
}
