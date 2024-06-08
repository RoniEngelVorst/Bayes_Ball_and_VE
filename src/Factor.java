import java.math.BigDecimal;
import java.util.*;


public class Factor implements Comparable<Factor>{
    private Map<String, BNode> vars;
    private Map<Map<String, String>, Double> cptTable;

    public Factor(Map<String, BNode> vars, Map<Map<String, String>, Double> cpt){
        this.vars = vars;
        this.cptTable = cpt;
    }

    public Map<String, BNode> getVars() {
        return vars;
    }

    public Map<Map<String, String>, Double> getCptTable() {
        return cptTable;
    }

    public double getProbability(Map<String, String> key){
        return this.cptTable.get(key);
    }

    public Set<String> getPossibleOutcomes(){
        Set<String> outcomes = new HashSet<>();
        for (Map.Entry<Map<String, String>, Double> outerEntry : this.cptTable.entrySet()) {
            for(Map.Entry<String, String> innerEntry : outerEntry.getKey().entrySet()){
                outcomes.add(innerEntry.getValue());
            }
        }
        return outcomes;
    }

    public Set<String> getPossibleOutcomesForVariable(String variable) {
        Set<String> outcomes = new HashSet<>();
        for (Map.Entry<Map<String, String>, Double> outerEntry : this.cptTable.entrySet()) {
            Map<String, String> innerMap = outerEntry.getKey();
            if (innerMap.containsKey(variable)) {
                outcomes.add(innerMap.get(variable));
            }
        }
        return outcomes;
    }

    public Factor PlaceGiven(Map<String, String> given){
        Map<Map<String, String>, Double> newCpt = new HashMap<>();
        Map<String, BNode> newVars = new HashMap<>();

        // Iterate over the cptTable to filter entries based on the given map
        for (Map.Entry<Map<String, String>, Double> outerEntry : this.cptTable.entrySet()) {
            Map<String, String> innerMap = outerEntry.getKey();
            Double outerValue = outerEntry.getValue();
            boolean match = true;

            // Check if the entry satisfies the given conditions
            for (Map.Entry<String, String> givenEntry : given.entrySet()) {
                String givenName = givenEntry.getKey();
                String givenValue = givenEntry.getValue();

                if (innerMap.containsKey(givenName) && !innerMap.get(givenName).equals(givenValue)) {
                    match = false;
                    break;
                }
            }

            // If the entry matches the given conditions, add it to the new CPT
            if (match) {
                Map<String, String> filteredInnerMap = new HashMap<>(innerMap);
                // Remove the given keys from the inner map
                for (String givenName : given.keySet()) {
                    filteredInnerMap.remove(givenName);
                }
                newCpt.put(filteredInnerMap, outerValue);
            }
        }

        // Populate newVars with the keys from the new CPT
        for (Map.Entry<Map<String, String>, Double> newOuterEntry : newCpt.entrySet()) {
            for (String key : newOuterEntry.getKey().keySet()) {
                newVars.put(key, this.vars.get(key));
            }
        }

        return new Factor(newVars, newCpt);
    }

    public Boolean isOneLine(){
        if (this.cptTable.isEmpty()) {
            return false;
        }

        for (Map<String, String> keyMap : this.cptTable.keySet()) {
            if (keyMap.size() != 1) {
                return false;
            }
        }
        return true;
    }



//    public static Factor join(Factor A, Factor B){
//        Map<String, BNode> newVars = new HashMap<>();
//        newVars.putAll(A.getVars());
//        newVars.putAll(B.getVars());
//        Map<Map<String, String>, Double> newCptTable = new HashMap<>();
////        List<String> allPossibleOutcomes = new ArrayList<>();
////        allPossibleOutcomes.addAll(A.getPossibleOutcomes());
////        allPossibleOutcomes.addAll(B.getPossibleOutcomes());
//
//        // Iterate over each entry in factor A
//        for (Map.Entry<Map<String, String>, Double> entryA : A.getCptTable().entrySet()) {
//            // Iterate over each entry in factor B
//            for (Map.Entry<Map<String, String>, Double> entryB : B.getCptTable().entrySet()) {
//                // Check if the assignments in entryA and entryB are compatible
//                if (areCompatibleAssignments(entryA.getKey(), entryB.getKey())) {
//                    // Multiply the probabilities and add the entry to the newCptTable
//                    Map<String, String> combinedAssignment = combineAssignments(entryA.getKey(), entryB.getKey());
//                    double combinedProbability = entryA.getValue() * entryB.getValue();
//                    newCptTable.put(combinedAssignment, combinedProbability);
//                }
//            }
//        }
//
//        return new Factor(newVars, newCptTable);
//    }
//
//    // Helper method to check if two assignments are compatible (have the same values for shared variables)
//    private static boolean areCompatibleAssignments(Map<String, String> assignmentA, Map<String, String> assignmentB) {
//        for (Map.Entry<String, String> entryA : assignmentA.entrySet()) {
//            String variable = entryA.getKey();
//            String valueA = entryA.getValue();
//            String valueB = assignmentB.get(variable);
//            if (valueB != null && !valueB.equals(valueA)) {
//                return false; // Incompatible assignment found
//            }
//        }
//        return true; // Assignments are compatible
//    }
//
//    // Helper method to combine two assignments into a single assignment
//    private static Map<String, String> combineAssignments(Map<String, String> assignmentA, Map<String, String> assignmentB) {
//        Map<String, String> combinedAssignment = new HashMap<>(assignmentA);
//        combinedAssignment.putAll(assignmentB); // Overwrites values in assignmentA with those from assignmentB if they share variables
//        return combinedAssignment;
//    }
//}
public Factor join(Factor otherFactor) {
    Map<String, BNode> newVars = new HashMap<>(this.vars);
    newVars.putAll(otherFactor.vars);

    Map<Map<String, String>, Double> newCptTable = new HashMap<>();

    for (Map.Entry<Map<String, String>, Double> entryA : this.cptTable.entrySet()) {
        for (Map.Entry<Map<String, String>, Double> entryB : otherFactor.cptTable.entrySet()) {
            if (areCompatibleAssignments(entryA.getKey(), entryB.getKey())) {
                Map<String, String> combinedAssignment = combineAssignments(entryA.getKey(), entryB.getKey());
//                double combinedProbability = entryA.getValue() * entryB.getValue();
//                newCptTable.put(combinedAssignment, combinedProbability);
                BigDecimal combinedProbability = BigDecimal.valueOf(entryA.getValue()).multiply(BigDecimal.valueOf(entryB.getValue()));
                newCptTable.put(combinedAssignment, combinedProbability.doubleValue());
            }
        }
    }

    return new Factor(newVars, newCptTable);
}

    // Helper method to check if two assignments are compatible (have the same values for shared variables)
    private boolean areCompatibleAssignments(Map<String, String> assignmentA, Map<String, String> assignmentB) {
        for (Map.Entry<String, String> entryA : assignmentA.entrySet()) {
            String variable = entryA.getKey();
            String valueA = entryA.getValue();
            String valueB = assignmentB.get(variable);
            if (valueB != null && !valueB.equals(valueA)) {
                return false; // Incompatible assignment found
            }
        }
        return true; // Assignments are compatible
    }

    // Helper method to combine two assignments into a single assignment
    private Map<String, String> combineAssignments(Map<String, String> assignmentA, Map<String, String> assignmentB) {
        Map<String, String> combinedAssignment = new HashMap<>(assignmentA);
        combinedAssignment.putAll(assignmentB); // Overwrites values in assignmentA with those from assignmentB if they share variables
        return combinedAssignment;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Variables:\n");
        for (String var : vars.keySet()) {
            sb.append(var).append("\n");
        }
        sb.append("CPT:\n");
        for (Map.Entry<Map<String, String>, Double> entry : this.cptTable.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Factor o) {
        return 0;
    }
}
