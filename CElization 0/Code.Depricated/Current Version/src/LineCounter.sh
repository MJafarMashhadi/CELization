#!/bin/sh
numberOfLines=0
for fileName in $(find *.java .); do 
	numberOfLines=$(expr $(wc -l $fileName | awk '{print $1}') + $numberOfLines)
done

echo Total number of lines \= $numberOfLines
