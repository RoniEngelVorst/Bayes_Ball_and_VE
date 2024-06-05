import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VariableElimination {

    public static void VE(VEQuery q, BayesianNetwork bn){
        BayesianNetwork newBn = adjustedNetwork(q, bn);
        List<BNode> dependents = findDependent(q, newBn);
    }

    //get network without non ancestors
    public static BayesianNetwork adjustedNetwork(VEQuery q, BayesianNetwork bn){
        BayesianNetwork newBn = new BayesianNetwork();
        List<BNode> ancestors = bn.onlyAncestors(q);
        for(int i = 0; i<ancestors.size(); i++){
            newBn.addNode(ancestors.get(i));
        }
        return newBn;
    }

    public static List<BNode> findDependent(VEQuery q, BayesianNetwork bn) {
        List<BNode> dependents = new ArrayList<>();

        // Convert the keys of the given map to a list
        List<String> given = new ArrayList<>(q.getGiven().keySet());

        for (BNode node : bn.getNodeList()) {
            BQuery bQuery = new BQuery(q.getQuery(), node.getName(), given);

            // Check if the node is independent
            String ans = BayesBall.isIndependent(bn, bQuery);
            if (ans.equals("Yes")) {
                dependents.add(node);
            }
        }

        return dependents;
    }
}
