package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Main extends Application {

    public ObservableList<String> cartItems = FXCollections.observableArrayList();
    public ObservableList<String> orderQueue = FXCollections.observableArrayList();
    public ObservableList<String> tableStatus = FXCollections.observableArrayList(
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



// Order Management Screen with Categories
        VBox orderPane = new VBox(10);
        orderPane.setAlignment(Pos.CENTER);
        Label orderLabel = new Label("Select a Menu Category");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(
                "Appetizers",
                "Salads",
                "Entrees",
                "Sides",
                "Sandwiches",
                "Burgers",
                "Beverages"
        );

// This would be expanded to show specific items when a category is selected
        ComboBox<String> itemComboBox = new ComboBox<>();
        itemComboBox.setVisible(false);

        Button confirmOrder = new Button("Confirm Order");
        Button backButton = new Button("Back");

        orderPane.getChildren().addAll(orderLabel, categoryComboBox, itemComboBox, confirmOrder, backButton);
        Scene orderScene = new Scene(orderPane, 400, 300);

// Add event handler for category selection
        categoryComboBox.setOnAction(e -> {
            itemComboBox.getItems().clear();
            String selectedCategory = categoryComboBox.getValue();

            switch(selectedCategory) {
                case "Appetizers":
                    itemComboBox.getItems().addAll(
                            "Chicken Nachos - $8.50",
                            "Pork Nachos - $8.50",
                            "Pork/Chicken Sliders (3) - $5.00",
                            "Catfish Bites - $6.50",
                            "Fried Veggies - $6.50"
                    );
                    break;
                case "Salads":
                    itemComboBox.getItems().addAll(
                            "House Salad - $7.50",
                            "Wedge Salad - $7.50",
                            "Caesar Salad - $7.50",
                            "Sweet Potato Chicken Salad - $11.50"
                    );
                    break;
                case "Entrees":
                    itemComboBox.getItems().addAll(
                            "Shrimp & Grits - $13.50",
                            "Sweet Tea Fried Chicken - $11.50",
                            "Caribbean Chicken - $11.50",
                            "Grilled Pork Chops - $11.00",
                            "New York Strip Steak - $17.00",
                            "Seared Tuna - $15.00",
                            "Captain Crunch Chicken Tenders - $11.50",
                            "Shock Top Grouper Fingers - $11.50",
                            "Mac & Cheese Bar - $8.50"
                    );
                    break;
                case "Sides":
                    itemComboBox.getItems().addAll(
                            "Curly Fries - $2.50",
                            "Wing Chips - $2.50",
                            "Sweet Potato Fries - $2.50",
                            "Creamy Cabbage Slaw - $2.50",
                            "Adluh Cheese Grits - $2.50",
                            "Mashed Potatoes - $2.50",
                            "Mac & Cheese - $2.50",
                            "Seasonal Vegetables - $2.50",
                            "Baked Beans - $2.50"
                    );
                    break;
                case "Sandwiches":
                    itemComboBox.getItems().addAll(
                            "Grilled Cheese - $5.50",
                            "Chicken BLT&A - $10.00",
                            "Philly - $13.50",
                            "Club - $10.00",
                            "Meatball Sub - $10.00"
                    );
                    break;
                case "Burgers":
                    itemComboBox.getItems().addAll(
                            "Bacon Cheeseburger - $11.00",
                            "Carolina Burger - $11.00",
                            "Portobello Burger (V) - $8.50",
                            "Vegan Boca Burger (V) - $10.50"
                    );
                    break;
                case "Beverages":
                    itemComboBox.getItems().addAll(
                            "Sweet/Unsweetened Tea - $2.00",
                            "Coke/Diet Coke - $2.00",
                            "Sprite - $2.00",
                            "Bottled Water - $2.00",
                            "Lemonade - $2.00",
                            "Orange Juice - $2.00"
                    );
                    break;
            }

            itemComboBox.setVisible(true);
        });

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

        // Table Status Screen (for waiters)
        VBox tableStatusPane = new VBox(10);
        tableStatusPane.setAlignment(Pos.CENTER);
        ListView<String> TableList = new ListView<>(tableStatus);
        Button orderButton = new Button("Take Order");
        Button markOccupied = new Button("Mark as Occupied");
        Button logoutButton = new Button("Logout");

        tableStatusPane.getChildren().addAll(new Label("Table Status"), TableList, orderButton, markOccupied, logoutButton);
        Scene tableStatusScene = new Scene(tableStatusPane, 400, 300);

        // Event Handlers
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (username.equals("waiter") && password.equals("1234")) {
                primaryStage.setScene(tableStatusScene);
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
        markOccupied.setOnAction(e -> {
            String selectedTable = TableList.getSelectionModel().getSelectedItem();
            if (selectedTable != null && selectedTable.contains("Clean")) {
                tableStatus.set(tableStatus.indexOf(selectedTable), selectedTable.replace("Clean", "Occupied"));
            }
        });

        markReady.setOnAction(e -> {
            if (!orderQueue.isEmpty()) {
                orderQueue.remove(0);
            }
        });

        logoutBusboy.setOnAction(e -> primaryStage.setScene(loginScene));
        backButton.setOnAction(e -> primaryStage.setScene(tableStatusScene));
        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutKitchen.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutManager.setOnAction(e -> primaryStage.setScene(loginScene));
        orderButton.setOnAction(e -> primaryStage.setScene(orderScene));
        confirmOrder.setOnAction(e -> {
            if (!itemComboBox.getSelectionModel().isEmpty()) {
                kitchenQueue.getItems().add(itemComboBox.getSelectionModel().getSelectedItem());
                primaryStage.setScene(tableStatusScene);
            }
        });
        markReady.setOnAction(e -> {
            if (!kitchenQueue.getItems().isEmpty()) {
                kitchenQueue.getItems().remove(0);
            }
        });

        loginScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());
        kitchenScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());
        orderScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());
        managerScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());
        busboyScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());
        tableStatusScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());


        primaryStage.setScene(loginScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}