package com.bsuir.funminsearch.controller;

import com.bsuir.funminsearch.data.SearchResultData;
import com.bsuir.funminsearch.logic.FunctionMinimumSearchService;
import com.bsuir.funminsearch.logic.impl.FunctionMinimumSearchServiceImpl;
import com.bsuir.funminsearch.model.SearchResult;
import com.bsuir.funminsearch.view.FunctionMinTableRow;
import com.bsuir.nonlinearequation.data.FunctionCoordinates;
import com.bsuir.nonlinearequation.utils.TaskUtils;
import com.bsuir.nonlinearequation.utils.ViewUtils;
import com.bsuir.nonlinearequation.view.FunctionCoordinateTableRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.*;

import static com.bsuir.funminsearch.utils.TaskUtils.EPS_MAS;
import static com.bsuir.funminsearch.utils.TaskUtils.calculateFunction;
import static com.bsuir.funminsearch.utils.VariantUtils.getF;

public class SearchingController {
    private final FunctionMinimumSearchService functionMinimumSearchService = new FunctionMinimumSearchServiceImpl();

    @FXML
    private TableView<FunctionCoordinateTableRow> functionTable;
    @FXML
    private TableColumn<FunctionCoordinateTableRow, Double> functionXColumn;
    @FXML
    private TableColumn<FunctionCoordinateTableRow, Double> functionYColumn;

    @FXML
    private TableView<FunctionMinTableRow> minimumsTable;
    @FXML
    private TableColumn<FunctionMinTableRow, Integer> minimumsNumColumn;
    @FXML
    private TableColumn<FunctionMinTableRow, Double> minimumsXColumn;
    @FXML
    private TableColumn<FunctionMinTableRow, Double> minimumsYColumn;

    @FXML
    private TextField x0Field;
    @FXML
    private TextField hField;

    @FXML
    private Pane pane;

    private FunctionCoordinates functionCalculationResult;
    private final List<Map<Double, SearchResultData>> searchingResults = new ArrayList<>();

    @FXML
    private void initialize() {
        functionXColumn.setCellValueFactory(cellData -> cellData.getValue().xProperty().asObject());
        functionYColumn.setCellValueFactory(cellData -> cellData.getValue().yProperty().asObject());

        minimumsNumColumn.setCellValueFactory(cellData -> cellData.getValue().numProperty().asObject());
        minimumsXColumn.setCellValueFactory(cellData -> cellData.getValue().xProperty().asObject());
        minimumsYColumn.setCellValueFactory(cellData -> cellData.getValue().yProperty().asObject());
    }

    public void initFunctionCoordinates(FunctionCoordinates functionCalculationResult) {
        this.functionCalculationResult = functionCalculationResult;
        functionTable.setItems(TaskUtils.convertToObservableList(functionCalculationResult));
    }

    @FXML
    public void findFunctionMinimum() throws IOException {
        double x0 = Double.parseDouble(x0Field.getText());
        double h = Double.parseDouble(hField.getText());

        Map<Double, SearchResultData> searchResultDataMap = new HashMap<>();
        for (double eps : EPS_MAS) {
            SearchResult searchResult = functionMinimumSearchService.findMinimum(x0, h, eps, getF());
            SearchResultData searchResultData = calculateFunction(searchResult, getF());
            searchResultDataMap.put(eps, searchResultData);
        }

        SearchResultData showingSearchResultData = searchResultDataMap.values()
                .stream()
                .reduce((f, s) -> s)
                .orElseThrow(RuntimeException::new);
        double z = showingSearchResultData.getZ();
        double fz = showingSearchResultData.getFz();

        ObservableList<FunctionMinTableRow> minimumsTableItems = minimumsTable.getItems();
        if (minimumsTableItems != null) {
            int currNumber = minimumsTableItems.size();
            FunctionMinTableRow functionMinTableRow = new FunctionMinTableRow(currNumber + 1, z, fz);
            minimumsTableItems.add(functionMinTableRow);
        } else {
            FunctionMinTableRow functionMinTableRow = new FunctionMinTableRow(1, z, fz);
            minimumsTable.setItems(FXCollections.observableArrayList(functionMinTableRow));
        }

        searchingResults.add(searchResultDataMap);

        showIsAllMinimumsAlert();
    }

    @FXML
    public void showIsAllMinimumsAlert() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle("Минимумы");
        alert.setHeaderText("Найдены все минимумы?");

        ButtonType yesButton = new ButtonType("Да");
        ButtonType noButton = new ButtonType("Нет");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(yesButton, noButton);

        Optional<ButtonType> answerOption = alert.showAndWait();

        if (answerOption.isPresent() && yesButton.equals(answerOption.get())) {
            showResultsWindow();
            ViewUtils.hideWindowByNode(pane);
        } else {
            x0Field.clear();
        }
    }

    private void showResultsWindow() throws IOException {
        ViewUtils.showWindow("/results.fxml",
                (c) -> {
                    ResultsController controller = ((ResultsController) c);
                    controller.initFunctionCoordinates(functionCalculationResult);
                    controller.initSearchingResults(searchingResults);
                });
    }
}
