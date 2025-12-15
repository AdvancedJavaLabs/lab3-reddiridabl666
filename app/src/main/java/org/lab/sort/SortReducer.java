package org.lab.sort;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

import java.io.IOException;

public class SortReducer extends Reducer<DoubleWritable, Text, Text, NullWritable> {
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        context.write(new Text("Category\tRevenue\tQuantity"), null);
    }

    @Override
    public void reduce(DoubleWritable key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        for (Text val : values) {
            context.write(val, NullWritable.get());
        }
    }
}
