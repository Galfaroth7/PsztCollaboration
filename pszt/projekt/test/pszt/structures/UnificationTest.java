package pszt.structures;

import org.junit.Before;
import org.junit.Test;
import pszt.parser.Parser;

import java.util.List;

import static org.junit.Assert.*;

public class UnificationTest {

    Parser parser = new Parser();

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
        Clause c1 = parser.parseClause("A(z) v ~B(z, M)");
        Clause c2 =  parser.parseClause("B(N, y) v C(y)");
        c1.performResolution(c2);
    }

    @Test
    public void clauseConstantAndVariableVariableUnification(){
        Clause c1 = parser.parseClause("A(z) v ~B(z, M)");
        Clause c2 =  parser.parseClause("B(x, y) v C(y)");
        c1.performResolution(c2);
    }

    @Test
    public void harderTest(){
        Clause c1 = parser.parseClause("A(x, y) v B(x, M) ");
        Clause c2 =  parser.parseClause("~B(F(x), x) v C(x, y)");
        List<Clause> clauses = c1.performResolution(c2);
        assertEquals("Pred: A(Func: F(Const: M), Var: y) v Pred: C(Const: M, Var: var1)",
                clauses.get(0).toString());
    }

    @Test
    public void harderTest2(){
        Clause c1 = parser.parseClause("A(x, y) v B(F(x, y), y) ");
        Clause c2 =  parser.parseClause("~B(x, M) v C(x, y)");
        List<Clause> clauses = c1.performResolution(c2);
        assertEquals("Pred: A(Var: x, Const: M) v Pred: C(Func: F(Var: x, Const: M), Var: var1)",
                clauses.get(0).toString());
    }

    @Test
    public void prepareToLecture(){
        List<Clause> clauses = parser.parseClausesFromFile("resources/test.in");
        String expected = "Pred: A(Const: N) v Pred: C(Const: M)\n" +
                "Pred: A(Var: x, Var: y, Var: z) v Pred: C(Func: F(Var: x), Const: M, Var: var2)\n" +
                "Pred: A(Var: x) v Pred: C(Const: M)\n" +
                "Pred: A(Func: F(Const: M), Var: y) v Pred: C(Const: M, Var: var1)\n" +
                "Pred: A(Const: M, Var: y) v Pred: A(Var: var0, Func: F(Const: M))\n" +
                "Pred: A(Var: x, Var: y, Var: z) v Pred: C(Func: F(Var: y), Const: M, Var: var2)\n" +
                "Pred: A(Func: F(Var: var0), Func: F(Const: M)) v Pred: C(Var: var0, Var: var1)\n" +
                "Pred: A(Var: x, Var: y, Var: z) v Pred: C(Func: F(Var: x), Const: M, Var: var2)\n" +
                "Pred: A(Var: x, Const: M) v Pred: C(Func: F(Func: F(Var: x)), Var: var1)\n" +
                "Pred: A(Var: x, Const: M) v Pred: C(Func: F(Var: x, Const: M), Var: var1)";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clauses.size(); i+=2) {
            List<Clause> resolutionResult = clauses.get(i).performResolution(clauses.get(i+1));
            resolutionResult.forEach( c -> result.append(c).append("\n") );
        }
        result.setLength( result.length() - 1 );
        assertEquals(expected, result.toString() );
    }

    @Test
    public void unificationNotPossible(){
        Clause clauseA = parser.parseClause( "A(Fun(M,x),Fun(H,N))" ), clauseB = parser.parseClause("~A(p,p)");
        assertEquals(0, clauseA.performResolution(clauseB).size());
    }

    @Test
    public void resolutionContributesTruth(){
        Clause clauseA = parser.parseClause( "A(Fun(M,x),Fun(y,N))" ), clauseB = parser.parseClause("~A(p, p)");
        assertEquals("", clauseA.performResolution(clauseB).get(0).toString());
    }
}