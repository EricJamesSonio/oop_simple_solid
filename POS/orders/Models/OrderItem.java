package POS.orders.Models;

public class OrderItem {
    private int menuId;
    private int qty;
    public OrderItem(int menuId,int qty){ this.menuId=menuId; this.qty=qty; }
    public int getMenuId(){return menuId;}
    public int getQty(){return qty;}
    public String toString(){ return menuId+"x"+qty; }
}
