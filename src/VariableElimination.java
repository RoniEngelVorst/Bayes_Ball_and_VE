import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class VariableElimination {


    public static String VE(VEQuery q, BayesianNetwork bn){
        int sumCounter = 0;
        int multyCounter = 0;
        BayesianNetwork newBn = adjustedNetwork(q, bn);
        List<BNode> dependents = findDependent(q, newBn);
        List<Factor> Factors = new ArrayList<>();
        List<Factor> updatedFactors = new ArrayList<>();
        //creating a list of Factors to begin with
        for(BNode node : dependents){
            Factor newFactor = new Factor(node.getVars(), node.getCptTable());
            Factors.add(newFactor);
        }
        System.out.println("factors: " +Factors);


        Map<String, String> givenMap = q.getGiven();

        // Apply the PlaceGiven method to each element in the list using Stream
        List<Factor> modifiedFactors = Factors.stream()
                .map(factor -> factor.PlaceGiven(givenMap))
                .filter(Objects::nonNull)
                .toList();

        System.out.println("modifiedFactors: "+ modifiedFactors);


        List<Factor> toLoopFactors = new ArrayList<>(modifiedFactors);

        List<String> hidden = q.getOrder();

//        if(toLoopFactors.size() == 1){
//            Factor resultFactor = toLoopFactors.getFirst();
//            for(String h : hidden){
//                resultFactor = resultFactor.eliminateHidden(h);
//            }
//            resultFactor.normalize();
//            System.out.println(resultFactor);
//            Map<String, String> key = new HashMap<>();
//            key.put(q.getQuery(), q.isQueryValue());
//            return resultFactor.getProbability(key);
//
//        }

        while(!hidden.isEmpty()){
            String h = hidden.removeFirst();
            List<Factor> factorsContainingH = toLoopFactors.stream()
                    .filter(factor -> factor.containsVariable(h))
                    .toList();

            toLoopFactors.removeAll(factorsContainingH);
            System.out.println("toLoopFactors: " + toLoopFactors);


            List<Factor> sortedHList = new ArrayList<>(factorsContainingH);
            Collections.sort(sortedHList);

            System.out.println("sorted h list for: " + h + "\n" + sortedHList);
            while (sortedHList.size() > 1) {
                Factor temp = sortedHList.removeFirst().join(sortedHList.removeFirst());
                sortedHList.add(temp);
                multyCounter = multyCounter + temp.getSize();
                System.out.println(temp);
                System.out.println("temp factor size: " + temp.getSize());
            }
            if(sortedHList.size() == 0){
                continue;
            }

            Factor joinedFactor = sortedHList.getFirst();

            //eliminate h from the result factor
            Factor eliminatedFactor = joinedFactor.eliminateHidden(h);
            int sumActs = eliminatedFactor.getSize();
            System.out.println("eliminated factor: ");
            System.out.println(eliminatedFactor);
            System.out.println("eliminating. number of sum acts: " + sumActs);
            sumCounter = sumCounter + sumActs;


            toLoopFactors.add(eliminatedFactor);



            hidden.remove(h);

        }

        System.out.println("final join");
        System.out.println("toLoopFactors size: " + toLoopFactors.size());
        System.out.println(toLoopFactors);
        while (toLoopFactors.size() > 1) {
            Factor temp = toLoopFactors.removeFirst().join(toLoopFactors.removeFirst());
            toLoopFactors.add(temp);
            multyCounter = multyCounter + temp.getSize();
            System.out.println(temp);
            System.out.println("temp factor size: " + temp.getSize());
        }
        Factor resultFactor = toLoopFactors.getFirst();

        resultFactor.normalize();
        System.out.println("normalizing, adding one act to sum counter.");
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
        dependents.add(bn.getNode(q.getQuery()));

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
