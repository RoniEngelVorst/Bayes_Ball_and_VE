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

        List<String> e1 = new ArrayList<>();
        List<String> e2 = new ArrayList<>();
        e2.add("J");
        List<String> e3 = new ArrayList<>();
        e3.add("A");



        BQuery q1 = new BQuery("B","E",e1);
        BQuery q2 = new BQuery("B","E",e2);
        BQuery q3 = new BQuery("J","E",e3);

        BayesBall.isIndependent(bn, q1);
        BayesBall.isIndependent(bn, q2);
        BayesBall.isIndependent(bn, q3);


    }

}