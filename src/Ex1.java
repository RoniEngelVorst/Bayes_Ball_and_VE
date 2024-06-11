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
            for (Query query : parser.getQueries()) {
                if (query instanceof BQuery) {
                    BQuery bQuery = (BQuery) query;
                    String output = BayesBall.isIndependent(bn, bQuery);
                    writer.write(output + "\n");
                    // Process BQuery
                    System.out.println("BayesBall Query: " + bQuery);
                } else if (query instanceof VEQuery) {
                    VEQuery veQuery = (VEQuery) query;
                    String output = VariableElimination.VE(veQuery, bn);
                    writer.write(output + "\n");
                    // Process VEQuery
                    System.out.println("Variable Elimination Query: " + veQuery);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        //checking input2
        // Path to the XML file
        inputParser parser2 = new inputParser();
        parser2.parseFile("input2.txt");

        BayesianNetwork bn2 = NetworkBuilder.XML_to_Network((parser2.getXmlFileName()));
        bn2.printNetwork();

        // Write the output string to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output2.txt"))) {
            for (Query query : parser2.getQueries()) {
                if (query instanceof BQuery) {
                    BQuery bQuery = (BQuery) query;
                    String output = BayesBall.isIndependent(bn2, bQuery);
                    writer.write(output + "\n");
                    // Process BQuery
                } else if (query instanceof VEQuery) {
                    VEQuery veQuery = (VEQuery) query;
                    String output = VariableElimination.VE(veQuery, bn2);
                    writer.write(output+ "\n");
                    // Process VEQuery
                    System.out.println("Variable Elimination Query: " + veQuery);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //checking input3
        // Path to the XML file
        inputParser parser3 = new inputParser();
        parser3.parseFile("input3.txt");

        System.out.println(parser3.getXmlFileName());
        BayesianNetwork bn3 = NetworkBuilder.XML_to_Network((parser3.getXmlFileName()));
        bn3.printNetwork();

        // Write the output string to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output3.txt"))) {
            for (Query query : parser3.getQueries()) {
                if (query instanceof BQuery) {
                    BQuery bQuery = (BQuery) query;
                    String output = BayesBall.isIndependent(bn3, bQuery);
                    writer.write(output + "\n");
                    // Process BQuery
                } else if (query instanceof VEQuery) {
                    VEQuery veQuery = (VEQuery) query;
                    String output = VariableElimination.VE(veQuery, bn3);
                    writer.write(output+ "\n");
                    // Process VEQuery
                    System.out.println("Variable Elimination Query: " + veQuery);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}