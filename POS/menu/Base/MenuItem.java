package POS.menu.Base;

import java.time.LocalDate;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private String description;
    private String expDate; // store as text for simplicity

    public MenuItem(int id, String name, double price, String desc, String expDate) {
        this.id = id; this.name = name; this.price = price; this.description = desc; this.expDate = expDate;
    }
    public int getId(){return id;}
    public String getName(){return name;}
    public double getPrice(){return price;}
    public String getDescription(){return description;}
    public String getExpDate(){return expDate;}
    public void setName(String n){this.name = n;}
    public void setPrice(double p){this.price = p;}
    public void setDescription(String d){this.description = d;}
    public void setExpDate(String e){this.expDate = e;}
    public String toString(){
        return id+" | "+name+" | "+price+" | "+description+" | exp:"+expDate;
    }
}
