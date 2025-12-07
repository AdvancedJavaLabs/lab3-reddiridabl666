package org.lab;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);

    private Text word = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        // Разбиваем строку на слова
        String[] words = line.split("\\W+");

        for (String wordStr : words) {
            if (!wordStr.isEmpty()) {
                // Преобразуем слово в нижний регистр
                word.set(wordStr.toLowerCase());

                // Отправляем слово и 1 в контекст
                context.write(word, one);
            }
        }
    }
}
