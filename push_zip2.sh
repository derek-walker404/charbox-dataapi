#!/bin/bash

scp -i ~/.ssh/charbot-stage.pem temp/charbot-dataapi.zip ubuntu@ec2-54-84-219-171.compute-1.amazonaws.com:~
