#!/bin/bash

echo 'update starts'

mvn compile

mvn exec:java -Dexec.mainClass="data.GankDataHanlder"

echo 'update stops'


