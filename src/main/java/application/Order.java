public class Order {
    //Direction - Indicates which seat the order is associated with (North, South, East, West, or N/A)
    //Name - Menu item name. Food items are named exactly as they appear on the menu
    //Price - Price of menu item
    //Ticket time - Time when the order was placed (unused at the moment)
    //id/nextId - Gives each Order object a unique id

    private String direction;
    private String name;
    private double price;
    private int id;
    private static int nextId = 0;
    private String ticketTime;

    public Order(String direction, String name, double price){
        this.direction = direction;
        this.name = name;
        this.price = price;
        id = nextId++;
    }

    //Getters and setters for each variable
    public String getDirection(){
        return(direction);
    }

    public String getName(){
        return(name);
    }

    public double getPrice(){
        return(price);
    }

    public int getId(){
        return id;
    }

    public void setDirection(String d){
        direction = d;
    }
    public void setName(String n){
        name = n;
    }

    public void setPrice(double p){
        price = p;
    }

    public String toString(){
        String result = "";
        result += "***" + direction + "***\n";
        result += name + " - " + price + "\n";
        return result;
    }
}
