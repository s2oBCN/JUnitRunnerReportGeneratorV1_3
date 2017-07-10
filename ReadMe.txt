##################################################################################
####################################ReadMe.txt####################################
##################################################################################

How to install and configure the JUnit Testcase runner and report generator

1- Extract JUnitRunnerReportGeneratorV1_x.zip to some location

2- Modify setEnv.bat to set the appropriate value of property JAVA_HOME.

3- This build includes a sample testclass which is pre-configured in junitTest.properties, the class path of test class is set in property TEST_CLASS_PATH that to be modified with the class path folder of your your test classes.

3- This build comes with HSQLDB embedded into it. Optionally, if you want to choose another database then modify the junitTest.properties and provide db properties of your choice.

4- Optionally modify the junitTest.properties to give appropriate title to your report.


All properties defined in junitTest.properties are self explanatory. 



############
How to run
############


Once extracted JUnitRunnerReportGeneratorV1_x.zip there will 3 main bat files in bin folder. 


1) dbSetup.bat - to perform the initial database setup

2) testRunner.bat - to start JUnit test run on configured test classes 

3) reportGenerator.bat - Once testRunner.bat completes then run reportGenerator.bat that will generate the HTML test report in Report folder.



Please note that sample database is provided with this build so to run sample test case you don’t require to run dbSetup.bat, if you want to create a fresh database then first you have to remove the already created database then run the dbSetup.bat. 



Once setup is done (or skipped for sample test case run) the next step is to run the testRunner.bat, this process will pick all test case classed from the class path and run them one by one. While execution of test cases the test runner process stores the test results in a database.



Once testRunner completes, the last step is to run the reportGenerator.bat that will pull the test result data from database and generate the HTML report in folder “Report”. 

Please try this tool and provide your feedback. If you stuck due to any issue please post your queries on wiki or open a ticket of this project at sourceforge.net we will try to resolve your query as soon as possible.