package example.steps;

import java.io.File;
import java.util.UUID;

import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;

import example.core.StoryConfigurator;

public final class ScreenshotOnFailureStep extends WebDriverScreenshotOnFailure {

    private String screenshotPath;

    public ScreenshotOnFailureStep() {
        super(StoryConfigurator.getInstance().getDriverProvider(), StoryConfigurator.getInstance().getStoryReportBuilder());
    }

    @Override
    public void afterScenarioFailure(UUIDExceptionWrapper uuidWrappedFailure) throws Exception {
        super.afterScenarioFailure(uuidWrappedFailure);
        if (this.screenshotPath != null) {
            File screenshot = new File(this.screenshotPath);
            this.screenshotPath = null;
        }
    }

    @Override
    protected String screenshotPath(UUID uuid) {
        this.screenshotPath = super.screenshotPath(uuid);
        return this.screenshotPath;
    }

}
