package POS.tables.Repository;

import POS.tables.Models.Table;
import java.util.*;
import java.io.*;
public class TableRepository {
    private String path;
    public TableRepository(String file){ this.path=file; ensure(); }
    private void ensure(){ try{ new File(path).getParentFile().mkdirs(); new File(path).createNewFile(); }catch(Exception e){} }
    public List<Table> loadAll(){
        List<Table> list = new ArrayList<>();
        try(BufferedReader r=new BufferedReader(new FileReader(path))){
            String line;
            while((line=r.readLine())!=null && !line.isEmpty()){
                String[] p=line.split("\\|",-1);

                if(p.length>=4){
                    int id=Integer.parseInt(p[0].trim());
                    int cap=Integer.parseInt(p[1].trim());
                    String status=p[2].trim();
                    int people=Integer.parseInt(p[3].trim());
                    list.add(new Table(id,cap,status,people));
                }
            }
        }catch(Exception e){}
        return list;
    }
    public void saveAll(List<Table> items){
        try(PrintWriter w=new PrintWriter(new FileWriter(path,false))){
            for(Table t: items) w.println(t.getId()+"\\|"+t.getCapacity()+"\\|"+t.getStatus()+"\\|"+t.getPeople());
        }catch(Exception e){}
    }
}
