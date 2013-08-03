#!/bin/sh
numberOfLines=0
numberOfFiles=0
for fileName in $(find -name *.java); do 
	numberOfLines=$(expr $(wc -l $fileName | awk '{print $1}') + $numberOfLines)
	(( numberOfFiles++ ))
done

echo Total number of lines \= $numberOfLines in $numberOfFiles files \(only .java files\)
