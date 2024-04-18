import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Algorithm {
    public static void value_iteration(List<State> stateList, double discountFactor) {
        List<Double> iterationTimes = new ArrayList<>(); // To store time taken for each iteration
        List<Double> totalDifferences = new ArrayList<>();  // To store maximum differences per iteration
        double[] utility = new double[stateList.size()];
        double[] newUtility = new double[stateList.size()];
        double epsilon = 0.01; // Small threshold for determining convergence
        boolean continueIteration;
        int iterationCount = 0; // Counter to track the number of iterations

        long totalStartTime = System.nanoTime(); // Start time measurement for total runtime

        System.out.println("Initial Values:");
        for (int i = 0; i < stateList.size(); i++) {
            System.out.println("Utility of state " + i + ": " + utility[i]);
        }
        System.out.println();


        System.arraycopy(utility, 0, newUtility, 0, stateList.size());

        do {
            long iterationStartTime = System.nanoTime(); // Start time measurement for this iteration
            double totalDifference = 0;  // Reset max difference for this iteration

            continueIteration = false;
            iterationCount++; // Increment the iteration counter

            for (int i = 0; i < stateList.size(); i++) {
                State state = stateList.get(i);
                if (state.isTerminal()) {
                    newUtility[i] = state.getCurrentStateReward();
                    double difference = Math.abs(newUtility[i] - utility[i]);
                    totalDifference += difference; // Accumulate differences for terminal states
                    continue;
                }

                List<Action> actions = state.getActions();
                double maxUtility = Double.NEGATIVE_INFINITY;

                for (Action action : actions) {
                    double expectedUtility = 0;

                    for (Transition currentTransition : action.getActionList()) {
                        int transitionNextState = currentTransition.getNextState();
                        expectedUtility += currentTransition.getProbabilityOfGoingToNextState() * utility[transitionNextState];
                    }

                    if (expectedUtility > maxUtility) {
                        maxUtility = expectedUtility;
                    }
                }

                newUtility[i] = state.getCurrentStateReward() + discountFactor * maxUtility;
                double difference = Math.abs(newUtility[i] - utility[i]);
                totalDifference += difference; // Accumulate differences for all states


                if (Math.abs(newUtility[i] - utility[i]) > epsilon) {
                    continueIteration = true;
                }
            }

            // Update the utilities for the next iteration
            System.arraycopy(newUtility, 0, utility, 0, stateList.size());

            long iterationEndTime = System.nanoTime(); // End time measurement for this iteration
            double iterationTime = (iterationEndTime - iterationStartTime) / 1e9; // Convert ns to seconds
            iterationTimes.add(iterationTime); // Store iteration time

            totalDifferences.add(totalDifference);  // Store the max difference for this iteration

            System.out.println("Iteration #" + iterationCount);
            for (int i = 0; i < stateList.size(); i++) {
                System.out.println("Utility of state " + i + ": " + newUtility[i]);
            }
            System.out.println();

        } while (continueIteration);

        long totalEndTime = System.nanoTime(); // End time measurement for total runtime
        double totalTime = (totalEndTime - totalStartTime) / 1e9; // Convert ns to seconds
        System.out.println("Total execution time: " + totalTime + " seconds");
        System.out.println("Value Iteration Converged after " + iterationCount + " iterations.");
        for (int i = 0; i < stateList.size(); i++) {
            System.out.println("Utility of state " + i + ": " + utility[i]);
        }
        PlotGraph.plotConvergenceTime(iterationTimes);
        PlotGraph.plotConvergenceUtility(totalDifferences);
    }

    public static void policy_iteration(List<State> stateList, double discountFactor) {
        List<Double> iterationTimes = new ArrayList<>(); // To store time taken for each iteration
        List<Integer> actionsChangedCounts = new ArrayList<>(); // To store the count of changed actions per iteration
        int numStates = stateList.size();
        double[] policy = new double[numStates];
        double[] newPolicy;
        boolean policyStable;
        int policyIterationCount = 0; // Counter to track the number of policy iterations

        // Initialize policy arbitrarily (e.g., always take the first action if available)
        for (int i = 0; i < numStates; i++) {
            policy[i] = (stateList.get(i).getActions().isEmpty() ? -1 : 0); // -1 for terminal or no available actions
        }

        double[] utility = new double[numStates];
        // Initialize utilities arbitrarily
        Arrays.fill(utility, 0.0);

        System.out.println("Initial policy: " + Arrays.toString(policy));
        System.out.println();

        do {
            long iterationStartTime = System.nanoTime(); // Start time measurement for this iteration
            int totalDifference = 0; // Reset total difference for this iteration
            policyIterationCount++; // Increment the iteration counter each time the loop starts
            utility = policy_evaluation(stateList, policy, utility, discountFactor);
            newPolicy = policy_improvement(stateList, utility);

            // Check if the policy has changed
            policyStable = Arrays.equals(policy, newPolicy);

            // Check changes in policy
            for (int i = 0; i < numStates; i++) {
                if (policy[i] != newPolicy[i]) {
                    totalDifference += 1;
                }
            }
            actionsChangedCounts.add(totalDifference); // Store the count of changed actions
            policy = newPolicy;


            long iterationEndTime = System.nanoTime(); // End time measurement for this iteration
            double iterationTime = (iterationEndTime - iterationStartTime) / 1e9; // Convert ns to seconds
            iterationTimes.add(iterationTime); // Store iteration time


            System.out.println("Policy: " + Arrays.toString(policy) + " after " + policyIterationCount + " iterations.");
            System.out.println();
        } while (!policyStable);

        System.out.println("Policy Iteration Converged after " + policyIterationCount + " iterations.");
        System.out.println("Optimal policy: " + Arrays.toString(policy));

        PlotGraph.plotConvergenceTime(iterationTimes);
        PlotGraph.plotActionsChanged(actionsChangedCounts);
    }

    private static double[] policy_evaluation(List<State> stateList, double[] policy, double[] utility, double discountFactor) {
        int numStates = stateList.size();

        double epsilon = 0.01; // Small threshold for determining convergence
        boolean continueIteration;

        do {
            continueIteration = false;
            double[] newUtility = Arrays.copyOf(utility, numStates);

            for (int i = 0; i < numStates; i++) {
                State state = stateList.get(i);
                if (state.isTerminal()) {
                    newUtility[i] = state.getCurrentStateReward();
                    continue;
                }

                double expectedUtility = 0;
                List<Action> actions = state.getActions();
                if (actions.isEmpty()) {
                    continue; // Skip if no actions are possible
                }
                Action chosenAction = actions.get((int) policy[i]);  // Current policy dictates this action

                for (Transition currentTransition : chosenAction.getActionList()) {
                    int transitionNextState = currentTransition.getNextState();
                    expectedUtility += currentTransition.getProbabilityOfGoingToNextState() * utility[transitionNextState];
                }

                newUtility[i] = state.getCurrentStateReward() + discountFactor * expectedUtility;

                if (Math.abs(newUtility[i] - utility[i]) > epsilon) {
                    continueIteration = true;
                }
            }

            // Copy new utilities to the utility array for the next iteration
            utility = newUtility;

        } while (continueIteration);

        return utility;
    }

    private static double[] policy_improvement(List<State> stateList, double[] utility) {
        int numStates = stateList.size();
        double[] newPolicy = new double[numStates];
        for (int i = 0; i < numStates; i++) {
            State state = stateList.get(i);
            if (state.isTerminal() || state.getActions().isEmpty()) {
                newPolicy[i] = -1;  // Indicate no action necessary for terminal states or states with no actions
                continue;
            }

            List<Action> actions = state.getActions();
            double maxUtility = Double.NEGATIVE_INFINITY;
            int bestActionIndex = -1;

            for (int actionIndex = 0; actionIndex < actions.size(); actionIndex++) {
                double expectedUtility = 0;
                Action action = actions.get(actionIndex);
                for (Transition currentTransition : action.getActionList()) {
                    int transitionNextState = currentTransition.getNextState();
                    expectedUtility += currentTransition.getProbabilityOfGoingToNextState() * utility[transitionNextState];
                }

                if (expectedUtility > maxUtility) {
                    maxUtility = expectedUtility;
                    bestActionIndex = actionIndex;
                }
            }

            newPolicy[i] = bestActionIndex;
        }
        return newPolicy;
    }


    public static void rtdp(List<State> stateList, State initialState, double discountFactor) {
        final int MAX_ITERATIONS = 1000;
        Random random = new Random();
        double[] utility = new double[stateList.size()];
        int[] currentPolicy = new int[stateList.size()];
        int[] newPolicy = new int[stateList.size()];

        // Initialize utilities and policies arbitrarily
        for (int i = 0; i < stateList.size(); i++) {
            utility[i] = stateList.get(i).getCurrentStateReward();  // Initial utility can be set as the current state reward
            currentPolicy[i] = -1;  // Initialize policy with an invalid action index
        }

        int iteration; // To track the number of iterations executed
        for (iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
            int policyChanges = 0;  // Track number of policy changes per iteration
            State currentState = initialState;

            // Continue until a terminal state is reached
            do {
                Action bestAction = getBestAction(currentState, utility, stateList);
                int currentStateIndex = stateList.indexOf(currentState);

                if (bestAction == null) {
                    currentState = getNextState(random, stateList);  // Move randomly if no valid action is found
                    continue;
                }

                double maxUtility = Double.NEGATIVE_INFINITY;
                int bestActionIndex = -1;

                // Perform a Bellman update and find the best action
                List<Action> actions = currentState.getActions();
                for (int a = 0; a < actions.size(); a++) {
                    double actionUtility = 0;
                    for (Transition transition : actions.get(a).getActionList()) {
                        State transitionNextState = stateList.get(transition.getNextState()); // Safe retrieval
                        int successorIndex = stateList.indexOf(transitionNextState);
                        actionUtility += transition.getProbabilityOfGoingToNextState() * utility[successorIndex];
                    }
                    if (actionUtility > maxUtility) {
                        maxUtility = actionUtility;
                        bestActionIndex = a;
                    }
                }

                newPolicy[currentStateIndex] = bestActionIndex;
                utility[currentStateIndex] = currentState.getCurrentStateReward() + discountFactor * maxUtility;

                // Detect policy change
                if (newPolicy[currentStateIndex] != currentPolicy[currentStateIndex]) {
                    policyChanges++;
                    currentPolicy[currentStateIndex] = newPolicy[currentStateIndex];
                }

                // Move to the next state based on the action chosen
                currentState = getNextState(bestAction, random, stateList);
            } while (!currentState.isTerminal());

            System.out.println("Iteration " + iteration + ": Policy changes = " + policyChanges);
            if (policyChanges == 0) {  // Stop if no policy changes
                break;
            }
        }

        System.out.println();
        System.out.println("RTDP completed after " + iteration + " iterations.");
        for (int i = 0; i < stateList.size(); i++) {
            System.out.println("Utility of state " + stateList.get(i).getStateId() + ": " + utility[i]);
        }
    }

    private static Action getBestAction(State state, double[] utility, List<State> stateList) {
        double maxUtility = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        for (Action action : state.getActions()) {
            double actionUtility = 0;
            for (Transition transition : action.getActionList()) {
                State transitionNextState = stateList.get(transition.getNextState()); // Assume safe retrieval
                int successorIndex = stateList.indexOf(transitionNextState);
                actionUtility += transition.getProbabilityOfGoingToNextState() * utility[successorIndex];
            }

            if (actionUtility > maxUtility) {
                maxUtility = actionUtility;
                bestAction = action;
            }
        }

        return bestAction;
    }

    private static State getNextState(Action action, Random random, List<State> stateList) {
        List<Transition> transitions = action.getActionList();
        double roll = random.nextDouble();
        double sum = 0;

        for (Transition transition : transitions) {
            sum += transition.getProbabilityOfGoingToNextState();
            if (roll <= sum) {
                return stateList.get(transition.getNextState());
            }
        }

        // Fallback to the last transition's state if no transition was chosen
        return stateList.get(transitions.get(transitions.size() - 1).getNextState());
    }

    private static State getNextState(Random random, List<State> stateList) {
        // Fallback method to choose a random state if no action can be taken
        return stateList.get(random.nextInt(stateList.size()));
    }
}