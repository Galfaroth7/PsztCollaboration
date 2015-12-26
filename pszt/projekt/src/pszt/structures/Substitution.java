package pszt.structures;

import java.util.*;
import java.util.stream.Collectors;

public class Substitution {
    
    private Map<String, Term> currentMap = new HashMap<>();
    private Map<String, Term> archiveMap = new HashMap<>();


    void substitute(Term k, Term v) {
        if( k.type() != TermType.VARIABLE )
            throw new UnsupportedOperationException("Only variables might be substituted.");

        if(archiveMap.isEmpty() == false){
            Substitution s = new Substitution();
            s.substitute(k, v);
            archiveMap.forEach((pk,pv) -> {
                pv.applySubstitution(s);
            });
        }

        archiveMap.put(k.name(), new Term(v));
        currentMap.put(k.name(), new Term(v));
    }
    
    Term getSubstituteOf(Term k) {
        return currentMap.get(k.name());
    }
    Term getSubstituteOf(String k) {
        return currentMap.get(k);
    }

    @Override
    public String toString() {
        return "{" + currentMap.keySet().parallelStream()
                .map(x -> String.format("%s/%s", x, currentMap.get(x).name()))
                .collect(Collectors.joining(",")) + "}";
    }

    void clear(){
        currentMap.clear();
    }

    public void swap() {
        //Map temp = currentMap;
        currentMap = archiveMap;
        //archiveMap = temp;
    }

    public boolean isEmptyOrVarByVar(Set<String> variablesToScan){
        return archiveMap.isEmpty()
                || archiveMap.keySet().stream()
                .filter(variablesToScan::contains)
                .map(archiveMap::get)
                .filter(term -> term.type() != TermType.VARIABLE)
                .count() == 0;
    }
}