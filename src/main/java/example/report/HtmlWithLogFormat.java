package example.report;

import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.PrintStreamOutput;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;

public class HtmlWithLogFormat extends Format {

    private final JBehaveAppender appender;

    public HtmlWithLogFormat(JBehaveAppender pAppender) {
        super("HTML");
        this.appender = pAppender;
    }

    public JBehaveAppender getAppender() {
        return this.appender;
    }

    @Override
    public StoryReporter createStoryReporter(FilePrintStreamFactory factory, StoryReporterBuilder builder) {
        factory.useConfiguration(builder.fileConfiguration("html"));
        HtmlOutputWithLog storyReporter = new HtmlOutputWithLog(factory.createPrintStream(), builder.keywords());
        this.appender.setLogAccepter(storyReporter);
        PrintStreamOutput output = storyReporter.doReportFailureTrace(builder.reportFailureTrace());
        return output.doCompressFailureTrace(builder.compressFailureTrace());
    }

}