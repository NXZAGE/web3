package com.itmo.nxzage.utils;

import java.io.Serializable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class ArrayUtils implements Serializable {
    public double[] range(double from, double to, double step) {
        int size = (int) ((to - from) / step) + 1;
        double[] result = new double[size];
        double value = from;

        for (int i = 0; i < size; i++) {
            result[i] = value;
            value += step;
        }
        return result;
    }
}
