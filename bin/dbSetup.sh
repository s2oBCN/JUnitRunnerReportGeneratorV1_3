#!/bin/ksh -f

. ./setEnv.sh


for i in `ls ../lib/`

do
CLASSPATH=$CLASSPATH:../lib/$i
done

export CLASSPATH=${CLASSPATH}:${TEST_CLASS_PATH}:../config
echo ${CLASSPATH}

echo ${JAVA_HOME}/bin/java -classpath ${CLASSPATH} com.junit.tools.Main.Setup
${JAVA_HOME}/bin/java -classpath ${CLASSPATH} com.junit.tools.Main.Setup