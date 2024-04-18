import java.util.ArrayList;
import java.util.List;

public class Action {
    private List<Transition> actionList = new ArrayList<>();

    public void addAction(Transition successor) {
        this.actionList.add(successor);
    }

    public List<Transition> getActionList() {
        return new ArrayList<>(actionList); // Defensive copy to prevent external modifications
    }

    public void setActionList(List<Transition> actionList) {
        this.actionList = new ArrayList<>(actionList); // Defensive copy to ensure encapsulation
    }

    @Override
    public String toString() {
        return "Action{" + "actionList=" + actionList + '}';
    }
}
