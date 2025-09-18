package POS.orders.Services;

import POS.orders.Repository.OrderRepository;
import POS.menu.Services.MenuService;
import POS.tables.Services.TableService;
import POS.orders.Models.Order;
import POS.orders.Models.OrderItem;
import POS.menu.Base.MenuItem;
import java.util.*;
import java.time.*;
import POS.reporting.Repository.SalesRepository;

public class OrderService {
    private OrderRepository repo;
    private MenuService menuService;
    private TableService tableService;
    private List<Order> cache;

    public OrderService(OrderRepository r, MenuService m, TableService t){
        this.repo = r; this.menuService = m; this.tableService = t; reload();
    }
    public void reload(){ cache = repo.loadAll(); }

    public List<Order> getAll(){ return new ArrayList<>(cache); }
    public Order findById(int id){ for(Order o:cache) if(o.getId()==id) return o; return null; }

    private int nextId(){ int id=1; for(Order o:cache) if(o.getId()>=id) id=o.getId()+1; return id; }

    // create walkin and checkout immediately, record cashier username
    public void createWalkIn(String discount, String cashierUsername){
        ScannerWrap sw = new ScannerWrap();
        List<OrderItem> items = new ArrayList<>();
        while(true){
            menuService.reload();
            System.out.println("Menu:");
            for (MenuItem m: menuService.getAll()) System.out.println(m);
            System.out.print("Enter menu id to add (blank to finish): ");
            String s = sw.nextLine();
            if (s.trim().isEmpty()) break;
            int mid = Integer.parseInt(s.trim());
            System.out.print("Qty: ");
            int q = Integer.parseInt(sw.nextLine().trim());
            items.add(new OrderItem(mid,q));
        }
        double total = computeTotal(items, discount);
        Order o = new Order(nextId(), null, "COMPLETE", items, total, discount==null?"none":discount, cashierUsername);
        cache.add(o); repo.saveAll(cache);
        SalesRepository sales = new SalesRepository("resources/data/sales.txt");
        sales.addSaleRecord(o, cashierUsername);
    }

    public void createForTable(int tableId, String cashierUsername){
        menuService.reload();
        ScannerWrap sw = new ScannerWrap();
        List<OrderItem> items = new ArrayList<>();
        while(true){
            System.out.println("Menu:");
            for (MenuItem m: menuService.getAll()) System.out.println(m);
            System.out.print("Enter menu id to add (blank to finish): ");
            String s = sw.nextLine();
            if (s.trim().isEmpty()) break;
            int mid = Integer.parseInt(s.trim());
            System.out.print("Qty: ");
            int q = Integer.parseInt(sw.nextLine().trim());
            items.add(new OrderItem(mid,q));
        }
        double total = computeTotal(items, "none");
        Order o = new Order(nextId(), tableId, "PENDING", items, total, "none", cashierUsername);
        cache.add(o); repo.saveAll(cache);
        tableService.assign(tableId, Math.max(1, items.size()));
    }

    private double computeTotal(List<OrderItem> items, String discount){
        double tot=0;
        for(OrderItem it: items){
            MenuItem m = menuService.findById(it.getMenuId());
            if (m!=null) tot += m.getPrice() * it.getQty();
        }
        if (discount!=null && (discount.equalsIgnoreCase("pwd")||discount.equalsIgnoreCase("senior"))){
            tot = tot * 0.9; // 10% discount
        }
        return Math.round(tot*100.0)/100.0;
    }

    public boolean checkout(int id, String cashierUsername){
        Order o = findById(id);
        if (o==null) return false;
        if (o.getStatus().equals("COMPLETE")) return false;
        o.setStatus("COMPLETE");
        repo.saveAll(cache);
        SalesRepository sales = new SalesRepository("resources/data/sales.txt");
        sales.addSaleRecord(o, cashierUsername);
        if (o.getTableId()!=null) tableService.markDirty(o.getTableId());
        return true;
    }

    public boolean refund(int id){
        Order o = findById(id); if (o==null) return false;
        if (o.getStatus().equals("COMPLETE")) { o.setStatus("REFUNDED"); repo.saveAll(cache); return true; }
        return false;
    }

    public boolean remove(int id){ Iterator<Order> it = cache.iterator(); while(it.hasNext()){ if(it.next().getId()==id){ it.remove(); repo.saveAll(cache); return true; } } return false; }

    static class ScannerWrap {
        private java.util.Scanner sc = new java.util.Scanner(System.in);
        public String nextLine(){ return sc.nextLine(); }
    }
}
