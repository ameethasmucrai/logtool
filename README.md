# LogTool

### Description

This application reads from a log file supplied as input (absolute file path), 
flag any long events that take longer than 4ms (configurable) with a column in 
the database called "alert".

Every event has 2 entries in a log - one entry when the event was started and another when
the event was finished. The entries in a log file have no specific order 
(it can occur that a specific event is logged before the event starts).

### How to run / test the program

Note: 
- These instructions assume a Linux machine, but it can also be applied to a Windows machine 
using similar commands.
- It requires Java 8
 
For testing purpuses, there's an example log file supplied "example.log" under "data" folder. 
Copy this file to some path in the system, for instance "/tmp".

Under "data" folder:
- Start the HSQLDB
    - java -cp hsqldb.jar org.hsqldb.server.Server --database.0 logtooldb --dbname.0 logtooldb
- Start the Manager to see the results in the database 
    - java -cp hsqldb.jar org.hsqldb.util.DatabaseManagerSwing
    - Login with the default credentials: user: SA, password: none

Execute, on the project root folder:
 
./gradlew build && java -jar build/libs/logtool-1.0-SNAPSHOT.jar /tmp/example.log

To run the unit tests, on the project root folder:

./gradlew test --rerun-tasks  -i



### Additional information

This application was developed with Spring Boot, using IntelliJ IDEA Community 2019. 

Certain configurations can be adjusted in the application.properties file, for instance:
- Set the threshold to alert if event duration is above the specified
- Encoding of the log file being read
- Number of threads in the thread pool, to process the logs

This application was made to be able process large files (GB).
The following test was done:
- Log file with 20 million lines, 1.6GB
- Application configured with 1000 threads
- It took around 20min to process the log file, and more 20min to finish writing 10 million records to the database 
- Machine: Core i7 8th gen
     


### Features and improvements planned for the next releases
- Implement reading log files in realtime
- If a log entry does not have it's start or finish pair, the thread should not wait forever 


### Changes
#### 2019-08-20
- Removed commandLineRunner bean on application start-up, and moved the code to the main method.
- Checks if the file exists before trying to read, and shows a graceful error message instead of stack trace.
- Added wait time in the loop that checks if the pair of the log has arrived.
#### 2019-08-21
- Improved shutdown of the thread pool
- Improved memory handling and application performance
- Added a counter that logs each time 10000 lines are read
#### 2019-08-23
- Improved unit tests
