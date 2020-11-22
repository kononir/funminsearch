package com.bsuir.funminsearch.logic;

import com.bsuir.funminsearch.model.SearchResult;

import java.util.function.Function;

public interface FunctionMinimumSearchService {
    SearchResult findMinimum(double x0, double h, double eps, Function<Double, Double> fun);
}
