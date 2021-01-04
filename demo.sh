#!/bin/bash
NBCLIENTS=2
JARFILE="client.jar"
[ $# -eq 1 ] && NBCLIENTS=$1
[ $# -eq 2 ] && JARFILE=$2

[ ! -f $JARFILE ] && echo "$JARFILE not found" && exit 0

#launch prgrm
for N in $(seq $NBCLIENTS) ; do
	ID="client$N"
	java -jar client.jar &
done
