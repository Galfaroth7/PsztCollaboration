/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pszt.structures;

import java.util.*;
import java.util.stream.Collectors;

public class Clause {
    
    List<Predicate> predicates = new LinkedList<>();

    public void addPredicate(Predicate p){
        predicates.add(p);
    }
    
    @Override
    public String toString() {
        return predicates.stream().map(Object::toString)
                .collect(Collectors.joining(" v "));
    }
    
    public List<Predicate> getPredicates(){
        return Collections.unmodifiableList(predicates);
    }

    public List<Clause> performResolution(Clause other){
        List<Clause> resolutionResults = new ArrayList<>(predicates.size());

        // rename variables in one clause
        Set<String> variables = new HashSet<>();
        Clause source = this.predicates.size() > other.predicates.size() ?
                other : this;
        source.predicates.forEach(p -> variables.addAll(p.getAllVariables()));
        Clause target = source == this? other : this;
        Substitution s = new Substitution();
        target.predicates.forEach(p -> p.renameVariables(variables, s));

        for (int i = 0; i < predicates.size(); i++) {
            Predicate thisPredicate = predicates.get(i);
            for (int j = 0; j < predicates.size(); j++) {
                Predicate otherPredicate = other.predicates.get(j);
                if(thisPredicate.isNegationOf(otherPredicate)){

                    // TODO tu musi nastąpić przemianowanie
                    /*Set<String> thisVar = thisPredicate.getAllVariables();
                    Set<String> otherVar = otherPredicate.getAllVariables();*/
                    // if we gonna find unification we copy predicates
                    thisPredicate = new Predicate(thisPredicate);
                    otherPredicate = new Predicate(otherPredicate);

                    Unification un = new Unification();
                    try{
                        while(!thisPredicate.equals(otherPredicate)){
                            thisPredicate.findUnification(otherPredicate, un);
                            thisPredicate.applySubstitution(un.first);
                            otherPredicate.applySubstitution(un.second);
                            un.clear();
                        }
                    }catch(UnificationNotFoundException e){
                        continue;
                    }

                    un.switchToArchive();
                    Unification u = un;
                    /*un.first.removeAllInCollection(thisVar);
                    un.second.removeAllInCollection(otherVar);*/

                    Predicate toSkip = predicates.get(i);
                    final Clause resolution = new Clause();
                    this.predicates.stream()
                            .filter(p -> !p.equals(toSkip))
                            .forEach(p -> {
                                p.applySubstitution(u.first);
                                resolution.addPredicate(new Predicate(p));
                            });
                    Predicate toSkip2 = other.predicates.get(j);
                    other.predicates.stream()
                            .filter(p -> !p.equals(toSkip2))
                            .forEach(p -> {
                                p.applySubstitution(u.second);
                                resolution.addPredicate(new Predicate(p));
                            });
                    resolutionResults.add(resolution);
                }
            }
        }
        return resolutionResults;
    }

}
