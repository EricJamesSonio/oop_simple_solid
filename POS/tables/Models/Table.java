package POS.tables.Models;

public class Table {
    private int id;
    private int capacity;
    private String status; // AVAILABLE, OCCUPIED, DIRTY
    private int people;

    public Table(int id,int capacity,String status,int people){
        this.id=id; this.capacity=capacity; this.status=status; this.people=people;
    }
    public int getId(){return id;}
    public int getCapacity(){return capacity;}
    public String getStatus(){return status;}
    public int getPeople(){return people;}
    public void setStatus(String s){this.status=s;}
    public void setPeople(int p){this.people=p;}
    public String toString(){
        return id+" | cap:"+capacity+" | status:"+status+" | people:"+people;
    }
}
