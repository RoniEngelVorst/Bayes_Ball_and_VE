import java.util.ArrayList;
import java.util.List;


public class BQuery {
    private String variable1;
    private String variable2;
    private List<String> evidence  = new ArrayList<>();

    public BQuery(String v1, String v2, List<String> e){
        this.variable1 = v1;
        this.variable2 = v2;
        this.evidence = e;
    }

    public String getVariable1() {
        return variable1;
    }

    public String getVariable2() {
        return variable2;
    }

    public List<String> getEvidence() {
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

