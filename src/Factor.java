import java.math.BigDecimal;
import java.util.*;


public class Factor implements Comparable<Factor>{
    private Map<String, BNode> vars;
    private Map<Map<String, String>, Double> cptTable;

    public Factor(Map<String, BNode> vars, Map<Map<String, String>, Double> cpt){
        this.vars = vars;
        this.cptTable = cpt;
    }


    public double getProbability(Map<String, String> key){
        Double probability = this.cptTable.get(key);
        if (probability != null) {
            return probability; // Return the probability if it exists
        } else {
            // Handle the case where the key does not exist
            throw new IllegalArgumentException("Key not found in CPT table: " + key);
        }
    }

    public int getSize(){
        int size = this.cptTable.size();
        return size;

    }

    public boolean containsVariable(String h) {
        return this.vars.containsKey(h);
    }

    //place the given in the factor
    public Factor PlaceGiven(Map<String, String> given){
        Map<Map<String, String>, Double> newCpt = new HashMap<>();
        Map<String, BNode> newVars = new HashMap<>();

        if(this.isOneLine() && given.containsKey(this.vars.keySet().iterator().next())){
            return null;
        }

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

        // add to newVars the keys from the new CPT
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


    public Factor join(Factor otherFactor) {
        Map<String, BNode> newVars = new HashMap<>(this.vars);
        newVars.putAll(otherFactor.vars);

        Map<Map<String, String>, Double> newCptTable = new HashMap<>();

        for (Map.Entry<Map<String, String>, Double> entryA : this.cptTable.entrySet()) {
            for (Map.Entry<Map<String, String>, Double> entryB : otherFactor.cptTable.entrySet()) {
                if (areCompatibleAssignments(entryA.getKey(), entryB.getKey())) {
                    Map<String, String> combinedAssignment = combineAssignments(entryA.getKey(), entryB.getKey());
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

    public Factor eliminateHidden(String h){
        Map<Map<String, String>, Double> newCpt = new HashMap<>();
        Map<String, BNode> newVars = this.vars;

        newVars.remove(h);
        for (Map.Entry<Map<String, String>, Double> entry : this.cptTable.entrySet()) {
            Map<String, String> key = entry.getKey();
            key.remove(h);
            if (newCpt.containsKey(key)) {
                newCpt.put(key, newCpt.get(key) + entry.getValue());
            } else {
                newCpt.put(key, entry.getValue());
            }
        }

        return new Factor(newVars, newCpt);
    }

    public void normalize(){
        double sum = 0;
        for (Map.Entry<Map<String, String>, Double> entry : this.cptTable.entrySet()) {
            sum = sum + entry.getValue();
        }
        for (Map.Entry<Map<String, String>, Double> entry : this.cptTable.entrySet()) {
            entry.setValue(entry.getValue()/sum);
        }
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
        int numVarsComparison = Integer.compare(this.vars.size(), o.vars.size());
        if (numVarsComparison != 0) {
            return numVarsComparison;
        }

        // If the number of variables is the same, compare by the ASCII value of the first variable name
        String thisFirstVarName = this.vars.keySet().iterator().next();
        String otherFirstVarName = o.vars.keySet().iterator().next();

        return thisFirstVarName.compareTo(otherFirstVarName);
    }
}
