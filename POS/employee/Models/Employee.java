package POS.employee.Models;

public class Employee {
    private int id;
    private String name;
    private String position; // ADMIN or CASHIER
    public Employee(int id, String name, String position){
        this.id=id; this.name=name; this.position=position;
    }
    public int getId(){return id;}
    public String getName(){return name;}
    public String getPosition(){return position;}
    public String toString(){ return id+" | "+name+" | "+position; }
}
