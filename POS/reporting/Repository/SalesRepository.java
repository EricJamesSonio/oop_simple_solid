package POS.reporting.Repository;

import POS.orders.Models.Order;
import java.util.*;
import java.io.*;
import java.time.*;

public class SalesRepository {
    private String path;
    public SalesRepository(String file){ this.path=file; ensure(); }
    private void ensure(){ try{ new File(path).getParentFile().mkdirs(); new File(path).createNewFile(); }catch(Exception e){} }

    // store: orderId|date|total|discount|cashierId
    public void addSaleRecord(Order o, String cashierId){
        try(FileWriter fw = new FileWriter(path,true); PrintWriter w = new PrintWriter(fw)){
            w.println(o.getId()+"|"+LocalDate.now().toString()+"|"+o.getTotal()+"|"+o.getDiscountType()+"|"+(cashierId==null?"unknown":cashierId));
        }catch(Exception e){}
    }

    public List<String> loadAll(){
        List<String> out = new ArrayList<>();
        try(BufferedReader r=new BufferedReader(new FileReader(path))){
            String line;
            while((line=r.readLine())!=null && !line.isEmpty()) out.add(line);
        }catch(Exception e){}
        return out;
    }
}
