package org.lab;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.lab.sort.SortMapper;
import org.lab.sort.SortReducer;
import org.lab.util.DescendingComparator;

public class PriceQuantityDriver {
    private static final String unsortedResultsSuffix = "-raw";

    private static int reducersNum = 1;

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: PriceQuantityDriver <input path> <output path> <reducers num>");
            System.exit(-1);
        }

        reducersNum = Integer.parseInt(args[2]);

        Configuration conf = new Configuration();

        var ok = runMapReduceJob(conf, args);
        if (!ok) {
            System.exit(1);
        }

        ok = runSortJob(conf, args);
        if (!ok) {
            System.exit(1);
        }
    }

    static boolean runMapReduceJob(Configuration conf, String[] args) throws Exception {
        Job job = Job.getInstance(conf, "Price and quantity reduce");

        job.setNumReduceTasks(reducersNum);

        job.setJarByClass(PriceQuantityDriver.class);
        job.setMapperClass(PriceQuantityMapper.class);
        job.setReducerClass(PriceQuantityReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(PriceQuantityWritable.class);

        MultipleOutputs.addNamedOutput(job, "output",
                SequenceFileOutputFormat.class,
                Text.class, PriceQuantityWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));

        FileOutputFormat.setOutputPath(job, new Path(args[1] + unsortedResultsSuffix));

        return job.waitForCompletion(true);
    }

    static boolean runSortJob(Configuration conf, String[] args) throws Exception {
        Job job = Job.getInstance(conf, "Sort");

        job.setNumReduceTasks(reducersNum);

        job.setJarByClass(PriceQuantityDriver.class);
        job.setMapperClass(SortMapper.class);
        job.setReducerClass(SortReducer.class);

        job.setMapOutputKeyClass(DoubleWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setSortComparatorClass(DescendingComparator.class);

        MultipleOutputs.addNamedOutput(job, "output",
                SequenceFileOutputFormat.class,
                Text.class, PriceQuantityWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[1] + unsortedResultsSuffix));

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        return job.waitForCompletion(true);
    }
}
