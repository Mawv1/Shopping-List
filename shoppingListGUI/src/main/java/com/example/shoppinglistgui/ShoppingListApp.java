package com.example.shoppinglistgui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ShoppingListApp extends Application {

    final int FONT_SIZE = 18;

    private TextField productNameField;
    private TextField quantityField;
    private TextField priceField;
    private ComboBox<String> unitField;
    private CheckBox isIntegerCheckBox;
    private TableView<Product> productTable;
    private Label totalPriceLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Shopping List");

        Label productNameLabel = new Label("Product Name:");
        productNameLabel.setFont(new Font(FONT_SIZE));

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.setFont(new Font(FONT_SIZE));

        Label unitLabel = new Label("Unit:");
        unitLabel.setFont(new Font(FONT_SIZE));

        Label priceLabel = new Label("Price:");
        priceLabel.setFont(new Font(FONT_SIZE));

        productNameField = new TextField();
        productNameField.setPromptText("Product Name");
        productNameField.setMaxWidth(200);

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        quantityField.setMaxWidth(200);

        unitField = new ComboBox<>();
        unitField.getItems().addAll("Kg", "L", "g", "ml", "pieces");
        unitField.setPromptText("Unit");

        priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setMaxWidth(200);

        isIntegerCheckBox = new CheckBox("Is Integer");
        isIntegerCheckBox.setFont(new Font(FONT_SIZE));

        Button addButton = new Button("Add Product");
        addButton.setPrefWidth(175);
        addButton.setFont(new Font(FONT_SIZE));
        addButton.setStyle("-fx-background-color: #E8E8E8; -fx-border-color: #A9A9A9;");

        Button deleteButton = new Button("Delete Product");
        deleteButton.setStyle("-fx-background-color: #E8E8E8; -fx-border-color: #A9A9A9;");
        deleteButton.setPrefWidth(175);
        deleteButton.setFont(new Font(FONT_SIZE));

        Button clearButton = new Button("Clear Input");
        clearButton.setStyle("-fx-background-color: #E8E8E8; -fx-border-color: #A9A9A9;");
        clearButton.setPrefWidth(175);
        clearButton.setFont(new Font(FONT_SIZE));

        Button updateButton = new Button("Update Product");
        updateButton.setStyle("-fx-background-color: #E8E8E8; -fx-border-color: #A9A9A9;");
        updateButton.setPrefWidth(175);
        updateButton.setFont(new Font(FONT_SIZE));

        addButton.setOnAction(e -> addProduct());
        deleteButton.setOnAction(e -> deleteProduct());
        clearButton.setOnAction(e -> {
            productNameField.clear();
            quantityField.clear();
            priceField.clear();
            unitField.setValue(null);
            isIntegerCheckBox.setSelected(false);
        });
        updateButton.setOnAction(e -> updateProduct());

        productTable = new TableView<>();

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setResizable(false);
        nameColumn.setPrefWidth(190);

        TableColumn<Product, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setResizable(false);
        quantityColumn.setPrefWidth(80);

        TableColumn<Product, String> unitColumn = new TableColumn<>("Unit");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitColumn.setResizable(false);
        unitColumn.setPrefWidth(50);

        TableColumn<Product, Boolean> isIntegerColumn = new TableColumn<>("Is Int");
        isIntegerColumn.setCellValueFactory(new PropertyValueFactory<>("isInteger"));
        isIntegerColumn.setResizable(false);
        isIntegerColumn.setPrefWidth(50);

        TableColumn<Product, Number> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setResizable(false);
        priceColumn.setPrefWidth(80);

        productTable.getColumns().setAll(nameColumn, quantityColumn, unitColumn, priceColumn, isIntegerColumn);
        productTable.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-background-color: #ffffff; -fx-border-style: solid;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.add(productNameLabel, 0, 0);
        inputGrid.add(productNameField, 1, 0);
        inputGrid.add(quantityLabel, 0, 1);
        inputGrid.add(quantityField, 1, 1);
        inputGrid.add(priceLabel, 0, 2);
        inputGrid.add(priceField, 1, 2);
        inputGrid.add(unitLabel, 0, 3);
        inputGrid.add(unitField, 1, 3);
        inputGrid.add(isIntegerCheckBox, 1, 4);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setAlignment(Pos.CENTER);
        buttonGrid.add(addButton, 0, 0);
        buttonGrid.add(deleteButton, 1, 0);
        buttonGrid.add(clearButton, 0, 1);
        buttonGrid.add(updateButton, 1, 1);

        totalPriceLabel = new Label("Estimated price for shopping: 0.00");
        totalPriceLabel.setFont(new Font(FONT_SIZE));
        totalPriceLabel.setStyle("-fx-background-color: #FFF68F;");

        VBox vbox = new VBox(inputGrid, buttonGrid, productTable, new Separator(), totalPriceLabel);
        vbox.setSpacing(10);
        vbox.setBackground(new Background(new BackgroundFill(Color.web("#FFF68F"), CornerRadii.EMPTY, Insets.EMPTY)));
        vbox.setPadding(new Insets(20, 20, 20, 20));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 500, 600);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        populateProductTable();
    }

    private void clearInput() {
        productNameField.clear();
        quantityField.clear();
        unitField.setValue(null);
        isIntegerCheckBox.setSelected(false);
        priceField.clear();
    }

    private void updateProduct() {
        Product product = productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a product to update");
            alert.showAndWait();
            return;
        }

        if (checkIfAnyFieldIsFilled()) return;

        String name = productNameField.getText().isEmpty() ? product.getName() : productNameField.getText();
        try {
            Double tmp = null;
            if (!quantityField.getText().isEmpty()) {
                tmp = Double.parseDouble(quantityField.getText());
            }
            if (tmp != null && tmp <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Quantity must be a positive number");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Quantity must be a number");
            alert.showAndWait();
            return;
        }
        double quantity = quantityField.getText().isEmpty() ? product.getQuantity() : Double.parseDouble(quantityField.getText());
        String unit = unitField.getValue() == null ? product.getUnit() : unitField.getValue();
        double price = priceField.getText().isEmpty() ? product.getPrice() : Double.parseDouble(priceField.getText());
        double totalPrice = product.getTotalPrice();
        boolean isInteger = isIntegerCheckBox.isSelected();
        System.out.println(product);
        System.out.println(name + " " + quantity + " " + unit);

        String jsonInputString = String.format("{\"name\":\"%s\", \"price\":\"%s\", \"quantity\": %s, \"totalPrice\":\"%s\", \"unit\":\"%s\", \"isInteger\":\"%b\"}", name, price, quantity, totalPrice, unit, isInteger);
        System.out.println(jsonInputString);
        try {
            URL url = new URL("http://localhost:8080/api/v1/product/" + product.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            conn.getOutputStream().write(jsonInputString.getBytes("utf-8"));
            // Print all the response headers
            for (Map.Entry<String, List<String>> header : conn.getHeaderFields().entrySet()) {
                System.out.println(header.getKey() + "=" + header.getValue());
            }
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                System.out.println("Response Content: " + content.toString());
                in.close();
                populateProductTable();
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateProductTable();

        clearInput();
    }

    private boolean checkIfAnyFieldIsFilled() {
        if (productNameField.getText().isEmpty() && quantityField.getText().isEmpty() && unitField.getValue() == null && !isIntegerCheckBox.isSelected() && priceField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please fill at least one field");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private void deleteProduct() {
        Product product = productTable.getSelectionModel().getSelectedItem();
        if (product == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please select a product to delete");
            alert.showAndWait();
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/api/v1/product/" + product.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            if (conn.getResponseCode() == 200) {
                System.out.println("Product deleted");
                populateProductTable();
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        if (checkIfInputFieldsAreFilled()) return;

        String name = productNameField.getText();
        try {
            Double tmp = Double.parseDouble(quantityField.getText());
            if (tmp <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Quantity must be a positive number");
                alert.showAndWait();
                return;
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Quantity must be a number");
            alert.showAndWait();
            return;
        }
        double quantity = Double.parseDouble(quantityField.getText());
        String unit = unitField.getValue();
        boolean isInteger = isIntegerCheckBox.isSelected();
        double price = Double.parseDouble(priceField.getText());

        String jsonInputString = String.format("{\"isInteger\": %b, \"name\":\"%s\", \"quantity\": %s, \"unit\":\"%s\", \"price\": %s}",
                isInteger, name, quantity, unit, price);

        try {
            URL url = new URL("http://localhost:8080/api/v1/product");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            conn.getOutputStream().write(jsonInputString.getBytes("utf-8"));

            if (conn.getResponseCode() == 201) {
                populateProductTable();
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateProductTable();

        clearInput();
    }

    private boolean AreInputFieldsFilled() {
        return productNameField.getText().isEmpty() || quantityField.getText().isEmpty() || unitField.getValue() == null;
    }

    private boolean checkIfInputFieldsAreFilled() {
        if (AreInputFieldsFilled()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private void populateProductTable() {
        productTable.getItems().clear();

        try {
            URL url = new URL("http://localhost:8080/api/v1/product");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                String response = scanner.nextLine();

                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Product product = new Product(
                            jsonObject.getLong("id"),
                            jsonObject.getString("name"),
                            jsonObject.getDouble("price"),
                            jsonObject.getDouble("quantity"),
                            jsonObject.getString("unit"),
                            jsonObject.getBoolean("isInteger")
                    );
                    productTable.getItems().add(product);
                }
            }

            scanner.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double sumPriceWithoutQuantity = productTable.getItems().stream().mapToDouble(Product::getPrice).sum();
        totalPriceLabel.setText(String.format("Estimated price for shopping: %.2f", sumPriceWithoutQuantity));
    }
}
