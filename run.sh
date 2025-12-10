./gradlew fatJar

export HADOOP_NAME=custom-hadoop

docker cp app/src/main/resources/org/lab/test.txt $HADOOP_NAME:/tmp/

docker exec $HADOOP_NAME hdfs dfs -put /tmp/test.txt /test.txt

docker cp app/build/libs/app-standalone.jar $HADOOP_NAME:/tmp/job.jar

docker exec $HADOOP_NAME hdfs dfs -rm -r /out
docker exec $HADOOP_NAME hadoop jar /tmp/job.jar /test.txt /out

docker exec $HADOOP_NAME hdfs dfs -cat /out/part-r-00000
