package pszt.structures;

import org.junit.Test;

import java.util.List;

/**
 * Created by hejcz on 25.12.2015.
 */
public class ClauseTest {

    @Test(expected = UnsupportedOperationException.class)
    public void addingPredicatesToClausesPredicatesListObtainedByGetter() throws Exception {
        Clause clause = new Clause();
        Predicate predicateA = new Predicate("A");
        clause.add(new Predicate("A"));
        clause.add(new Predicate("B"));
        List<Predicate> predicates = clause.getPredicates();
        predicates.add(new Predicate("C"));
    }

}