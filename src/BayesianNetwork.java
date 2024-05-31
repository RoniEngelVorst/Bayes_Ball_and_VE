import java.sql.SQLOutput;
import java.util.*;

class BayesianNetwork {
    private Map<String, BNode> nodes;

    public BayesianNetwork() {
        nodes = new HashMap<>();
    }

    public void addNode(BNode node) {
        nodes.put(node.getName(), node);
    }

    public BNode getNode(String name) {
        return nodes.get(name);
    }
    public void addEdge(String parentName, String childName) {
        BNode parent = nodes.get(parentName);
        BNode child = nodes.get(childName);
        if (parent != null && child != null) {
            child.addParent(parent);
            parent.addSon(child);
        }
    }

    public void setCPT(String nodeName, String condition, double probability) {
        BNode node = nodes.get(nodeName);
        if (node != null) {
            node.setCPT(condition, probability);
        }
    }

    public void printNetwork() {
        for (BNode node : nodes.values()) {
            System.out.println("Node: " + node.getName());
            System.out.println("Outcomes: " + node.getOutcomes());
            System.out.print("Parents: ");
            for (BNode parent : node.getParents()) {
                System.out.print(parent.getName() + " ");
            }
            System.out.print("Sons:");
            for(BNode son : node.getSons()){
                System.out.print(son.getName() + " ");
            }
            System.out.println();
            System.out.println("CPT:");

            List<String> parentNames = new ArrayList<>();
            for (BNode parent : node.getParents()) {
                parentNames.add(parent.getName());
            }

            // Generate all combinations of parent outcomes
            List<String> combinations = generateCombinations(parentNames, node.getOutcomes());
            for (String combination : combinations) {
                System.out.println(" " + combination + ": " + node.getProbability(combination));
            }
            System.out.println();
        }
    }

    private List<String> generateCombinations(List<String> parentNames, List<String> outcomes) {
        if (parentNames.isEmpty()) {
            return outcomes;
        } else {
            List<String> combinations = new ArrayList<>();
            generateCombinationsRecursive(parentNames, outcomes, "", combinations);
            return combinations;
        }
    }

    private void generateCombinationsRecursive(List<String> parentNames, List<String> outcomes, String prefix, List<String> combinations) {
        if (parentNames.isEmpty()) {
            for (String outcome : outcomes) {
                combinations.add(prefix + outcome);
            }
        } else {
            String parentName = parentNames.get(0);
            List<String> remainingParentNames = parentNames.subList(1, parentNames.size());
            for (String parentOutcome : nodes.get(parentName).getOutcomes()) {
                generateCombinationsRecursive(remainingParentNames, outcomes, prefix + parentOutcome + ",", combinations);
            }
        }
    }

}