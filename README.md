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
- These instructions assume a Linux machine, but it can also be applied to a Windows machine.
- It requires Java 8
 
For testing purpuses, there's an example log file supplied "example.log" under "resources" folder. 
Copy this file to some path in the system, for instance "/tmp".

Under "data" folder:
- Start the HSQLDB
    - java -cp hsqldb.jar org.hsqldb.server.Server --database.0 logtooldb --dbname.0 logtooldb
- Start the Manager to see the results in the database 
    - java -cp hsqldb.jar org.hsqldb.util.DatabaseManagerSwing
    - Login with the default credentials: user: SA, password: none

Execute, on the project root folder:
 
gradlew build && java -jar build/libs/logtool-1.0-SNAPSHOT.jar /tmp/example.log 


### Additional information

This application was developed with Spring Boot, using IntelliJ IDEA Community 2019. 

Certain configurations can be adjusted in the application.properties file, for instance:
- Set the threshold to alert if event duration is above the specified
- Encoding of the log file being read
- Number of threads in the thread pool, to process the logs

Although this application was made to be able process large files, it was not yet tested with a large file (GB).   

### Features and improvements planned for the next releases
- Implement reading log files in realtime
- If a log entry does not have it's start or finish pair, the thread should not wait forever 


