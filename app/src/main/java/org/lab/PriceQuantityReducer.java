package org.lab;

import org.apache.hadoop.io.*;

import org.apache.hadoop.mapreduce.*;

import java.io.IOException;

public class PriceQuantityReducer extends Reducer<Text, PriceQuantityWritable, Text, PriceQuantityWritable> {
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        context.write(new Text("Category\tRevenue\tQuantity"), null);
    }

    @Override
    public void reduce(Text key, Iterable<PriceQuantityWritable> values, Context context)
            throws IOException, InterruptedException {
        double totalPrice = 0;
        int totalQuantity = 0;

        for (PriceQuantityWritable val : values) {
            totalPrice += val.getTotalPrice();
            totalQuantity += val.getQuantity();
        }

        context.write(key, new PriceQuantityWritable(totalPrice, totalQuantity));
    }
}
