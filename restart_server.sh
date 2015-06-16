#!/bin/bash

ssh -i ~/.ssh/charbot-stage.pem ubuntu@api01.charbot.co './install.sh'

ssh -i ~/.ssh/charbot-stage.pem ubuntu@api01.charbot.co "sh -c 'nohup ./start_server.sh > /dev/null 2>&1 &'"
