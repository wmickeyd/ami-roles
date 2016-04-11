#!/usr/bin/env bash

if [ -e /usr/bin/apt-get ];
 then 
  sudo apt-get clean
  sudo apt-get update
  sudo apt-get -y install python-pip python-setuptools python-dev
 else
   echo "This doesn't appear to be Unbuntu"
 fi