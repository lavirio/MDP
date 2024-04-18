import java.util.ArrayList;
import java.util.List;

public class State {
    private int stateId;
    private double currentStateReward;
    private boolean isTerminal;
    private int numberOfActions;
    private List<Action> actions;

    public State() {
        this.actions = new ArrayList<>();
    }

    public double getCurrentStateReward() {
        return currentStateReward;
    }

    public void setCurrentStateReward(double currentStateReward) {
        this.currentStateReward = currentStateReward;
    }

    public void addActions(Action action){
        this.actions.add(action);
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
    public void setNextStateReward(double nextStateReward) {
        this.currentStateReward = nextStateReward;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public int getNumberOfActions() {
        return numberOfActions;
    }

    public void setNumberOfActions(int numberOfActions) {
        this.numberOfActions = numberOfActions;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "State{" +
                "state=" + stateId +
                ", nextStateReward=" + currentStateReward +
                ", isTerminal=" + isTerminal +
                ", numberOfActions=" + numberOfActions +
                ", actions=" + actions +
                '}';
    }
}
