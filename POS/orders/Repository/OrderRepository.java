package POS.orders.Repository;

import POS.orders.Models.Order;
import POS.orders.Models.OrderItem;
import java.util.*;
import java.io.*;
import java.time.*;
public class OrderRepository {
    private String path;
    public OrderRepository(String file){ this.path=file; ensure(); }
    private void ensure(){ try{ new File(path).getParentFile().mkdirs(); new File(path).createNewFile(); }catch(Exception e){} }

    public List<Order> loadAll(){
        List<Order> list = new ArrayList<>();
        try(BufferedReader r=new BufferedReader(new FileReader(path))){
            String line;
            while((line=r.readLine())!=null && !line.isEmpty()){
                // id|tableId|null|status|total|discount|items(semi)|createdBy
                String[] p=line.split("\\|",-1);

                int id=Integer.parseInt(p[0].trim());
                Integer tableId = p[1].trim().equals("null")?null:Integer.parseInt(p[1].trim());
                String status=p[2].trim();
                double total=Double.parseDouble(p[3].trim());
                String discount=p[4].trim();
                String itemsStr = p.length>5?p[5].trim() : "";
                String createdBy = p.length>6?p[6].trim():"unknown";
                List<OrderItem> items = new ArrayList<>();
                if (!itemsStr.isEmpty()){
                    String[] its = itemsStr.split(";", -1);
                    for(String it: its){
                        if (it.trim().isEmpty()) continue;
                        String[] parts = it.split(":", -1);
                        items.add(new OrderItem(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
                    }
                }
                list.add(new Order(id,tableId,status,items,total,discount,createdBy));
            }
        }catch(Exception e){}
        return list;
    }

    public void saveAll(List<Order> list){
        try(PrintWriter w=new PrintWriter(new FileWriter(path,false))){
            for(Order o: list){
                String table = o.getTableId()==null?"null":o.getTableId().toString();
                StringBuilder sb=new StringBuilder();
                for(int i=0;i<o.getItems().size();i++){
                    sb.append(o.getItems().get(i).getMenuId()).append(":").append(o.getItems().get(i).getQty());
                    if (i<o.getItems().size()-1) sb.append(";");
                }
                w.println(o.getId()+"|"+table+"|"+o.getStatus()+"|"+o.getTotal()+"|"+o.getDiscountType()+"|"+sb.toString()+"|"+o.getCreatedBy());
            }
        }catch(Exception e){}
    }
}
