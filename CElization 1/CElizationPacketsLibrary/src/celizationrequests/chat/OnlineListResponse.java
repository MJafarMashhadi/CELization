package celizationrequests.chat;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author mjafar
 */
public class OnlineListResponse extends celizationrequests.CELizationRequest{
    private ArrayList<String> list;

    public OnlineListResponse() {
        list = new ArrayList<>();
    }

    public void add(String user) {
        list.add(user);
    }

    public void addAll(Set<String> s) {
        list.addAll(s);
    }

    public ArrayList<String> getList() {
        return list;
    }
}
