#!/bin/bash
chmod -R 777 directory
if [ -d "directory" ]; then rm -r directory; fi
java -jar cgi-java.jar
