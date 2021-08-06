#!/usr/bin/env bash

MYSQL=0
KLOIA=0
ACTION=0
DB=0

usage() { echo "Usage: $0 [-a down|up|stop|build] [-mk] [-h(help)]";}

help() {
	usage
	echo "
	h: Show help
	m: Mysql
	k: Kloia Microservices"
	exit 0;
}


while getopts ":a:mkhu:" arg; do
	case $arg in
	a) # Specify action.
		#echo "p is ${OPTARG}"
		ACTION=${OPTARG}
		;;
	u) DB=${OPTARG}
		;;
	h) help
		;;
	m) MYSQL=1
		;;
	k) KLOIA=1
		;;
	?) # Display help.
		usage
		exit 1
		;;
	*) # Display help.
		usage
		exit 1
	esac
done

#echo $ACTION

if [ $# -eq 0 ];
then
    usage
	exit 1
fi

if [ $MYSQL -eq 0 ] && [ $KLOIA -eq 0 ]
then
    usage
	exit 1
fi


if [ $ACTION != "up" ] && [ $ACTION != "down" ] && [ $ACTION != "stop" ] && [ $ACTION != "build" ]
then
    usage
	exit 1
fi

set -a
. kloiaEnv.env
set +a


if [ $DB = "create" ]
then
	DB_EXECUTE=create
else
	DB_EXECUTE=update
fi


if [ $ACTION = "up" ]
then
    ACTION+=" -d"
	LOGACTION="Starting"
elif [ $ACTION = "down" ]
then
	LOGACTION="Removing"
elif [ $ACTION = "build" ]
then
	ACTION=" up -d --build"
	LOGACTION="Building"
else
	LOGACTION="Stopping"
fi

if [ $MYSQL -eq 1 ]
then
	echo "##$LOGACTION Mysql DB##"
	docker-compose -f mysql/docker-compose.yml $ACTION  #Change path
	if [ $? -ne 0 ]
	then
		echo "##$LOGACTION Mysql DB has failed!##"
		exit 1
	fi
fi

if [ $KLOIA -eq 1 ]
then
	echo "##$LOGACTION Kloia Microservice##"
	docker-compose -f Packages/docker-compose.yml $ACTION  #Change path
	if [ $? -ne 0 ]
	then
		echo "##$LOGACTION Kloia Microservice has failed!##"
		exit 1
	fi
fi


echo "##Finished Successfully##"
