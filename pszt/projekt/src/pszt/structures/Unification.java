package pszt.structures;

/**
 * Created by hejcz on 24.12.2015.
 */
public class Unification {

    public Substitution sigma = new Substitution(), sigmaPrime = new Substitution();

    @Override
    public String toString() {
        return String.format("%s%n%s", sigma.toString(), sigmaPrime.toString());
    }

    public void clear() {
        sigma.clear();
        sigmaPrime.clear();
    }

    void loadOverallUnification(){
        sigma.swap();
        sigmaPrime.swap();
    }
}
