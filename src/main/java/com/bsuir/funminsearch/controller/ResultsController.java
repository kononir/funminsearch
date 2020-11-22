package com.bsuir.funminsearch.controller;

import com.bsuir.funapproximation.util.ViewUtils;
import com.bsuir.funminsearch.data.SearchResultData;
import com.bsuir.linearsystem.model.Vector;
import com.bsuir.nonlinearequation.data.FunctionCoordinates;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultsController {
    @FXML
    private LineChart<Number, Number> functionGraphic;
    @FXML
    private LineChart<Number, Number> iterationsGraphic;
    @FXML
    private VBox uniqueMinimumsVBox;

    @FXML
    private Pane pane;

    public void initFunctionCoordinates(FunctionCoordinates fCoords) {
        ViewUtils.addSeries(functionGraphic, "f(x)", fCoords.getXVector(), fCoords.getYVector());
    }

    public void initSearchingResults(List<Map<Double, SearchResultData>> results) {
        for (int i = 0; i < results.size(); i++) {
            Map<Double, SearchResultData> minimumMap = results.get(i);
            Vector epsVector = new Vector(new ArrayList<>(minimumMap.keySet()));
            Vector itVector = new Vector(minimumMap.values().stream()
                    .map(SearchResultData::getIt)
                    .map(Integer::doubleValue)
                    .collect(Collectors.toList()));
            ViewUtils.addSeries(iterationsGraphic, "it" + i + "(ε)", itVector, epsVector);
        }

        uniqueMinimumsVBox.getChildren().addAll(buildSearchingResultLabels(results));
    }

    @FXML
    public void goToMainPage() throws IOException {
        com.bsuir.nonlinearequation.utils.ViewUtils.showWindowWithoutInitialization("/main.fxml");
        com.bsuir.nonlinearequation.utils.ViewUtils.hideWindowByNode(pane);
    }

    private List<Label> buildSearchingResultLabels(List<Map<Double, SearchResultData>> results) {
        List<Label> zValueLines = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Map<Double, SearchResultData> minimumMap = results.get(i);
            SearchResultData minimum = minimumMap.values()
                    .stream()
                    .reduce((f, s) -> s)
                    .orElseThrow(RuntimeException::new);;
            String zValueLine = "z" + i + " = " + minimum.getZ() + ", f(z" + i + ") = " + minimum.getFz();
            zValueLines.add(new Label(zValueLine));
        }
        return zValueLines;
    }
}
