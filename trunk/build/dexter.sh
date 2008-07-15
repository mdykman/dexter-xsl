#!/bin/sh

#find a JRE
if [ "$JAVA_HOME" != "" ]; then
	JAVA=$JAVA_HOME/bin/java
elif [ "$JAVA" != "" ]; then
	JAVA=$JAVA
else
	JAVA=`which java`
fi;

DEXTER_HOME=`dirname $0`
DEXTER_HOME=$DEXTER_HOME/..
MAIN_CLASS=org.dykman.dexter.Main
DEXTER_JAR=$DEXTER_HOME/build/dexter.jar
GETOPT_JAR=$DEXTER_HOME/lib/gnu-getopt.jar

CLASSPATH=$DEXTER_JAR:$GETOPT_JAR

${JAVA} -cp ${CLASSPATH} ${MAIN_CLASS} $@
