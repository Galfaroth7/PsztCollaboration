package pszt.structures;

/**
 * Created by hejcz on 24.12.2015.
 */
public class Unification {
    public Substitution first, second;
    Unification(){
        first = new Substitution();
        second = new Substitution();
    }

    @Override
    public String toString() {
        return String.format("%s%n%s", first.toString(), second.toString());
    }

    public void clear() {
        first.clear();
        second.clear();
    }

    void switchToArchive(){
        first.swap();
        second.swap();
    }
}
