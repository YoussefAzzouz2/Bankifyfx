package Controllers;
import Models.CompteClient;
import Services.CompteClientService;
import Utils.PDFGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import Utils.Statistics;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static java.time.zone.ZoneRulesProvider.refresh;

public class getCompteClient {

    private final Statistics statistics = new Statistics();
    private final CompteClientService compteClientService = new CompteClientService();

    @FXML
    private TableView<CompteClient> compteClientTable;

    @FXML
    private TableColumn<CompteClient, String> typeCompteColumn;

    @FXML
    private TableColumn<CompteClient, String> packCompteColumn;

    @FXML
    private TableColumn<CompteClient, String> nomColumn;

    @FXML
    private TableColumn<CompteClient, String> prenomColumn;

    @FXML
    private TableColumn<CompteClient, String> ribColumn;

    @FXML
    private TableColumn<CompteClient, String> mailColumn;

    @FXML
    private TableColumn<CompteClient, String> telColumn;

    @FXML
    private TableColumn<CompteClient, Float> soldeColumn;


    @FXML
    private TableColumn<CompteClient, Void> modifyColumn;

    @FXML
    private TableColumn<CompteClient, Void> deleteColumn;

    @FXML
    private Label maleCountLabel;

    @FXML
    private Label femaleCountLabel;

    @FXML
    private Label otherCountLabel;
    @FXML
    private TableColumn<CompteClient, ImageView> qrCodeColumn;

    @FXML
    private  TextField tfrecherche;

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        ribColumn.setCellValueFactory(new PropertyValueFactory<>("rib"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("mail"));
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
        soldeColumn.setCellValueFactory(new PropertyValueFactory<>("solde"));
        typeCompteColumn.setCellValueFactory(new PropertyValueFactory<>("type_compte"));
        packCompteColumn.setCellValueFactory(new PropertyValueFactory<>("pack_compte"));



        modifyColumn.setCellFactory(new Callback<TableColumn<CompteClient, Void>, TableCell<CompteClient, Void>>() {
            @Override
            public TableCell<CompteClient, Void> call(final TableColumn<CompteClient, Void> param) {
                return new TableCell<CompteClient, Void>() {
                    private final Button modifyButton = new Button("Modifier");

                    {
                        modifyButton.setOnAction(event -> {
                            CompteClient compteClient = getTableView().getItems().get(getIndex());
                            handleModify(compteClient);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                        }
                    }
                };
            }
        });

        deleteColumn.setCellFactory(column -> new TableCell<CompteClient, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    CompteClient compteClient = getTableView().getItems().get(getIndex());
                    handleDelete(compteClient);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        qrCodeColumn.setCellFactory(column -> new TableCell<CompteClient, ImageView>() {
            private final ImageView imageView = new ImageView();
            {
                // Set up rendering of the QR code image
                imageView.setFitHeight(115);
                imageView.setFitWidth(115);
            }

            @Override
            protected void updateItem(ImageView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CompteClient compteClient = getTableView().getItems().get(getIndex());
                    Image qrCodeImage = generateQRCodeImage(compteClient.getId());
                    imageView.setImage(qrCodeImage);
                    setGraphic(imageView);
                }
            }
        });

        loadData();
    }
    private void refresh(Set<CompteClient> compte) {
        // Convert the Set of Cartes to an ObservableList
        ObservableList<CompteClient> observableCartes = FXCollections.observableArrayList(compte);

        // Update the TableView with the new data
        compteClientTable.setItems(observableCartes);

        // If you're using a ComboBox for sorting criteria and want to refresh its selection, you can do that here
        // Example: if you want to clear the ComboBox selection

    }
    public void recherche_avance(){


        Set<CompteClient> CompteClientSet = new HashSet<>(CompteClientService.getAllCompte());

        // Refresh the view with the set of Cartes
        refresh(CompteClientSet);
        ObservableList<CompteClient> data= FXCollections.observableArrayList(CompteClientService.getAllCompte());
        FilteredList<CompteClient> filteredList=new FilteredList<>(data, c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty()|| newValue==null){
                    return true;
                }
                if(c.getNom().contains(newValue)){
                    return true;
                }
                else if(c.getPrenom().contains(newValue)){
                    return true;
                }
                else if(c.getRib().contains(newValue)){
                    return true;
                }

                else{
                    return false;
                }
            });

            refresh(new HashSet<>(filteredList));
        });
    }

    private void handleModify(CompteClient compteClient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modiCompte.fxml"));
            Parent root = loader.load();
            modiCompte controller = loader.getController();
            controller.initData(compteClient);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le compte client");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(CompteClient compteClient) {
        try {
            int compteClientId = compteClient.getId();
            compteClientService.delete(compteClientId);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            List<CompteClient> compteClients = compteClientService.getAll();
            compteClientTable.getItems().setAll(compteClients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showCompteStatistics() {
        // Create a stage for the pie charts
        Stage stage = new Stage();
        stage.setTitle("Compte Statistics");

        try {
            // Get the type_compte statistics from the CompteClientService
            Map<String, Integer> typeStatistics = compteClientService.getTypeStatistics();
            // Get the pack_compte statistics from the CompteClientService
            Map<String, Integer> packStatistics = compteClientService.getPackStatistics();

            // Create the PieCharts with the data
            PieChart typePieChart = createPieChart("Type Compte Distribution", typeStatistics);
            PieChart packPieChart = createPieChart("Pack Compte Distribution", packStatistics);

            // Set colors for typePieChart
            ObservableList<PieChart.Data> typeData = typePieChart.getData();
            for (int i = 0; i < typeData.size(); i++) {
                typeData.get(i).getNode().setStyle("-fx-pie-color: " + getColorHexString(i));
            }

            // Set colors for packPieChart
            ObservableList<PieChart.Data> packData = packPieChart.getData();
            for (int i = 0; i < packData.size(); i++) {
                packData.get(i).getNode().setStyle("-fx-pie-color: " + getColorHexString(i));
            }

            // Customize tooltips to display count when hovering over segments
            customizePieChartTooltip(typePieChart);
            customizePieChartTooltip(packPieChart);

            // Create a layout pane to hold the PieCharts
            HBox root = new HBox();
            root.setSpacing(20);
            root.getChildren().addAll(typePieChart, packPieChart);

            // Create a scene with the root pane and set it on the stage
            Scene scene = new Scene(root, 1000, 400);
            stage.setScene(scene);

            // Show the stage with the pie charts
            stage.show();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    private PieChart createPieChart(String title, Map<String, Integer> statistics) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : statistics.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(title);
        pieChart.setMinWidth(400);
        pieChart.setMaxHeight(300);
        return pieChart;
    }

    private String getColorHexString(int index) {
        // Here you can define your own color scheme
        String[] colors = {"#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b", "#e377c2", "#7f7f7f", "#bcbd22", "#17becf"};
        return colors[index % colors.length];
    }
    private void customizePieChartTooltip(PieChart pieChart) {
        for (PieChart.Data data : pieChart.getData()) {
            String countText = String.valueOf((int) data.getPieValue());
            Tooltip tooltip = new Tooltip(countText);
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    public void NewCompte(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutCompte.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajout Compte");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generatePDF(ActionEvent actionEvent) {
        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.generatePDF(actionEvent, compteClientTable, "CompteClient Table");
    }

    private Image generateQRCodeImage(int qrCodeData) {
        int qrCodeSize = 100; // Set the size of the QR code image (reduced size)

        // Title to be displayed when scanning the QR code
        String title = "L'id de ce compte est : ";

        // Combine title and qrCodeData
        String qrCodeText = title + qrCodeData;

        // Create QR code writer
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        // Convert BitMatrix to BufferedImage
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        WritableImage writableImage = new WritableImage(width, height);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writableImage.getPixelWriter().setArgb(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return writableImage;
    }

}
