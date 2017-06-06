package learningType;

import entity.Component;
import environment.GridWorld;
import simulation.Main;

import java.math.BigDecimal;

/**
 * Created by toprak on 5/17/2017.
 */
public class QLearning {
    public static void simulateQLearning(Component actor, Component goal, GridWorld gridWorld) throws InterruptedException {
        int stepNumber = 0;
        int counter = 0;
        for (int i = 0; i < 1000; i++) {
            boolean isGoal = false;
            actor.setxAxis(0);
            actor.setyAxis(0);
            while (!isGoal) {
                String direction = gridWorld.epsilonGreedyExploration(gridWorld.getEpsilon(), actor);
                gridWorld.reloadWorldAfterMovementForQLearning(direction);
                stepNumber++;
                Thread.sleep(1);
                if (actor.getxAxis() == goal.getxAxis() && actor.getyAxis() == goal.getyAxis()) {
                    System.out.println(counter++ + ": " + stepNumber);
                    isGoal = true;
                    if (gridWorld.getEpsilon() > 0.01)
                        gridWorld.decreaseEpsilon(0.001);
                    continue;
                }

            }
            stepNumber = 0;
        }
    }
}
