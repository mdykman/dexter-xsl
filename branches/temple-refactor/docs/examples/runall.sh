#!/bin/sh

for dd in `find -type d -name 'test-*'`; do
	cd ${dd} 
	echo -n ${dd} "	"
	sh run.sh source.xml data.xml result.xml
	cd ..
done;
