import java.util.*;

public class BayesBall {


    public static String isIndependent(BayesianNetwork bn, BQuery q){

        Queue<BNode> vars = new LinkedList<>();
        Set<BNode> visited = new HashSet<>();
        //'coloring' the evidences
        for(int i = 0; i<q.getEvidence().size(); i++){
            bn.getNode(q.getEvidence().get(i)).setEvidence(true);
        }

        //inserting start
        vars.add(bn.getNode(q.getVariable1()));
//        bn.getNode(q.getVariable1()).setFromChild(false);
        visited.add(bn.getNode(q.getVariable1()));

        //while the queue is not empty
        while(!vars.isEmpty()){
            BNode curr = vars.remove();
            if(curr.getName().equals(q.getVariable1())){
                for(int i = 0; i<curr.getParents().size(); i++) {
                    BNode p = curr.getParents().get(i);
                    p.setFromChild(true);
//                        if (visited.add(p)) {
//                            vars.add(p);
//                        }
                    visited.add(p);
                    vars.add(p);
                }
                for(int i = 0; i<curr.getSons().size(); i++){
                    BNode s = curr.getSons().get(i);
                    s.setFromChild(false);
                    if(visited.add(s)){
                        vars.add(s);

                    }
                }
            }
            else{
                //if coming from child
                if(curr.isFromChild()){
                    if(!curr.isEvidence()){
                        for(int i = 0; i<curr.getParents().size(); i++) {
                            BNode p = curr.getParents().get(i);
                            p.setFromChild(true);
//                        if (visited.add(p)) {
//                            vars.add(p);
//                        }
                            visited.add(p);
                            vars.add(p);
                        }
                        for(int i = 0; i<curr.getSons().size(); i++){
                            BNode s = curr.getSons().get(i);
                            s.setFromChild(false);
                            if(visited.add(s)){
                                vars.add(s);

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
//                        System.out.println("adding " + p.getName());

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


        }
        //end of while loop
        bn.clearEvidence();
//        bn.clearFromChild();
//        System.out.println("visited: ");
//        printNodeNames(visited);
        boolean ans = !visited.contains(bn.getNode(q.getVariable2()));


        if(ans == false){
            return "no";
        }
        else{
            return "yes";
        }
    }

    public static void printNodeNames(Set<BNode> nodes) {
        for (BNode node : nodes) {
            System.out.println(node.getName());
        }
    }

}
