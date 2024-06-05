import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class VEQuery {
    private String query;
    private boolean queryValue;
    private Map<String, Boolean> given = new HashMap<>();
    private List<String> order  = new ArrayList<>();

    public VEQuery(String q, boolean qv, Map<String, Boolean> g, List<String> o){
        this.query = q;
        this.queryValue = qv;
        this.given = g;
        this.order = o;
    }

    public String getQuery() {
        return query;
    }

    public boolean isQueryValue() {
        return queryValue;
    }

    public Map<String, Boolean> getGiven() {
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
