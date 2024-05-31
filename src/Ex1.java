//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Ex1 {
    public static void main(String[] args) {
        // Path to the XML file
        inputParser parser = new inputParser();
        parser.parseFile("input.txt");

        // Output the results
        System.out.println("XML File Name: " + parser.getXmlFileName());
        System.out.println("BayesBall Queries: " + parser.getBayesBallQueries());
        System.out.println("Variable Elimination Queries: " + parser.getVariableEliminationQueries());

        BayesianNetwork bn = NetworkBuilder.XML_To_Network(parser.getXmlFileName());
        bn.printNetwork();
    }
}