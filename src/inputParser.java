import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class inputParser {
    private String XMLFilePath;
    private List<BQuery> BQueries = new ArrayList<>();
    private List<VEQuery> VEQueries = new ArrayList<>();
    private List<Query> queries = new ArrayList<>();


    public inputParser() {
    }

    public void parseFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Read the XML file name
            if ((line = br.readLine()) != null) {
                XMLFilePath = line;
            }

            // Read the rest of the queries
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;  // Skip empty lines and comments
                }

                // Check if the line starts with 'P(' to identify VE queries
                if (line.startsWith("P(")) {
                    queries.add(parseVEQuery(line));
                } else {
                    queries.add(parseBayesBallQuery(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BQuery parseBayesBallQuery(String line) {
        String[] parts = line.split("\\|");
        String[] v1AndV2 = parts[0].split("-");
        String v1 = v1AndV2[0];
        String v2 = v1AndV2[1];
        List<String> evidence = new ArrayList<>();
        if (parts.length > 1) {
            // Split the evidence part by commas or another delimiter
            String[] evidenceParts = parts[1].split(",");
            for (String part : evidenceParts) {
                String[] variableAndValue = part.trim().split("=");
                if (variableAndValue.length > 0) {
                    evidence.add(variableAndValue[0].trim()); // Add only the variable name
                }
            }
            // If there is evidence, update v2 if it's included in the evidence
            if (evidence.contains(v2)) {
                v2 = null; // Set v2 to null if it's in the evidence
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
        String queryValue = queryVarParts[1];

        // Extract the given variables and their values
        Map<String, String> given = new HashMap<>();
        if (queryParts.length > 1) {
            String[] givenParts = queryParts[1].split(",");
            for (String g : givenParts) {
                String[] varParts = g.split("=");
                given.put(varParts[0], varParts[1]);
            }
        }

        // Extract the elimination order using a regex to match variable names
        List<String> order = new ArrayList<>();
        if (parts.length > 1) {
            // Match sequences of letters followed by numbers
            Pattern pattern = Pattern.compile("[A-Za-z]+\\d*");
            Matcher matcher = pattern.matcher(parts[1]);
            while (matcher.find()) {
                order.add(matcher.group());
            }
        }

        return new VEQuery(queryVar, queryValue, given, order);
    }

    public String getXmlFileName() {
        return XMLFilePath;
    }

    public List<Query> getQueries() {
        return queries;
    }
}