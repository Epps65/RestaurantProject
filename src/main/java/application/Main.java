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

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    public static class Order { // Made static for easier instantiation if needed elsewhere
        public final String tableId;
        public final List<String> items;

        public Order(String tableId, List<String> items) {
            this.tableId = tableId;
            // Create a new list to avoid issues if the original cartItems is modified later
            this.items = new ArrayList<>(items);
        }

        public String getTableId() {
            return tableId;
        }

        public List<String> getItems() {
            return items;
        }

        @Override
        public String toString() {
            // This controls how the Order appears in the main kitchen queue ListView
            return tableId + " (" + items.size() + " item" + (items.size() != 1 ? "s" : "") + ")";
        }
    }

    public ObservableList<String> cartItems = FXCollections.observableArrayList();
    public ObservableList<Order> orderQueue = FXCollections.observableArrayList();
    public Label tableLabel;


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

            // --- Initialize ALL tables with a random status ---
            String[] statuses = {"Clean", "Occupied", "Dirty"};
            java.util.Random random = new java.util.Random();

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    // Assign a random status from the array
                    String randomStatus = statuses[random.nextInt(statuses.length)];
                    setTableStatus(row, col, randomStatus);
                }
            }
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

        //Kitchen Screen

        VBox kitchenPane = new VBox(10);
        kitchenPane.setAlignment(Pos.CENTER);
        kitchenPane.setPadding(new Insets(15));

        Label kitchenTitleLabel = new Label("Incoming Orders");
        kitchenTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Main queue ListView
        ListView<Order> kitchenQueueListView = new ListView<>(orderQueue);
        kitchenQueueListView.setPrefHeight(150);
        Label selectedOrderLabel = new Label("Selected Order Items:");
        selectedOrderLabel.setStyle("-fx-font-weight: bold;");

        // ListView to display items of the selected order
        ListView<String> orderItemsDisplay = new ListView<>();
        orderItemsDisplay.setPrefHeight(150); // Adjust height

        Button markReadyButton = new Button("Mark as Ready");
        Button cookPunchButton = new Button("Punch");
        Button cookLogoutButton = new Button("Log Out");
        Label cookStatusLabel = new Label("COOK");

        // Cook Screen Layout
        HBox cookTopBar = new HBox(10, cookStatusLabel, cookPunchButton, cookLogoutButton);
        cookTopBar.setAlignment(Pos.CENTER_RIGHT);

        // - update item display on selection
        kitchenQueueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            orderItemsDisplay.getItems().clear(); // Clear previous items
            if (newSelection != null) {
                // Populate the items display with items from the selected order
                orderItemsDisplay.getItems().addAll(newSelection.getItems());
            }
        });

        // Add components to the kitchenPane
        kitchenPane.getChildren().addAll(
                cookTopBar,
                kitchenTitleLabel,
                kitchenQueueListView, // Main queue
                new Separator(javafx.geometry.Orientation.HORIZONTAL),
                selectedOrderLabel,
                orderItemsDisplay, // Items of selected order
                markReadyButton
        );

        Scene kitchenScene = new Scene(kitchenPane, 450, 550); // Adjust size if needed


        cookPunchButton.setOnAction(e -> {
            if ("Punch".equals(cookPunchButton.getText())) {
                cookPunchButton.setText("Clock Out");
                cookStatusLabel.setText("COOK - Clocked In");
            } else {
                cookPunchButton.setText("Punch");
                cookStatusLabel.setText("COOK - Clocked Out");
            }
        });





        // Manager Dashboard
        TableGrid managerTableGrid = new TableGrid(); // Add the table grid
        Button managerPunchButton = new Button("Punch");
        Button managerLogoutButton = new Button("Log Out");
        Button managerMenuButton = new Button("Manager Menu"); // Add Manager Menu button
        Label managerStatusLabel = new Label("MANAGER");

        // Hide middle TableGrid
        for(int i = 0; i<4;i++) {
            managerTableGrid.getTableButton(i, 2).setVisible(false);
            managerTableGrid.getTableButton(i, 3).setVisible(false);
        }

        // Top bar for status, punch, logout
        HBox managerTopBar = new HBox(10, managerStatusLabel, managerPunchButton, managerLogoutButton);
        managerTopBar.setAlignment(Pos.CENTER_RIGHT);
        managerTopBar.setPadding(new Insets(10));

        // Bottom bar for Manager Menu button (adjust layout as needed)
        HBox managerBottomBar = new HBox(managerMenuButton);
        managerBottomBar.setAlignment(Pos.CENTER);
        managerBottomBar.setPadding(new Insets(10, 0, 0, 0));

        // Main pane for the manager screen
        managerTableGrid.setAlignment(Pos.CENTER);
        VBox managerPane = new VBox(10); // Renamed from original managerPane to avoid conflict if needed elsewhere
        managerPane.getChildren().addAll(managerTopBar, managerTableGrid, managerBottomBar); // Add top bar, grid, and bottom bar
        managerPane.setPadding(new Insets(20));


        Scene managerScene = new Scene(managerPane, 800, 600);


        // Add event handlers for new manager buttons
        managerPunchButton.setOnAction(e -> {
            if ("Punch".equals(managerPunchButton.getText())) {
                managerPunchButton.setText("Clock Out");
                managerStatusLabel.setText("MANAGER - Clocked In");
            } else {
                managerPunchButton.setText("Punch");
                managerStatusLabel.setText("MANAGER - Clocked Out");
            }
        });

        managerLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));

        // --- Manager Menu Screen Definition ---
        Label managerMenuTitle_scene = new Label("Manager Menu");
        managerMenuTitle_scene.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;"); // Optional styling

        Button salesAnalyticsButton_scene = new Button("Sales Analytics");
        Button employeeAnalyticsButton_scene = new Button("Employee Analytics");
        Button performanceAnalyticsButton_scene = new Button("Performance Analytics"); // Added Performance Analytics
        Button employeeActionsButton_scene = new Button("Employee Actions");
        Button managerMenuBackButton_scene = new Button("Back");

        // Set preferred width for buttons for consistent look (optional)
        double buttonWidth_scene = 200; // Adjusted width slightly
        salesAnalyticsButton_scene.setPrefWidth(buttonWidth_scene);
        employeeAnalyticsButton_scene.setPrefWidth(buttonWidth_scene);
        performanceAnalyticsButton_scene.setPrefWidth(buttonWidth_scene);
        employeeActionsButton_scene.setPrefWidth(buttonWidth_scene);
        managerMenuBackButton_scene.setPrefWidth(buttonWidth_scene / 2);

        VBox managerMenuPane_scene = new VBox(15); // Spacing between elements
        managerMenuPane_scene.setAlignment(Pos.CENTER);
        managerMenuPane_scene.setPadding(new Insets(30));
        managerMenuPane_scene.getChildren().addAll(
                managerMenuTitle_scene,
                salesAnalyticsButton_scene,
                employeeAnalyticsButton_scene,
                performanceAnalyticsButton_scene,
                employeeActionsButton_scene,
                managerMenuBackButton_scene
        );

        Scene managerMenuScene = new Scene(managerMenuPane_scene, 400, 400);
        managerMenuScene.getStylesheets().add(Main.class.getResource("Styles.css").toExternalForm());

        //  Event Handlers for Manager Menu Buttons

        managerMenuBackButton_scene.setOnAction(e -> primaryStage.setScene(managerScene));

        // TODO: Replace these with navigation to the actual analytics/actions scenes
        salesAnalyticsButton_scene.setOnAction(e -> {
            System.out.println("Navigate to Sales Analytics");
            // Example: primaryStage.setScene(createSalesAnalyticsScene(primaryStage, managerMenuScene));
        });

        employeeAnalyticsButton_scene.setOnAction(e -> {
            System.out.println("Navigate to Employee Analytics");
            // Example: primaryStage.setScene(createEmployeeAnalyticsScene(primaryStage, managerMenuScene));
        });

        performanceAnalyticsButton_scene.setOnAction(e -> {
            System.out.println("Navigate to Performance Analytics");
            // Example: primaryStage.setScene(createPerformanceAnalyticsScene(primaryStage, managerMenuScene));
        });


        employeeActionsButton_scene.setOnAction(e -> {
            System.out.println("Navigate to Employee Actions");
            // Example: primaryStage.setScene(createEmployeeActionsScene(primaryStage, managerMenuScene));
        });





        // Placeholder for Manager Menu action
        managerMenuButton.setOnAction(e -> {
            primaryStage.setScene(managerMenuScene);
        });



        // Set up table click handlers for Manager
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                Button table = managerTableGrid.getTableButton(row, col);
                int finalRow = row;
                int finalCol = col;
                table.setOnAction(e -> {
                    if ("Dirty".equals(table.getUserData())) {
                        managerTableGrid.setTableStatus(finalRow, finalCol, "Clean");
                    }
                });
            }
        }


        // Busboy Screen
        TableGrid tableGrid1 = new TableGrid();
        Button busboyPunchButton = new Button("Punch");
        Button busboyLogoutButton = new Button("Log Out");
        Label busboyStatusLabel = new Label("BUSBOY");

        // Hide middle TableGrid
        for(int i = 0; i<4;i++) {
            tableGrid1.getTableButton(i, 2).setVisible(false);
            tableGrid1.getTableButton(i, 3).setVisible(false);
        }

        HBox busboyTopBar = new HBox(10, busboyStatusLabel, busboyPunchButton, busboyLogoutButton);
        busboyTopBar.setAlignment(Pos.CENTER_RIGHT);
        busboyTopBar.setPadding(new Insets(10));

        tableGrid1.setAlignment(Pos.CENTER);
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

        // Hide middle TableGrid
        for(int i = 0; i<4;i++) {
            tableGrid.getTableButton(i, 2).setVisible(false);
            tableGrid.getTableButton(i, 3).setVisible(false);
        }


        HBox topBar = new HBox(10, statusLabel, punchButton, logoutButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10));

        tableGrid.setAlignment(Pos.CENTER);
        VBox waiterPane = new VBox(10, topBar, tableGrid);
        waiterPane.setPadding(new Insets(20));
        waiterPane.setId("waiterPane");

        Scene waiterScene = new Scene(waiterPane, 800, 600);


        // --- Order Management Screen (Modified based on Slide 43) ---

        // Left Pane: Menu Categories and Items
        VBox menuSelectionPane = new VBox(10);
        menuSelectionPane.setAlignment(Pos.TOP_LEFT); // Align items to the top-left
        menuSelectionPane.setPadding(new Insets(15)); // Add padding
        Label orderLabel = new Label("Select a Menu Category");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setMinWidth(180); // Set minimum width for better layout
        categoryComboBox.getItems().addAll(
                "Appetizers",
                "Salads",
                "Entrees",
                "Sides",
                "Sandwiches",
                "Burgers",
                "Beverages"
        );

        // Item selection ComboBox (initially hidden)
        ComboBox<String> itemComboBox = new ComboBox<>();
        itemComboBox.setMinWidth(180); // Set minimum width
        itemComboBox.setVisible(false); // Initially hidden

        // Add item button (replaces direct confirm)
        Button addItemButton = new Button("Add Item");
        addItemButton.setMinWidth(180); // Match width

        menuSelectionPane.getChildren().addAll(orderLabel, categoryComboBox, itemComboBox, addItemButton);

        // Right Pane: Order Details (Check View)
        VBox orderDetailsPane = new VBox(10);
        orderDetailsPane.setAlignment(Pos.TOP_CENTER); // Align items to the top-center
        orderDetailsPane.setPadding(new Insets(15)); // Add padding
        orderDetailsPane.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1;"); // Optional border

        // --- Components based on Slide 43 ---
        Label checkLabel = new Label("Chk #1001"); // Placeholder
        checkLabel.setStyle("-fx-font-weight: bold;");
        tableLabel = new Label("Tbl: <Selected Table>");
        tableLabel.setStyle("-fx-font-weight: bold;");
        Label serverLabel = new Label("Server: <Name>"); // Placeholder
        serverLabel.setStyle("-fx-font-weight: bold;");

        // Use a ListView to display ordered items similar to the check
        ListView<String> orderItemsListView = new ListView<>(cartItems); // Use your existing cartItems list
        orderItemsListView.setPrefHeight(200); // Adjust height as needed

        Label totalLabel = new Label("Total: $0.00"); // Placeholder - Update this dynamically
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Buttons based on Slide 43
        Button payTabButton = new Button("PAY TAB");
        payTabButton.setMinWidth(100);
        Button sendOrderButton = new Button("SEND");
        sendOrderButton.setMinWidth(100);

        HBox orderActionButtons = new HBox(10, payTabButton, sendOrderButton);
        orderActionButtons.setAlignment(Pos.CENTER);

        // Back button
        Button backButton = new Button("Back to Tables"); // Renamed for clarity

        orderDetailsPane.getChildren().addAll(
                checkLabel,
                tableLabel,
                serverLabel,
                new Separator(javafx.geometry.Orientation.HORIZONTAL), // Separator line
                new Label("Current Order:"),
                orderItemsListView,
                new Separator(javafx.geometry.Orientation.HORIZONTAL), // Separator line
                totalLabel,
                orderActionButtons,
                backButton // Add back button here or elsewhere as needed
        );


        // Main Pane for Order Screen using HBox for side-by-side layout
        HBox orderPane = new HBox(10); // Use HBox for the two columns
        orderPane.setPadding(new Insets(10));
        orderPane.getChildren().addAll(menuSelectionPane, orderDetailsPane); // Add left and right panes

        Scene orderScene = new Scene(orderPane, 650, 450); // Adjust size as needed


        // Event Handlers

        // Category selection handler (similar to before)
        categoryComboBox.setOnAction(e -> {
            itemComboBox.getItems().clear();
            String selectedCategory = categoryComboBox.getValue();

            switch (selectedCategory) {
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
                // Add cases for other categories if needed
            }
            itemComboBox.setVisible(true); // Show item dropdown
        });

        // Add Item button handler
        addItemButton.setOnAction(e -> {
            String selectedItem = itemComboBox.getValue();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                cartItems.add(selectedItem); // Add to your cart list
                itemComboBox.getSelectionModel().clearSelection(); // Clear selection
            }
        });

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
                        String selectedTableId = "Tbl " + (char)('A' + finalCol) + (finalRow + 1);
                        tableLabel.setText(selectedTableId);


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


        backButton.setOnAction(e -> primaryStage.setScene(waiterScene));
        logoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        cookLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        // Send Order button handler
        sendOrderButton.setOnAction(e -> {
            if (!cartItems.isEmpty()) {
                // --- Get the table ID from the label ---
                String currentTableId = tableLabel.getText(); // Assumes tableLabel holds the correct ID

                // --- Create a new Order object ---
                Order newOrder = new Order(currentTableId, cartItems);

                // --- Add the Order object to the kitchen queue ---
                orderQueue.add(newOrder); // <<< ADD Order OBJECT

                System.out.println("Order Sent: " + newOrder.getItems() + " for " + newOrder.getTableId());

                cartItems.clear(); // Clear the cart for this table
                totalLabel.setText("Total: $0.00");
                primaryStage.setScene(waiterScene); // Go back to table view
            }
        });

        // Pay Tab button handler
        payTabButton.setOnAction(e -> {
            // primaryStage.setScene(paymentScene); // Navigate to your payment scene
        });


        // Back Button handler
        backButton.setOnAction(e -> {
            cartItems.clear();
            totalLabel.setText("Total: $0.00"); // Reset total
            primaryStage.setScene(waiterScene);
        });
        markReadyButton.setOnAction(e -> {
            Order selectedOrder = kitchenQueueListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                orderQueue.remove(selectedOrder); // Remove the selected Order object
                orderItemsDisplay.getItems().clear(); // Clear the item display
                kitchenQueueListView.getSelectionModel().clearSelection(); // Clear selection in main list
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