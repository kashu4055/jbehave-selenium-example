package example.report;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.io.PrintStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.reporters.XmlOutput;

public class XmlOutputWithLog extends XmlOutput implements StoryReportLogAccepter {

    public XmlOutputWithLog(PrintStream output, Keywords keywords) {
        super(output, keywords);
    }

    public XmlOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace, boolean compressFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace, compressFailureTrace);
    }

    public XmlOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace);
    }

    public XmlOutputWithLog(PrintStream output, Properties outputPatterns, Keywords keywords) {
        super(output, outputPatterns, keywords);
    }

    public XmlOutputWithLog(PrintStream output, Properties outputPatterns) {
        super(output, outputPatterns);
    }

    public XmlOutputWithLog(PrintStream output) {
        super(output);
    }

    @Override
    public void log(LoggingEvent event) {
        Appender appender = Logger.getRootLogger().getAppender("xml");
        assertThat(appender, instanceOf(JBehaveAppender.class));
        Priority threshold = ((JBehaveAppender) appender).getThreshold();
        if (event.getLevel().isGreaterOrEqual(threshold)) {
            Layout layout = appender.getLayout();
            String log4jMessage = StringUtils.removeEnd(layout.format(event), "\r\n");
            String message = String.format("<log>%s</log>", log4jMessage);
            super.print(message);
        }
    }

}
