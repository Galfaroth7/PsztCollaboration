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

    public void addPredicate(Predicate p) {
        predicates.add(p);
    }

    public void addPredicates(Collection<Predicate> predicates){
        this.predicates.addAll(predicates);
    }
    
    @Override
    public String toString() {
        return predicates.stream().map(Object::toString)
                .collect(Collectors.joining(" v "));
    }
    
    public List<Predicate> getPredicates() {
        return Collections.unmodifiableList(predicates);
    }

    public List<Clause> performResolution(Clause other){
        List<Clause> resolutionResults = new ArrayList<>(predicates.size());
        renameVariables(other);
        for (Predicate thisPredicateA : this.predicates) {
            for (Predicate otherPredicateB : other.predicates) {
                if(thisPredicateA.isNegationOf(otherPredicateB)){
                    Predicate predicateA = new Predicate(thisPredicateA);
                    Predicate predicateB = new Predicate(otherPredicateB);
                    Unification un = findUnification(predicateA, predicateB);
                    if (un == null) continue;
                    un.switchToArchive();
                    Unification u = un;
                    Clause resolution = new Clause();
                    resolution.addPredicates(preparePredicates(this, u.first, thisPredicateA));
                    resolution.addPredicates(preparePredicates(other, u.second, otherPredicateB));
                    resolutionResults.add(resolution);
                }
            }
        }
        return resolutionResults;
    }

    private List<Predicate> preparePredicates(Clause other, Substitution s, Predicate toSkip2) {
        return other.predicates.stream()
                .filter(p -> !p.equals(toSkip2))
                .map(p -> p.applySubstitution(s))
                .collect(Collectors.toList());
    }

    private Unification findUnification(Predicate thisPredicate, Predicate otherPredicate) {
        Unification un = new Unification();
        try{
            while(!thisPredicate.equals(otherPredicate)){
                thisPredicate.findUnification(otherPredicate, un);
                thisPredicate.applySubstitution(un.first);
                otherPredicate.applySubstitution(un.second);
                un.clear();
            }
        }catch(UnificationNotFoundException e){
            return null;
        }
        return un;
    }

    private void renameVariables(Clause other) {
        Set<String> variables = new HashSet<>();
        Clause source = this.predicates.size() > other.predicates.size() ?
                other : this;
        source.predicates.forEach(p -> variables.addAll(p.getAllVariables()));
        Clause target = source == this? other : this;
        Substitution s = new Substitution();
        target.predicates.forEach(p -> p.renameVariables(variables, s));
    }

}
