package application;

import javafx.application.Application;
import javafx.geometry.Insets;
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


    public class TableGrid extends GridPane {
        public static final int ROWS = 6;
        public static final int COLS = 6;
        public Button[][] tables = new Button[ROWS][COLS];

        public TableGrid() {
            this.setHgap(10);
            this.setVgap(10);
            this.setPadding(new Insets(20));

            // Add column headers (A-F)
            for (int col = 0; col < COLS; col++) {
                Label header = new Label(Character.toString((char)('A' + col)));
                header.setStyle("-fx-font-weight: bold;");
                this.add(header, col + 1, 0);
            }

            // Add row numbers (1-6) and table buttons
            for (int row = 0; row < ROWS; row++) {
                this.add(new Label(Integer.toString(row + 1)), 0, row + 1);

                for (int col = 0; col < COLS; col++) {
                    Button table = new Button();
                    table.setMinSize(60, 60);
                    table.setId(row + "-" + col); // Store position in ID
                    tables[row][col] = table;
                    this.add(table, col + 1, row + 1);
                }
            }

            // Initialize with some sample data
            setTableStatus(0, 0, "Clean");
            setTableStatus(0, 1, "Occupied");
            setTableStatus(0, 2, "Dirty");
        }

        public void setTableStatus(int row, int col, String status) {
            Button table = tables[row][col];
            table.setUserData(status);

            switch (status) {
                case "Clean":
                    table.setStyle("-fx-background-color: #34c759; -fx-text-fill: white;");
                    break;
                case "Occupied":
                    table.setStyle("-fx-background-color: #FFFF00; -fx-text-fill: white;");
                    break;
                case "Dirty":
                    table.setStyle("-fx-background-color: #ff3b30; -fx-text-fill: white;");
                    break;
            }

            table.setText("Table " + (char)('A' + col) + (row + 1));
        }

        public String getTableStatus(int row, int col) {
            return (String) tables[row][col].getUserData();
        }

        public Button getTableButton(int row, int col) {
            return tables[row][col];
        }
    }

    @Override
    public void start(Stage primaryStage) {


        primaryStage.setTitle("Restaurant Management System");


        // Login Screen
        Label titleLabel = new Label("J's Corner Restaurant");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label userLabel = new Label("Enter ID:");
        TextField userField = new TextField();
        Label passLabel = new Label("Enter Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("LOGIN");
        loginButton.setStyle("-fx-font-weight: bold;");
        Label messageLabel = new Label();

        VBox loginPane = new VBox(20);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(40));
        loginPane.getChildren().addAll(
                titleLabel,
                userLabel,
                userField,
                passLabel,
                passField,
                loginButton,
                messageLabel
        );

        Scene loginScene = new Scene(loginPane, 400, 500);

        // Kitchen Queue Screen
        VBox kitchenPane = new VBox(10);
        kitchenPane.setAlignment(Pos.CENTER);
        Label kitchenLabel = new Label("Kitchen Queue");
        ListView<String> kitchenQueue = new ListView<>(orderQueue);
        Button markReady = new Button("Mark as Ready");
        Button cookPunchButton = new Button("Punch");
        Button cookLogoutButton = new Button("Log Out");
        Label cookStatusLabel = new Label("COOK");

// Cook Screen Layout
        HBox cookTopBar = new HBox(10, cookStatusLabel, cookPunchButton, cookLogoutButton);
        cookTopBar.setAlignment(Pos.CENTER_RIGHT);
        cookTopBar.setPadding(new Insets(10));


        kitchenPane.getChildren().addAll(cookTopBar, kitchenLabel, kitchenQueue, markReady);
        Scene kitchenScene = new Scene(kitchenPane, 400, 300);


        cookPunchButton.setOnAction(e -> {
            if ("Punch".equals(cookPunchButton.getText())) {
                cookPunchButton.setText("Clock Out");
                cookStatusLabel.setText("COOK - Clocked In");
            } else {
                cookPunchButton.setText("Punch");
                cookStatusLabel.setText("COOK - Clocked Out");
            }
        });



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

        // Busboy Screen
        TableGrid tableGrid1 = new TableGrid();
        Button busboyPunchButton = new Button("Punch");
        Button busboyLogoutButton = new Button("Log Out");
        Label busboyStatusLabel = new Label("BUSBOY");

        HBox busboyTopBar = new HBox(10, busboyStatusLabel, busboyPunchButton, busboyLogoutButton);
        busboyTopBar.setAlignment(Pos.CENTER_RIGHT);
        busboyTopBar.setPadding(new Insets(10));

        VBox busboyPane = new VBox(10, busboyTopBar, tableGrid1);
        busboyPane.setPadding(new Insets(20));
        busboyPane.setId("busboyPane");

        Scene busboyScene = new Scene(busboyPane, 800, 600);

// Set up table click handlers for busboy
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Button table = tableGrid1.getTableButton(row, col);
                int finalRow = row;
                int finalCol = col;
                table.setOnAction(e -> {
                    if ("Dirty".equals(table.getUserData())) {
                        tableGrid1.setTableStatus(finalRow, finalCol, "Clean");
                    }
                });
            }
        }

// Busboy punch functionality
        busboyPunchButton.setOnAction(e -> {
            if ("Punch".equals(busboyPunchButton.getText())) {
                busboyPunchButton.setText("Clock Out");
                busboyStatusLabel.setText("BUSBOY - Clocked In");
            } else {
                busboyPunchButton.setText("Punch");
                busboyStatusLabel.setText("BUSBOY - Clocked Out");
            }
        });

        busboyLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

// Waiter Screen
        TableGrid tableGrid = new TableGrid();
        Button punchButton = new Button("Punch");
        Button logoutButton = new Button("Log Out");
        Label statusLabel = new Label("WAITER");

        HBox topBar = new HBox(10, statusLabel, punchButton, logoutButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10));

        VBox waiterPane = new VBox(10, topBar, tableGrid);
        waiterPane.setPadding(new Insets(20));
        waiterPane.setId("waiterPane");

        Scene waiterScene = new Scene(waiterPane, 800, 600);


        // Event Handlers
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            if (username.equals("waiter") && password.equals("1234")) {
                primaryStage.setScene(waiterScene);
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


        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Button table = tableGrid.getTableButton(row, col);
                int finalRow = row;
                int finalCol = col;
                table.setOnAction(e -> {
                    if ("Clean".equals(table.getUserData())) {
                        tableGrid.setTableStatus(finalRow, finalCol, "Occupied");
                        primaryStage.setScene(orderScene);
                    }
                });
            }
        }

        backButton.setOnAction(e -> primaryStage.setScene(waiterScene));
        punchButton.setOnAction(e -> {
            // Toggle punch status
            if ("Punch".equals(punchButton.getText())) {
                punchButton.setText("Clock Out");
                statusLabel.setText("WAITER - Clocked In");
            } else {
                punchButton.setText("Punch");
                statusLabel.setText("WAITER - Clocked Out");
            }
        });
        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        markReady.setOnAction(e -> {
            if (!orderQueue.isEmpty()) {
                orderQueue.remove(0);
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(waiterScene));
        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        cookLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        logoutManager.setOnAction(e -> primaryStage.setScene(loginScene));
        confirmOrder.setOnAction(e -> {
            if (!itemComboBox.getSelectionModel().isEmpty()) {
                kitchenQueue.getItems().add(itemComboBox.getSelectionModel().getSelectedItem());
                primaryStage.setScene(waiterScene);
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
        waiterScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());

        titleLabel.setId("titleLabel");
        loginPane.setId("loginPane");
        loginButton.setId("loginButton");
        punchButton.setId("punchButton");
        logoutButton.setId("logoutButton");
        statusLabel.setId("statusLabel");


        primaryStage.setScene(loginScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}