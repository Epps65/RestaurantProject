package application;

import java.util.*;

public class User {
    //access - Assigns the user to either waiter, manager, cook, or busboy
        //The user object's permissions alters depending on this variable
    //tables - ArrayList of table objects initialized in main
    private String access;
    private ArrayList<Table> tables;

    public User(String access, ArrayList<Table> tables){
        this.tables = tables;
        this.access = access;
    }

    //Returns a formatted string of either...
        //All tables statuses if the user is waiter or manager
        //Only all dirty tables if the user is busboy
    public String getTableStatuses(){
        String result = "";
        if(access.equals("Waiter") || access.equals("Manager")){
            for(int i = 0; i < tables.size(); i++){
                String status = tables.get(i).getStatus();
                if(tables.get(i).getActivity()){
                    result += "Table " + tables.get(i).getId() + " - " + status + "\n";
                }
            }
        } else if(access.equals("Busboy")){
            for(int i = 0; i < tables.size(); i++){
                if(tables.get(i).getStatus().equals("d")){
                    result += "Table " + tables.get(i).getId() + " needs to be cleaned." + "\n";
                }
            }
        }
        return result;
    }

    //Method that sets table tableID's status to newStatus
        //Can be accessed by waiter, manager, and busboy
        //Busboy will only ever access this method if newStatus is clean, and table tableID's status is dirty as of when this method is called
    public void setTableStatus(String tableID, String newStatus){
        if(access.equals("Waiter") || access.equals("Manager") || access.equals("Busboy")){
            for(int i = 0; i < tables.size(); i++){
                if(tables.get(i).getId().equals(tableID)){
                    tables.get(i).setStatus(newStatus);
                }
            }

        }
    }

    //Method that adds an order to table tableID based on the parameters passed in
        //Only accessed by waiter and manager
    public void addTableOrder(String tableID, String direction, String itemName, double itemPrice){
        if(access.equals("Manager") || access.equals("Waiter")){
            for(int i = 0; i < tables.size(); i++){
                if(tables.get(i).getId().equals(tableID)){
                    tables.get(i).addTableOrder(direction, itemName, itemPrice);
                }
            }
        }
    }

    //Method that returns table tableID's orders
        //Only accessed by waiter and manager
    public String getTableOrders(String tableID){
        if(!access.equals("Manager") && !access.equals("Waiter")){
            return("Invalid access.");
        }

        String result = "";
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).getId().equals(tableID)){
                result = tables.get(i).getTableOrder();
            }
        }
        return result;
    }

    //Method that removes foodItem from table tableID
        //Only accessed by manager and waiter
    public void removeTableOrder(String tableID, String foodItem){
        if(access.equals("Manager") || access.equals("Waiter")){
            for(int i = 0; i < tables.size(); i++){
                if(tables.get(i).getId().equals(tableID)){
                    if(!tables.get(i).getTableOrder().equals(foodItem)){
                        System.out.println("This food item does not exist.");
                        return;
                    }
                    tables.get(i).removeTableOrder(foodItem);
                }
            }
        }
    }

    //Method that sets the table tableID's active variable to true or false. Only accessed by the manager
    public void setTableActivity(String tableID){
        if(access.equals("Manager")){
            for(int i = 0; i < tables.size(); i++){
                if(tables.get(i).getId().equals(tableID)){
                    tables.get(i).changeActive();
                    char col = tableID.charAt(0);
                    int row = Integer.parseInt(tableID.substring(1));
                }
            }

        }
    }

    public String viewRecentTickets(ArrayList<Ticket> tableQueue){
        if(access.equals("Cook") || access.equals("Manager")){
            String result = "";
            if(tableQueue.isEmpty()){
                return("There are no tickets in queue.");
            } else {
                int count = 1;

                for(Ticket t : tableQueue){
                    result += t.toString() + "\n";
                    count++;
                    if(count == 3){
                        break;
                    }
                }
                return result;
            }
        }
        return "Invalid access.";
    }

    public void clearTicket(ArrayList<Ticket> tableQueue, String clear){
        for(int i = 0; i < tableQueue.size(); i++){
            if(tableQueue.get(i).getBody().equals(clear)){
                tableQueue.remove(i);
                break;
            }
        }
    }
}
