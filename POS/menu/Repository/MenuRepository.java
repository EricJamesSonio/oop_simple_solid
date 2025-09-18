package POS.menu.Repository;

import POS.menu.Base.MenuItem;
import java.util.*;
import java.io.*;
public class MenuRepository {
    private String path;
    public MenuRepository(String file) { this.path = file; ensureFile(); }

    private void ensureFile(){
        try { new File(path).getParentFile().mkdirs(); new File(path).createNewFile(); } catch(Exception e){}
    }

    public List<MenuItem> loadAll(){
        List<MenuItem> list = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(path))){
            String line;
            while ((line = r.readLine())!=null && !line.isEmpty()){
                String[] p = line.split("\\|", -1);
                if (p.length>=5){
                    int id = Integer.parseInt(p[0].trim());
                    String name = p[1].trim();
                    double price = Double.parseDouble(p[2].trim());
                    String desc = p[3].trim();
                    String exp = p[4].trim();
                    list.add(new MenuItem(id,name,price,desc,exp));
                }
            }
        } catch(Exception e){}
        return list;
    }

    public void saveAll(List<MenuItem> items){
        try (PrintWriter w = new PrintWriter(new FileWriter(path,false))){
            for (MenuItem m : items){
                w.println(m.getId()+"|"+m.getName()+"|"+m.getPrice()+"|"+m.getDescription()+"|"+m.getExpDate());
            }
        } catch(Exception e){}
    }
}
