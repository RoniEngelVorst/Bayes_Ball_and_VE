import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BayesBall {


    public static void isIndependent(BayesianNetwork bn, BQuery q){
        Queue<BNode> vars = new LinkedList<>();
        Set<BNode> visited = new HashSet<>();
        //'coloring' the evidences
        for(int i = 0; i<q.getEvidence().size(); i++){
            bn.getNode(q.getEvidence().get(i)).setEvidence(true);
        }

        //inserting start
        vars.add(bn.getNode(q.getVariable1()));
        visited.add(bn.getNode(q.getVariable1()));

        //while the queue is not empty
        while(!vars.isEmpty()){
            BNode curr = vars.remove();
            //if coming from child
            if(curr.isFromChild()){
                if(!curr.isEvidence()){
                    for(int i = 0; i<curr.getParents().size(); i++) {
                        BNode p = curr.getParents().get(i);
                        p.setFromChild(true);
                        if (visited.add(p)) {
                            vars.add(p);
                        }
                    }
                }
            }
            else{
                //if the node is not from child and also an evidence
                if(curr.isEvidence()){
                    for(int i = 0; i<curr.getParents().size(); i++) {
                        BNode p = curr.getParents().get(i);
                        p.setFromChild(true);
                        vars.add(p);
                    }
                }
                //not from child but isn't an evidence
                else{
                    for(int i = 0; i<curr.getSons().size(); i++){
                        BNode s = curr.getSons().get(i);
                        s.setFromChild(false);
                        if(visited.add(s)){
                            vars.add(s);
                        }
                    }
                }
            }
        }
        //end of while loop
        boolean ans = !visited.contains(bn.getNode(q.getVariable2()));
        if(ans == false){
            System.out.println("No");
        }
        else{
            System.out.println("Yes");
        }
    }

}
