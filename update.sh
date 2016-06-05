#!/bin/bash

echo 'update starts'

mvn compile

mvn exec:java -Dexec.mainClass="data.GankDataHanlder"

echo 'git starts'

git add .

git commit -m "week update $(date +%Y%m%d)"

git push

echo 'git stops'

