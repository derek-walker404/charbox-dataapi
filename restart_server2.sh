#!/bin/bash

ssh -i ~/.ssh/charbot-stage.pem ubuntu@ec2-54-84-219-171.compute-1.amazonaws.com './install.sh'

ssh -i ~/.ssh/charbot-stage.pem ubuntu@ec2-54-84-219-171.compute-1.amazonaws.com "sh -c 'nohup ./start_server.sh > /dev/null 2>&1 &'"
