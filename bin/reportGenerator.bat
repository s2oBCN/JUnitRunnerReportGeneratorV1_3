@echo off
call setEnv.bat


setLocal EnableDelayedExpansion

 for /R ../lib %%a in (*.jar) do (
   set CLASSPATH=!CLASSPATH!;%%a
 )


set CLASSPATH=%CLASSPATH%;%TEST_CLASS_PATH%;../config


%JAVA_HOME%\bin\java com.junit.tools.Main.ReportUtil