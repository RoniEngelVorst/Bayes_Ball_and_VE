import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class inputParser {
    private String filePath = "input.txt";
    private String XMLFilePath;
    private List<BQuery> BQueries = new ArrayList<>();
    private List<VEQuery> VEQueries = new ArrayList<>();

    public inputParser() {
    }

    public void parseFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isBayesBallQuery = true;  // Assuming BayesBall queries come first

            // Read the XML file name
            if ((line = br.readLine()) != null) {
                XMLFilePath = line;
            }

            // Read the rest of the queries
            while ((line = br.readLine()) != null) {
                if (line.startsWith("P(")) {
                    isBayesBallQuery = false;  // Switch to Variable Elimination queries
                }
                if (isBayesBallQuery) {
                    BQueries.add(parseBayesBallQuery(line));
                } else {
                    VEQueries.add(parseVEQuery(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BQuery parseBayesBallQuery(String line) {
        String[] parts = line.split("\\|");
        String[] variables = parts[0].split("-");
        String v1 = variables[0];
        String v2 = variables[1];
        List<String> evidence = new ArrayList<>();
        if (parts.length > 1) {
            // Split the evidence part by commas or another delimiter
            String[] evidenceParts = parts[1].split(",");
            for (String c : evidenceParts) {
                if (!c.equals("=")) {
                    evidence.add(c.trim()); // Add trimmed evidence strings
                }
            }
        }
        return new BQuery(v1, v2, evidence);
    }

    private VEQuery parseVEQuery(String line) {
        String[] parts = line.split("\\) ");
        String queryPart = parts[0].substring(2); // Remove the leading 'P(' and the space
        String[] queryParts = queryPart.split("\\|");

        // Extract the query variable and its value
        String[] queryVarParts = queryParts[0].split("=");
        String queryVar = queryVarParts[0];
        boolean queryValue = queryVarParts[1].equals("T");

        // Extract the given variables and their values
        Map<String, Boolean> given = new HashMap<>();
        if (queryParts.length > 1) {
            String[] givenParts = queryParts[1].split(",");
            for (String g : givenParts) {
                String[] varParts = g.split("=");
                given.put(varParts[0], varParts[1].equals("T"));
            }
        }

        // Extract the elimination order
        List<String> order = new ArrayList<>();
        if (parts.length > 1) {
            for (char c : parts[1].toCharArray()) {
                if (c != '-' && c != ' ' && c != '>') {
                    order.add(String.valueOf(c));
                }
            }
        }

        return new VEQuery(queryVar, queryValue, given, order);
    }

    public String getXmlFileName() {
        return XMLFilePath;
    }

    public List<BQuery> getBayesBallQueries() {
        return BQueries;
    }

    public List<VEQuery> getVariableEliminationQueries() {
        return VEQueries;
    }
}