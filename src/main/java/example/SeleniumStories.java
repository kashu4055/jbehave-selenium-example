package example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.embedder.executors.SameThreadExecutors;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.guice.GuiceStepsFactory;
import org.jbehave.web.selenium.PropertyWebDriverProvider.Browser;

import com.google.inject.Guice;
import com.google.inject.Injector;

import example.core.LocaleSetter;
import example.core.StepLocator;
import example.core.StoryConfigurator;

public class SeleniumStories extends JUnitStories {

    private static final Logger LOGGER = Logger.getLogger(SeleniumStories.class);
    
    public SeleniumStories() {
        LocaleSetter.setDefaultLocale(Locale.GERMAN);
        configuredEmbedder().embedderControls().doFailOnStoryTimeout(false);
        ExecutorService service = new SameThreadExecutors().create(configuredEmbedder().embedderControls());
        configuredEmbedder().useExecutorService(service);
        initializeWebDrivers();
    }
    
    public static Browser getBrowser() {
        return Browser.CHROME;
    }
    
    private void initializeWebDrivers() {
        String basePath = "src/main/resources/driver/";
        File chromeDriver = new File(basePath + "chromedriver_win32_2.10.exe");
        File ieDriver = new File(basePath + "IEDriverServer_Win32_2.41.0.exe");
        assertThat(chromeDriver.exists(), equalTo(true));
        assertThat(ieDriver.exists(), equalTo(true));
        System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
        System.setProperty("webdriver.ie.driver", ieDriver.getAbsolutePath());
    }
    
    @Override
    public void run() throws Throwable {
        try {
            String dryRun = configuredEmbedder().systemProperties().getProperty("dryRun");
            if (!StringUtils.isEmpty(dryRun)) {
                // Sonst gibt es eine NPE:
                StoryConfigurator.getInstance().getConfiguration().storyControls();
                StoryConfigurator.getInstance().getConfiguration().doDryRun(Boolean.valueOf(dryRun));
            }
            super.run();
        } finally {
            File outputDirectory = StoryConfigurator.getInstance().getConfiguration().storyReporterBuilder().outputDirectory();
            // browseToReports(outputDirectory);
        }
    }

    private void browseToReports(File outputDirectory) {
        String reports = MessageFormat.format("{0}/view/reports.html", outputDirectory);
        File reportsFile = new File(reports);
        if (reportsFile.exists() && Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(reportsFile.toURI());
            } catch (IOException e) {
                LOGGER.error("Could not open HTML reports file in a browser: " + reports, e);
            }
        } else if (!reportsFile.exists()) {
            LOGGER.error("HTML reports file does not exist at: " + reports);
        } else if (!Desktop.isDesktopSupported()) {
            LOGGER.error("Browsing to HTML reports file impossible because desktop is not supported");
        }
    }
    
    @Override
    public Configuration configuration() {
        StoryConfigurator conf = StoryConfigurator.getInstance();
        conf.initialize(this.getClass());
        return conf.getConfiguration();
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        Injector injector = Guice.createInjector(new StepLocator("**/steps/**"));
        return new GuiceStepsFactory(StoryConfigurator.getInstance().getConfiguration(), injector);
    }

    @Override
    protected List<String> storyPaths() {
        List<String> storyPaths = new ArrayList<>();
        URL codeLocationFromClass = CodeLocations.codeLocationFromClass(this.getClass());
        storyPaths = new StoryFinder().findPaths(codeLocationFromClass.getFile(), Arrays.asList("**/*.story"), null);
        return storyPaths;
    }

}
