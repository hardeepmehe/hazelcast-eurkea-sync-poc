#!/usr/bin/env bash
#runpoc.sh


kill $(ps aux | grep 'user-service' | awk '{print $2}')
kill $(ps aux | grep 'eureka-server' | awk '{print $2}')