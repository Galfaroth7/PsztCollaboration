/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pszt.structures;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Term {

    private String name;
    private TermType type;
    private List<Term> arguments;
    
    public Term(String name, TermType type) {
        this.name = name;
        this.type = type;
    }

    public Term(Term origin){
        this.name = origin.name;
        this.type = origin.type;
        if(type == TermType.FUNCTION){
            arguments = new LinkedList<>();
            origin.arguments.forEach(x -> this.arguments.add(new Term(x)));
        }
    }

    public void addArguments(List<Term> arguments) {
        if(type != TermType.FUNCTION)
            throw new UnsupportedOperationException("Only function term might have arguments.");
        if(this.arguments == null)
            this.arguments = new LinkedList<>();
        this.arguments.addAll(arguments);
    }

    public void subtitute(final Term other, final Unification substitution){

        Procedure thisIsOther = () -> substitution.first.substitute(this, other);
        Procedure otherIsThis = () -> substitution.second.substitute(other, this);

        if(this.equals(other))
            return;
        else if(this.type == TermType.VARIABLE && other.type == TermType.CONSTANT)
            thisIsOther.execute();
        else if(this.type == TermType.CONSTANT && other.type == TermType.VARIABLE)
            otherIsThis.execute();
        else if(this.type == TermType.VARIABLE && other.type == TermType.FUNCTION)
            thisIsOther.execute();
        else if(this.type == TermType.FUNCTION && other.type == TermType.VARIABLE)
            otherIsThis.execute();
        else if (this.type == TermType.VARIABLE && other.type == TermType.VARIABLE)
            thisIsOther.execute();
        else if(this.type == TermType.FUNCTION && other.type == TermType.FUNCTION){
            for (int i = 0; i < this.arguments.size(); i++) {
                this.arguments.get(i).subtitute(other.arguments.get(i), substitution);
            }
        } else if(this.type == TermType.CONSTANT && other.type == TermType.CONSTANT){
            throw new UnificationNotFoundException();
        }
    }

    String getName() {
        return name;
    }
    
    TermType getType() {
        return type;
    }

    @Override
    public String toString() {
        switch(type) {
            case CONSTANT: return "Const: " + name;
            case VARIABLE: return "Var: " + name;
        }
        return "Func: " + name + "(" + arguments.stream().map(Object::toString).collect( Collectors.joining(", ") ) + ")";
    }

    void applySubstitution(Substitution s){
        if(type == TermType.FUNCTION){
            arguments.forEach(a -> a.applySubstitution(s));
        }
        else if(type == TermType.VARIABLE){
            Term t = s.getSubstituteOf(this);
            if(t != null ){
                this.name = String.valueOf(t.name);
                this.type = t.type;
                if(type == TermType.FUNCTION){
                    arguments = new LinkedList<>();
                    t.arguments.forEach(a -> arguments.add(new Term(a)));
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Term))
            return false;
        if(this == obj)
            return true;
        Term t = (Term) obj;
        EqualsBuilder eb = new EqualsBuilder().append(this.name, t.name).append(this.type, t.type);
        if(!eb.build())
            return false;
        if(type == TermType.FUNCTION)
            for (int i = 0; i < arguments.size(); i++) {
                eb.append(this.arguments.get(i), t.arguments.get(i));
            }
        return eb.build();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder().append(name).append(type);
        if(type == TermType.FUNCTION)
            for (int i = 0; i < arguments.size(); i++) {
                hcb.append(this.arguments.get(i));
            }
        return hcb.build();
    }

    public Collection<? extends String> getVariables() {
        Set<String> ret = new HashSet<>();
        if(this.type == TermType.CONSTANT)
            return Collections.EMPTY_SET;
        else if(this.type == TermType.VARIABLE){
            ret.add(this.name);
        }
        else {
            for (Term t: arguments)
                ret.addAll(t.getVariables());
        }
        return ret;
    }
}