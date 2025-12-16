#!/bin/bash

export PATH=$JAVA_HOME/bin:$PATH

export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_SECONDARYNAMENODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root

service ssh start

hdfs namenode -format

start-dfs.sh

start-yarn.sh

hdfs dfsadmin -report

tail -f /dev/null
