/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pszt.structures;

import java.util.*;
import java.util.stream.Collectors;

public class Predicate {

    private final String name;
    private final List<Term> terms;
    private boolean isNegated;

    public Predicate(String name) {
        this.name = name;
        this.terms = new LinkedList<>();
    }

    public Predicate(Predicate origin){
        this.name = origin.name;
        this.isNegated = origin.isNegated();
        this.terms = new LinkedList<>();
        origin.terms.forEach(x -> terms.add(new Term(x)));
    }

    public String getName(){
        return name;
    }

    public void addTerm(Term t) {
        terms.add(t);
    }
    
    public void addTerms(List<Term> terms){
        this.terms.addAll(terms);
    }
    
    public void isNegated(boolean value) {
        this.isNegated = value;
    }
    
    public boolean isNegated() {
        return this.isNegated;
    }

    public boolean isNegated(Predicate other){
        return this.isNegated != other.isNegated
            && this.name.equals(other.name);
    }

    boolean equals(Predicate other){
        boolean ret = this.name.equals(other.name);
        if(!ret)
            return false;
        for (int i = 0; i < terms.size(); i++) {
            if(!terms.get(i).equals(other.terms.get(i)))
                return false;
        }
        return ret;
    }

    boolean equalsWithoutVariablesName(Predicate other){
        boolean ret = this.name.equals(other.name);
        if(!ret)
            return false;
        for (int i = 0; i < terms.size(); i++) {
            if(!terms.get(i).equalsWithoutVariablesName(other.terms.get(i)))
                return false;
        }
        return ret;
    }

    Predicate applySubstitution(Substitution substitution){
        terms.forEach(t -> t.applySubstitution(substitution));
        return this;
    }

    void tryToUnify(Predicate other, Unification u){
        for (int i = 0; i < terms.size(); i++) {
            terms.get(i).substitute(other.terms.get(i), u);
        }
    }

    Set<String> getAllVariables(){
        Set<String> ret = new HashSet<>();
        for(Term t: terms){
            ret.addAll(t.getVariables());
        }
        return ret;
    }

    void renameVariables(Set<String> forbiddenNames, final Substitution s){

        this.getAllVariables().stream()
                .filter(forbiddenNames::contains)
                .forEach(p -> {
                    int index = 0;
                    if(s.getSubstituteOf(p) == null){
                        while(forbiddenNames.contains("var" + index++));
                        String newName = "var" + (index - 1);
                        s.substitute(new Term(p, TermType.VARIABLE)
                                , new Term(newName, TermType.VARIABLE));
                        forbiddenNames.add(newName);
                    }
                });
        this.applySubstitution(s);

    }

    @Override
    public String toString() {
        return String.format("%sPred: %s(%s)", isNegated?"~ ":"",name, terms.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
    
}
