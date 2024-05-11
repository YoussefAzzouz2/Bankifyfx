package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import Services.CompteClientService;

import java.sql.SQLException;
import java.util.Map;

public class stat {

    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private PieChart typeComptePieChart;

    @FXML
    private PieChart packComptePieChart;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Load data into the PieCharts when the controller is initialized
        loadTypeCompteDistribution();
        loadPackCompteDistribution();
    }

    /**
     * Loads type_compte distribution data and sets it on the typeComptePieChart.
     */
    private void loadTypeCompteDistribution() {
        try {
            // Get type_compte distribution statistics from CompteClientService
            Map<String, Integer> typeCompteStatistics = compteClientService.getTypeStatistics();

            // Create an ObservableList to hold the data for the PieChart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Populate the pieChartData with the type_compte statistics
            for (Map.Entry<String, Integer> entry : typeCompteStatistics.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            // Set the data on the typeComptePieChart
            typeComptePieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately (e.g., log the error or show an alert)
        }
    }

    /**
     * Loads pack_compte distribution data and sets it on the packComptePieChart.
     */
    private void loadPackCompteDistribution() {
        try {
            // Get pack_compte distribution statistics from CompteClientService
            Map<String, Integer> packCompteStatistics = compteClientService.getPackStatistics();

            // Create an ObservableList to hold the data for the PieChart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Populate the pieChartData with the pack_compte statistics
            for (Map.Entry<String, Integer> entry : packCompteStatistics.entrySet()) {
                pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }

            // Set the data on the packComptePieChart
            packComptePieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions appropriately (e.g., log the error or show an alert)
        }
    }

    /**
     * Handles the back button click event.
     * Closes the current statistics window.
     *
     * @param event The event that triggered this method.
     */
    public void handleBackButton(ActionEvent event) {
        // Get the source of the event (the button)
        Button sourceButton = (Button) event.getSource();
        // Close the window associated with the button
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.close();
    }

}
