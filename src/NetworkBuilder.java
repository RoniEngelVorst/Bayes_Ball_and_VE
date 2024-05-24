import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class NetworkBuilder {
    public static BayesianNetwork XML_To_Network(String filePath){
        BayesianNetwork bn = new BayesianNetwork();
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Parse <VARIABLE> elements and add nodes to the network
            NodeList variableList = doc.getElementsByTagName("VARIABLE");
            for (int i = 0; i < variableList.getLength(); i++) {
                Node variableNode = variableList.item(i);
                if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variableElement = (Element) variableNode;
                    String name = variableElement.getElementsByTagName("NAME").item(0).getTextContent();
                    List<String> outcomes = new ArrayList<>();
                    NodeList outcomeList = variableElement.getElementsByTagName("OUTCOME");
                    for (int j = 0; j < outcomeList.getLength(); j++) {
                        outcomes.add(outcomeList.item(j).getTextContent());
                    }
                    bn.addNode(new BNode(name, outcomes));
                }
            }

            // Parse <DEFINITION> elements and add CPTs to the nodes
            NodeList definitionList = doc.getElementsByTagName("DEFINITION");
            for (int i = 0; i < definitionList.getLength(); i++) {
                Node definitionNode = definitionList.item(i);
                if (definitionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element definitionElement = (Element) definitionNode;
                    String forElement = definitionElement.getElementsByTagName("FOR").item(0).getTextContent();
                    NodeList givenList = definitionElement.getElementsByTagName("GIVEN");
                    List<String> parents = new ArrayList<>();
                    for (int j = 0; j < givenList.getLength(); j++) {
                        String given = givenList.item(j).getTextContent();
                        parents.add(given);
                        bn.addEdge(given, forElement);
                    }

                    String[] tableValues = definitionElement.getElementsByTagName("TABLE").item(0).getTextContent().split("\\s+");
                    List<String> parentOutcomes = new ArrayList<>();
                    for (String parent : parents) {
                        parentOutcomes.addAll(bn.getNode(parent).getOutcomes());
                    }

                    int index = 0;
                    List<String> parentNames = new ArrayList<>(parents);
                    List<String> outcomeCombinations = bn.getNode(forElement).getOutcomes();

                    // Generate combinations of parent outcomes and node outcomes
                    List<String> combinations = generateCombinations(parentNames, outcomeCombinations, bn);

                    for (String combination : combinations) {
                        double probability = Double.parseDouble(tableValues[index++]);
                        bn.setCPT(forElement, combination, probability);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bn;
    }

    private static List<String> generateCombinations(List<String> parentNames, List<String> outcomes, BayesianNetwork bn) {
        List<String> combinations = new ArrayList<>();
        generateCombinationsRecursive(parentNames, outcomes, "", combinations, bn);
        return combinations;
    }

    private static void generateCombinationsRecursive(List<String> parentNames, List<String> outcomes, String prefix, List<String> combinations, BayesianNetwork bn) {
        if (parentNames.isEmpty()) {
            for (String outcome : outcomes) {
                combinations.add(prefix + outcome);
            }
        } else {
            String parentName = parentNames.get(0);
            List<String> remainingParentNames = parentNames.subList(1, parentNames.size());
            for (String parentOutcome : bn.getNode(parentName).getOutcomes()) {
                generateCombinationsRecursive(remainingParentNames, outcomes, prefix + parentOutcome + ",", combinations, bn);
            }
        }
    }
}
