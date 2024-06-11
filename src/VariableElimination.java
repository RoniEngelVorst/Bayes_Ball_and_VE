import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class VariableElimination {


    public static String VE(VEQuery q, BayesianNetwork bn){
        int sumCounter = 0;
        int multyCounter = 0;
        //create new network without the non ancestors
        BayesianNetwork newBn = adjustedNetwork(q, bn);
        //run bayesball to find only what is dependent
        List<BNode> dependents = findDependent(q, newBn);
        List<Factor> Factors = new ArrayList<>();
        //creating a list of Factors to begin with
        for(BNode node : dependents){
            Factor newFactor = new Factor(node.getVars(), node.getCptTable());
            Factors.add(newFactor);
        }

        //if there are no given, just get the probability
        if(q.getGiven().isEmpty()){
            Map<String, String> key = new HashMap<>();
            key.put(q.getQuery(), q.isQueryValue());
            String probability = Double.toString(Factors.get(0).getProbability(key));
            String ans = probability + "," + sumCounter + "," + multyCounter;
            return ans;
        }


        Map<String, String> givenMap = q.getGiven();

        // Apply the PlaceGiven method to each element in the list using Stream
        List<Factor> modifiedFactors = Factors.stream()
                .map(factor -> factor.PlaceGiven(givenMap))
                .filter(Objects::nonNull)
                .toList();



        List<Factor> toLoopFactors = new ArrayList<>(modifiedFactors);

        List<String> hidden = q.getOrder();

        while(!hidden.isEmpty()){
            String h = hidden.removeFirst();
            //get all the factors that containing this hidden variable
            List<Factor> factorsContainingH = toLoopFactors.stream()
                    .filter(factor -> factor.containsVariable(h))
                    .toList();


            toLoopFactors.removeAll(factorsContainingH);


            List<Factor> sortedHList = new ArrayList<>(factorsContainingH);
            Collections.sort(sortedHList);

            //join all the factors that contain h
            while (sortedHList.size() > 1) {
                Factor temp = sortedHList.removeFirst().join(sortedHList.removeFirst());
                sortedHList.add(temp);
                multyCounter = multyCounter + temp.getSize();
            }
            if(sortedHList.size() == 0){
                continue;
            }

            Factor joinedFactor = sortedHList.getFirst();

            //eliminate h from the result factor
            Factor eliminatedFactor = joinedFactor.eliminateHidden(h);
            int sumActs = eliminatedFactor.getSize();
            sumCounter = sumCounter + sumActs;

            toLoopFactors.add(eliminatedFactor);

            hidden.remove(h);

        }


        //final join to the remaining factors
        while (toLoopFactors.size() > 1) {
            Factor temp = toLoopFactors.removeFirst().join(toLoopFactors.removeFirst());
            toLoopFactors.add(temp);
            multyCounter = multyCounter + temp.getSize();
        }

        Factor resultFactor = toLoopFactors.getFirst();

        //normalizing the final factor
        resultFactor.normalize();
        sumCounter++;

        Map<String, String> key = new HashMap<>();
        key.put(q.getQuery(), q.isQueryValue());
        String probability = String.format("%.5f", resultFactor.getProbability(key));
        String ans = probability + "," + sumCounter + "," + multyCounter;

        return ans ;
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
            if (ans.equals("no")) {
                dependents.add(node);
            }
        }

        return dependents;
    }
}
