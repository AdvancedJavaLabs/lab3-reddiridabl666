package org.lab.sort;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable, Text, DoubleWritable, Text> {
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() == 0) {
            return;
        }

        String[] fields = value.toString().split("\t");

        double price = Double.parseDouble(fields[1]);

        context.write(new DoubleWritable(price), value);
    }
}
