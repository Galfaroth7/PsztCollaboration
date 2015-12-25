package pszt.structures;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hejcz on 25.12.2015.
 */
public class ClauseTest {

    @Test(expected = UnsupportedOperationException.class)
    public void addingPredicatesToClausesPredicatesListObtainedByGetter() throws Exception {
        Clause clause = new Clause();
        Predicate predicateA = new Predicate("A");
        clause.addPredicate(new Predicate("A"));
        clause.addPredicate(new Predicate("B"));
        List<Predicate> predicates = clause.getPredicates();
        predicates.add(new Predicate("C"));
    }

}