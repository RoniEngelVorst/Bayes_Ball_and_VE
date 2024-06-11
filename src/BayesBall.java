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
        visited.add(bn.getNode(q.getVariable1()));

        //while the queue is not empty
        while(!vars.isEmpty()){
            BNode curr = vars.remove();

            //the first variable can go anywhere
            if(curr.getName().equals(q.getVariable1())){
                //iterating over all the parents and adding them to visited and vars
                for(int i = 0; i<curr.getParents().size(); i++) {
                    BNode p = curr.getParents().get(i);
                    p.setFromChild(true);
                    visited.add(p);
                    vars.add(p);
                }
                //iterating over all the sons and adding them to visited and vars.
                for(int i = 0; i<curr.getSons().size(); i++){
                    BNode s = curr.getSons().get(i);
                    s.setFromChild(false);
                    if(visited.add(s)){
                        vars.add(s);

                    }
                }
            }
            //for all the variables that not variable1 in the query
            else{
                //if coming from child
                if(curr.isFromChild()){
                    //if coming from child and not an evidence
                    if(!curr.isEvidence()){
                        //iterating over all the parents and adding them to visited and vars
                        for(int i = 0; i<curr.getParents().size(); i++) {
                            BNode p = curr.getParents().get(i);
                            p.setFromChild(true);
                            visited.add(p);
                            vars.add(p);
                        }
                        //iterating over all the sons and adding them to visited and vars.
                        for(int i = 0; i<curr.getSons().size(); i++){
                            BNode s = curr.getSons().get(i);
                            s.setFromChild(false);
                            if(visited.add(s)){
                                vars.add(s);
                            }
                        }
                    }
                    //there is no else because if coming from child and an evidence there is no where to go
                }
                //if not coming from child
                else{
                    //if the node is not from child and also an evidence
                    if(curr.isEvidence()){
                        //iterating over all the parents and adding them to visited and vars
                        for(int i = 0; i<curr.getParents().size(); i++) {
                            BNode p = curr.getParents().get(i);
                            p.setFromChild(true);
                            vars.add(p);
                        }
                    }
                    //not from child but isn't an evidence
                    else{
                        //iterating over all the sons and adding them to visited and vars.
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

        //make all the variables in the network not evidence so the next query will not be affected
        bn.clearEvidence();
        //the answer is if our visited set not containing variable 2
        boolean ans = !visited.contains(bn.getNode(q.getVariable2()));


        if(ans == false){
            return "no";
        }
        else{
            return "yes";
        }
    }

}
