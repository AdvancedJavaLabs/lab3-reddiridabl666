package org.lab;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCountDriver {
    public static void main(String[] args) throws Exception {
        // Проверка аргументов (путей для входных и выходных данных)
        if (args.length != 2) {
            System.err.println("Usage: WordCountDriver <input path> <output path>");

            System.exit(-1);
        }

        // Создаем конфигурацию Hadoop
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Word Count");

        // Устанавливаем главный класс (Driver)
        job.setJarByClass(WordCountDriver.class);

        // Устанавливаем Mapper и Reducer
        job.setMapperClass(WordCountMapper.class);

        job.setReducerClass(WordCountReducer.class);

        // Устанавливаем типы выходных данных для Mapper и Reducer
        job.setOutputKeyClass(Text.class);

        job.setOutputValueClass(IntWritable.class);

        // Устанавливаем путь к входным и выходным данным
        FileInputFormat.addInputPath(job, new Path(args[0]));

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // Ожидаем завершения работы задачи
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
