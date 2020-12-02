#!/bin/bash

mkdir -p /var/log/KafkaTestTool

sh /opt/KafkaTestTool/start.sh > /var/log/KafkaTestTool/out.log && tail -F /var/log/KafkaTestTool/out.log