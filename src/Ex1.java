//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Ex1 {
    public static void main(String[] args) {
        // Path to the XML file
        String filePath = "alarm_net.xml";

        BayesianNetwork bn = NetworkBuilder.XML_To_Network(filePath);
        bn.printNetwork();
    }
}