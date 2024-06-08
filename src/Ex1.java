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

        System.out.println(BayesBall.isIndependent(bn, q1));
        System.out.println(BayesBall.isIndependent(bn, q2));
        System.out.println(BayesBall.isIndependent(bn, q3));


        Map<String, BNode> vars = new HashMap<>();
        vars.put("A",bn.getNode("A"));
        vars.put("M", bn.getNode("M"));

        Factor MFactor = new Factor(vars, bn.getNode("M").getCptTable());

        Map<String, BNode> vars2 = new HashMap<>();
        vars.put("A",bn.getNode("A"));
        vars.put("J", bn.getNode("J"));

        Factor JFactor = new Factor(vars2, bn.getNode("J").getCptTable());

        System.out.println(MFactor);
        System.out.println(JFactor);
        System.out.println("joined factor: ");

        Factor joinedFactor = MFactor.join(JFactor);
        System.out.println(joinedFactor);

        System.out.println("Eliminating A: ");
        Factor eliminateA = joinedFactor.eliminateHidden("A");
        System.out.println(eliminateA);

        System.out.println("normalized: ");
        eliminateA.normalize();
        System.out.println(eliminateA);




    }

}