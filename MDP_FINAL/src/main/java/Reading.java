import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reading {
    public static List<State> reading(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        List<State> stateList = new ArrayList<>();
        int round = 0;
        int isAdd = 0;
        State state = new State();
        Action action = new Action();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#") || line.startsWith("number") || line.startsWith("start")) {
                continue;
            }

            if (line.split(" ").length == 3) {
                String[] currentState = line.split(" ");
                state.setNextStateReward(Double.parseDouble(currentState[0]));
                if (Integer.parseInt(currentState[1]) == 0) {
                    state.setTerminal(false);
                } else {
                    state.setTerminal(true);
                    state.setStateId(round);
                }
                state.setNumberOfActions(Integer.parseInt(currentState[2]));

            } else {
                String[] actions = line.split(" ");
                int numberOfSuccessors = Integer.parseInt(actions[0]);

                int nextState = 0;
                double probability = 0;
                for (int i = 1; i < actions.length; i++) {
                    boolean isFull = false;

                    if (i % 2 == 1) {
                        nextState = Integer.parseInt(actions[i]);
                    } else {
                        probability = Double.parseDouble(actions[i]);
                        isFull = true;
                    }

                    if (isFull) {
                        Transition successor = new Transition(nextState, probability);
                        assert action != null;
                        action.addAction(successor);

                        nextState = 0;
                        probability = 0;
                    }
                }
                if (numberOfSuccessors == action.getActionList().size()) {
                    state.addActions(action);
                    state.setStateId(round);
                    action = new Action();
                    isAdd++;
                }
            }
            if (isAdd == state.getNumberOfActions()) {
                stateList.add(state);
                state = new State();
                isAdd = 0;
                round++;
            }
        }

        return stateList;
    }

    private static String eraseFirstNumber(String line) {
        int firstSpaceIndex = line.indexOf(" ");
        return line.substring(firstSpaceIndex + 1);
    }
}
