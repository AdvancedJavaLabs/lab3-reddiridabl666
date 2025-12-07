./gradlew fatJar

HADOOP_NAME=namenode

docker compose cp app/src/main/resources/org/lab/test.txt $HADOOP_NAME:/tmp/

docker compose exec $HADOOP_NAME hdfs dfs -put /tmp/test.txt /test.txt

docker compose cp app/build/libs/app-standalone.jar $HADOOP_NAME:/tmp/job.jar

docker compose exec $HADOOP_NAME hadoop jar /tmp/job.jar /test.txt /out.txt
