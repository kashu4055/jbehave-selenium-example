package example.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.io.PrintStream;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.reporters.TxtOutput;

public class TxtOutputWithLog extends TxtOutput implements StoryReportLogAccepter {

    public TxtOutputWithLog(PrintStream output) {
        super(output);
    }

    public TxtOutputWithLog(PrintStream output, Properties outputPatterns) {
        super(output, outputPatterns);
    }

    public TxtOutputWithLog(PrintStream output, Keywords keywords) {
        super(output, keywords);
    }

    public TxtOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords) {
        super(output, outputPatterns, keywords);
    }

    public TxtOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace);
    }

    public TxtOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace, boolean compressFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace, compressFailureTrace);
    }

    @Override
    public void log(LoggingEvent event) {
        Appender appender = Logger.getRootLogger().getAppender("txt");
        assertThat(appender, instanceOf(JBehaveAppender.class));
        Priority threshold = ((JBehaveAppender) appender).getThreshold();
        if (event.getLevel().isGreaterOrEqual(threshold)) {
            Layout layout = appender.getLayout();
            String message = layout.format(event);
            super.print(message);
        }
    }

}
