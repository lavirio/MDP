public class Transition {
    private int nextState;
    private double probabilityOfGoingToNextState;
    private double nextStateUtility;

    public Transition() {
    }

    public Transition(int nextState, double probabilityOfGoingToNextState) {
        this.nextState = nextState;
        this.probabilityOfGoingToNextState = probabilityOfGoingToNextState;
    }

    public double getNextStateUtility() {
        return nextStateUtility;
    }

    public void setNextStateUtility(double nextStateUtility) {
        this.nextStateUtility = nextStateUtility;
    }

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int nextState) {
        this.nextState = nextState;
    }

    public double getProbabilityOfGoingToNextState() {
        return probabilityOfGoingToNextState;
    }

    public void setProbabilityOfGoingToNextState(double probabilityOfGoingToNextState) {
        this.probabilityOfGoingToNextState = probabilityOfGoingToNextState;
    }

    @Override
    public String toString() {
        return "Successor{" +
                "nextState=" + nextState +
                ", probabilityOfGoingToNextState=" + probabilityOfGoingToNextState +
                '}';
    }
}

