import java.util.*;

public class BNode {
    private String name;
    private List<String> outcomes;
    private List<BNode> sons;
    private List<BNode> parents;
    private Map<String, Double> cpt;
    private Map<Map<String, String>, Double> cptTable = new HashMap<>();
    private boolean isEvidence;
    private boolean isFromChild;

    public BNode(String name, List<String> outcomes) {
        this.name = name;
        this.outcomes = outcomes;
        this.sons = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.cpt = new HashMap<>();
        this.isEvidence = false;
        this.isFromChild = false;
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

    public Map<String, BNode> getVars(){
        Map<String, BNode> vars = new HashMap<>();
        vars.put(this.getName(), this);
        for(BNode p : this.getParents()){
            vars.put(p.getName(), p);
        }
        return vars;
    }

    public boolean isFromChild() {
        return isFromChild;
    }

    public void setFromChild(boolean fromChild) {
        isFromChild = fromChild;
    }

    public boolean isEvidence() {
        return isEvidence;
    }

    public void addSon(BNode son) {
        sons.add(son);
    }

    public void addParent(BNode parent) {
        parents.add(parent);
    }


    public void setEvidence(boolean evidence) {
        isEvidence = evidence;
    }

    // Method to get all ancestors
    public Set<BNode> getAncestors() {
        Set<BNode> ancestors = new HashSet<>();
        collectAncestors(this, ancestors);
        return ancestors;
    }

    private void collectAncestors(BNode node, Set<BNode> ancestors) {
        for (BNode parent : node.getParents()) {
            if (ancestors.add(parent)) { // If the parent was added, continue to collect its ancestors
                collectAncestors(parent, ancestors);
            }
        }
    }


    public void createCptTable(List<BNode> variables, List<Double> probs) {
        List<String> possibleOutcomes = new ArrayList<>();
        createCptTableHelper(variables, probs, possibleOutcomes, 0);
    }

    private int createCptTableHelper(List<BNode> variables, List<Double> probs, List<String> possibleOutcomes, int index) {
        if (possibleOutcomes.size() == variables.size()) {
            Map<String, String> key = new HashMap<>();
            for (int i = 0; i < variables.size(); i++) {
                key.put(variables.get(i).getName(), possibleOutcomes.get(i));
            }
            this.cptTable.put(key, probs.get(index));
            return index + 1;
        }

        BNode var = variables.get(possibleOutcomes.size());
        for (String outcome : var.getOutcomes()) {
            possibleOutcomes.add(outcome);
            index = createCptTableHelper(variables, probs, possibleOutcomes, index);
            possibleOutcomes.remove(possibleOutcomes.size() - 1);
        }
        return index;
    }


    public Map<Map<String, String>, Double> getCptTable() {
        return cptTable;
    }

    public void printCptTable() {
        List<Map<String, String>> sortedKeys = sortedKeys(cptTable.keySet());
        for (Map<String, String> condition : sortedKeys) {
            StringBuilder conditionStr = new StringBuilder();
            for (Map.Entry<String, String> entry : condition.entrySet()) {
                conditionStr.append(entry.getKey()).append("=").append(entry.getValue()).append(", ");
            }
            conditionStr.delete(conditionStr.length() - 2, conditionStr.length()); // Remove trailing comma and space
            System.out.println("Condition: {" + conditionStr + "} -> Probability: " + cptTable.get(condition));
        }
    }

    private List<Map<String, String>> sortedKeys(Set<Map<String, String>> keys) {
        List<Map<String, String>> sortedKeys = new ArrayList<>(keys);
        sortedKeys.sort(new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                List<String> keys1 = new ArrayList<>(o1.keySet());
                List<String> keys2 = new ArrayList<>(o2.keySet());
                Collections.sort(keys1);
                Collections.sort(keys2);
                for (int i = 0; i < keys1.size(); i++) {
                    String key1 = keys1.get(i);
                    String key2 = keys2.get(i);
                    int result = key1.compareTo(key2);
                    if (result != 0) {
                        return result;
                    }
                    result = o1.get(key1).compareTo(o2.get(key2));
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        });
        return sortedKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BNode other = (BNode) o;
        return Objects.equals(name, other.name) &&
                Objects.equals(outcomes, other.outcomes) &&
                Objects.equals(cpt, other.cpt); // Include other fields if necessary
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, outcomes, cpt); // Include other fields if necessary
    }
}

