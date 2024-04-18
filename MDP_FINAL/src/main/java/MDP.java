import java.util.List;
import java.util.Scanner;

public class MDP {
    public static void main(String[] args) {
        try {
            double discounted_factor = 0.1;
            List<State> stateList = Reading.reading("src/main/java/Files/standard.mdp");
            State initialState = stateList.get(0);
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the name of the algorithm (value or policy):");
            String method = "rtdp";
            if (method.equals("value")){
                Algorithm.value_iteration(stateList, discounted_factor);
            } else if (method.equals("policy")) {
                Algorithm.policy_iteration(stateList, discounted_factor);
            } else {
                Algorithm.rtdp(stateList, initialState, discounted_factor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}