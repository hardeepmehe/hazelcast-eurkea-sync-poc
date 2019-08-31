#!/usr/bin/env bash
#runpoc.sh

nohup mvn spring-boot:run -f eureka-server/pom.xml &
#nohup mvn spring-boot:run -Drun.arguments=--server.port=8080  -f user-service/pom.xml -Drun.jvmArguments="-Dserver.port=8080" &
nohup mvn spring-boot:run -Drun.jvmArguments=--server.port=8081 -f user-service/pom.xml -Drun.jvmArguments=--server.port=8081 &

tail -1000f nohup.out