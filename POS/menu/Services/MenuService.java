package POS.menu.Services;

import POS.menu.Base.MenuItem;
import POS.menu.Repository.MenuRepository;
import java.util.*;
public class MenuService {
    private MenuRepository repo;
    private List<MenuItem> cache;
    public MenuService(MenuRepository r){ this.repo = r; reload(); }

    public void reload(){ cache = repo.loadAll(); }
    public List<MenuItem> getAll(){ return new ArrayList<>(cache); }
    public MenuItem findById(int id){ for (MenuItem m: cache) if (m.getId()==id) return m; return null; }

    public void add(String name, double price, String desc, String exp){
        int id = 1;
        for (MenuItem m: cache) if (m.getId()>=id) id = m.getId()+1;
        MenuItem it = new MenuItem(id,name,price,desc,exp);
        cache.add(it); repo.saveAll(cache);
    }

    public boolean remove(int id){
        Iterator<MenuItem> it = cache.iterator();
        while (it.hasNext()){
            if (it.next().getId()==id){ it.remove(); repo.saveAll(cache); return true; }
        }
        return false;
    }

    public void update(int id, String name, String priceStr, String desc, String exp){
        MenuItem m = findById(id);
        if (m==null) return;
        if (name!=null) m.setName(name);
        if (priceStr!=null) try{ m.setPrice(Double.parseDouble(priceStr)); }catch(Exception e){}
        if (desc!=null) m.setDescription(desc);
        if (exp!=null) m.setExpDate(exp);
        repo.saveAll(cache);
    }
}
