package com.test.logtool;

import com.test.logtool.entity.Configuration;
import com.test.logtool.entity.LogEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogtoolApplicationTests {

    @Autowired
    private LogtoolApplication application;

	@Test
	public void isArgumentInvalidTest() {
	    String[] args = {};
        boolean isArgumentValid = application.isArgumentValid(args);
        assertFalse(isArgumentValid);
	}

    @Test
    public void isArgumentValidTest() {
        String[] args = {"/tmp/example.log"};
        boolean isArgumentValid = application.isArgumentValid(args);
        assertTrue(isArgumentValid);
    }

}
