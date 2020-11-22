package com.bsuir.funminsearch.utils;

import java.util.function.Function;

public class VariantUtils {
    private VariantUtils() {
    }

    public static Function<Double, Double> getF() {
        return (x) -> x * x - 4 * x * Math.sin(x) + Math.cos(x);
    }
}
