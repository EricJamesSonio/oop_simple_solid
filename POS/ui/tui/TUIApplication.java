package POS.ui.tui;

import java.util.*;
import java.io.*;
import POS.orders.Models.Order;
import POS.tables.Models.Table;
import POS.orders.Services.OrderService;
import POS.tables.Services.TableService;
import POS.ui.UI;
import POS.menu.Services.MenuService;
import POS.reporting.Services.SalesService;
import POS.employee.Services.EmployeeService;
import POS.auth.Services.AuthService;
import POS.menu.Base.MenuItem;
import POS.employee.Models.Employee;

public class TUIApplication implements UI {
    private MenuService menuService;
    private TableService tableService;
    private OrderService orderService;
    private SalesService salesService;
    private EmployeeService employeeService;
    private AuthService authService;

    private Scanner sc = new Scanner(System.in);
    private Employee loggedIn = null;
    private String loggedUsername = null;

    public TUIApplication(MenuService m, TableService t, OrderService o, SalesService s, EmployeeService es, AuthService au) {
        this.menuService = m;
        this.tableService = t;
        this.orderService = o;
        this.salesService = s;
        this.employeeService = es;
        this.authService = au;
    }

    public void run() {
        if (!login()) return;
        if (loggedIn.getPosition().equalsIgnoreCase("ADMIN")) adminDashboard();
        else cashierDashboard();
    }

    private boolean login(){
        int attempts = 0;
        while(attempts<3){
            System.out.print("Username: "); String u = sc.nextLine().trim();
            System.out.print("Password: "); String p = sc.nextLine().trim();
            String empIdStr = authService.authenticate(u,p);
            if (empIdStr!=null){
                int empId = Integer.parseInt(empIdStr);
                Employee emp = employeeService.findById(empId);
                if (emp!=null){
                    loggedIn = emp; loggedUsername = u; System.out.println("Welcome, "+emp.getName()+" ("+emp.getPosition()+")"); pause(); return true;
                } else {
                    System.out.println("Linked employee not found."); return false;
                }
            } else {
                attempts++; System.out.println("Invalid credentials ("+attempts+"/3)"); if (attempts>=3) { System.out.println("Too many attempts."); return false; }
            }
        }
        return false;
    }

    private void adminDashboard() {
        while (true) {
            clear();
            System.out.println("=== ADMIN DASHBOARD ===");
            System.out.println("1) Menu Management\n2) Table Management\n3) Orders\n4) Sales Reports\n5) Employee Management\n0) Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": menuMenu(); break;
                case "2": tableMenu(); break;
                case "3": ordersMenu(); break;
                case "4": salesMenu(true); break;
                case "5": employeeMenu(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void cashierDashboard() {
        while (true) {
            clear();
            System.out.println("=== CASHIER DASHBOARD ===");
            System.out.println("1) Create Order\n2) View My Sales\n3) View My Orders\n4) Table Management\n5) Order Management\n0) Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": createOrder(false); break;
                case "2": salesMenu(false); break;
                case "3": viewMyOrders(); break;
                case "4": tableMenuCashier(); break;
                case "5": cashierOrderMenu(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void tableMenuCashier() {
        while (true) {
            clear();
            System.out.println("--- Table Management ---");
            System.out.println("1) View Tables");
            System.out.println("2) Assign Customer to Table");
            System.out.println("3) Mark Table as Clean");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": viewTables(); break;
                case "2": assignCustomer(); break;
                case "3": cleanTable(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    // Menu Management (same as before)
    private void menuMenu() {
        while (true) {
            clear();
            System.out.println("--- Menu Management ---");
            System.out.println("1) View Menu\n2) Add Item\n3) Remove Item\n4) Update Item\n0) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": viewMenu(); break;
                case "2": addMenuItem(); break;
                case "3": removeMenuItem(); break;
                case "4": updateMenuItem(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void viewMenu() {
        clear();
        System.out.println("Menu Items:");
        for (MenuItem m : menuService.getAll()) {
            System.out.println(m);
        }
        pause();
    }

    private void addMenuItem() {
        try {
            System.out.print("Name: "); String name = sc.nextLine().trim();
            System.out.print("Price: "); double price = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Description: "); String desc = sc.nextLine().trim();
            System.out.print("Expiry (YYYY-MM-DD) or blank: "); String exp = sc.nextLine().trim();
            menuService.add(name, price, desc, exp);
            System.out.println("Added."); pause();
        } catch (Exception e) { System.out.println("Invalid input."); pause(); }
    }

    private void removeMenuItem() {
        viewMenu();
        System.out.print("Enter id to remove: ");
        String id = sc.nextLine().trim();
        if (menuService.remove(Integer.parseInt(id))) System.out.println("Removed."); else System.out.println("Not found.");
        pause();
    }

    private void updateMenuItem() {
        viewMenu();
        System.out.print("Enter id to update: ");
        String id = sc.nextLine().trim();
        MenuItem m = menuService.findById(Integer.parseInt(id));
        if (m==null) { System.out.println("Not found"); pause(); return; }
        System.out.print("New name (blank to keep) ["+m.getName()+"]: "); String name = sc.nextLine().trim();
        System.out.print("New price (blank to keep) ["+m.getPrice()+"]: "); String p = sc.nextLine().trim();
        System.out.print("New description (blank to keep) ["+m.getDescription()+"]: "); String d = sc.nextLine().trim();
        System.out.print("New expiry (YYYY-MM-DD) blank to keep) ["+m.getExpDate()+"]: "); String e = sc.nextLine().trim();
        menuService.update(m.getId(), name.isEmpty() ? null : name, p.isEmpty() ? null : p, d.isEmpty() ? null : d, e.isEmpty() ? null : e);
        System.out.println("Updated."); pause();
    }

    // Table (same as before)
    private void tableMenu() {
        while (true) {
            clear();
            System.out.println("--- Table Management ---");
            System.out.println("1) View Tables\n2) Assign Customer\n3) Clean Table\n4) Remove Table\n5) Add Table\n0) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": viewTables(); break;
                case "2": assignCustomer(); break;
                case "3": cleanTable(); break;
                case "4": removeTable(); break;
                case "5": addTable(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void viewTables() {
        clear();
        for (Table t : tableService.getAll()) {
            System.out.println(t);
        }
        pause();
    }

    private void assignCustomer() {
        viewTables();
        System.out.print("Enter table id to assign: ");
        String id = sc.nextLine().trim();
        Table t = tableService.findById(Integer.parseInt(id));
        if (t == null) { 
            System.out.println("Table not found"); 
            pause(); 
            return; 
        }
        if (!t.getStatus().equals("AVAILABLE")) { 
            System.out.println("Table is not available. Current status: " + t.getStatus()); 
            pause(); 
            return; 
        }
        System.out.print("Number of people: "); 
        int pc = Integer.parseInt(sc.nextLine().trim());
        if (tableService.assign(Integer.parseInt(id), pc)) {
            System.out.println("Table " + id + " assigned successfully!");
        } else {
            System.out.println("Failed to assign table. Please check table capacity.");
        }
        pause();
    }

    private void cleanTable() {
        viewTables();
        System.out.print("Enter table id to clean: ");
        String id = sc.nextLine().trim();
        if (tableService.clean(Integer.parseInt(id))) System.out.println("Cleaned"); else System.out.println("Not dirty or not found");
        pause();
    }

    private void removeTable() {
        viewTables();
        System.out.print("Enter table id to remove: ");
        String id = sc.nextLine().trim();
        if (tableService.remove(Integer.parseInt(id))) System.out.println("Removed"); else System.out.println("Not found");
        pause();
    }

    private void addTable() {
        System.out.print("Capacity: ");
        int c = Integer.parseInt(sc.nextLine().trim());
        tableService.add(c);
        System.out.println("Added"); pause();
    }

    // Orders
    private void ordersMenu() {
        while (true) {
            clear();
            System.out.println("--- Orders ---");
            System.out.println("1) View Orders\n2) Create Order\n3) Checkout Order\n4) Update Order Status\n5) Delete Order\n0) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": viewOrders(); break;
                case "2": createOrder(true); break;
                case "3": checkoutOrder(); break;
                case "4": updateOrder(); break;
                case "5": deleteOrder(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void cashierOrderMenu() {
        while (true) {
            clear();
            System.out.println("--- Order Management (My Orders) ---");
            System.out.println("1) View My Orders\n2) Checkout Order\n3) Update Order Status\n0) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1": viewMyOrders(); break;
                case "2": checkoutMyOrder(); break;
                case "3": updateMyOrder(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void viewMyOrders() {
        clear();
        System.out.println("=== MY ORDERS ===");
        boolean found = false;
        for (Order o : orderService.getAll()) {
            if (o.getCreatedBy().equals(loggedUsername)) {
                System.out.println(o.brief());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No orders found.");
        }
        pause();
    }

    private void checkoutMyOrder() {
        viewMyOrders();
        System.out.print("Enter order id to checkout: ");
        String id = sc.nextLine().trim();
        try {
            Order order = orderService.findById(Integer.parseInt(id));
            if (order != null && order.getCreatedBy().equals(loggedUsername)) {
                if (orderService.checkout(Integer.parseInt(id), loggedUsername)) {
                    System.out.println("Checkout complete.");
                } else {
                    System.out.println("Checkout failed or already complete.");
                }
            } else {
                System.out.println("Order not found or you don't have permission to checkout this order.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID.");
        }
        pause();
    }

    private void updateMyOrder() {
        viewMyOrders();
        System.out.print("Enter order id to update status to REFUNDED: ");
        String id = sc.nextLine().trim();
        try {
            Order order = orderService.findById(Integer.parseInt(id));
            if (order != null && order.getCreatedBy().equals(loggedUsername)) {
                if (orderService.refund(Integer.parseInt(id))) {
                    System.out.println("Order marked as REFUNDED.");
                } else {
                    System.out.println("Failed to update order status.");
                }
            } else {
                System.out.println("Order not found or you don't have permission to update this order.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID.");
        }
        pause();
    }

    private void viewOrders() {
        clear();
        for (Order o : orderService.getAll()) {
            System.out.println(o.brief());
        }
        pause();
    }

    private void createOrder(boolean asAdmin) {
        System.out.println("Is this Walk-in? (y/n): ");
        String w = sc.nextLine().trim();
        boolean isWalkIn = w.equalsIgnoreCase("y");
        if (isWalkIn) {
            System.out.print("Discount? (none/pwd/senior): ");
            String d = sc.nextLine().trim();
            orderService.createWalkIn(d, loggedUsername);
            System.out.println("Walk-in order created and checked out."); pause();
            return;
        } else {
            viewTables();
            System.out.print("Choose table id: ");
            String tid = sc.nextLine().trim();
            Table t = tableService.findById(Integer.parseInt(tid));
            if (t==null || !t.getStatus().equals("OCCUPIED")) { System.out.println("Selected table has no customer."); pause(); return; }
            orderService.createForTable(Integer.parseInt(tid), loggedUsername);
            System.out.println("Order created as pending for table."); pause();
        }
    }

    private void checkoutOrder() {
        viewOrders();
        System.out.print("Enter order id to checkout: ");
        String id = sc.nextLine().trim();
        try {
            Order order = orderService.findById(Integer.parseInt(id));
            if (order != null && order.getCreatedBy().equals(loggedUsername)) {
                if (orderService.checkout(Integer.parseInt(id), loggedUsername)) {
                    System.out.println("Checkout complete.");
                } else {
                    System.out.println("Checkout failed or already complete.");
                }
            } else {
                System.out.println("Order not found or you don't have permission to checkout this order.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID.");
        }
        pause();
    }

    private void updateOrder() {
        viewOrders();
        System.out.print("Enter order id to update status to REFUNDED: ");
        String id = sc.nextLine().trim();
        try {
            Order order = orderService.findById(Integer.parseInt(id));
            if (order != null && order.getCreatedBy().equals(loggedUsername)) {
                if (orderService.refund(Integer.parseInt(id))) {
                    System.out.println("Order marked as REFUNDED.");
                } else {
                    System.out.println("Failed to update order status.");
                }
            } else {
                System.out.println("Order not found or you don't have permission to update this order.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID.");
        }
        pause();
    }

    private void deleteOrder() {
        viewOrders();
        System.out.print("Enter order id to delete: ");
        String id = sc.nextLine().trim();
        if (orderService.remove(Integer.parseInt(id))) System.out.println("Deleted."); else System.out.println("Not found");
        pause();
    }

    // Sales
    private void salesMenu(boolean adminViewAll) {
        clear();
        System.out.println("--- Sales Reports ---");
        System.out.println("1) View All Sales\n2) Daily Sales\n3) Monthly Sales\n4) Yearly Sales\n0) Back");
        System.out.print("Choose: ");
        String c = sc.nextLine().trim();
        switch (c) {
            case "1": if(adminViewAll) { for (String s : salesService.getAll()) System.out.println(s); } else { for (String s: salesService.filterByCashier(loggedUsername)) System.out.println(s); } pause(); break;
            case "2": for (String s : salesService.daily()) System.out.println(s); pause(); break;
            case "3": System.out.print("Month (1-12): "); int m = Integer.parseInt(sc.nextLine().trim());
                      System.out.print("Year: "); int y = Integer.parseInt(sc.nextLine().trim());
                      for (String s : salesService.monthly(m,y)) System.out.println(s); pause(); break;
            case "4": System.out.print("Year: "); int y2 = Integer.parseInt(sc.nextLine().trim());
                      for (String s : salesService.yearly(y2)) System.out.println(s); pause(); break;
            case "0": return;
            default: pause();
        }
    }

    // Employee management
    private void employeeMenu(){
        while(true){
            clear();
            System.out.println("--- Employee Management ---");
            System.out.println("1) View Employees\n2) Add Employee\n3) Remove Employee\n4) Register Employee (create login)\n0) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch(c){
                case "1": viewEmployees(); break;
                case "2": addEmployee(); break;
                case "3": removeEmployee(); break;
                case "4": registerEmployee(); break;
                case "0": return;
                default: pause();
            }
        }
    }

    private void viewEmployees(){ clear(); for(Employee e: employeeService.getAll()) System.out.println(e); pause(); }

    private void addEmployee(){
        System.out.print("Name: "); String name = sc.nextLine().trim();
        System.out.print("Position (ADMIN/CASHIER): "); String pos = sc.nextLine().trim();
        employeeService.add(name, pos.toUpperCase());
        System.out.println("Added employee."); pause();
    }

    private void removeEmployee(){
        viewEmployees();
        System.out.print("Enter employee id to remove: "); int id = Integer.parseInt(sc.nextLine().trim());
        if (employeeService.remove(id)) System.out.println("Removed."); else System.out.println("Not found."); pause();
    }

    private void registerEmployee(){
        viewEmployees();
        System.out.print("Enter employee id to register login for: "); int id = Integer.parseInt(sc.nextLine().trim());
        Employee e = employeeService.findById(id);
        if (e==null){ System.out.println("Not found"); pause(); return; }
        System.out.print("Choose username: "); String u = sc.nextLine().trim();
        System.out.print("Choose password: "); String p = sc.nextLine().trim();
        boolean ok = authService.registerForEmployee(u,p,e.getId());
        if (ok) System.out.println("Registered."); else System.out.println("Username taken."); pause();
    }

    private void pause() {
        System.out.println("Press enter to continue...");
        sc.nextLine();
    }

    private void clear() {
        System.out.println("\n\n");
    }
}
