import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLParser {
    public static void main(String[] args){
        try {
            File inputFile = new File("alarm_net.xml");  // Replace with the path to your XML file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            // Parse <VARIABLE> elements
            NodeList variableList = doc.getElementsByTagName("VARIABLE");
            for (int i = 0; i < variableList.getLength(); i++) {
                Node variableNode = (Node) variableList.item(i);
                if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element variableElement = (Element) variableNode;
                    String name = variableElement.getElementsByTagName("NAME").item(0).getTextContent();
                    System.out.println("Variable NAME: " + name);

                    NodeList outcomeList = variableElement.getElementsByTagName("OUTCOME");
                    for (int j = 0; j < outcomeList.getLength(); j++) {
                        String outcome = outcomeList.item(j).getTextContent();
                        System.out.println("  OUTCOME: " + outcome);
                    }
                }
            }

            // Parse <DEFINITION> elements
            NodeList definitionList = doc.getElementsByTagName("DEFINITION");
            for (int i = 0; i < definitionList.getLength(); i++) {
                Node definitionNode = (Node) definitionList.item(i);
                if (definitionNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element definitionElement = (Element) definitionNode;
                    String forElement = definitionElement.getElementsByTagName("FOR").item(0).getTextContent();
                    System.out.println("Definition FOR: " + forElement);

                    NodeList givenList = definitionElement.getElementsByTagName("GIVEN");
                    for (int j = 0; j < givenList.getLength(); j++) {
                        String given = givenList.item(j).getTextContent();
                        System.out.println("  GIVEN: " + given);
                    }

                    String table = definitionElement.getElementsByTagName("TABLE").item(0).getTextContent();
                    System.out.println("  TABLE: " + table);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

