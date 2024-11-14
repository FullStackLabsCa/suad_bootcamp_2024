//package io.reactivestax;
//
//import org.junit.rules.ExternalResource;
//import org.slf4j.LoggerFactory;
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.core.OutputStreamAppender;
//import java.io.ByteArrayOutputStream;
//
//public class LogCaptureRule extends ExternalResource {
//    private ByteArrayOutputStream logStream;
//    private OutputStreamAppender appender;
//    private LoggerContext loggerContext;
//
//    @Override
//    protected void before() throws Throwable {
//        logStream = new ByteArrayOutputStream();
//        loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
//        appender = new OutputStreamAppender();
//        appender.setContext(loggerContext);
//        appender.setOutputStream(logStream);
//        appender.start();
//
//        // Add the appender to the root logger
//        loggerContext.getLogger("ROOT").addAppender(appender);
//    }
//
//    @Override
//    protected void after() {
//        // Remove the appender after the test
//        loggerContext.getLogger("ROOT").detachAppender(appender);
//    }
//
//    public String getCapturedLogs() {
//        return logStream.toString();
//    }
//}
