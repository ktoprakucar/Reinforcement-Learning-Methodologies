package learningType;

import entity.Component;
import environment.GridWorld;

/**
 * Created by toprak on 5/14/2017.
 */
public class MonteCarlo {
    public static void simulateMonteCarlo(Component actor,Component goal, GridWorld gridWorld) throws InterruptedException {
        int stepNumber = 0;
        int counter=0;
        for (int i = 0; i < 10000; i++) {
            boolean isGoal = false;
            actor.setxAxis(0);
            actor.setyAxis(0);
            while (!isGoal) {
                String direction = gridWorld.epsilonGreedyExploration(gridWorld.getEpsilon(), actor);
                gridWorld.reloadWorldAfterMovementForMonteCarlo(direction);
                stepNumber++;
                //Thread.sleep(5);
                if (actor.getxAxis() == goal.getxAxis() && actor.getyAxis() == goal.getyAxis()) {
                    System.out.println(counter++ + ": " + stepNumber);
                    gridWorld.recalculateReturnValues();
                    isGoal = true;
                    gridWorld.decreaseEpsilon(0.001);
                    continue;
                }
            }
            stepNumber = 0;
        }
    }
}
