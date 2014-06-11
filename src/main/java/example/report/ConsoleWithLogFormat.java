package example.report;

import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.PrintStreamOutput;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;

public class ConsoleWithLogFormat extends Format {

    private final JBehaveAppender appender;

    public ConsoleWithLogFormat(JBehaveAppender pAppender) {
        super("CONSOLE");
        this.appender = pAppender;
    }

    public JBehaveAppender getAppender() {
        return this.appender;
    }

    @Override
    public StoryReporter createStoryReporter(FilePrintStreamFactory factory, StoryReporterBuilder builder) {
        factory.useConfiguration(builder.fileConfiguration("xml"));
        ConsoleOutputWithLog storyReporter = new ConsoleOutputWithLog();
        this.appender.setLogAccepter(storyReporter);
        PrintStreamOutput output = storyReporter.doReportFailureTrace(builder.reportFailureTrace());
        return output.doCompressFailureTrace(builder.compressFailureTrace());
    }

}