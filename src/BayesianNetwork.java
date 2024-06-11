import java.util.*;

class BayesianNetwork {
    private Map<String, BNode> nodes;
    private List<BNode> nodeList;


    public BayesianNetwork() {
        nodes = new HashMap<>();
        nodeList = new ArrayList<>();
    }

    public void addNode(BNode node) {
        nodes.put(node.getName(), node);
        nodeList.add(node);
    }

    public List<BNode> getNodeList(){
        return nodeList;
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

    //helper function mainly for debugging
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
            node.printCptTable();
        }
    }

    public void clearEvidence(){
        for(int i = 0; i<this.nodeList.size(); i++){
            this.nodeList.get(i).setEvidence(false);
        }
    }

    //helper function for variable elimination. return only the ancestors of query and evidence.
    public List<BNode> onlyAncestors(VEQuery q){
        Set<BNode> ancestors = new HashSet<>();
        BNode query = this.getNode(q.getQuery());
        for(Map.Entry<String, String> entry : q.getGiven().entrySet()){
            String key = entry.getKey();
            ancestors.add(this.getNode(key));
            ancestors.addAll(this.getNode(key).getAncestors());

        }
        ancestors.addAll(query.getAncestors());
        ancestors.add(query);
        return ancestors.stream().toList();
    }



}