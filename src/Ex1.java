import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class Ex1 {
    public static void main(String[] args) {
        // Path to the XML file
        inputParser parser = new inputParser();
        parser.parseFile("input.txt");

        // Output the results
        System.out.println("XML File Name: " + parser.getXmlFileName());
        System.out.println("BayesBall Queries: " + parser.getBayesBallQueries());
        System.out.println("Variable Elimination Queries: " + parser.getVariableEliminationQueries());

//        BayesianNetwork bn = NetworkBuilder.XML_To_Network(parser.getXmlFileName());
        BayesianNetwork bn = NetworkBuilder.XML_to_Network2((parser.getXmlFileName()));
        bn.printNetwork();


    }

}