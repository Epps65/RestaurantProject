package application;

import java.util.*;

public class BackendMain {
    public static Scanner myScanner = new Scanner(System.in);
    public static HashMap<String, Double> menu = new HashMap<>();
    public static ArrayList<Ticket> tableQueue = new ArrayList<>();

    public static void main(String[] args) {
        //------- Code is made with print statements as a mockup for frontend and is intended to be deleted later ------

        System.out.println("[BACKEND MOCKUP]");

        //tables is a central variable for this program, as it holds Table objects that are operated on
        ArrayList<Table> tables;
        makeMenu(menu);


        try{
            TableManager myReader = new TableManager("tables.csv");
            myReader.loadTables();
            //Reading data from TableData csv file and using it to setup the tables
            //All tables are initialized to clean and their unique Table ID
            //TODO: Initialize tables to active or inactive based on TableData csv

            tables = new ArrayList<>();

            String[] keys = new String[]{"A1", "A2", "A3", "A4", "A5", "A6",
                    "B1", "B2", "B3", "B4", "B5", "B6",
                    "C5", "C6",
                    "D5", "D6",
                    "E1", "E2", "E3", "E4", "E5", "E6",
                    "F1", "F2", "F3", "F4", "F5", "F6"};

            for (String key : keys) {
                boolean[] statuses = myReader.getStatus(key);
                boolean exists = statuses[0];
                boolean[] seats = new boolean[]{statuses[1], statuses[2], statuses[3],statuses[4]};
                if (exists) {
                    tables.add(new Table("Clean", key, seats));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int input;
        do{
            printOptions("main");
            input = myScanner.nextInt();
            //Based on user input, access is waiter, manager, cook, or busboy

            switch (input){
                case 0:
                    waiterAccess(tables);
                    break;
                case 1:
                    managerAccess(tables);
                    break;
                case 2:
                    cookAccess(tables);
                    break;
                case 3:
                    busboyAccess(tables);
                    break;
                case 4:
                    System.out.println("Shutting down...");
                    break;
                default: System.out.println("Invalid credentials.");
            }
        } while(input != 4);
    }

    //Print options is labeled for what options to print
        //main - Main menu options
        //waiter - Waiter options
        //manager - Manager options
        //cook - Cook access
        //busboy - Busboy access
        //food item types

    public static void printOptions(String ID){
        switch(ID){
            case "main":
                System.out.println("Who are you?");
                System.out.println("0. Waiter");
                System.out.println("1. Manager");
                System.out.println("2. Cook");
                System.out.println("3. Busboy");
                System.out.println("4. Quit");
                break;
            case "waiter":
                System.out.println("As the waiter, what would you like to do?");
                System.out.println("0. Check table statuses");
                System.out.println("1. Update table status");
                System.out.println("2. Take table order");
                System.out.println("3. See table orders");
                System.out.println("4. Remove table order");
                System.out.println("5. Quit to menu");
                break;
            case "manager":
                System.out.println("As the manager, what would you like to do?");
                System.out.println("0. Activate table");
                System.out.println("1. Deactivate table");
                System.out.println("2. Quit to menu");
                break;
            case "cook":
                System.out.println("As the cook, what would you like to do?");
                System.out.println("0. View recent tickets");
                System.out.println("1. Clear ticket");
                System.out.println("2. Quit to menu");
                break;
            case "busboy":
                System.out.println("As the busboy, what would you like to do?");
                System.out.println("0. Check dirty tables");
                System.out.println("1. Clean dirty table");
                System.out.println("2. Quit to menu");
                break;
            case "food item types":
                System.out.println("a = Appetizer");
                System.out.println("sd = Salad");
                System.out.println("e = Entree");
                System.out.println("s = Side");
                System.out.println("sw = Sandwich");
                System.out.println("b = Burger");
                System.out.println("bv = Beverage");
                break;
        }
    }

    public static void waiterAccess(ArrayList<Table> tables){
        //Instance of User class is created. User's access is waiter

        User waiter = new User("Waiter", tables);
        System.out.println("[WAITER ACCESS]\n");
        int input;
        String tableID;

        do{
            printOptions("waiter");
            input = myScanner.nextInt();
            switch (input){
                case 0:
                    //Prints table statuses of all tables
                    System.out.println(waiter.getTableStatuses());
                    break;
                case 1:
                    //Based on the table id given by the user, a table's status variable is changed

                    System.out.println("Which table would you like to update?");
                    tableID = myScanner.nextLine();

                    //Makes sure the table the user wants to access is active
                    if(!checkActive(tableID, tables)){
                        System.out.println("This table is inactive.");
                        break;
                    }

                    System.out.println("What status are you setting this table to (clean, dirty, or occupied)?");
                    String newStatus = myScanner.nextLine().toLowerCase();
                    waiter.setTableStatus(tableID, newStatus);
                    System.out.println("Table " + tableID + "'s status has been set.");

                    break;
                case 2:
                    //Take table order
                   boolean repeat = true;

                   //While loop in place to ask the user whether they want to put in another order
                   while(repeat){
                       //Based on the table id given by the user, a table has a new order added to it's tableOrder ArrayList
                       //The user provides...
                            //tableID to access a specific table
                            //direction to determine which seat of the table the order belongs to (north, east, west, south)
                            //foodItemType to determine what type of order is put in place for the table (Appetizer, entree, etc.)
                            //foodItemName for what specific item on the menu to order
                       repeat = false;

                       System.out.println("Which table would you like to add an order to?");
                       tableID = myScanner.nextLine();

                       //Makes sure the table the user wants to access is active
                       if(!checkActive(tableID, tables)){
                           System.out.println("This table is inactive.");
                           break;
                       }

                       System.out.println("Which seat is this order for? (North, South, East, West, or N/A if the order is for the table)");
                       myScanner.nextLine();
                       String direction = myScanner.nextLine();

                       System.out.println("What type of item are you adding to the order?");
                       printOptions("food item types");
                       String foodItemType = myScanner.nextLine().toLowerCase();

                       System.out.println("Which food item are you adding to the order?");
                       //Prints the menu items of the food item type
                            //For example, if the user input appetizer, then only all appetizers from the menu are shown
                       for(String i : menu.keySet()){
                           String[] temp = i.split("-");
                           if(temp[0].equals(foodItemType)){
                               System.out.println(temp[1] + " - " + menu.get(i));
                           }
                       }
                       String foodItemName = myScanner.nextLine();

                       //Add the order based on the parameters to the table's tableOrder ArrayList
                       //Also sends a ticket so that the cook can see the order
                       waiter.addTableOrder(tableID, direction, foodItemName, menu.get(foodItemType+"-"+foodItemName));
                       sendTicket(tableID, foodItemName, menu.get(foodItemType+"-"+foodItemName));
                       System.out.println("Food item added to table.");
                       System.out.println("Do you want to add another? (Y/N)");
                       //If the user wants to add another food item, then the user is prompted ot add another item
                       if(myScanner.nextLine().equalsIgnoreCase("y")){
                           repeat = true;
                       }
                   }
                    break;
                case 3:
                    //Views a specific tables order based on the tableID provided by the user

                    System.out.println("Which table's order would you like to see?");
                    tableID = myScanner.nextLine();
                    if(!checkActive(tableID, tables)){
                        System.out.println("This table is inactive.");
                        break;
                    }
                    System.out.println(waiter.getTableOrders(tableID));

                    break;
                case 4:
                    //Removes a specific tables order based on the tableID provided and the name of the item provided by the user

                    System.out.println("Which table would you like to remove an order from?");
                    tableID = myScanner.nextLine();
                    System.out.println(waiter.getTableOrders(tableID));
                    System.out.println("Which item would you like to remove?");
                    String remove = myScanner.nextLine();

                    waiter.removeTableOrder(tableID, remove);
                    break;
            }
        } while(input != 5);

        printOptions("waiter");

    }

    public static void managerAccess(ArrayList<Table> tables){
        //Instance of User class is created. User's access is manager
        User manager = new User("Manager", tables);
        System.out.println("[MANAGER ACCESS]\n");
        int input;
        String tableID;
        int element;

        do{
            printOptions("manager");
            input = myScanner.nextInt();
            switch(input){
                case 0:
                    //Changes a table's active variable to TRUE if it is FALSE
                    System.out.println("Which table would you like to activate?");
                    tableID = myScanner.nextLine();
                    myScanner.nextLine();
                    if(checkActive(tableID, tables)){
                        System.out.println("That table is already active.");
                    } else {
                        manager.setTableActivity(tableID);
                    }
                    //Use writer
                    break;
                case 1:
                    //Changes a table's active variable to FALSE if it is TRUE
                    System.out.println("Which table would you like to deactivate?");
                    tableID = myScanner.nextLine();
                    manager.setTableActivity(tableID);
                    //Use writer
                    break;
            }
        } while(input != 2);

    }

    public static void cookAccess(ArrayList<Table> tables){

        User cook = new User("Cook", tables);
        System.out.println("[COOK ACCESS]\n");
        int input;

        do{
            printOptions("cook");
            input = myScanner.nextInt();

            switch(input){
                case 0:
                    System.out.println(cook.viewRecentTickets(tableQueue));
                    break;
                case 1:
                    System.out.println(cook.viewRecentTickets(tableQueue));
                    System.out.println("Which item would you like to clear?");
                    String clear = myScanner.nextLine();

                    cook.clearTicket(tableQueue, clear);
            }

        } while(input != 2);
    }

    public static void busboyAccess(ArrayList<Table> tables){
        //Instance of User class is created. User's access is manager
        User busboy = new User("Busboy", tables);
        System.out.println("[BUSBOY ACCESS]\n");
        int input;
        String tableID;
        
        do{
            printOptions("busboy");
            input = myScanner.nextInt();
            switch(input){
                case 0:
                    //Busboy can only see tables that are dirty
                    busboy.getTableStatuses();
                    break;
                case 1:
                    //Busboy sets a table status to clean if the table's status is dirty
                    System.out.println("Which table would you like to clean?");
                    tableID = myScanner.nextLine();
                    if(!checkActive(tableID, tables)){
                        System.out.println("This table is inactive.");
                        break;
                    }
                    busboy.setTableStatus(tableID, "Clean");
                    break;
            }
        } while(input != 2);
    }

    public static void makeMenu(HashMap<String, Double> menu){
        //Hash map for menu items. User will input menu item name -> loop through hash map -> add item to order
            //If order isn't there give an order

        //String format: <itemType>-<name>
        //Double is just the price

        //Appetizers
        menu.put("a-Chicken Nachos", 8.50);
        menu.put("a-Pork Nachos", 8.50);
        menu.put("a-Pork or Chicken Sliders (3x)", 5.00);
        menu.put("a-Catfish Bites", 6.50);
        menu.put("a-Fried Veggies", 6.50);

        //Salads
        menu.put("sd-House Salad", 7.50);
        menu.put("sd-Wedge Salad", 7.50);
        menu.put("sd-Caesar Salad", 7.50);
        menu.put("sd-Sweet Potato Chicken Salad", 11.50);

        //Entrées (if entrée is ordered, ask for 2 sides and a topping)
        menu.put("e-Shrimp & Grits", 13.50);
        menu.put("e-Sweet Tea Fried Chicken", 11.50);
        menu.put("e-Caribbean Chicken", 11.50);
        menu.put("e-Grilled Pork Chops", 11.00);
        menu.put("e-New York Strip Steak", 17.00);
        menu.put("e-Seared Tuna", 15.00);
        menu.put("e-Captain Crunch Chicken Tenders", 11.50);
        menu.put("e-Shock Top Grouper Fingers", 11.50);
        menu.put("e-Mac & Cheese Bar", 8.50);

        //TODO toppings for mac and cheese bar

        //Sides
        menu.put("s-Curly Fries", 2.50);
        menu.put("s-Wing Chips", 2.50);
        menu.put("s-Sweet Potato Fries", 2.50);
        menu.put("s-Creamy Cabbage Slaw", 2.50);
        menu.put("s-Adluh Cheese Grits", 2.50);
        menu.put("s-Mashed Potatoes", 2.50);
        menu.put("s-Mac & Cheese", 2.50);
        menu.put("s-Seasonal Vegetables", 2.50);
        menu.put("s-Baked Beans", 2.50);

        //Sandwiches
        menu.put("sw-Grilled Cheese", 5.50);
        menu.put("sw-Chicken BLT&A", 10.00);
        menu.put("sw-Philly", 13.50);
        menu.put("sw-Club", 10.00);
        menu.put("sw-Meatball Sub", 10.00);

        //Burgers
        menu.put("b-Bacon Cheeseburger", 11.00);
        menu.put("b-Carolina Burger", 11.00);
        menu.put("b-Portobello Burger (V)", 8.50);
        menu.put("b-Vegan Boca Burger (V)", 10.50);

        //Beverages
        menu.put("bv-Sweet Tea", 2.00);
        menu.put("bv-Unsweetened Tea", 2.00);
        menu.put("bv-Coke", 2.00);
        menu.put("bv-Diet Coke", 2.00);
        menu.put("bv-Sprite", 2.00);
        menu.put("bv-Bottled water", 2.00);
        menu.put("bv-Lemonade", 2.00);
        menu.put("bv-Orange Juice", 2.00);
    }

    public static void sendTicket(String tableID, String ticketBody, double ticketCost){
        //Helper method that creates ticket object and adds the ticket to the tableQueue
        Ticket ticket = new Ticket(tableID, ticketBody, ticketCost);
        tableQueue.add(ticket);
    }

    public static boolean checkActive(String tableID, ArrayList<Table> tables){
        //Helper method that checks if a table is active or not
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).getId().equals(tableID)){
                if(!tables.get(i).getActivity()){
                    return false;
                }
            }
        }
        return true;
    }
}