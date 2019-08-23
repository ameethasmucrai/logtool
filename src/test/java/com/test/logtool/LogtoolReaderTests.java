package com.test.logtool;

import com.test.logtool.entity.Configuration;
import com.test.logtool.entity.LogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogtoolReaderTests {

    @Autowired
    private LogReader logReader;

    @Autowired
    ResourceLoader resourceLoader;

	@Test
	public void processFileNotFoundTest() {
		String fileName = "/tmp/xxxxxxx.log";
		boolean result = logReader.processFile(fileName);
		assertFalse(result);
	}

    @Test
    public void processTest() {
	    Resource resource = resourceLoader.getResource("file:data/example.log");
        String fileName = null;
        try {
            fileName = resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean result = logReader.processFile(fileName);
        assertTrue(result);
    }

    @Test
    public void jsonToObjectTest() {
	    String json = "{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\",\"host\":\"12345\", \"timestamp\":1491377495212}";
	    LogEntity logEntity = logReader.jsonToObject(json);
        assertNotNull(logEntity);
    }

}
