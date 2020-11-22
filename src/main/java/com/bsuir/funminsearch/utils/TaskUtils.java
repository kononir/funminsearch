package com.bsuir.funminsearch.utils;

import com.bsuir.funminsearch.data.SearchResultData;
import com.bsuir.funminsearch.model.SearchResult;

import java.util.function.Function;

public class TaskUtils {
    public static final double[] EPS_MAS = {1e-2, 1e-3, 1e-4, 1e-5};

    private TaskUtils() {
    }

    public static SearchResultData calculateFunction(SearchResult searchResult, Function<Double, Double> fun) {
        double z = searchResult.getZ();
        return new SearchResultData(z, fun.apply(z), searchResult.getIt());
    }
}
