package pszt.structures;

import java.util.*;
import java.util.stream.Collectors;

public class Clause implements Comparable<Clause> {
    
    private List<Predicate> predicates = new LinkedList<>();

    public Clause(){}

    public Clause(Clause origin){
        origin.predicates.forEach(p -> this.predicates.add(new Predicate(p)));
    }

    public void add(Predicate predicate) {
        this.predicates.add(predicate);
    }

    public void add(Collection<Predicate> predicates) {
        this.predicates.addAll(predicates);
    }
    
    public List<Predicate> getPredicates() {
        return Collections.unmodifiableList(predicates);
    }

    public List<Clause> performResolution(Clause other){
        List<Clause> result = new ArrayList<>(this.predicates.size());
        renameVariables(other);
        for (Predicate thisPredicate : this.predicates) {
            for (Predicate otherPredicate : other.predicates) {
                if(thisPredicate.isNegated(otherPredicate)){
                    Unification unification = findUnification(new Predicate(thisPredicate), new Predicate(otherPredicate));
                    if (unification == null) continue;
                    Clause resolution = new Clause();
                    resolution.add(adjustPredicates(new Clause(this), unification.sigma, thisPredicate));
                    resolution.add(adjustPredicates(new Clause(other), unification.sigmaPrime, otherPredicate));
                    try{
                        resolution.removeDuplicates();
                        result.add(resolution);
                    }catch (ClauseEvaluatedToTrueException ex){
                        // if the exception was thrown, this clause gives us true
                    }
                }
            }
        }
        return result;
    }

    private void removeDuplicates() {
        Set<Predicate> toRemove = new HashSet<>();
        for(Predicate predicateA: this.predicates){
            for(Predicate predicateB: this.predicates){
                if(predicateA == predicateB
                        || !predicateA.getName().equals(predicateB.getName()))
                    continue;
                if(predicateA.isNegated() != predicateB.isNegated()){
                    if(predicateA.equalsWithoutVariablesName(predicateB))
                        throw new ClauseEvaluatedToTrueException();
                }
                else {
                    Unification unification = findUnification(new Predicate(predicateA), new Predicate(predicateB));
                    if (unification == null) continue;
                    Set<String> bVars = predicateB.getAllVariables();
                    Set<String> aVars = predicateA.getAllVariables();
                    if(unification.sigma.isEmptyOrVarByVar(aVars) && !unification.sigmaPrime.isEmptyOrVarByVar(bVars)){
                        toRemove.add(predicateA);
                    }
                    if(!unification.sigma.isEmptyOrVarByVar(bVars) && unification.sigmaPrime.isEmptyOrVarByVar(aVars)) {
                        toRemove.add(predicateB);
                    }
                }
            }
        }
        this.predicates.removeAll(toRemove);
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
                unification.prepareForNewIteration();
            }
        } catch(UnificationNotFoundException e){
            return null;
        }
        unification.loadResult();
        return unification;
    }

    private void renameVariables(Clause other) {
        Set<String> forbiddenVariables = this.predicates.stream()
                .map( Predicate::getAllVariables )
                .flatMap( Set::stream )
                .collect( Collectors.toSet() );
        Substitution substitution = new Substitution();
        other.predicates.forEach(predicate -> predicate.renameVariables(forbiddenVariables, substitution));
    }

    @Override
    public String toString() {
        return predicates.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" v "));
    }
    @Override
    public int compareTo(Clause o) {
        // TODO Auto-generated method stub
        return predicates.size() - o.getPredicates().size();
    }
}
