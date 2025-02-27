package com.example.restaruantnew;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

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

        // Table Status Screen (for waiters)
        VBox tableStatusPane = new VBox(10);
        tableStatusPane.setAlignment(Pos.CENTER);
        Button table1 = new Button("Table 1 (Open)");
        Button table2 = new Button("Table 2 (Occupied)");
        Button table3 = new Button("Table 3 (Dirty)");
        Button orderButton = new Button("Take Order");
        Button logoutButton = new Button("Logout");

        tableStatusPane.getChildren().addAll(new Label("Table Status"), table1, table2, table3, orderButton, logoutButton);
        Scene tableStatusScene = new Scene(tableStatusPane, 400, 300);

        // Order Management Screen
        VBox orderPane = new VBox(10);
        orderPane.setAlignment(Pos.CENTER);
        Label orderLabel = new Label("Select an Item");
        ComboBox<String> menuItems = new ComboBox<>();
        menuItems.getItems().addAll("Appetizer", "Entree", "Side", "Dessert");
        Button confirmOrder = new Button("Confirm Order");
        Button backButton = new Button("Back");

        orderPane.getChildren().addAll(orderLabel, menuItems, confirmOrder, backButton);
        Scene orderScene = new Scene(orderPane, 400, 300);

        // Kitchen Queue Screen
        VBox kitchenPane = new VBox(10);
        kitchenPane.setAlignment(Pos.CENTER);
        Label kitchenLabel = new Label("Kitchen Queue");
        ListView<String> orderQueue = new ListView<>();
        Button markReady = new Button("Mark as Ready");
        Button logoutKitchen = new Button("Logout");


        kitchenPane.getChildren().addAll(kitchenLabel, orderQueue, markReady, logoutKitchen);
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

        // Event Handlers
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (username.equals("waiter") && password.equals("1234")) {
                primaryStage.setScene(tableStatusScene);
            } else if (username.equals("manager") && password.equals("admin")) {
                primaryStage.setScene(managerScene);
            } else if (username.equals("cook") && password.equals("kitchen")) {
                primaryStage.setScene(kitchenScene);
            } else {
                messageLabel.setText("Invalid credentials");
            }
        });

        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutManager.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutKitchen.setOnAction(e -> primaryStage.setScene(loginScene));
        backButton.setOnAction(e -> primaryStage.setScene(tableStatusScene));

        orderButton.setOnAction(e -> primaryStage.setScene(orderScene));
        confirmOrder.setOnAction(e -> {
            if (!menuItems.getSelectionModel().isEmpty()) {
                orderQueue.getItems().add(menuItems.getSelectionModel().getSelectedItem());
                primaryStage.setScene(tableStatusScene);
            }
        });
        markReady.setOnAction(e -> {
            if (!orderQueue.getItems().isEmpty()) {
                orderQueue.getItems().remove(0);
            }
        });

        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
