

REM edit this batch file as you need to.  
REM it you have placed your jar files in DEXTER_HOME

set DEXTER_HOME=C:\dexter
set MAIN_CLASS=org.dykman.dexter.Main
set DEXTER_JAR=%DEXTER_HOME%\dexter.jar
set GETOPT_JAR=%DEXTER_HOME%\gnu-getopt.jar

set CLASSPATH=%DEXTER_JAR%;%GETOPT_JAR%


java -cp %CLASSPATH% %MAIN_CLASS% %1 %2 %3 %4 %5 %6 %7 %8 %9
