import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class NetworkBuilder {
    int p = 0;
    Map<Map<String ,String>, Double> cptTable = new HashMap<>();

//    public void CreateCptTable(List<BNode> variables, List<Double> probs, List<String> possibleOutcomes) {
//
//        if(variables.size() == probs.size()){
//            Map<String, String> key = new HashMap<>();
//
//            for(int i = 0; i< variables.size(); i++){
//                key.put(variables.get(i).getName(), possibleOutcomes.get(i));
//            }
//            cptTable.put(key, probs.get(p));
//
//            System.out.println("variable: "+ variables + ", value: " + probs.get(p));
//            p++;
//            return;
//        }
//        BNode var = variables.get(possibleOutcomes.size());
//        for(int i = 0; i<var.getOutcomes().size(); i++){
//            possibleOutcomes.add(var.getOutcomes().get(i));
//            CreateCptTable(variables, probs, possibleOutcomes);
//            possibleOutcomes.removeLast();
//
//        }
//
//
//    }

    public static BayesianNetwork XML_to_Network(String filePath){
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
                    List<BNode> parents = new ArrayList<>();
                    for (int j = 0; j < givenList.getLength(); j++) {
                        String given = givenList.item(j).getTextContent();
                        parents.add(bn.getNode(given));
                        bn.addEdge(given, forElement);
                    }
                    List<BNode> variables = new ArrayList<>(parents);
                    variables.add(bn.getNode(forElement));

                    String[] tableValues = definitionElement.getElementsByTagName("TABLE").item(0).getTextContent().split("\\s+");
                    List<Double> tableDoubles = Arrays.stream(tableValues)
                            .map(Double::parseDouble)
                            .collect(Collectors.toList());

                    bn.getNode(forElement).createCptTable(variables, tableDoubles);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bn;
    }







}
