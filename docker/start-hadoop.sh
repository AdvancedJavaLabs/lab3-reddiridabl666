#!/bin/bash

# Настройка
export HADOOP_HOME=/opt/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

export HADOOP_YARN_HOME=$HADOOP_HOME
export HADOOP_SECURE_DN_USER=root
export HADOOP_DATANODE_SECURE_USER=false

export YARN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED \
--add-opens java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens java.base/java.io=ALL-UNNAMED \
--add-opens java.base/java.net=ALL-UNNAMED \
--add-opens java.base/java.nio=ALL-UNNAMED \
--add-opens java.base/java.util=ALL-UNNAMED \
--add-opens java.base/java.util.concurrent=ALL-UNNAMED \
--add-opens java.base/sun.nio.ch=ALL-UNNAMED"

mkdir -p /data/dfs/{nn,dn,snn}
chmod 755 /data/dfs/*

# Форматирование HDFS (только при первом запуске)
if [ ! -d /data/dfs/name/current ]; then
    echo "Formatting HDFS..."
    hdfs namenode -format -force
fi

# Запуск всех сервисов Hadoop
echo "Starting Hadoop services..."
hdfs --daemon start namenode
sleep 5

for i in {1..3}; do
    mkdir -p /data/dn$i

    HADOOP_PID_DIR=/tmp/dn$i \
    hdfs datanode \
        -Ddfs.datanode.address=0.0.0.0:$((50010 + i)) \
        -Ddfs.datanode.data.dir=/data/dn$i \
        -Ddfs.datanode.ipc.address=0.0.0.0:$((50020 + i)) \
        -Ddfs.datanode.http.address=0.0.0.0:$((9866 + i)) \
        &

    if [ $? -ne 0 ]; then
        echo "DataNode failed to start!"
        # Покажи ошибку
        hdfs datanode 2>&1 | head -20
    fi

    sleep 8
done

sleep 3  
yarn --daemon start resourcemanager
sleep 2
yarn --daemon start nodemanager

# Ждем запуска
sleep 5

# Проверка
echo "Checking HDFS..."
hdfs dfsadmin -report

# Создание стандартных директорий
hdfs dfs -mkdir -p /tmp
hdfs dfs -chmod -R 1777 /tmp
hdfs dfs -mkdir -p /user/root

echo "Hadoop is running!"
echo "HDFS Web UI: http://localhost:9870"
echo "YARN Web UI: http://localhost:8088"

# Держим контейнер запущенным
tail -f /dev/null
