#!/bin/ksh -f

. ./setEnv.sh


for i in `ls ../lib/`

do
CLASSPATH=$CLASSPATH:../lib/$i
done

export CLASSPATH=${CLASSPATH}:${TEST_CLASS_PATH}:../config

${JAVA_HOME}/bin/java -classpath ${CLASSPATH} com.junit.tools.Main.TestRunner
