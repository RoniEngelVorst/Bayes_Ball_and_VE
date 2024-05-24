import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class VEQuery {
    private char query;
    private boolean queryValue;
    private Map<Character, Boolean> given = new HashMap<>();
    private List<Character> order  = new ArrayList<>();

    public VEQuery(char q, boolean qv, Map<Character, Boolean> g, List<Character> o){
        this.query = q;
        this.queryValue = qv;
        this.given = g;
        this.order = o;
    }

    public char getQuery() {
        return query;
    }

    public boolean isQueryValue() {
        return queryValue;
    }

    public Map<Character, Boolean> getGiven() {
        return given;
    }

    public List<Character> getOrder() {
        return order;
    }
}
