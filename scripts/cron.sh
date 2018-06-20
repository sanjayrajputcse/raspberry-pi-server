#!/bin/bash
date
out=$(ps aux | grep "[r]aspberry-pi-server")
if [[ -z ${out} ]]
then
        echo "starting raspberry-pi-server..."
        /home/sanjay.rajput/run.sh
else
        echo "raspberry-pi-server already running!!!"
fi