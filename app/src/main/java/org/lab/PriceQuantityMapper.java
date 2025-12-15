package org.lab;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;

import java.io.IOException;

public class PriceQuantityMapper extends Mapper<LongWritable, Text, Text, PriceQuantityWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() == 0) {
            return;
        }

        String[] fields = value.toString().split(",");

        String category = fields[2];

        double price = Double.parseDouble(fields[3]);

        int quantity = Integer.parseInt(fields[4]);

        context.write(new Text(category), new PriceQuantityWritable(price * quantity, quantity));
    }
}
