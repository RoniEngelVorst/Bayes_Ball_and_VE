import java.util.ArrayList;
import java.util.List;

public class BQuery {
    private char variable1;
    private char variable2;
    private List<Character> evidence  = new ArrayList<>();

    public BQuery(char v1, char v2, List<Character> e){
        this.variable1 = v1;
        this.variable2 = v2;
        this.evidence = e;
    }

    public char getVariable1() {
        return variable1;
    }

    public char getVariable2() {
        return variable2;
    }

    public List<Character> getEvidence() {
        return evidence;
    }

    @Override
    public String toString() {
        return "BQuery{" +
                "variable1=" + variable1 +
                ", variable2=" + variable2 +
                ", evidence=" + evidence +
                '}';
    }
}

