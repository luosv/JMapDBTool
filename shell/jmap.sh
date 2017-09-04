#!/bin/sh
#Create by luosv 2017-08-25
#Output jmap histo live every 120 seconds to the log file

echo $$ Running...

GAMEPID=$(ps aux | grep java | grep ./gameserver/GameServer.jar | awk '{print $2}')

if [ ! -n "$GAMEPID" ]; then
    echo Error...
    echo Unable to get to the gamepid！
    exit
fi

echo $GAMEPID

LOGPATH=jmaplog

echo Start the output log...

while [ true ]; do

    LOGDAY=$(date +%Y%m%d)
    LOGHOUR=$(date +%Y%m%d%H)
    LOGFILE=$(date +%Y%m%d%H%M%S).txt

    mkdir -p ./$LOGPATH/$LOGDAY/$LOGHOUR

    jmap -J-d64 -histo:live $GAMEPID >> ./$LOGPATH/$LOGDAY/$LOGHOUR/$LOGFILE

    sleep 120

done

