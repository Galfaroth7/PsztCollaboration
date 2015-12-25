package pszt.structures;

import java.util.*;
import java.util.stream.Collectors;

public class Substitution {
    
    private Map<String, Term> currentMap = new HashMap<>();
    private Map<String, Term> archiveMap = new HashMap<>();


    void substitute(Term k, Term v) {
        if( k.getType() != TermType.VARIABLE )
            throw new UnsupportedOperationException("Only variables might be substituted.");

        if(archiveMap.isEmpty() == false){
            Substitution s = new Substitution();
            s.substitute(k, v);
            archiveMap.forEach((pk,pv) -> {
                pv.applySubstitution(s);
            });
        }

        archiveMap.put(k.getName(), new Term(v));
        currentMap.put(k.getName(), new Term(v));
    }
    
    Term getSubstituteOf(Term k) {
        return currentMap.get(k.getName());
    }
    Term getSubstituteOf(String k) {
        return currentMap.get(k);
    }

    @Override
    public String toString() {
        return "{" + currentMap.keySet().parallelStream()
                .map(x -> String.format("%s/%s", x, currentMap.get(x).getName()))
                .collect(Collectors.joining(",")) + "}";
    }

    void clear(){
        currentMap.clear();
    }

    public void swap() {
        Map temp = currentMap;
        currentMap = archiveMap;
        archiveMap = currentMap;
    }

    /**
     * Mozna zrobic tak, ze przed zastosowaniem podstawienia do reszty klazuli usuwamy wszystkie
     * podstawienia, ktore są za zmienna ktorej nie bylo w poczatkowym predykacie
     * @param list
     */
    public void removeAllInCollection(Collection<String> list){
        Set<String> s = archiveMap.keySet().stream().filter(k -> !list.contains(k)).collect(Collectors.toSet());
        archiveMap.keySet().removeAll(s);
    }
}