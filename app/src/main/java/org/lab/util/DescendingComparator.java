package org.lab.util;

import org.apache.hadoop.io.*;

public class DescendingComparator extends WritableComparator {
    public DescendingComparator() {
        super(DoubleWritable.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        return -1 * super.compare(a, b);
    }
}
