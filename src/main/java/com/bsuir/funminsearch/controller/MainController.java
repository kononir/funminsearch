package com.bsuir.funminsearch.controller;

import com.bsuir.funapproximation.logic.FunctionService;
import com.bsuir.funapproximation.logic.impl.FunctionServiceImpl;
import com.bsuir.linearsystem.model.Vector;
import com.bsuir.nonlinearequation.data.FunctionCoordinates;
import com.bsuir.nonlinearequation.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static com.bsuir.funminsearch.utils.VariantUtils.getF;

public class MainController {
    private final FunctionService functionService = new FunctionServiceImpl();

    @FXML
    private TextField aField;
    @FXML
    private TextField bField;
    @FXML
    private TextField mField;

    @FXML
    private Pane pane;

    public void calculateAndShowFunction() throws IOException {
        double a = Double.parseDouble(aField.getText());
        double b = Double.parseDouble(bField.getText());
        int m = Integer.parseInt(mField.getText());

        Vector xVector = functionService.calculateXVector(a, b, m + 1);
        Vector yVector = functionService.calculateYVector(getF(), xVector);

        FunctionCoordinates functionCalculationResult = FunctionCoordinates.builder()
                .xVector(xVector)
                .yVector(yVector)
                .build();

        ViewUtils.showWindow("/searching.fxml",
                (controller) -> ((SearchingController) controller).initFunctionCoordinates(functionCalculationResult));
        ViewUtils.hideWindowByNode(pane);
    }
}
