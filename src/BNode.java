import java.util.*;

public class BNode {
    private String name;
    private List<String> outcomes;
    private List<BNode> parents;
    private Map<String, Double> cpt;

    public BNode(String name, List<String> outcomes) {
        this.name = name;
        this.outcomes = outcomes;
        this.parents = new ArrayList<>();
        this.cpt = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public List<BNode> getParents() {
        return parents;
    }

    public void addParent(BNode parent) {
        parents.add(parent);
    }

    public void setCPT(String condition, Double probability) {
        cpt.put(condition, probability);
    }

    public Double getProbability(String condition) {
        return cpt.get(condition);
    }

    public Map<String, Double> getProbabilityTable() {
        return cpt;
    }


}
