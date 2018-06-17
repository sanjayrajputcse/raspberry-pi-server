#!/bin/bash

cloud_ip=<>
cloud_user=<>
out=$(ps aux | grep "[3]360:$cloud_ip:3306")
if [[ -z $out ]]
then
        echo creating tunnel 3360:$cloud_ip:3306
        /usr/bin/ssh -i ~/.ssh/google_compute_engine -f -N -q -L 3360:$cloud_ip:3306 $cloud_user@$cloud_ip
else
        echo Tunnel already exist!!!
fi
