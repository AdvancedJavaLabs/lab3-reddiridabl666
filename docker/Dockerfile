FROM eclipse-temurin:17-jdk-noble

# Установка Hadoop
RUN apt-get update && apt-get install -y wget openssh-server && \
    wget -q https://downloads.apache.org/hadoop/common/hadoop-3.4.1/hadoop-3.4.1.tar.gz && \
    tar -xzf hadoop-3.4.1.tar.gz -C /opt/ && \
    mv /opt/hadoop-3.4.1 /opt/hadoop && \
    rm hadoop-3.4.1.tar.gz

RUN apt update && apt install -y jsvc

# Настройка
ENV HADOOP_HOME=/opt/hadoop
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

# Конфигурация для standalone режима
RUN mkdir -p /hadoop/dfs/name /hadoop/dfs/data && \
    echo "export HADOOP_SSH_OPTS=\"-o PasswordAuthentication=no\"" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh

# Копируем конфиги
COPY etc/*.xml $HADOOP_HOME/etc/hadoop/

# Скрипт запуска
COPY start-hadoop.sh /start-hadoop.sh
RUN chmod +x /start-hadoop.sh

EXPOSE 9870 8088

CMD ["/start-hadoop.sh"]
