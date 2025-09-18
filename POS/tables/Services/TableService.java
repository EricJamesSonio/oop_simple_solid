package POS.tables.Services;

import POS.tables.Models.Table;
import POS.tables.Repository.TableRepository;
import java.util.*;
public class TableService {
    private TableRepository repo;
    private List<Table> cache;
    public TableService(TableRepository r){ this.repo=r; reload(); }
    public void reload(){ cache = repo.loadAll(); }
    public List<Table> getAll(){ return new ArrayList<>(cache); }
    public Table findById(int id){ for(Table t: cache) if (t.getId()==id) return t; return null; }
    public void add(int capacity){
        int id=1; for(Table t:cache) if(t.getId()>=id) id=t.getId()+1;
        cache.add(new Table(id,capacity,"AVAILABLE",0)); repo.saveAll(cache);
    }
    public boolean remove(int id){ Iterator<Table> it=cache.iterator(); while(it.hasNext()){ if(it.next().getId()==id){ it.remove(); repo.saveAll(cache); return true; } } return false; }
    public boolean assign(int id,int people){
        Table t = findById(id); if (t==null) return false;
        if (t.getCapacity() < people) return false;
        t.setPeople(people); t.setStatus("OCCUPIED"); repo.saveAll(cache); return true;
    }
    public boolean clean(int id){
        Table t = findById(id); if (t==null) return false;
        if (!t.getStatus().equals("DIRTY")) return false;
        t.setStatus("AVAILABLE"); t.setPeople(0); repo.saveAll(cache); return true;
    }
    public void markDirty(int id){
        Table t=findById(id); if (t!=null){ t.setStatus("DIRTY"); t.setPeople(0); repo.saveAll(cache); }
    }
    public void markAvailable(int id){
        Table t=findById(id); if (t!=null){ t.setStatus("AVAILABLE"); t.setPeople(0); repo.saveAll(cache); }
    }
}
