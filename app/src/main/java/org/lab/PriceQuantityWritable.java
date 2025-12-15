package org.lab;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceQuantityWritable implements Writable {
    private double totalPrice;
    private int quantity;

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(totalPrice);
        out.writeInt(quantity);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        totalPrice = in.readDouble();
        quantity = in.readInt();
    }

    @Override
    public String toString() {
        return String.format("%f\t%d", totalPrice, quantity);
    }
}
