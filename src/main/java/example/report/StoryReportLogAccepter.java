package example.report;

import org.apache.log4j.spi.LoggingEvent;

public interface StoryReportLogAccepter {

    void log(LoggingEvent event);

}
