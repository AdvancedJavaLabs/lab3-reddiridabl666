./gradlew fatJar

export HADOOP_NAME=custom-hadoop

INPUT=$1.csv
REDUCERS=$2

if [ $# -eq 0 ]; then
    INPUT=0.csv
fi

if [ $# -lt 2 ]; then
    REDUCERS=1
fi

docker cp $INPUT $HADOOP_NAME:/tmp/

docker exec $HADOOP_NAME hdfs dfs -put /tmp/$INPUT /$INPUT

docker cp app/build/libs/app-standalone.jar $HADOOP_NAME:/tmp/job.jar

docker exec $HADOOP_NAME hdfs dfs -rm -r /out
docker exec $HADOOP_NAME hdfs dfs -rm -r /out-raw

docker exec $HADOOP_NAME hadoop jar /tmp/job.jar /$INPUT /out $REDUCERS

# for i in {0..9}; do
#     docker exec $HADOOP_NAME hdfs dfs -cat /out/part-r-0000$i
# done

docker exec $HADOOP_NAME hdfs dfs -cat /out/part-r-00000
