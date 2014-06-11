package example.core;

import static example.report.FormatWithLog.CONSOLE_WITH_LOG;
import static example.report.FormatWithLog.HTML_WITH_LOG;
import static example.report.FormatWithLog.TXT_WITH_LOG;
import static example.report.FormatWithLog.XML_WITH_LOG;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;

import java.awt.Toolkit;

import org.apache.log4j.Logger;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.context.Context;
import org.jbehave.core.context.ContextView;
import org.jbehave.core.context.JFrameContextView;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.reporters.ContextOutput;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ContextStepMonitor;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.web.selenium.SeleniumConfiguration;
import org.jbehave.web.selenium.SeleniumContextStoryReporter;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

public final class StoryConfigurator {
    
    private static StoryConfigurator instance;
    
    private static final Logger LOGGER = Logger.getLogger(StoryConfigurator.class);
    
    private final MyWebDriverProvider driverProvider = new MyWebDriverProvider();
    
    private final Context context = new Context();
    private final Format contextFormat = new ContextOutput(this.context);
    private final ContextView contextView = createContextView();
    private final CrossReference xref = new CrossReference();
    private final ContextStepMonitor stepMonitor = new ContextStepMonitor(this.context, this.contextView, this.xref.getStepMonitor());
    
    private Configuration configuration;
    
    private StoryConfigurator() {
        // singleton
    }
    
    public static StoryConfigurator getInstance() {
        if (instance == null) {
            instance = new StoryConfigurator();
        }
        return instance;
    }
    
    public MyWebDriverProvider getDriverProvider() {
        return this.driverProvider;
    }
    
    public StoryReporterBuilder getStoryReportBuilder() {
        return this.configuration.storyReporterBuilder();
    }
    
    private JFrameContextView createContextView() {
        Dimension size = getDefaultSize();
        int width = size.getWidth();
        int height = size.getHeight();
        Point location = getDefaultSizeLocation();
        int xPos = location.getX();
        int yPos = location.getY();
        LOGGER.debug(String.format("Creating context view with width=%s, height=%s, x-location=%s and y-location=%s ...", 
                width, height, xPos, yPos));
        return new JFrameContextView().sized(width, height).located(xPos, yPos);
    }
    
    private Dimension getDefaultSize() {
        java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double defaultWidth = screenSize.getWidth() * 0.25;
        double defaultHeight = screenSize.getHeight() * 0.15;
        return new Dimension((int) defaultWidth, (int) defaultHeight);
    }
    
    private Point getDefaultSizeLocation() {
        int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int defaultWidth = getDefaultSize().getWidth();
        return new Point(screenWidth - defaultWidth, 0);
    }
    
    public void initialize(Class<? extends Embeddable> embeddableClass) {
        assertThat("Configuration is already initialized", this.configuration, nullValue());
        SeleniumConfiguration seleniumConf = new SeleniumConfiguration().useWebDriverProvider(getDriverProvider());
        StoryReporterBuilder storyReporterBuilder = new StoryReporterBuilder().withCodeLocation(codeLocationFromClass(embeddableClass))
            .withReporters(new SeleniumContextStoryReporter(seleniumConf.seleniumContext()))
            .withFailureTrace(true)
            .withCrossReference(this.xref).withDefaultFormats()
            .withFormats(this.contextFormat, CONSOLE_WITH_LOG, HTML_WITH_LOG, XML_WITH_LOG, TXT_WITH_LOG);
        this.configuration = seleniumConf
            .useStoryLoader(new LoadFromClasspath(embeddableClass))
            .useParameterControls(new ParameterControls().useDelimiterNamedParameters(true))
            .useStepMonitor(this.stepMonitor)
            .useStoryReporterBuilder(storyReporterBuilder);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }

}
