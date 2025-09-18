package POS.orders.Models;

import java.time.LocalDateTime;
import java.util.*;
public class Order {
    private int id;
    private Integer tableId; // null if walkin
    private String status; // PENDING, COMPLETE, REFUNDED
    private List<OrderItem> items;
    private double total;
    private LocalDateTime created;
    private String discountType; // none, pwd, senior
    private String createdBy; // username or employee id who created the order

    public Order(int id, Integer tableId, String status, List<OrderItem> items, double total, String discountType, String createdBy){
        this.id=id; this.tableId=tableId; this.status=status; this.items=items; this.total=total; this.created=LocalDateTime.now(); this.discountType=discountType; this.createdBy=createdBy;
    }

    public int getId(){return id;}
    public Integer getTableId(){return tableId;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
    public double getTotal(){return total;}
    public List<OrderItem> getItems(){return items;}
    public String getDiscountType(){return discountType;}
    public String getCreatedBy(){return createdBy;}
    public String brief(){ return "Order:"+id+" | table:"+tableId+" | status:"+status+" | total:"+total+" | by:"+createdBy; }
}
