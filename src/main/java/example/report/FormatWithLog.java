package example.report;

import org.apache.log4j.Logger;

public final class FormatWithLog {
    
    public static final ConsoleWithLogFormat CONSOLE_WITH_LOG;
    public static final TxtWithLogFormat TXT_WITH_LOG;
    public static final HtmlWithLogFormat HTML_WITH_LOG;
    public static final XmlWithLogFormat XML_WITH_LOG;
    
    private static final Logger LOGGER = Logger.getRootLogger();

    static {
        CONSOLE_WITH_LOG = new ConsoleWithLogFormat(new JBehaveAppender());
        TXT_WITH_LOG = new TxtWithLogFormat(new JBehaveAppender());
        HTML_WITH_LOG = new HtmlWithLogFormat(new JBehaveAppender());
        XML_WITH_LOG = new XmlWithLogFormat(new JBehaveAppender());

        LOGGER.addAppender(CONSOLE_WITH_LOG.getAppender());
        LOGGER.addAppender(TXT_WITH_LOG.getAppender());
        LOGGER.addAppender(HTML_WITH_LOG.getAppender());
        LOGGER.addAppender(XML_WITH_LOG.getAppender());
        
        
    }
    
    private FormatWithLog() {
        // singleton
    }
    
}
