/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pszt.structures;

import java.util.*;
import java.util.stream.Collectors;

public class Clause {
    
    private List<Predicate> predicates = new LinkedList<>();

    public Clause(){

    }

    Clause(Clause origin){
        origin.predicates.forEach(p -> this.predicates.add(new Predicate(p)));
    }

    public void addPredicate(Predicate p) {
        predicates.add(p);
    }

    public void addPredicates(Collection<Predicate> predicates) {
        this.predicates.addAll(predicates);
    }
    
    public List<Predicate> getPredicates() {
        return Collections.unmodifiableList(predicates);
    }

    public List<Clause> performResolution(Clause other){
        List<Clause> result = new ArrayList<>(predicates.size());
        renameVariables(other);
        for (Predicate thisPredicate : this.predicates) {
            for (Predicate otherPredicate : other.predicates) {
                if(thisPredicate.isNegationOf(otherPredicate)){
                    Unification unification = findUnification(new Predicate(thisPredicate), new Predicate(otherPredicate));
                    if (unification == null) continue;
                    Clause resolution = new Clause();
                    resolution.addPredicates(adjustPredicates(new Clause(this), unification.sigma, thisPredicate));
                    resolution.addPredicates(adjustPredicates(new Clause(other), unification.sigmaPrime, otherPredicate));
                    result.add(resolution);
                }
            }
        }
        return result;
    }

    private List<Predicate> adjustPredicates(Clause source, Substitution substitution, Predicate exclude) {
        return source.predicates.stream()
                .filter(p -> !p.equals(exclude))
                .map(p -> p.applySubstitution(substitution))
                .collect(Collectors.toList());
    }

    private Unification findUnification(final Predicate copyA, final Predicate copyB) {
        Unification unification = new Unification();
        try{
            while(!copyA.equals(copyB)){
                copyA.tryToUnify(copyB, unification);
                copyA.applySubstitution(unification.sigma);
                copyB.applySubstitution(unification.sigmaPrime);
                unification.clear();
            }
        }catch(UnificationNotFoundException e){
            return null;
        }
        unification.loadOverallUnification();
        return unification;
    }

    private void renameVariables(Clause other) {
        Set<String> variables = new HashSet<>();
        this.predicates.forEach(p -> variables.addAll(p.getAllVariables()));
        Substitution s = new Substitution();
        other.predicates.forEach(p -> p.renameVariables(variables, s));
    }


    @Override
    public String toString() {
        return predicates.stream().map(Object::toString)
                .collect(Collectors.joining(" v "));
    }
}
