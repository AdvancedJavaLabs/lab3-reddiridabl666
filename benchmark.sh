run_test() {
    local reducers=$1
    local block_size=$2

    echo "Тест: reducers=$reducers, block_size=$block_size KB"

    docker exec $CONTAINER_NAME hdfs dfs -rm -r /out /out-raw 2>/dev/null || true

    local START_TIME=$(date +%s)

    echo "Запуск MapReduce задания..."

    if docker exec $CONTAINER_NAME hadoop jar /tmp/job.jar /input /out $reducers $block_size > /tmp/hadoop_benchmark.log 2>&1; then

        if docker exec $CONTAINER_NAME hdfs dfs -test -e /out/_SUCCESS 2>/dev/null; then
            local success=1
            echo "Задание завершилось"
        else
            local success=0
            echo "Задание завершилось, но _SUCCESS не найден"
        fi
    else
        local success=0
        echo "Ошибка выполнения задания"
    fi

    local END_TIME=$(date +%s)
    local duration=$((END_TIME - START_TIME))

    echo "$reducers,$block_size,$duration" >> "$RESULTS_FILE"

    if [ $success -eq 1 ]; then
        echo "Успешно! Время: ${duration}с"
    else
        echo "Ошибка! Время: ${duration}с"
        echo "Последние строки лога:"
        tail -10 /tmp/hadoop_benchmark.log
    fi

    sleep 2
}

CONTAINER_NAME=custom-hadoop

REDUCERS_LIST=(16 8 4 2 1)
BLOCK_SIZES_KB=(64 128 256 512 1024 2048 4096)

RESULTS_FILE="benchmark/benchmark_results.csv"

echo "reducers,block_size_kb,time_sec" > "$RESULTS_FILE"

./gradlew fatJar

docker cp app/build/libs/app-standalone.jar $CONTAINER_NAME:/tmp/job.jar

echo 'Uploading test data'

for csv_file in *.csv; do
    if [ -f "$csv_file" ]; then
        filename=$(basename "$csv_file")
        docker cp "$csv_file" $CONTAINER_NAME:/tmp/
        docker exec $CONTAINER_NAME hdfs dfs -put -f /tmp/$filename /input
    fi
done


echo 'Running tests'

for reducers in "${REDUCERS_LIST[@]}"; do
    for block_size in "${BLOCK_SIZES_KB[@]}"; do
        run_test $reducers $block_size
    done
done
