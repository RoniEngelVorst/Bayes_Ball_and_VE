import java.util.ArrayList;
import java.util.List;

public class Algorithms {


    public static boolean areIndependent(BayesianNetwork bn, BQuery q){
        for(String e: q.getEvidence()){
            bn.getNode(e).setEvidence(true);
        }
        BNode start = bn.getNode(q.getVariable1());
        BNode end = bn.getNode(q.getVariable2());
        return bayesball(start, end, new ArrayList<>(), null);
    }

    public static boolean bayesball(BNode current, BNode target, ArrayList<BNode> visited, BNode comingFrom){
        if (current.getName() == target.getName()) {
            return true;
        }
        visited.add(current);



        return true;
    }
}
