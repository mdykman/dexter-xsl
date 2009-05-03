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

SAXON_PATH=""
for j in `find saxon -name '*.jar'`; do
	SAXON_PATH=$SAXON_PATH:$j
done;

CLASSPATH=$DEXTER_JAR:$GETOPT_JAR:$SAXON_PATH

JAVA_OPTS=-Djavax.xml.transform.TransformerFactory=net.sf.saxon.TransformerFactoryImpl

${JAVA} $JAVA_OPTS -cp ${CLASSPATH} ${MAIN_CLASS} $@
