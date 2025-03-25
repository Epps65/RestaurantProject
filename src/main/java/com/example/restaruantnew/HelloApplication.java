package com.example.restaruantnew;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HelloApplication extends Application {

    private ObservableList<String> cartItems = FXCollections.observableArrayList();
    private ObservableList<String> orderQueue = FXCollections.observableArrayList();
    private ObservableList<String> tableStatus = FXCollections.observableArrayList(
            "Table 1 - Clean", "Table 2 - Occupied", "Table 3 - Dirty", "Table 4 - Clean", "Table 5 - Occupied"
    );

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurant Management System");

        // Login Screen
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        GridPane loginPane = new GridPane();
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        loginPane.add(userLabel, 0, 0);
        loginPane.add(userField, 1, 0);
        loginPane.add(passLabel, 0, 1);
        loginPane.add(passField, 1, 1);
        loginPane.add(loginButton, 1, 2);
        loginPane.add(messageLabel, 1, 3);

        Scene loginScene = new Scene(loginPane, 400, 300);

        // Kitchen Queue Screen
        VBox kitchenPane = new VBox(10);
        kitchenPane.setAlignment(Pos.CENTER);
        Label kitchenLabel = new Label("Kitchen Queue");
        ListView<String> kitchenQueue = new ListView<>(orderQueue);
        Button markReady = new Button("Mark as Ready");
        Button logoutKitchen = new Button("Logout");

        kitchenPane.getChildren().addAll(kitchenLabel, kitchenQueue, markReady, logoutKitchen);
        Scene kitchenScene = new Scene(kitchenPane, 400, 300);

        // Manager Dashboard
        VBox managerPane = new VBox(10);
        managerPane.setAlignment(Pos.CENTER);
        Label managerLabel = new Label("Manager Dashboard");
        Button viewReports = new Button("View Reports");
        Button manageStaff = new Button("Manage Staff");
        Button logoutManager = new Button("Logout");

        managerPane.getChildren().addAll(managerLabel, viewReports, manageStaff, logoutManager);
        Scene managerScene = new Scene(managerPane, 400, 300);

        // Busboy Table Status Screen
        VBox busboyPane = new VBox(10);
        busboyPane.setAlignment(Pos.CENTER);
        Label busboyLabel = new Label("Table Status");
        ListView<String> tableList = new ListView<>(tableStatus);
        Button markClean = new Button("Mark as Clean");
        Button logoutBusboy = new Button("Logout");

        busboyPane.getChildren().addAll(busboyLabel, tableList, markClean, logoutBusboy);
        Scene busboyScene = new Scene(busboyPane, 400, 300);

        // Event Handlers
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (username.equals("waiter") && password.equals("1234")) {
                primaryStage.setScene(kitchenScene);
            } else if (username.equals("manager") && password.equals("admin")) {
                primaryStage.setScene(managerScene);
            } else if (username.equals("busboy") && password.equals("cleaner")) {
                primaryStage.setScene(busboyScene);
            } else if (username.equals("cook") && password.equals("kitchen")) {
                primaryStage.setScene(kitchenScene);
            } else {
                messageLabel.setText("Invalid credentials");
            }
        });

        markClean.setOnAction(e -> {
            String selectedTable = tableList.getSelectionModel().getSelectedItem();
            if (selectedTable != null && selectedTable.contains("Dirty")) {
                tableStatus.set(tableStatus.indexOf(selectedTable), selectedTable.replace("Dirty", "Clean"));
            }
        });

        markReady.setOnAction(e -> {
            if (!orderQueue.isEmpty()) {
                orderQueue.remove(0);
            }
        });

        logoutBusboy.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutKitchen.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutManager.setOnAction(e -> primaryStage.setScene(loginScene));

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}