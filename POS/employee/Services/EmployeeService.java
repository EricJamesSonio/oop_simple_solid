package POS.employee.Services;

import POS.employee.Models.Employee;
import POS.employee.Repository.EmployeeRepository;
import java.util.*;
public class EmployeeService {
    private EmployeeRepository repo;
    private List<Employee> cache;
    public EmployeeService(EmployeeRepository r){ this.repo=r; reload(); }
    public void reload(){ cache = repo.loadAll(); }
    public List<Employee> getAll(){ return new ArrayList<>(cache); }
    public Employee findById(int id){ for(Employee e: cache) if (e.getId()==id) return e; return null; }
    public void add(String name, String position){
        int id=1; for(Employee e:cache) if (e.getId()>=id) id=e.getId()+1;
        cache.add(new Employee(id,name,position)); repo.saveAll(cache);
    }
    public boolean remove(int id){ Iterator<Employee> it = cache.iterator(); while(it.hasNext()){ if(it.next().getId()==id){ it.remove(); repo.saveAll(cache); return true; } } return false; }
}
