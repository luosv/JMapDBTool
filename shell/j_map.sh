#!/bin/sh
#Create by luosv 2017-08-25
#Output jmap histo live every 120 seconds to the log file

echo $$ Running...

GAMEPID=$(ps aux | grep java | grep ../../game/gameserver/GameServer.jar | awk '{print $2}')

if [ ! -n "$GAMEPID" ]; then
    echo Error...
    echo Unable to get to the gamepid！
    exit
fi

echo $GAMEPID

echo Start the output log...

jmap -J-d64 -histo:live $GAMEPID >> ../logs/j_map.log

