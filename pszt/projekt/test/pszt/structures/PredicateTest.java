/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pszt.structures;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hejcz
 */
public class PredicateTest {

    /**
     * Test of toString method, of class Predicate.
     */
    @Test
    public void testToString() {
        Term v1 = new Term("x", TermType.VARIABLE);
        Term c1 = new Term("X", TermType.CONSTANT);
        Term f1 = new Term("F", TermType.FUNCTION);
        List<Term> arguments = new ArrayList<>();
        arguments.add(v1);
        arguments.add(c1);
        f1.addArguments(arguments);
        Predicate p = new Predicate("A");
        p.addTerm(v1);
        p.addTerm(c1);
        p.addTerm(f1);
        assertEquals("Pred: A(Var: x, Const: X, Func: F(Var: x, Const: X))", p.toString());
    }

    @Test
    public void predicateNegationRecognizing() throws Exception {
        Predicate p1 = new Predicate("ALA"), p2 = new Predicate("ALA");
        Predicate r1 = new Predicate("ALB"), r2 = new Predicate("ALB");
        p1.isNegated(true);
        assertEquals(true, p1.isNegationOf(p2));
        assertEquals(true, p2.isNegationOf(p1));
        assertEquals(false, p1.isNegationOf(r1));
        assertEquals(false, r1.isNegationOf(p1));
        assertEquals(false, p1.isNegationOf(p1));
        assertEquals(false, r1.isNegationOf(r2));
        assertEquals(false, r2.isNegationOf(r1));
        r2.isNegated(true);
        assertEquals(true, r1.isNegationOf(r2));
        assertEquals(true, r2.isNegationOf(r1));
    }
}
