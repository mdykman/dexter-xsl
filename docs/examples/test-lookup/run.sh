#!/bin/sh

INPUT=$1
shift
DATA=$1
shift
RESULT=$1
shift

DIFF="diff -w --ignore-blank-lines"
TMPDAT="tmp-${DATA}.res"

dexter.sh ${INPUT}
dexter.sh -x${INPUT}.xsl ${DATA} > $TMPDAT
$DIFF ${RESULT} ${TMPDAT}
if [[ $? == "0" ]]; then
	echo ok
	rm $TMPDAT
#	rm *.xsl
	exit 0 
else
	echo fail
	exit 1
fi;
