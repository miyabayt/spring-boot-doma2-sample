#!/bin/sh

while true; 
  do date;
  if docker logs oracle12 | grep "Database ready to use. Enjoy! ;)" >/dev/null ; then
    break
  fi
  sleep 5s;
done
