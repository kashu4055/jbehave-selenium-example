package example.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.io.PrintStream;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.reporters.HtmlOutput;

public class HtmlOutputWithLog extends HtmlOutput implements StoryReportLogAccepter {

    public HtmlOutputWithLog(PrintStream output, Keywords keywords) {
        super(output, keywords);
    }

    public HtmlOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace);
    }

    public HtmlOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords) {
        super(output, outputPatterns, keywords);
    }

    public HtmlOutputWithLog(PrintStream output, Properties outputPatterns) {
        super(output, outputPatterns);
    }

    public HtmlOutputWithLog(PrintStream output) {
        super(output);
    }

    public HtmlOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace, boolean compressFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace, compressFailureTrace);
    }

    @Override
    public void log(LoggingEvent event) {
        Appender appender = Logger.getRootLogger().getAppender("html");
        assertThat(appender, instanceOf(JBehaveAppender.class));
        Priority threshold = ((JBehaveAppender) appender).getThreshold();
        if (event.getLevel().isGreaterOrEqual(threshold)) {
            Layout layout = appender.getLayout();
            // This formatting is old style but otherwise we would have to maintain the JBehave Site Resources by ourselves.
            String formatted = layout.format(event).replaceFirst("\r\n", "");
            String message = String.format("<pre>%s</pre>\r\n", formatted);
            if (Level.WARN.equals(event.getLevel())) {
                message = String.format("<font color=\"orange\">%s</font>", message);
            } else if (Level.ERROR.equals(event.getLevel()) || Level.FATAL.equals(event.getLevel())) {
                message = String.format("<font color=\"red\">%s</font>", message);
            }
            super.print(message);
        }
    }

}
