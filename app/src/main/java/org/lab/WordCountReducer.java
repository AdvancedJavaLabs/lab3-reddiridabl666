package org.lab;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;

import java.io.IOException;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable result = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        int sum = 0;

        for (IntWritable val : values) {
            // Суммируем все значения для одного слова
            sum += val.get();
        }

        // Устанавливаем результат
        result.set(sum);

        // Записываем результат в контекст (Reduce)
        context.write(key, result);
    }
}
