package POS.reporting.Services;

import POS.reporting.Repository.SalesRepository;
import java.util.*;
import java.time.*;
public class SalesService {
    private SalesRepository repo;
    public SalesService(SalesRepository r){ this.repo=r; }

    public List<String> getAll(){ return repo.loadAll(); }

    public List<String> daily(){
        List<String> all = repo.loadAll(); List<String> out = new ArrayList<>();
        String today = LocalDate.now().toString();
        for(String s: all) if (s.contains("|"+today+"|")) out.add(s);
        return out;
    }

    public List<String> monthly(int month, int year){
        List<String> all = repo.loadAll(); List<String> out = new ArrayList<>();
        for(String s: all){
            String[] p = s.split("\\|", -1);
            if (p.length<2) continue;
            LocalDate d = LocalDate.parse(p[1]);
            if (d.getMonthValue()==month && d.getYear()==year) out.add(s);
        }
        return out;
    }

    public List<String> yearly(int year){
        List<String> all = repo.loadAll(); List<String> out = new ArrayList<>();
        for(String s: all){
            String[] p = s.split("\\|", -1);
            if (p.length<2) continue;
            LocalDate d = LocalDate.parse(p[1]);
            if (d.getYear()==year) out.add(s);
        }
        return out;
    }

    public List<String> filterByCashier(String cashierId){
        List<String> all = repo.loadAll(); List<String> out = new ArrayList<>();
        for(String s: all){
            String[] p = s.split("\\|", -1);
            if (p.length>=5 && p[4].equals(cashierId)) out.add(s);
        }
        return out;
    }
}
