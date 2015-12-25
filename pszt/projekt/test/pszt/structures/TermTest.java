/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pszt.structures;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author hejcz
 */
public class TermTest {
    
    public TermTest() {
    }

    @Test(expected = UnsupportedOperationException.class)
    public void creatingAndComparingSimpleTerms() {
        Term v1 = new Term("x", TermType.VARIABLE);
        Term c1 = new Term("X", TermType.CONSTANT);
        Term f1 = new Term("F", TermType.FUNCTION);
        List<Term> arguments = new ArrayList<>();
        arguments.add(v1);
        arguments.add(c1);
        v1.addArguments(arguments);
    }
    
    @Test
    public void constAndVariableSubtitution() {
        Term v1 = new Term("x", TermType.VARIABLE);
        Term c1 = new Term("X", TermType.CONSTANT);
        
    }
    
    @Test
    public void stringForm() {
        Term v1 = new Term("x", TermType.VARIABLE);
        Term c1 = new Term("X", TermType.CONSTANT);
        Term f1 = new Term("F", TermType.FUNCTION);
        List<Term> arguments = new ArrayList<>();
        arguments.add(v1);
        arguments.add(c1);
        f1.addArguments(arguments);
        Assert.assertEquals("Var: x", v1.toString());
        Assert.assertEquals("Const: X", c1.toString());
        Assert.assertEquals("Func: F(Var: x, Const: X)", f1.toString());
    }
}
