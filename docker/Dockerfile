FROM eclipse-temurin:17-jdk-noble

RUN apt-get update && apt-get install -y wget openssh-server sudo && \
    wget -q https://downloads.apache.org/hadoop/common/hadoop-3.4.1/hadoop-3.4.1.tar.gz && \
    tar -xzf hadoop-3.4.1.tar.gz -C /opt/ && \
    mv /opt/hadoop-3.4.1 /opt/hadoop && \
    rm hadoop-3.4.1.tar.gz

RUN ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
    chmod 0600 ~/.ssh/authorized_keys

ENV HADOOP_HOME=/opt/hadoop
ENV PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

COPY etc/* $HADOOP_HOME/etc/hadoop/

COPY start-hadoop.sh /start-hadoop.sh
RUN chmod +x /start-hadoop.sh

EXPOSE 9870 8088

CMD ["/start-hadoop.sh"]
