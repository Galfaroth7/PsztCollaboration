/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pszt.structures;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Term {

    private String name;
    private TermType type;
    private List<Term> arguments;
    
    public Term(String name, TermType type) {
        this.name = name;
        this.type = type;
    }

    public Term(Term origin){
        transformTo(origin);
    }

    String name() {
        return name;
    }

    TermType type() {
        return type;
    }

    public void addArguments(List<Term> arguments) {
        if(type != TermType.FUNCTION)
            throw new UnsupportedOperationException("Only function terms might have arguments.");
        if(this.arguments == null)
            this.arguments = new LinkedList<>();
        this.arguments.addAll(arguments);
    }

    void substitute(final Term other, final Unification unification) {
        final Procedure thisIsOther = () -> unification.sigma.substitute(this, other);
        final Procedure otherIsThis = () -> unification.sigmaPrime.substitute(other, this);

        if (this.equals(other))
            return;
        else if (this.type == TermType.VARIABLE && other.type == TermType.CONSTANT)
            thisIsOther.execute();
        else if (this.type == TermType.CONSTANT && other.type == TermType.VARIABLE)
            otherIsThis.execute();
        else if (this.type == TermType.VARIABLE && other.type == TermType.FUNCTION)
            thisIsOther.execute();
        else if (this.type == TermType.FUNCTION && other.type == TermType.VARIABLE)
            otherIsThis.execute();
        else if (this.type == TermType.VARIABLE && other.type == TermType.VARIABLE)
            thisIsOther.execute();
        else if (this.type == TermType.FUNCTION && other.type == TermType.FUNCTION) {
            for (int i = 0; i < this.arguments.size(); i++) {
                this.arguments.get(i).substitute(other.arguments.get(i), unification);
            }
        } else {
            throw new UnificationNotFoundException();
        }
    }

    void applySubstitution(Substitution substitution){
        switch(this.type){
            case VARIABLE:
                transformTo( substitution.getSubstituteOf(this.name) );
                break;
            case FUNCTION:
                arguments.forEach(argument -> argument.applySubstitution(substitution));
        }
    }

    private void transformTo(Term other){
        if(other == null || this.equals(other))
            return;
        this.name = other.name;
        this.type = other.type;
        if(this.type == TermType.FUNCTION){
            arguments = new LinkedList<>();
            arguments.addAll( other.arguments.stream()
                    .map(Term::new)
                    .collect(Collectors.toList()) );
        }
    }

    public Collection<String> getVariables() {
        switch(this.type){
            case VARIABLE:
                return Stream.of(this.name)
                        .collect(Collectors.toSet());
            case FUNCTION:
                return arguments.stream()
                        .map(Term::getVariables)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
            default:
                return Collections.EMPTY_SET;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        Term other = (Term) obj;
        EqualsBuilder builder = new EqualsBuilder()
                .append(this.name, other.name)
                .append(this.type, other.type);
        if(!builder.isEquals())
            return false;
        if(this.type == TermType.FUNCTION)
            builder.append(this.arguments.toArray(), other.arguments.toArray());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 19)
                .append(name)
                .append(type);
        if(type == TermType.FUNCTION)
            builder.append(this.arguments.toArray());
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        switch(type) {
            case CONSTANT: return "Const: " + name;
            case VARIABLE: return "Var: " + name;
        }
        return "Func: " + name + "(" + arguments.stream().map(Object::toString).collect( Collectors.joining(", ") ) + ")";
    }
}