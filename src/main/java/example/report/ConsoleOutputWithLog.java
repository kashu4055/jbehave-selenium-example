package example.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.jbehave.core.reporters.ConsoleOutput;

public class ConsoleOutputWithLog extends ConsoleOutput implements StoryReportLogAccepter {

    @Override
    public void log(LoggingEvent event) {
        Appender appender = Logger.getRootLogger().getAppender("console");
        assertThat(appender, instanceOf(JBehaveAppender.class));
        Priority threshold = ((JBehaveAppender) appender).getThreshold();
        if (event.getLevel().isGreaterOrEqual(threshold)) {
            Layout layout = appender.getLayout();
            String message = layout.format(event);
            super.print(message);
        }
    }

}
