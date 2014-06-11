package example.core;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

public class MyWebDriverEventListener extends AbstractWebDriverEventListener {

    private static final Logger LOGGER = Logger.getLogger(MyWebDriverEventListener.class);

    final boolean highlightFoundElements;
    final long delay;

    private By previousBy;

    public MyWebDriverEventListener(final boolean pHighlightFoundElements, final long pDelay) {
        this.highlightFoundElements = pHighlightFoundElements;
        this.delay = pDelay;
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        if (this.highlightFoundElements) {
            clearHighlight(driver);
            showHighlight(by, driver);
        }
    }

    private void showHighlight(By by, WebDriver driver) {
        try {
            WebElement foundElement = driver.findElement(by);
            JavascriptExecutor jsExec = (JavascriptExecutor) driver;
            jsExec.executeScript("arguments[0].style.border='2px solid blue'", foundElement);
            this.previousBy = by;
        } catch (ElementNotVisibleException e) {
            LOGGER.warn("Highlighting WebElement failed because it is not visible", e);
        }
    }

    private void clearHighlight(WebDriver driver) {
        if (this.previousBy != null) {
            try {
                WebElement previousElement = driver.findElement(this.previousBy);
                JavascriptExecutor jsExec = (JavascriptExecutor) driver;
                jsExec.executeScript("arguments[0].setAttribute('style', arguments[1]);", previousElement, "");
            } catch (NoSuchElementException e) {
                // ignore it (maybe we are on another page)
                LOGGER.debug("Clearing highlight of WebElement failed because there is no such element", e);
            }
        }
    }

}
