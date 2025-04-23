package application;

//Testing if the push works instantly

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


public class Main extends Application {

    public ObservableList<String> cartItems = FXCollections.observableArrayList();
    public ObservableList<Order> orderQueue = FXCollections.observableArrayList();
    public ObservableList<Order> KitchenOrders = FXCollections.observableArrayList();

    // Store completed/paid orders if needed later
    // public ObservableList<Order> completedOrders = FXCollections.observableArrayList();
    // Store which table's order is being paid
    public String currentOrderForPayment = null;
    public String currentTableIdForPaymentOrAdd = null;

    public Label tableLabel; // For Order Screen
    public Label paymentTableLabel; // For Payment Screen
    public Label paymentTotalLabel; // For Payment Screen
    public ListView<String> paymentItemsListView; // For Payment Screen
    public Label orderTotalLabel; // For Order Screen Total
    public Scene currentUserRole;


    // --- Order Class ---
    public static class Order {
        public String tableId;
        public final List<String> items;
        public double total; // Store calculated total

        public Order(String tableId, List<String> items) {
            this.tableId = tableId;
            this.items = new ArrayList<>(items); // Create a copy
            this.total = calculateTotal(items); // Calculate total on creation
        }

        public String getTableId() {
            return tableId; }
        public List<String> getItems() {
            return items; }
        public double getTotal() {
            return total; } // Getter for total

        // Method to update the order with new items and recalculate total
        public void updateOrder(List<String> newItems) {
            this.items.clear();
            this.items.addAll(newItems);
            this.total = calculateTotal(this.items);
        }

        // Helper method to parse price and calculate total
        public static double calculateTotal(List<String> items) {
            double sum = 0.0;
            for (String item : items) {
                try {
                    // Assumes format "Item Name - $Price"
                    String[] parts = item.split(" - \\$");
                    if (parts.length > 1) {
                        sum += Double.parseDouble(parts[1].trim()); // Trim whitespace
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Could not parse price for item: " + item);
                }
            }
            return sum;
        }

        // Format items for display
        public List<String> getFormattedItemsWithPrices() {
            return new ArrayList<>(items);
        }

        @Override
        public String toString() {
            // Controls how the Order appears in the main kitchen queue ListView
            return tableId + " (" + items.size() + " item" + (items.size() != 1 ? "s" : "") + ")";
        }
    }

    // --- TableGrid Class ---
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
                this.add(header, col + 1, 0); // Add to grid
            }

            // Add row numbers (1-6) and table buttons
            for (int row = 0; row < ROWS; row++) {
                this.add(new Label(Integer.toString(row + 1)), 0, row + 1); // Add row number label

                for (int col = 0; col < COLS; col++) {
                    Button table = new Button();
                    table.setMinSize(60, 60);
                    // Store full table ID
                    String tableId = "Tbl " + (char)('A' + col) + (row + 1);
                    table.setId(tableId); // Use full ID for the button ID
                    tables[row][col] = table;
                    this.add(table, col + 1, row + 1); // Add button to grid
                }
            }
            //Todo replace with the data from the data base
            // Initialize tables with random status (Clean, Occupied, Dirty)
            String[] statuses = {"Clean", "Occupied", "Dirty"};
            java.util.Random random = new java.util.Random();
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    // Ensure table button exists before setting status
                    if (tables[row][col] != null) {
                        String randomStatus = statuses[random.nextInt(statuses.length)];
                        setTableStatus(row, col, "Clean");
                    }
                }
            }
        }

        // Sets the visual style and text based on status
        public void setTableStatus(int row, int col, String status) {
            if (row < 0 || row >= ROWS || col < 0 || col >= COLS || tables[row][col] == null) {
                System.err.println("Invalid table coordinates or null button at: " + row + "," + col);
                return;
            }
            Button table = tables[row][col];
            table.setUserData(status); // Store status in UserData

            switch (status) {
                case "Clean":
                    table.setStyle("-fx-background-color: #34c759; -fx-text-fill: white;"); // Green
                    break;
                case "Occupied":
                    table.setStyle("-fx-background-color: #FFFF00; -fx-text-fill: white;"); // Orange
                    break;
                case "Dirty":
                    table.setStyle("-fx-background-color: #ff3b30; -fx-text-fill: white;"); // Red
                    break;
                default: // Handle unexpected status
                    System.err.println("Unknown table status applied: " + status);
                    table.setStyle("");
                    break;
            }
            table.setText(table.getId());
        }

        // Set status using the full Table ID (e.g., "Tbl A1")
        public void setTableStatusById(String tableId, String status) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    // Check if button exists and ID matches
                    if (tables[row][col] != null && tables[row][col].getId().equals(tableId)) {
                        setTableStatus(row, col, status);
                        return; // Found and set
                    }
                }
            }
            System.err.println("Table ID not found in grid to set status: " + tableId);
        }

        // Get status from UserData
        public String getTableStatus(int row, int col) {
            return (String) tables[row][col].getUserData();
        }

        // Get the button object itself
        public Button getTableButton(int row, int col) {
            return tables[row][col];
        }

        // Helper to get button by ID
        public Optional<Button> getTableButtonById(String tableId) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (tables[r][c] != null && tables[r][c].getId().equals(tableId)) {
                        return Optional.of(tables[r][c]);
                    }
                }
            }
            return Optional.empty();
        }

        // Getter for the full Table ID based on row/col
        public String getTableId(int row, int col) {
            if (row < 0 || row >= ROWS || col < 0 || col >= COLS || tables[row][col] == null) return null;
            return tables[row][col].getId();
        }
    }


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Restaurant Management System");

        // --- Login Screen ---
        Label titleLabel = new Label("J's Corner Restaurant");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label userLabel = new Label("Enter ID:");
        TextField userField = new TextField();
        Label passLabel = new Label("Enter Password:");
        PasswordField passField = new PasswordField();
        Button loginButton = new Button("LOGIN");
        loginButton.setStyle("-fx-font-weight: bold;");
        Label messageLabel = new Label();
        VBox loginPane = new VBox(20, titleLabel, userLabel, userField, passLabel, passField, loginButton, messageLabel);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(40));
        Scene loginScene = new Scene(loginPane, 400, 500);
        loginPane.setId("loginPane");
        titleLabel.setId("titleLabel");
        loginButton.setId("loginButton");

        // --- Kitchen Screen ---
        VBox kitchenPane = new VBox(10);
        kitchenPane.setAlignment(Pos.CENTER);
        kitchenPane.setPadding(new Insets(15));
        Label kitchenTitleLabel = new Label("Incoming Orders");
        kitchenTitleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        ListView<Order> kitchenQueueListView = new ListView<>(KitchenOrders);
        kitchenQueueListView.setPrefHeight(150);
        Label selectedOrderLabel = new Label("Selected Order Items:");
        selectedOrderLabel.setStyle("-fx-font-weight: bold;");
        ListView<String> orderItemsDisplay = new ListView<>();
        orderItemsDisplay.setPrefHeight(150);
        Button markReadyButton = new Button("Mark as Ready");
        Button cookPunchButton = new Button("Punch");
        Button cookLogoutButton = new Button("Log Out");
        Label cookStatusLabel = new Label("COOK");
        HBox cookTopBar = new HBox(10, cookStatusLabel, cookPunchButton, cookLogoutButton);
        cookTopBar.setAlignment(Pos.CENTER_RIGHT);

        // Update item display when an order is selected in the kitchen queue
        kitchenQueueListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            orderItemsDisplay.getItems().clear();
            if (newSelection != null) {
                orderItemsDisplay.getItems().addAll(newSelection.getItems()); // Show items from the selected Order
            }
        });

        kitchenPane.getChildren().addAll(
                cookTopBar, kitchenTitleLabel, kitchenQueueListView,
                new Separator(javafx.geometry.Orientation.HORIZONTAL),
                selectedOrderLabel, orderItemsDisplay, markReadyButton
        );
        Scene kitchenScene = new Scene(kitchenPane, 450, 550);

        // --- Manager Dashboard ---
        TableGrid managerTableGrid = new TableGrid(); // Separate grid instance for Manager
        Button managerPunchButton = new Button("Punch");
        Button managerLogoutButton = new Button("Log Out");
        Button managerMenuButton = new Button("Manager Menu");
        Label managerStatusLabel = new Label("MANAGER");

        // Hide middle Tables in manager view (optional)
        for(int i = 0; i < 4; i++) {
           managerTableGrid.getTableButton(i, 2).setVisible(false);
          managerTableGrid.getTableButton(i, 3).setVisible(false);
        }

        HBox managerTopBar = new HBox(10, managerStatusLabel, managerPunchButton, managerLogoutButton);
        managerTopBar.setAlignment(Pos.CENTER_RIGHT);
        managerTopBar.setPadding(new Insets(10));
        HBox managerBottomBar = new HBox(managerMenuButton);
        managerBottomBar.setAlignment(Pos.CENTER);
        managerBottomBar.setPadding(new Insets(10, 0, 0, 0));
        managerTableGrid.setAlignment(Pos.CENTER);
        VBox managerPane = new VBox(10, managerTopBar, managerTableGrid, managerBottomBar);
        managerPane.setPadding(new Insets(20));
        Scene managerScene = new Scene(managerPane, 800, 600);

        // --- Manager Menu Screen ---
        Label managerMenuTitle_scene = new Label("Manager Menu");
        managerMenuTitle_scene.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button salesAnalyticsButton_scene = new Button("Sales Analytics");
        Button employeeAnalyticsButton_scene = new Button("Employee Analytics");
        Button performanceAnalyticsButton_scene = new Button("Performance Analytics");
        Button employeeActionsButton_scene = new Button("Employee Actions");
        Button managerMenuBackButton_scene = new Button("Back");
        double buttonWidth_scene = 200;
        salesAnalyticsButton_scene.setPrefWidth(buttonWidth_scene);
        employeeAnalyticsButton_scene.setPrefWidth(buttonWidth_scene);
        performanceAnalyticsButton_scene.setPrefWidth(buttonWidth_scene);
        employeeActionsButton_scene.setPrefWidth(buttonWidth_scene);
        managerMenuBackButton_scene.setPrefWidth(buttonWidth_scene / 2);
        VBox managerMenuPane_scene = new VBox(15, managerMenuTitle_scene, salesAnalyticsButton_scene, employeeAnalyticsButton_scene, performanceAnalyticsButton_scene, employeeActionsButton_scene, managerMenuBackButton_scene);
        managerMenuPane_scene.setAlignment(Pos.CENTER);
        managerMenuPane_scene.setPadding(new Insets(30));
        Scene managerMenuScene = new Scene(managerMenuPane_scene, 400, 400);

        // --- Manager add employee Screen ---
        VBox addNewEmployeePane = new VBox(15);
        addNewEmployeePane.setPadding(new Insets(30));
        addNewEmployeePane.setAlignment(Pos.CENTER);
        addNewEmployeePane.setId("addNewEmployeePane");

        Label addNewEmployeeTitle = new Label("Add New Employee");
        addNewEmployeeTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        inputGrid.add(firstNameLabel, 0, 0);
        inputGrid.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        inputGrid.add(lastNameLabel, 0, 1);
        inputGrid.add(lastNameField, 1, 1);

        Label roleLabel = new Label("Role:");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Waiter", "Busboy", "Manager", "Cook");
        roleComboBox.setPromptText("Select Role");
        inputGrid.add(roleLabel, 0, 2);
        inputGrid.add(roleComboBox, 1, 2);

// Action Buttons for the Add Employee scene
        Button saveEmployeeButton = new Button("Save Employee");
        Button addEmployeeBackButton = new Button("Back");

        HBox addEmployeeButtonBox = new HBox(10, saveEmployeeButton, addEmployeeBackButton);
        addEmployeeButtonBox.setAlignment(Pos.CENTER);

        addNewEmployeePane.getChildren().addAll(
                addNewEmployeeTitle,
                inputGrid,
                addEmployeeButtonBox
        );

        Scene addNewEmployeeScene = new Scene(addNewEmployeePane, 450, 350);



        // --- Busboy Screen ---
        TableGrid busboyTableGrid = new TableGrid();
        Button busboyPunchButton = new Button("Punch");
        Button busboyLogoutButton = new Button("Log Out");
        Label busboyStatusLabel = new Label("BUSBOY");

        // Hide middle tables in busboy view (optional)
        for(int i = 0; i < 4; i++) { // Rows 0-3
         busboyTableGrid.getTableButton(i, 2).setVisible(false);
        busboyTableGrid.getTableButton(i, 3).setVisible(false);
        }

        HBox busboyTopBar = new HBox(10, busboyStatusLabel, busboyPunchButton, busboyLogoutButton);
        busboyTopBar.setAlignment(Pos.CENTER_RIGHT);
        busboyTopBar.setPadding(new Insets(10));
        busboyTableGrid.setAlignment(Pos.CENTER);
        VBox busboyPane = new VBox(10, busboyTopBar, busboyTableGrid);
        busboyPane.setPadding(new Insets(20));
        busboyPane.setId("busboyPane");
        Scene busboyScene = new Scene(busboyPane, 800, 600);


        // --- Waiter Screen ---
        TableGrid waiterTableGrid = new TableGrid(); // Separate grid instance for Waiter
        Button waiterPunchButton = new Button("Punch");
        Button waiterLogoutButton = new Button("Log Out");
        Label waiterStatusLabel = new Label("WAITER");
        waiterPunchButton.setId("punchButton"); // CSS IDs
        waiterLogoutButton.setId("logoutButton");
        waiterStatusLabel.setId("statusLabel");

        // Hide middle tables in waiter view
        for(int i = 0; i < 4; i++) {
            waiterTableGrid.getTableButton(i, 2).setVisible(false);
           waiterTableGrid.getTableButton(i, 3).setVisible(false);
        }

        HBox waiterTopBar = new HBox(10, waiterStatusLabel, waiterPunchButton, waiterLogoutButton);
        waiterTopBar.setAlignment(Pos.CENTER_RIGHT);
        waiterTopBar.setPadding(new Insets(10));
        waiterTableGrid.setAlignment(Pos.CENTER);
        VBox waiterPane = new VBox(10, waiterTopBar, waiterTableGrid);
        waiterPane.setPadding(new Insets(20));
        waiterPane.setId("waiterPane");
        Scene waiterScene = new Scene(waiterPane, 800, 600);

        // --- Employee Actions Screen
        VBox employeeActionsPane = new VBox(15);
        employeeActionsPane.setPadding(new Insets(30));
        employeeActionsPane.setAlignment(Pos.CENTER);
        employeeActionsPane.setId("employeeActionsPane");

        Label employeeActionsTitle = new Label("Employee Actions");
        employeeActionsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button addNewEmployeeButton = new Button("Add New Employee");
        Button viewEditEmployeeButton = new Button("View/Edit Employee Info");
        Button employeeActionsBackButton = new Button("Back");

        double employeeActionButtonWidth = 200;
        addNewEmployeeButton.setPrefWidth(employeeActionButtonWidth);
        viewEditEmployeeButton.setPrefWidth(employeeActionButtonWidth);
        employeeActionsBackButton.setPrefWidth(employeeActionButtonWidth / 2);

        employeeActionsPane.getChildren().addAll(
                employeeActionsTitle,
                addNewEmployeeButton,
                viewEditEmployeeButton,
                employeeActionsBackButton
        );

        Scene employeeActionsScene = new Scene(employeeActionsPane, 400, 300);


        // --- Order Management Screen  ---
        // Left Pane: Menu Categories and Items
        VBox menuSelectionPane = new VBox(10);
        menuSelectionPane.setAlignment(Pos.TOP_LEFT); menuSelectionPane.setPadding(new Insets(15));
        Label orderMenuLabel = new Label("Select a Menu Category"); // Renamed variable
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.setMinWidth(180);
        categoryComboBox.getItems().addAll("Appetizers", "Salads", "Entrees", "Sides", "Sandwiches", "Burgers", "Beverages");
        ComboBox<String> itemComboBox = new ComboBox<>();
        itemComboBox.setMinWidth(180); itemComboBox.setVisible(false); // Initially hidden
        Button addItemButton = new Button("Add Item");
        addItemButton.setMinWidth(180);
        menuSelectionPane.getChildren().addAll(orderMenuLabel, categoryComboBox, itemComboBox, addItemButton);

        // Right Pane: Order Details (Check View)
        VBox orderDetailsPane = new VBox(10);
        orderDetailsPane.setAlignment(Pos.TOP_CENTER); orderDetailsPane.setPadding(new Insets(15));
        orderDetailsPane.setStyle("-fx-border-color: lightgrey; -fx-border-width: 1;");
        Label checkLabel = new Label("Chk #1001"); checkLabel.setStyle("-fx-font-weight: bold;"); // Placeholder
        if(Objects.equals(currentUserRole, waiterScene)){
            tableLabel = new Label("Tbl: "+waiterTableGrid.getId()); tableLabel.setStyle("-fx-font-weight: bold;"); // Initialize field
        } else{
            tableLabel = new Label("Tbl: "+managerTableGrid.getId()); tableLabel.setStyle("-fx-font-weight: bold;"); // Initialize field
        }
        Label serverLabel = new Label("Server: <Name>"); serverLabel.setStyle("-fx-font-weight: bold;"); // Placeholder
        ListView<String> orderItemsListView = new ListView<>(cartItems); // Binds to the cartItems list
        orderItemsListView.setPrefHeight(200);
        orderTotalLabel = new Label("Total: $0.00"); // Initialize field
        orderTotalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        Button payTabButton = new Button("PAY TAB"); payTabButton.setMinWidth(100);
        Button sendOrderButton = new Button("SEND"); sendOrderButton.setMinWidth(100);
        HBox orderActionButtons = new HBox(10, sendOrderButton); orderActionButtons.setAlignment(Pos.CENTER);
        Button orderBackButton = new Button("Back to Tables"); // Back button for this screen

        orderDetailsPane.getChildren().addAll(
                checkLabel, tableLabel, serverLabel, new Separator(javafx.geometry.Orientation.HORIZONTAL),
                new Label("Current Order:"), orderItemsListView, new Separator(javafx.geometry.Orientation.HORIZONTAL),
                orderTotalLabel, orderActionButtons, orderBackButton
        );

        // Main Pane for Order Screen
        HBox orderPane = new HBox(10, menuSelectionPane, orderDetailsPane);
        orderPane.setPadding(new Insets(10));
        Scene orderScene = new Scene(orderPane, 650, 450);


        // --- Payment Screen ---
        VBox paymentPane = new VBox(15);
        paymentPane.setPadding(new Insets(20));
        paymentPane.setAlignment(Pos.CENTER);
        paymentPane.setId("paymentPane");

        Label paymentTitleLabel = new Label("Pay Screen");
        paymentTitleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        paymentTableLabel = new Label("Table: "); // Initialize field
        paymentTableLabel.setStyle("-fx-font-weight: bold;");
        paymentTotalLabel = new Label("Total: $0.00"); // Initialize field
        paymentTotalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        paymentItemsListView = new ListView<>(); // Initialize field
        paymentItemsListView.setPrefHeight(150);
        Button cashButton = new Button("Cash"); cashButton.setMinWidth(100);
        Button cardButton = new Button("Card"); cardButton.setMinWidth(100);
        Button addToOrderButton = new Button("Add to Order");
        addToOrderButton.setMinWidth(100);
        HBox paymentMethodBox = new HBox(20, cashButton, cardButton, addToOrderButton); paymentMethodBox.setAlignment(Pos.CENTER);
        Button paymentBackButton = new Button("Back to Tables");

        paymentPane.getChildren().addAll(
                paymentTitleLabel, paymentTableLabel, new Separator(javafx.geometry.Orientation.HORIZONTAL),
                new Label("Order Items:"), paymentItemsListView, new Separator(javafx.geometry.Orientation.HORIZONTAL),
                paymentTotalLabel, paymentMethodBox, paymentBackButton
        );
        Scene paymentScene = new Scene(paymentPane, 450, 450);


        // --- Event Handlers ---

        // Login Handler
        loginButton.setOnAction(e -> {
            String username = userField.getText(); String password = passField.getText();
            if (username.equals("waiter") && password.equals("1234")) {
                primaryStage.setScene(waiterScene);
                currentUserRole = waiterScene;}
            else if (username.equals("manager") && password.equals("admin")) { primaryStage.setScene(managerScene);
                currentUserRole = managerScene;}
            else if (username.equals("busboy") && password.equals("cleaner")) { primaryStage.setScene(busboyScene); }
            else if (username.equals("cook") && password.equals("kitchen")) { primaryStage.setScene(kitchenScene); }
            else { messageLabel.setText("Invalid credentials"); }
            // Clear fields after attempt
            userField.clear();
            passField.clear();
        });

        addNewEmployeeButton.setOnAction(e -> {
            primaryStage.setScene(addNewEmployeeScene);
        });

        saveEmployeeButton.setOnAction(saveEvent -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String role = roleComboBox.getValue();

            if (firstName == null || firstName.trim().isEmpty() ||
                    lastName == null || lastName.trim().isEmpty() ||
                    role == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all employee details.");
                alert.showAndWait();
            } else {

                //Todo save it to the database
                System.out.println("--- Saving New Employee ---");
                System.out.println("First Name: " + firstName.trim());
                System.out.println("Last Name: " + lastName.trim());
                System.out.println("Role: " + role);
                System.out.println("--------------------------");


                // Clear the input fields after successful save
                firstNameField.clear();
                lastNameField.clear();
                roleComboBox.getSelectionModel().clearSelection();

                // Show confirmation and return to the previous screen
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Employee '" + firstName.trim() + " " + lastName.trim() + "' added successfully!");
                confirmation.showAndWait();

                primaryStage.setScene(employeeActionsScene); // Go back to Employee Actions
            }
        });

        addEmployeeBackButton.setOnAction(backEvent -> {
            firstNameField.clear();
            lastNameField.clear();
            roleComboBox.getSelectionModel().clearSelection();
            primaryStage.setScene(employeeActionsScene);
        });


        // Waiter Screen Table Click Handler
        for (int row = 0; row < TableGrid.ROWS; row++) {
            for (int col = 0; col < TableGrid.COLS; col++) {
                Button tableButton = waiterTableGrid.getTableButton(row, col);
                if (tableButton == null) continue; // Skip if button doesn't exist

                final int finalRow = row;
                final int finalCol = col;
                final String currentTableId = waiterTableGrid.getTableId(row, col); // Get ID here

                tableButton.setOnAction(e -> {
                    String status = waiterTableGrid.getTableStatus(finalRow, finalCol);
                    if (status == null) status = "Clean"; // Default if somehow null

                    switch (status) {
                        case "Clean":
                            // Go to Order Screen to start a new order
                            // Table status remains 'Clean' until order is sent
                            tableLabel.setText(currentTableId); // Set table label on order screen
                            cartItems.clear(); // Clear cart for the new order
                            orderTotalLabel.setText("Total: $0.00"); // Reset total on order screen
                            // Reset menu selections (optional but good practice)
                            categoryComboBox.getSelectionModel().clearSelection();
                            itemComboBox.getItems().clear();
                            itemComboBox.setVisible(false);
                            primaryStage.setScene(orderScene);
                            break;

                        case "Occupied":

                            currentOrderForPayment = currentTableId;
                            // Go to Payment Screen for this table's existing order
                            List <Order> orderOpt = orderQueue.stream()
                                    .filter(order -> order.getTableId().equals(currentTableId))
                                    .collect(Collectors.toList());;

                            if (!orderOpt.isEmpty()) {
                                // Aggregate items and calculate total from ALL found orders
                                List<String> combinedItems = new ArrayList<>();
                                double combinedTotal = 0.0;
                                for(Order order : orderOpt) { // Loop through each order found
                                    combinedItems.addAll(order.getFormattedItemsWithPrices()); // Add its items
                                    combinedTotal += order.getTotal(); // Add its total
                                }

                                // Update payment screen labels and item list WITH COMBINED DATA
                                paymentTableLabel.setText("Table: " + currentTableId);
                                paymentTotalLabel.setText(String.format("Total: $%.2f", combinedTotal));
                                paymentItemsListView.getItems().setAll(combinedItems); // Display combined items list

                                currentUserRole = waiterScene;
                                primaryStage.setScene(paymentScene);
                            } else {
                                // Error: Table is 'Occupied' but no matching order found in queue
                                System.err.println("Error: Occupied table " + currentTableId + " has no matching order in active queue.");
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Order Not Found");
                                alert.setHeaderText(null);
                                alert.setContentText("Could not find the order details for table " + currentTableId + ".\nThe order might be completed or an error occurred.");
                                alert.showAndWait();
                            }
                            break;

                        case "Dirty":
                            // Inform user the table needs cleaning
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Table Status");
                            alert.setHeaderText(null);
                            alert.setContentText("Table " + currentTableId + " is dirty and needs cleaning.");
                            alert.showAndWait();
                            break;

                        default:
                            System.out.println("Unhandled status clicked for table " + currentTableId + ": " + status);
                            break;
                    }
                });
            }
        }

        // Add to Order button on Payment Screen
        // Add to Order button on Payment Screen
        addToOrderButton.setOnAction(e -> {
            if (currentOrderForPayment != null) {
                tableLabel.setText(currentOrderForPayment); // Set table label on order screen

                // *** Clear the cartItems for the new order entry ***
                cartItems.clear();
                orderTotalLabel.setText("Total: $0.00"); // Reset total label

                // Reset menu selections (optional but good practice)
                categoryComboBox.getSelectionModel().clearSelection();
                itemComboBox.getItems().clear();
                itemComboBox.setVisible(false);

                primaryStage.setScene(orderScene); // Go back to order screen
            } else {
                // Should not happen if button is only reachable when a table was selected
                System.err.println("Error: Add to Order clicked with no table context.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot add to order. No table selected.");
                alert.showAndWait();
                primaryStage.setScene(currentUserRole); // Go back to table view
            }
        });


        // Order Screen Handlers
        // Category selection populates item combo box
        categoryComboBox.setOnAction(e -> {
            itemComboBox.getItems().clear();
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory == null) {
                itemComboBox.setVisible(false);
                return;
            }
            switch (selectedCategory) {
                case "Appetizers": itemComboBox.getItems().addAll("Chicken Nachos - $8.50", "Pork Nachos - $8.50", "Pork/Chicken Sliders (3) - $5.00", "Catfish Bites - $6.50", "Fried Veggies - $6.50"); break;
                case "Salads": itemComboBox.getItems().addAll("House Salad - $7.50", "Wedge Salad - $7.50", "Caesar Salad - $7.50", "Sweet Potato Chicken Salad - $11.50"); break;
                case "Entrees": itemComboBox.getItems().addAll("Shrimp & Grits - $13.50", "Sweet Tea Fried Chicken - $11.50", "Caribbean Chicken - $11.50", "Grilled Pork Chops - $11.00", "New York Strip Steak - $17.00", "Seared Tuna - $15.00", "Captain Crunch Chicken Tenders - $11.50", "Shock Top Grouper Fingers - $11.50", "Mac & Cheese Bar - $8.50"); break;
                case "Sides": itemComboBox.getItems().addAll("Curly Fries - $2.50", "Wing Chips - $2.50", "Sweet Potato Fries - $2.50", "Creamy Cabbage Slaw - $2.50", "Adluh Cheese Grits - $2.50", "Mashed Potatoes - $2.50", "Mac & Cheese - $2.50", "Seasonal Vegetables - $2.50", "Baked Beans - $2.50"); break;
                case "Sandwiches": itemComboBox.getItems().addAll("Grilled Cheese - $5.50", "Chicken BLT&A - $10.00", "Philly - $13.50", "Club - $10.00", "Meatball Sub - $10.00"); break;
                case "Burgers": itemComboBox.getItems().addAll("Bacon Cheeseburger - $11.00", "Carolina Burger - $11.00", "Portobello Burger (V) - $8.50", "Vegan Boca Burger (V) - $10.50"); break;
                case "Beverages": itemComboBox.getItems().addAll("Sweet/Unsweetened Tea - $2.00", "Coke/Diet Coke - $2.00", "Sprite - $2.00", "Bottled Water - $2.00", "Lemonade - $2.00", "Orange Juice - $2.00"); break;
            }
            itemComboBox.setVisible(!itemComboBox.getItems().isEmpty()); // Show only if items exist
        });

        // Add item button adds selected item to cart and updates total
        addItemButton.setOnAction(e -> {
            String selectedItem = itemComboBox.getValue();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                cartItems.add(selectedItem);
                // Update total label on Order Screen
                orderTotalLabel.setText(String.format("Total: $%.2f", Order.calculateTotal(cartItems)));
            }
        });

        cartItems.addListener((javafx.collections.ListChangeListener.Change<? extends String> c) -> {
            orderTotalLabel.setText(String.format("Total: $%.2f", Order.calculateTotal(cartItems)));
        });

        // Send Order button creates Order, adds to queue, updates status, returns to waiter view
        sendOrderButton.setOnAction(e -> {
            if (!cartItems.isEmpty()) {
                String currentTableId = tableLabel.getText(); // Get table ID from the label
                if (currentTableId == null || currentTableId.equals("Tbl: <Selected Table>")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot send order. Table ID is missing.");
                    alert.showAndWait();
                    return; // Prevent sending without a valid table ID
                }

                // Create a new Order object (calculates total internally)
                Order newOrder = new Order(currentTableId, cartItems);
                orderQueue.add(newOrder);
                KitchenOrders.add(newOrder); // Add to kitchen queue

                System.out.println("Order Sent: " + newOrder.getItems().size() + " items for " + newOrder.getTableId() + " Total: " + String.format("%.2f", newOrder.getTotal()));

                // *** Update the table status in ALL grids to 'Occupied' ***
                waiterTableGrid.setTableStatusById(currentTableId, "Occupied");
                managerTableGrid.setTableStatusById(currentTableId, "Occupied");
                busboyTableGrid.setTableStatusById(currentTableId, "Occupied");

                cartItems.clear(); // Clear the temporary cart
                orderTotalLabel.setText("Total: $0.00"); // Reset total label
                primaryStage.setScene(currentUserRole); // Go back to table view
            } else {
                // Alert if trying to send empty order
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Empty Order");
                alert.setHeaderText(null);
                alert.setContentText("Cannot send an empty order. Please add items first.");
                alert.showAndWait();
            }
        });



        // Order Back button clears cart, sets table to Clean, returns to waiter view
        orderBackButton.setOnAction(e -> {
            String currentTableId = tableLabel.getText();
            // If a valid table was being worked on, ensure it's set back to Clean
            // as the order was not sent.
            if (currentTableId != null && !currentTableId.equals("Tbl: <Selected Table>")) {
                waiterTableGrid.setTableStatusById(currentTableId, "Clean");
                managerTableGrid.setTableStatusById(currentTableId, "Clean");
                busboyTableGrid.setTableStatusById(currentTableId, "Clean");
            }
            cartItems.clear(); // Always clear cart when leaving this screen via Back button
            orderTotalLabel.setText("Total: $0.00"); // Reset total label
            primaryStage.setScene(currentUserRole);
        });


        // Kitchen Screen Handlers
        // Mark Ready removes order from queue and marks table Dirty
        markReadyButton.setOnAction(e -> {
            Order selectedOrder = kitchenQueueListView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                String tableIdToMarkDirty = selectedOrder.getTableId();
                KitchenOrders.remove(selectedOrder);
               // orderQueue.remove(selectedOrder); // Remove from kitchen queue
                orderItemsDisplay.getItems().clear(); // Clear display
                kitchenQueueListView.getSelectionModel().clearSelection(); // Deselect
                // Add to completed orders list
                 // completedOrders.add(selectedOrder);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an order from the list to mark as ready.");
                alert.showAndWait();
            }
        });
        cookPunchButton.setOnAction(e -> {
            if ("Punch".equals(cookPunchButton.getText())) {
                cookPunchButton.setText("Clock Out");
                cookStatusLabel.setText("Cook - Clocked In");
            } else {
                cookPunchButton.setText("Punch");
                cookStatusLabel.setText("Cook - Clocked Out");
            }
        });        cookLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));


        // Manager Screen Handlers
        managerPunchButton.setOnAction(e -> {
            if ("Punch".equals(managerPunchButton.getText())) {
                managerPunchButton.setText("Clock Out");
                managerStatusLabel.setText("Manager - Clocked In");
            } else {
                managerPunchButton.setText("Punch");
                managerStatusLabel.setText("Manager - Clocked Out");
            }
        });        managerLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        managerMenuButton.setOnAction(e -> primaryStage.setScene(managerMenuScene));
        // Manager table click handler (to mark Dirty tables Clean)
        for (int row = 0; row < TableGrid.ROWS; row++) {
            for (int col = 0; col < TableGrid.COLS; col++) {
                Button table = managerTableGrid.getTableButton(row, col);
                if (table == null) continue;
                int finalRow = row;
                int finalCol = col;
                final String currentTableId = managerTableGrid.getTableId(row, col); // Get ID here


                table.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY) {

                        String status = managerTableGrid.getTableStatus(finalRow, finalCol);
                        if (status == null) status = "Clean"; // Default if somehow null


                        switch (status) {
                            case "Clean":
                                // Go to Order Screen to start a new order
                                // Table status remains 'Clean' until order is sent
                                tableLabel.setText(currentTableId); // Set table label on order screen
                                cartItems.clear(); // Clear cart for the new order
                                orderTotalLabel.setText("Total: $0.00"); // Reset total on order screen
                                // Reset menu selections
                                categoryComboBox.getSelectionModel().clearSelection();
                                itemComboBox.getItems().clear();
                                itemComboBox.setVisible(false);
                                primaryStage.setScene(orderScene);
                                break;

                            case "Occupied":
                                currentOrderForPayment = currentTableId;
                                // Go to Payment Screen for this table's existing order
                                List <Order> orderOpt = orderQueue.stream()
                                        .filter(order -> order.getTableId().equals(currentTableId))
                                        .collect(Collectors.toList());;

                                if (!orderOpt.isEmpty()) {
                                    // Aggregate items and calculate total from ALL found orders
                                    List<String> combinedItems = new ArrayList<>();
                                    double combinedTotal = 0.0;
                                    for(Order order : orderOpt) { // Loop through each order found
                                        combinedItems.addAll(order.getFormattedItemsWithPrices()); // Add its items
                                        combinedTotal += order.getTotal(); // Add its total
                                    }

                                    // Update payment screen labels and item list WITH COMBINED DATA
                                    paymentTableLabel.setText("Table: " + currentTableId);
                                    paymentTotalLabel.setText(String.format("Total: $%.2f", combinedTotal));
                                    paymentItemsListView.getItems().setAll(combinedItems); // Display combined items list

                                    currentUserRole = managerScene;
                                    primaryStage.setScene(paymentScene);
                                } else {
                                    // Error: Table is 'Occupied' but no matching order found in queue
                                    System.err.println("Error: Occupied table " + currentTableId + " has no matching order in active queue.");
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Order Not Found");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Could not find the order details for table " + currentTableId + ".\nThe order might be completed or an error occurred.");
                                    alert.showAndWait();
                                }
                                break;

                            case "Dirty":
                                String tableId = managerTableGrid.getTableId(finalRow, finalCol);
                                managerTableGrid.setTableStatusById(tableId, "Clean");
                                waiterTableGrid.setTableStatusById(tableId, "Clean");
                                busboyTableGrid.setTableStatusById(tableId, "Clean");

                            default:
                                System.out.println("Unhandled status clicked for table " + currentTableId + ": " + status);
                                break;
                        }
                    }else if (e.getButton() == MouseButton.SECONDARY) {
                        String status = managerTableGrid.getTableStatus(finalRow, finalCol);

                        if (status == "Clean") {
                            String tableId = managerTableGrid.getTableId(finalRow, finalCol);
                            managerTableGrid.setTableStatusById(tableId, "Occupied");
                            waiterTableGrid.setTableStatusById(tableId, "Occupied");
                            busboyTableGrid.setTableStatusById(tableId, "Occupied");
                        }else if(status == "Dirty"){
                            String tableId = managerTableGrid.getTableId(finalRow, finalCol);
                            managerTableGrid.setTableStatusById(tableId, "Inactive");
                            waiterTableGrid.setTableStatusById(tableId, "Inactive");
                            busboyTableGrid.setTableStatusById(tableId, "Inactive");
                        }else if(status=="Occupied"){
                            String tableId = managerTableGrid.getTableId(finalRow, finalCol);
                            managerTableGrid.setTableStatusById(tableId, "Dirty");
                            waiterTableGrid.setTableStatusById(tableId, "Dirty");
                            busboyTableGrid.setTableStatusById(tableId, "Dirty");
                            orderQueue.remove(kitchenQueueListView.getSelectionModel().getSelectedItem());

                        }else{
                            String tableId = managerTableGrid.getTableId(finalRow, finalCol);
                            managerTableGrid.setTableStatusById(tableId, "Clean");
                            waiterTableGrid.setTableStatusById(tableId, "Clean");
                            busboyTableGrid.setTableStatusById(tableId, "Clean");
                        }
                    }
                });

            }
        }
        // Manager Menu Screen Handlers
        managerMenuBackButton_scene.setOnAction(e -> primaryStage.setScene(managerScene));
        employeeActionsBackButton.setOnAction(e -> primaryStage.setScene(managerMenuScene));
        salesAnalyticsButton_scene.setOnAction(e -> System.out.println("Navigate to Sales Analytics")); // Placeholder
        employeeAnalyticsButton_scene.setOnAction(e -> System.out.println("Navigate to Employee Analytics")); // Placeholder
        performanceAnalyticsButton_scene.setOnAction(e -> System.out.println("Navigate to Performance Analytics")); // Placeholder
        employeeActionsButton_scene.setOnAction(e -> {
            primaryStage.setScene(employeeActionsScene); // Navigate to Employee Actions screen
        });


        // Busboy Screen Handlers
        // Waiter Punch/Logout Handlers
        busboyPunchButton.setOnAction(e -> {
            if ("Punch".equals(busboyPunchButton.getText())) {
                busboyPunchButton.setText("Clock Out");
                busboyStatusLabel.setText("Busboy - Clocked In");
            } else {
                busboyPunchButton.setText("Punch");
                busboyStatusLabel.setText("Busboy - Clocked Out");
            }
        });
        busboyLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));
        // Busboy table click handler (to mark Dirty tables Clean)
        for (int row = 0; row < TableGrid.ROWS; row++) {
            for (int col = 0; col < TableGrid.COLS; col++) {
                Button table = busboyTableGrid.getTableButton(row, col);
                if (table == null) continue;
                int finalRow = row;
                int finalCol = col;
                table.setOnAction(e -> {
                    if ("Dirty".equals(busboyTableGrid.getTableStatus(finalRow, finalCol))) {
                        // Busboy cleans the table, update all grids
                        String tableId = busboyTableGrid.getTableId(finalRow, finalCol);
                        busboyTableGrid.setTableStatusById(tableId, "Clean");
                        waiterTableGrid.setTableStatusById(tableId, "Clean");
                        managerTableGrid.setTableStatusById(tableId, "Clean");
                    }
                });
            }
        }


        // Waiter Punch/Logout Handlers
        waiterPunchButton.setOnAction(e -> {
            if ("Punch".equals(waiterPunchButton.getText())) {
                waiterPunchButton.setText("Clock Out");
                waiterStatusLabel.setText("WAITER - Clocked In");
            } else {
                waiterPunchButton.setText("Punch");
                waiterStatusLabel.setText("WAITER - Clocked Out");
            }
        });
        waiterLogoutButton.setOnAction(e -> primaryStage.setScene(loginScene));


        // Payment Screen Handlers
        cashButton.setOnAction(e -> {
            if (currentOrderForPayment == null) {
                System.err.println("Error: Cash button pressed with no order selected for payment.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "No order selected for cash payment.");
                alert.showAndWait();
                return;
            }
            System.out.println("Processing Cash Payment for " + currentOrderForPayment);
            // Simulate successful payment for now:
            handleSuccessfulPayment(primaryStage, currentUserRole, waiterTableGrid, managerTableGrid, busboyTableGrid);
        });

        cardButton.setOnAction(e -> {
            if (currentOrderForPayment == null) {
                System.err.println("Error: Card button pressed with no order selected for payment.");
                Alert alert = new Alert(Alert.AlertType.ERROR, "No order selected for card payment.");
                alert.showAndWait();
                return;
            }
            System.out.println("Processing Card Payment for " + currentOrderForPayment);
            Alert processingAlert = new Alert(Alert.AlertType.INFORMATION, "Simulating card processing...");
            processingAlert.setTitle("Card Payment");
            processingAlert.setHeaderText(null);
            processingAlert.show();

            // Simulate successful payment for now:
            processingAlert.close(); // Close the "processing" alert
            handleSuccessfulPayment(primaryStage, currentUserRole, waiterTableGrid, managerTableGrid, busboyTableGrid);
            // ---
        });

        paymentBackButton.setOnAction(e -> {
            currentOrderForPayment = null; // Clear the temporary order reference
            primaryStage.setScene(currentUserRole);
        });


        // Apply CSS (Optional - requires Styles.css file in the same directory)
        try {
            String cssPath = getClass().getResource("Styles.css").toExternalForm();
            if (cssPath != null) {
                loginScene.getStylesheets().add(cssPath);
                kitchenScene.getStylesheets().add(cssPath);
                orderScene.getStylesheets().add(cssPath);
                managerScene.getStylesheets().add(cssPath);
                managerMenuScene.getStylesheets().add(cssPath);
                busboyScene.getStylesheets().add(cssPath);
                waiterScene.getStylesheets().add(cssPath);
                paymentScene.getStylesheets().add(cssPath);
                employeeActionsScene.getStylesheets().add(cssPath);
                addNewEmployeeScene.getStylesheets().add(cssPath);

            } else {
                System.err.println("Warning: Styles.css not found. UI will use default styles.");
            }
        } catch (NullPointerException npe) { // Catch if getResource returns null
            System.err.println("Warning: Styles.css not found. UI will use default styles.");
        }

        primaryStage.setScene(loginScene);
        primaryStage.show();

    }


    // --- Helper method for post-payment actions ---
    public void handleSuccessfulPayment(Stage primaryStage, Scene returnScene, TableGrid waiterGrid, TableGrid managerGrid, TableGrid busboyGrid) {
        if (currentOrderForPayment != null) {
            String paidTableId = currentOrderForPayment;
            System.out.println("Payment successful for " + paidTableId);

            orderQueue.remove(currentOrderForPayment);


            // *** Mark the table as 'Dirty' in ALL relevant views ***
            waiterGrid.setTableStatusById(paidTableId, "Dirty");
            managerGrid.setTableStatusById(paidTableId, "Dirty");
            busboyGrid.setTableStatusById(paidTableId, "Dirty");

            // Show a confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Payment Confirmed");
            alert.setHeaderText(null);
            alert.setContentText("Payment for " + paidTableId + " approved!");
            alert.showAndWait(); // Wait for user to close

            currentOrderForPayment = null; // Clear the order reference
            primaryStage.setScene(returnScene); // Return to table view
        } else {
            // Should not happen if payment buttons correctly check currentOrderForPayment
            System.err.println("Error: handleSuccessfulPayment called unexpectedly with no currentOrderForPayment.");
            Alert alert = new Alert(Alert.AlertType.ERROR, "An internal error occurred during payment processing.");
            alert.showAndWait();
            primaryStage.setScene(currentUserRole);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}