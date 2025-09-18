package POS;

import POS.ui.UI;
import POS.ui.tui.TUIApplication;
import POS.ui.gui.GUIApplication;
import POS.menu.Repository.MenuRepository;
import POS.menu.Services.MenuService;
import POS.orders.Repository.OrderRepository;
import POS.orders.Services.OrderService;
import POS.tables.Repository.TableRepository;
import POS.tables.Services.TableService;
import POS.reporting.Repository.SalesRepository;
import POS.reporting.Services.SalesService;
import POS.employee.Repository.EmployeeRepository;
import POS.employee.Services.EmployeeService;
import POS.auth.Repository.AuthRepository;
import POS.auth.Services.AuthService;

public class Main {
    public static void main(String[] args) {
        // Repositories
        MenuRepository menuRepo = new MenuRepository("resources/data/menu.txt");
        TableRepository tableRepo = new TableRepository("resources/data/tables.txt");
        OrderRepository orderRepo = new OrderRepository("resources/data/orders.txt");
        SalesRepository salesRepo = new SalesRepository("resources/data/sales.txt");
        EmployeeRepository empRepo = new EmployeeRepository("resources/data/employees.txt");
        AuthRepository authRepo = new AuthRepository("resources/data/auth.txt");

        // Services
        MenuService menuService = new MenuService(menuRepo);
        TableService tableService = new TableService(tableRepo);
        OrderService orderService = new OrderService(orderRepo, menuService, tableService);
        SalesService salesService = new SalesService(salesRepo);
        EmployeeService employeeService = new EmployeeService(empRepo);
        AuthService authService = new AuthService(authRepo, empRepo);

        // Switch UI here by hard-coding instantiation.
        UI app = new TUIApplication(menuService, tableService, orderService, salesService, employeeService, authService);
        // For GUI use (empty stub) replace the line above with:
        // UI app = new GUIApplication(menuService, tableService, orderService, salesService);

        app.run();
    }
}
