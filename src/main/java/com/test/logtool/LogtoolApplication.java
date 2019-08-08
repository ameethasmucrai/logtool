package com.test.logtool;

import com.test.logtool.entity.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.ObjectUtils;

/**
 * Main class
 */
@SpringBootApplication
@ComponentScan
@EnableConfigurationProperties({Configuration.class})
public class LogtoolApplication {

    private static final Logger log = LoggerFactory.getLogger(LogtoolApplication.class);

    // Take the input file path as input argument
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LogtoolApplication.class, args);
    }

    /**
     * Checks the argument and invoke log reader
     * @param ctx Application context, injected by Spring
     * @return
     */
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

		return args -> {
			log.info("LogTool Aplication Started...");
			if (isArgumentValid(args)) {
				String fileName = args[0];
				log.info("Argument: " + fileName);
				LogReader logReader = (LogReader) ctx.getBean("logReader");
				logReader.processFile(fileName);
			}
		};

	}

    /**
     * Verifies if the argument was supplied
     * @param args Arguments
     * @return True if the argument is valid
     */
    private boolean isArgumentValid(String args[]) {

        boolean isArgumentValid = true;
        log.info("Check arguments...");
        if (ObjectUtils.isEmpty(args)) {
            log.error("No arguments supplied. Expected argument: absolute file path");
            isArgumentValid = false;
        }
        return isArgumentValid;

    }

}
