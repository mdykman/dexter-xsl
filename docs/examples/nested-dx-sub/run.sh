#!/bin/sh

INPUT=$1
shift
DATA=$1
shift
RESULT=$1
shift

TMPDAT="tmp-${DATA}.res"

dexter.sh ${INPUT}
dexter.sh -x${INPUT}.xsl ${DATA} > $TMPDAT
diff $RESULT $TMPDAT
if [[ $? == "0" ]]; then
	echo success
	rm $TMPDAT
else
	echo test failed
fi;
