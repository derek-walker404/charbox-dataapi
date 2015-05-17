#!/bin/bash

rm -rf temp;mkdir temp
cp target/char*.jar temp/
cp app.properties temp/
cp conmon.yml temp/
cd temp
zip charbot-dataapi.zip ./*
