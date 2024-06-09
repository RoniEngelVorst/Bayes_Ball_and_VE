import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class VEQuery {
    private String query;
    private String queryValue;
    private Map<String, String> given = new HashMap<>();
    private List<String> order  = new ArrayList<>();

    public VEQuery(String q, String qv, Map<String, String> g, List<String> o){
        this.query = q;
        this.queryValue = qv;
        this.given = g;
        this.order = o;
    }

    public String getQuery() {
        return query;
    }

    public String isQueryValue() {
        return queryValue;
    }

    public Map<String, String> getGiven() {
        return given;
    }

    public List<String> getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "VEQuery{" +
                "query=" + query +
                ", queryValue=" + queryValue +
                ", given=" + given +
                ", order=" + order +
                '}';
    }
}
