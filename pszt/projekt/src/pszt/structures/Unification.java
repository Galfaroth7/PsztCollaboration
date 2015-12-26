package pszt.structures;

public class Unification {

    public Substitution sigma = new Substitution(), sigmaPrime = new Substitution();

    @Override
    public String toString() {
        return String.format("%s%n%s", sigma.toString(), sigmaPrime.toString());
    }

    public void prepareForNewIteration() {
        sigma.clear();
        sigmaPrime.clear();
    }

    void loadResult(){
        sigma.swap();
        sigmaPrime.swap();
    }
}
