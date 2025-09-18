package POS.employee.Repository;

import POS.employee.Models.Employee;
import java.util.*;
import java.io.*;
public class EmployeeRepository {
    private String path;
    public EmployeeRepository(String file){ this.path=file; ensure(); }
    private void ensure(){ try{ new File(path).getParentFile().mkdirs(); new File(path).createNewFile(); }catch(Exception e){} }
    public List<Employee> loadAll(){
        List<Employee> out = new ArrayList<>();
        try(BufferedReader r=new BufferedReader(new FileReader(path))){
            String line;
            while((line=r.readLine())!=null && !line.isEmpty()){
                String[] p=line.split("\\|", -1);
                int id = Integer.parseInt(p[0].trim());
                String name = p[1].trim();
                String pos = p[2].trim();
                out.add(new Employee(id, name, pos));
            }
        }catch(Exception e){}
        return out;
    }
    public void saveAll(List<Employee> list){
        try(PrintWriter w=new PrintWriter(new FileWriter(path,false))){
            for(Employee e: list) w.println(e.getId()+"|"+e.getName()+"|"+e.getPosition());
        }catch(Exception e){}
    }
}
