import java.util.*;

public class BNode {
    private String name;
    private List<String> outcomes;
    private List<BNode> sons;
    private List<BNode> parents;
    private Map<String, Double> cpt;
    private boolean isEvidence;

    public BNode(String name, List<String> outcomes) {
        this.name = name;
        this.outcomes = outcomes;
        this.sons = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.cpt = new HashMap<>();
        this.isEvidence = false;
    }

    public String getName() {
        return name;
    }

    public List<String> getOutcomes() {
        return outcomes;
    }

    public List<BNode> getSons() {
        return sons;
    }

    public List<BNode> getParents() {
        return parents;
    }

    public boolean isEvidence() {
        return isEvidence;
    }

    public void addSon(BNode son){
        sons.add(son);
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

    public void setEvidence(boolean evidence) {
        isEvidence = evidence;
    }

    public Map<String, Double> getProbabilityTable() {
        return cpt;
    }


}
