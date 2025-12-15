#!/bin/bash

# Настройка
# export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
# export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# export HADOOP_YARN_HOME=$HADOOP_HOME
# export HADOOP_SECURE_DN_USER=root
# export HADOOP_DATANODE_SECURE_USER=false

# export YARN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED \
# --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
# --add-opens java.base/java.io=ALL-UNNAMED \
# --add-opens java.base/java.net=ALL-UNNAMED \
# --add-opens java.base/java.nio=ALL-UNNAMED \
# --add-opens java.base/java.util=ALL-UNNAMED \
# --add-opens java.base/java.util.concurrent=ALL-UNNAMED \
# --add-opens java.base/sun.nio.ch=ALL-UNNAMED"

# mkdir -p /data/dfs/{nn,dn,snn}
# chmod 755 /data/dfs/*

# export HADOOP_HOME=/opt/hadoop

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
