package POS.auth.Repository;

import java.util.*;
import java.io.*;
public class AuthRepository {
    private String path;
    public AuthRepository(String file){ this.path=file; ensure(); }
    private void ensure(){ try{ new File(path).getParentFile().mkdirs(); new File(path).createNewFile(); }catch(Exception e){} }

    // username|password|employeeId
    public List<String> loadAll(){
        List<String> out = new ArrayList<>();
        try(BufferedReader r=new BufferedReader(new FileReader(path))){
            String line;
            while((line=r.readLine())!=null && !line.isEmpty()) out.add(line.trim());
        }catch(Exception e){}
        return out;
    }

    public void add(String username, String password, int empId){
        try(PrintWriter w=new PrintWriter(new FileWriter(path,true))){
            w.println(username+"|"+password+"|"+empId);
        }catch(Exception e){}
    }

    public void saveAll(List<String> list){
        try(PrintWriter w=new PrintWriter(new FileWriter(path,false))){
            for(String s: list) w.println(s);
        }catch(Exception e){}
    }
}
