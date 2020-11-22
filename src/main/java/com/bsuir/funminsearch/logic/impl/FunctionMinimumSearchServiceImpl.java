package com.bsuir.funminsearch.logic.impl;

import com.bsuir.funminsearch.logic.FunctionMinimumSearchService;
import com.bsuir.funminsearch.model.SearchResult;

import java.util.function.Function;

public class FunctionMinimumSearchServiceImpl implements FunctionMinimumSearchService {
    @Override
    public SearchResult findMinimum(double x0, double h, double eps, Function<Double, Double> fun) {
        double x1 = x0;
        double y1 = fun.apply(x1);
        double y0;
        int it = 0;
        do {
            it++;
            x0 = x1;
            y0 = y1;
            x1 = x0 + h;
            y1 = fun.apply(x1);
            if (y1 > y0) {
                h = -h / 4;
            }
        } while (y1 <= y0 && Math.abs(h) >= eps / 4);
        return new SearchResult(x0, it);
    }
}
