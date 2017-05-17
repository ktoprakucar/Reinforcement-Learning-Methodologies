package learningType;

import entity.Component;
import environment.GridWorld;

/**
 * Created by toprak on 5/18/2017.
 */
public class Sarsa {
    public static void simulateSarsa(Component actor, Component goal, GridWorld gridWorld) throws InterruptedException {
        int stepNumber = 0;
        for (int i = 0; i < 1000; i++) {
            boolean isGoal = false;
            actor.setxAxis(0);
            actor.setyAxis(0);
            String direction = gridWorld.epsilonGreedyExploration(gridWorld.getEpsilon());
            while (!isGoal) {
                direction = gridWorld.reloadWorldAfterMovementForSarsa(direction);
                stepNumber++;
                Thread.sleep(5);
                if (actor.getxAxis() == goal.getxAxis() && actor.getyAxis() == goal.getyAxis()) {
                    System.out.println(stepNumber);
                    isGoal = true;
                    gridWorld.decreaseEpsilon(0.001);
                    continue;
                }
            }
            stepNumber = 0;
        }
    }
}
