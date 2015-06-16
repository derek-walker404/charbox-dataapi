#!/bin/bash

scp -i ~/.ssh/charbot-stage.pem temp/charbot-dataapi.zip ubuntu@api01.charbot.co:~
