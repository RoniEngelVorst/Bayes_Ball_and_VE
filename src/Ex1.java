import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ex1 {
    public static void main(String[] args) {
        // Path to the XML file
        inputParser parser = new inputParser();
        parser.parseFile("input.txt");

        BayesianNetwork bn = NetworkBuilder.XML_to_Network((parser.getXmlFileName()));
        bn.printNetwork();

        // Write the output string to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for(BQuery bq : parser.getBayesBallQueries()){
                System.out.println(bq);
                String output = BayesBall.isIndependent(bn, bq);
                writer.write(output + "\n");
            }
            for(VEQuery veq : parser.getVariableEliminationQueries()){
                String output = VariableElimination.VE(veq, bn);
                writer.write(output+ "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Output the results
        System.out.println("XML File Name: " + parser.getXmlFileName());
        System.out.println("BayesBall Queries: " + parser.getBayesBallQueries());
        System.out.println("Variable Elimination Queries: " + parser.getVariableEliminationQueries());

//        BayesianNetwork bn = NetworkBuilder.XML_To_Network(parser.getXmlFileName());


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

        Map<String, String> given = new HashMap<>();
        given.put("J", "T");
        given.put("M", "T");
        List<String> order = new ArrayList<>();
        order.add("A");
        order.add("E");


        VEQuery q = new VEQuery("B", "T", given, order);

        System.out.println(VariableElimination.VE(q, bn));


//        Map<String, BNode> vars = new HashMap<>();
//        vars.put("A",bn.getNode("A"));
//        vars.put("M", bn.getNode("M"));
//
//        Factor MFactor = new Factor(vars, bn.getNode("M").getCptTable());
//
//        Map<String, BNode> vars2 = new HashMap<>();
//        vars.put("A",bn.getNode("A"));
//        vars.put("J", bn.getNode("J"));
//
//        Factor JFactor = new Factor(vars2, bn.getNode("J").getCptTable());
//
//        System.out.println(MFactor);
//        System.out.println(JFactor);
//        System.out.println("joined factor: ");
//
//        Factor joinedFactor = MFactor.join(JFactor);
//        System.out.println(joinedFactor);
//
//        System.out.println("Eliminating A: ");
//        Factor eliminateA = joinedFactor.eliminateHidden("A");
//        System.out.println(eliminateA);
//
//        System.out.println("normalized: ");
//        eliminateA.normalize();
//        System.out.println(eliminateA);




    }

}