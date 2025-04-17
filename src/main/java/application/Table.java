package application;

import java.util.ArrayList;

public class Table {
    //active - Determines whether the table is active and users can access it
    //status - Assigns the table as clean, dirty, or occupied
    //tableOrder - ArrayList of Order objects
    //id - Unique id associated with a table
    private boolean active;
    private String status;
    private ArrayList<Order> tableOrder;
    private String id;
    private boolean hasN; private boolean hasE; private boolean hasS; private boolean hasW;

    public Table(String status, String id, boolean[] seats){
        active = true;
        this.status = status;
        tableOrder = new ArrayList<>();
        this.id = id;
        this.hasN = seats[0];
        this.hasE = seats[1];
        this.hasS = seats[2];
        this.hasW = seats[3];
    }
    //Sets active to the opposite of what it is as of when the method is called
    public void changeActive(){
        active = !active;
    }

    public boolean getActivity(){
        return active;
    }

    public void addTableOrder(String direction, String itemName, double itemPrice){
        if(status.equals("Clean")){
            status = "Occupied";
        }
        String name = itemName.toLowerCase();
        Order temp = new Order(direction, name, itemPrice);
        tableOrder.add(temp);
    }

    public void removeTableOrder(String foodItem){
        String remove = foodItem.toLowerCase();
        for(int i = 0; i < tableOrder.size(); i++){
            if(remove.equals(tableOrder.get(i).getName())){
                tableOrder.remove(i);
                return;
            }
        }
        
        System.out.println("Either that food item does not exist, or this table has no food items.");
    }

    public String getTableOrder(){
        if(tableOrder.isEmpty()){
            return("This table has no orders.");
        }
        String result = "";

        for(int i = 0; i < tableOrder.size(); i++){
            result += tableOrder.get(i).toString();
        }
        return(result);
    }

    public String getStatus(){
        return(status);
    }

    public void setStatus(String newStatus){
        status = newStatus;
    }

    public String getId(){
        return(id);
    }
}
