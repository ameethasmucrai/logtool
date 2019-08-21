package com.test.logtool;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.logtool.entity.Configuration;
import com.test.logtool.entity.LogEntity;
import com.test.logtool.utils.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Reads the file, line by line and sends for processing
 */
@Component
public class LogReader implements HasLogger {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Configuration configuration;

    // Event has 2 entries in a log - one entry when the event was started and another when the event was finished
    // The entries in a log file have no specific order. The HashMap will help hold them temporarily
    // ConcurrentHashMap is used to prevent concurrency issues
    public static ConcurrentHashMap<String, LogEntity> firstEntryHashMap = new ConcurrentHashMap<String, LogEntity>();
    public static ConcurrentHashMap<String, LogEntity> secondEntryHashMap = new ConcurrentHashMap<String, LogEntity>();

    public void processFile(String fileName) {

        getLogger().info("Process file...");

        // Use the Spring resource loader to load the file
        Resource resource = resourceLoader.getResource("file:" + fileName);

        if (!resource.exists()) {

            getLogger().error("The file specified was not found! File: " + fileName);

        } else {

            // Create thread pool
            ExecutorService executorService = Executors.newFixedThreadPool(configuration.getProcessorThreads());

            // Read the file, line by line, to be able to process large files
            InputStream inputStream = null;
            Scanner sc = null;
            try {
                inputStream = resource.getInputStream();
                sc = new Scanner(inputStream, configuration.getFileEncoding());
                int i = 0;
                LogEntity logEntity;
                String line;
                String id;
                LogProcessor logProcessor;
                while (sc.hasNextLine()) {
                    i += 1;
                    if (i % 10000 == 0){
                        getLogger().info("Line: " + i);
                    }
                    line = sc.nextLine();
                    logEntity = jsonToObject(line);
                    id = logEntity.getId();
                    // If there is no entry in the first map, it means it is the first entry for that id, so add it to the map and invoke a thread to wait for the corresponding pair
                    if (!firstEntryHashMap.containsKey(id)) {
                        getLogger().debug("Key not found in first map. Key id=" + id);
                        firstEntryHashMap.put(id, logEntity);
                        logProcessor = (LogProcessor) applicationContext.getBean("logProcessor", logEntity);
                        executorService.submit(logProcessor);
                    }
                    // If there is already one entry in the first map, it means this one is the corresponding pair, so add it to the second map, and let the waiting thread pick it
                    else {
                        getLogger().debug("Key found in first map. Key id=" + id);
                        secondEntryHashMap.put(logEntity.getId(), logEntity);
                    }
                }
                // Scanner suppresses exceptions
                if (sc.ioException() != null) {
                    throw sc.ioException();
                }
            } catch (Exception e) {
                getLogger().error(e.getMessage(), e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception ex) {
                        getLogger().error(ex.getMessage(), ex);
                    }
                }
                if (sc != null) {
                    sc.close();
                }
            }

            // Shutdown thread pool but wait for termination of the threads...
            getLogger().info("Shutdown thread pool and wait for termination...");
            try {
                executorService.shutdown();
                while (true) {
                    executorService.awaitTermination(configuration.getAwaitTermination(), TimeUnit.MILLISECONDS);
                    if (executorService.isTerminated()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Wait until the ExecutorService says it is properly shutdown
            while (true) {
                if (executorService.isShutdown()) {
                    break;
                }
            }

        }

        getLogger().info("Finished processing file...");

    }

    /**
     * Converts json log entry into java object
     *
     * @param logEntry
     * @return
     */
    private LogEntity jsonToObject(String logEntry) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            // JSON string to Java Object
            LogEntity logEntity = mapper.readValue(logEntry, LogEntity.class);
            return logEntity;
        } catch (IOException e) {
            getLogger().error(e.getMessage(), e);
        }

        return null;

    }

}
