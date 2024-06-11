import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Ex1 {
    public static void main(String[] args) {
        // Path to the XML file
        inputParser parser = new inputParser();
        parser.parseFile("input.txt");

        BayesianNetwork bn = NetworkBuilder.XML_to_Network((parser.getXmlFileName()));

        // Write the output string to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (Query query : parser.getQueries()) {
                if (query instanceof BQuery) {
                    BQuery bQuery = (BQuery) query;
                    String output = BayesBall.isIndependent(bn, bQuery);
                    writer.write(output + "\n");
                } else if (query instanceof VEQuery) {
                    VEQuery veQuery = (VEQuery) query;
                    String output = VariableElimination.VE(veQuery, bn);
                    writer.write(output + "\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }







    }

}