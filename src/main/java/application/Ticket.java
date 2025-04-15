public class Ticket {
    //tableID - id of the table that this ticket belongs to
    //Body - Content of ticket
    //Cost - Total price of all items in the ticket
    //id/nextId - Gives each Ticket object a unique id

    private String tableID;
    private String body;
    private double cost;
    private static int nextId;
    private int id;

    public Ticket(String tableID, String body, double cost){
        this.tableID = tableID;
        this.body = body;
        this.cost = cost;
        id = nextId++;
    }

    public String toString(){
        String result = "";
        result += "Table: " + tableID + "\n";
        result += body + "\n";
        result += "Total: " + cost;

        return result;
    }

    public String getBody(){
        return body;
    }
}
