package example.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;

public class JBehaveAppender extends ConsoleAppender {

    private StoryReportLogAccepter logAccepter;
    
    private final List<LoggingEvent> pendingLoggingEvents = new ArrayList<>();

    @Override
    public void append(LoggingEvent event) {
        if (this.logAccepter != null) {
            logAndClearPendingEvents();
            this.logAccepter.log(event);
        } else {
            this.pendingLoggingEvents.add(event);
        }
    }
    
    /*
     * The logAccepter is set after JBehave's run method is started.
     * Anything which is logged before that would not be logged.
     * So we cache the logEvents and flush them when the logAccepter is set.
     */
    private void logAndClearPendingEvents() {
        for (LoggingEvent event : this.pendingLoggingEvents) {
            this.logAccepter.log(event);
        }
        this.pendingLoggingEvents.clear();
    }

    public void setLogAccepter(StoryReportLogAccepter pLogAccepter) {
        this.logAccepter = pLogAccepter;
    }

    public StoryReportLogAccepter getLogAccepter() {
        return this.logAccepter;
    }
    
}
