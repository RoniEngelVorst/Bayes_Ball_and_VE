import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class VariableElimination {


    public static String VE(VEQuery q, BayesianNetwork bn){

        System.out.println("Starting query: " + q);
        int sumCounter = 0;
        int multyCounter = 0;
        BayesianNetwork newBn = adjustedNetwork(q, bn);
        List<BNode> dependents = findDependent(q, newBn);
        List<Factor> Factors = new ArrayList<>();
        List<Factor> updatedFactors = new ArrayList<>();
        //creating a list of Factors to begin with
//        System.out.println("Dependent: ");
        for(BNode node : dependents){
//            System.out.println(node.getName());
            Factor newFactor = new Factor(node.getVars(), node.getCptTable());
            Factors.add(newFactor);
        }
//        System.out.println("factors: " +Factors);
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

//        System.out.println("modifiedFactors: "+ modifiedFactors);


        List<Factor> toLoopFactors = new ArrayList<>(modifiedFactors);

        List<String> hidden = q.getOrder();

        while(!hidden.isEmpty()){
            String h = hidden.removeFirst();
            List<Factor> factorsContainingH = toLoopFactors.stream()
                    .filter(factor -> factor.containsVariable(h))
                    .toList();
//            System.out.println("FactorsContaining: "+ h + "\n" +factorsContainingH);

            toLoopFactors.removeAll(factorsContainingH);
//            System.out.println("toLoopFactors: " + toLoopFactors);


            List<Factor> sortedHList = new ArrayList<>(factorsContainingH);
            Collections.sort(sortedHList);

//            System.out.println("sortedHList size: " + sortedHList.size());
//            System.out.println("sorted h list for: " + h + "\n" + sortedHList);
            while (sortedHList.size() > 1) {
                Factor temp = sortedHList.removeFirst().join(sortedHList.removeFirst());
                sortedHList.add(temp);
                multyCounter = multyCounter + temp.getSize();
//                System.out.println("temp factor: " +temp);
//                System.out.println("temp factor size: " + temp.getSize());
            }
            if(sortedHList.size() == 0){
                continue;
            }

            Factor joinedFactor = sortedHList.getFirst();

            System.out.println("factor before eliminating: ");
            System.out.println(joinedFactor);
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

//        System.out.println("final join");
//        System.out.println("toLoopFactors size: " + toLoopFactors.size());
//        System.out.println(toLoopFactors);
        while (toLoopFactors.size() > 1) {
            Factor temp = toLoopFactors.removeFirst().join(toLoopFactors.removeFirst());
            toLoopFactors.add(temp);
            multyCounter = multyCounter + temp.getSize();
//            System.out.println(temp);
//            System.out.println("temp factor size: " + temp.getSize());
        }
        Factor resultFactor = toLoopFactors.getFirst();

        resultFactor.normalize();
        System.out.println("normalizing, adding one act to sum counter.");
        System.out.println("Final Factor: " + resultFactor);

        sumCounter++;

        Map<String, String> key = new HashMap<>();
        key.put(q.getQuery(), q.isQueryValue());
        String probability = String.format("%.5f", resultFactor.getProbability(key));
        String ans = probability + "," + sumCounter + "," + multyCounter;
        System.out.println(ans);

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
//        dependents.add(bn.getNode(q.getQuery()));

        // Convert the keys of the given map to a list
        List<String> given = new ArrayList<>(q.getGiven().keySet());

        for (BNode node : bn.getNodeList()) {
            BQuery bQuery = new BQuery(q.getQuery(), node.getName(), given);
            System.out.println(bQuery);
            // Check if the node is independent
            String ans = BayesBall.isIndependent(bn, bQuery);
            if (ans.equals("no")) {
                dependents.add(node);
            }
        }

        return dependents;
    }
}
