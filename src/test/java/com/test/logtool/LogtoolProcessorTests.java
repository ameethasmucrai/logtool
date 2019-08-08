package com.test.logtool;

import com.test.logtool.entity.Configuration;
import com.test.logtool.entity.LogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogtoolProcessorTests {

    @Autowired
    private LogReader logReader;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Configuration configuration;

    @Test
    public void process() {

        ExecutorService executorService = Executors.newFixedThreadPool(configuration.getProcessorThreads());

        LogEntity logEntityFirst = new LogEntity();
        logEntityFirst.setId("scsmbstgra");
        logEntityFirst.setState("STARTED");
        logEntityFirst.setType("APPLICATION_LOG");
        logEntityFirst.setHost("12345");
        logEntityFirst.setTimestamp((long) 1491377495212.0);
        logReader.firstEntryHashMap.put("scsmbstgra", logEntityFirst);
        LogProcessor logProcessor1 = (LogProcessor) applicationContext.getBean("logProcessor", logEntityFirst);
        executorService.submit(logProcessor1);

        LogEntity logEntitySecond = new LogEntity();
        logEntitySecond.setId("scsmbstgra");
        logEntitySecond.setState("FINISHED");
        logEntitySecond.setType("APPLICATION_LOG");
        logEntitySecond.setHost("12345");
        logEntitySecond.setTimestamp((long) 1491377495217.0);
        logReader.secondEntryHashMap.put("scsmbstgra", logEntitySecond);
        LogProcessor logProcessor2 = (LogProcessor) applicationContext.getBean("logProcessor", logEntitySecond);
        executorService.submit(logProcessor2);

    }

}
