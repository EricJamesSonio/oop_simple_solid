package POS.auth.Services;

import POS.auth.Repository.AuthRepository;
import POS.employee.Repository.EmployeeRepository;
import POS.employee.Models.Employee;
import java.util.*;

public class AuthService {
    private AuthRepository repo;
    private EmployeeRepository empRepo;
    public AuthService(AuthRepository r, EmployeeRepository er){ this.repo=r; this.empRepo=er; }

    public String authenticate(String username, String password){
        List<String> all = repo.loadAll();
        for(String l: all){
            String[] p = l.split("\\|", -1);
            if (p.length>=3){
                if (p[0].equals(username) && p[1].equals(password)){
                    return p[2]; // employee id as string
                }
            }
        }
        return null;
    }

    public boolean registerForEmployee(String username, String password, int empId){
        // simple check for existing username
        List<String> all = repo.loadAll();
        for(String l: all){ String[] p = l.split("\\|", -1); if (p.length>0 && p[0].equals(username)) return false; }
        repo.add(username, password, empId); return true;
    }
}
