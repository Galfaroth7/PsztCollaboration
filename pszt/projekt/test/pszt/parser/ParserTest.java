/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pszt.parser;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;
import pszt.structures.Clause;
import pszt.structures.Predicate;

/**
 *
 * @author hejcz
 */
public class ParserTest {
    
    public ParserTest() {
    }

    @Test
    public void testSomeMethod() {
        String in0 = "PRED(F(x),k,L)";
        Parser p = new Parser();
        Predicate pred = p.parsePredicate(in0);
        Assert.assertEquals("Pred: PRED(Func: F(Var: x), Var: k, Const: L)", pred.toString());
    }
    
    @Test 
    public void someOtherShit() {
        String in0 = "ALA(xin, Gil(yeti, Hipcio(poli)), Ki, Ksi(po, Lok(ola,Ja(klif,Mona))))";
        Parser p = new Parser();
        Predicate pred = p.parsePredicate(in0);
        Assert.assertEquals("Pred: ALA(Var: xin, Func:  Gil(Var: yeti, Func:  Hipcio(Var: poli)), Const:  Ki, " +
                "Func:  Ksi(Var: po, Func:  Lok(Var: ola, Func: Ja(Var: klif, Const: Mona))))", pred.toString());
    }
    
    @Test
    public void clauses(){
        Parser p = new Parser();
        List<Clause> clauses = p.parseClausesFromFile("resources/test.in");
        String res =    "Pred: A(Var: z) v ~ Pred: B(Var: z, Const: M)\n" +
                        "Pred: B(Const: N, Var: y) v Pred: C(Var: y)\n" +
                        "Pred: A(Var: x, Var: y, Var: z) v Pred: B(Func: F(Var: x), Const: M)\n" +
                        "~ Pred: B(Var: x, Var: y) v Pred: C(Var: x, Var: y, Var: z)\n" +
                        "Pred: A(Var: z) v ~ Pred: B(Var: z, Const: M)\n" +
                        "Pred: B(Var: x, Var: y) v Pred: C(Var: y)\n" +
                        "Pred: A(Var: x, Var: y) v Pred: B(Var: x, Const: M)\n" +
                        "~ Pred: B(Func: F(Var: x), Var: x) v Pred: C(Var: x, Var: y)\n" +
                        "Pred: A(Var: x, Var: y) v Pred: B(Var: x, Func: F(Const: M))\n" +
                        "Pred: A(Var: x, Var: y) v ~ Pred: B(Const: M, Var: y)\n" +
                        "Pred: A(Var: x, Var: y, Var: z) v Pred: B(Func: F(Var: y), Const: M)\n" +
                        "~ Pred: B(Var: x, Var: y) v Pred: C(Var: x, Var: y, Var: z)\n" +
                        "Pred: A(Var: x, Func: F(Var: y)) v Pred: B(Var: x, Var: y)\n" +
                        "~ Pred: B(Func: F(Var: x), Const: M) v Pred: C(Var: x, Var: y)\n" +
                        "Pred: A(Var: x, Var: y, Var: z) v Pred: B(Func: F(Var: x), Const: M)\n" +
                        "~ Pred: B(Var: x, Var: y) v Pred: C(Var: x, Var: y, Var: z)\n" +
                        "Pred: A(Var: x, Var: y) v Pred: B(Func: F(Var: x), Var: y)\n" +
                        "~ Pred: B(Var: x, Const: M) v Pred: C(Func: F(Var: x), Var: y)\n" +
                        "Pred: A(Var: x, Var: y) v Pred: B(Func: F(Var: x, Var: y), Var: y)\n" +
                        "~ Pred: B(Var: x, Const: M) v Pred: C(Var: x, Var: y)";
        Assert.assertEquals(res, clauses.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }
    
}
