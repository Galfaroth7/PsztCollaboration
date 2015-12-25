package pszt.structures;

import org.junit.Test;
import pszt.parser.Parser;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hejcz on 24.12.2015.
 */
public class UnificationTest {

    @Test
    public void substituteVariableByConstant(){
        Term variable1 = new Term("x", TermType.VARIABLE);
        Term constant1 = new Term("M", TermType.CONSTANT);
        Unification u = new Unification();
        variable1.subtitute(constant1, u);
        assertEquals("{x/M}", u.first.toString());
        assertEquals("{}", u.second.toString());
        constant1.subtitute(variable1, u);
        assertEquals("{x/M}", u.first.toString());
        assertEquals("{x/M}", u.second.toString());
    }

    @Test
    public void clauseConstantVariableUnification(){
        Parser p = new Parser();
        Clause c1 = p.parseClause("A(z) v ~B(z, M)");
        Clause c2 =  p.parseClause("B(N, y) v C(y)");
        c1.performResolution(c2);
    }

    @Test
    public void clauseConstantAndVariableVariableUnification(){
        Parser p = new Parser();
        Clause c1 = p.parseClause("A(z) v ~B(z, M)");
        Clause c2 =  p.parseClause("B(x, y) v C(y)");
        c1.performResolution(c2);
    }

    @Test
    public void harderTest(){
        Parser p = new Parser();
        Clause c1 = p.parseClause("A(x, y) v B(x, M) ");
        Clause c2 =  p.parseClause("~B(F(x), x) v C(x, y)");
        List<Clause> clauses = c1.performResolution(c2);
        assertEquals("Pred: A(Func: F(Const: M), Var: y) v Pred: C(Const: M, Var: var1)",
                clauses.get(0).toString());
    }

    @Test
    public void harderTest2(){
        Parser p = new Parser();
        Clause c1 = p.parseClause("A(x, y) v B(F(x, y), y) ");
        Clause c2 =  p.parseClause("~B(x, M) v C(x, y)");
        List<Clause> clauses = c1.performResolution(c2);
        assertEquals("Pred: A(Var: x, Const: M) v Pred: C(Func: F(Var: x, Const: M), Var: var1)",
                clauses.get(0).toString());
    }

    @Test
    public void prepareToLecture(){
        Parser p = new Parser();
        List<Clause> clauses = p.parseClausesFromFile("resources/test.in");
        for (int i = 0; i < clauses.size(); i+=2) {
            clauses.get(i).performResolution(clauses.get(i+1)).forEach(System.out::println);
        }
    }
}