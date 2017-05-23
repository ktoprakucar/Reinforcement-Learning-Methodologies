package learningType;

import entity.Component;
import entity.PType;
import environment.GridWorld;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by toprak on 5/23/2017.
 */
public class PrioritizedSweeping {
    public static void simulatePS(Component actor, Component goal, GridWorld gridWorld) throws InterruptedException {
        int stepNumber = 0;
        for (int i = 0; i < 1000; i++) {
            boolean isGoal = false;
            actor.setxAxis(0);
            actor.setyAxis(0);
            while (!isGoal) {
                String direction = gridWorld.epsilonGreedyExploration(gridWorld.getEpsilon());
                gridWorld.reloadWorldAfterMovementForPS(direction);
                planning(gridWorld, 4);
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

    private static void planning(GridWorld gridWorld, int stepSize) {
        for (int i = 0; i < stepSize; i++) {
            for (PType p : gridWorld.pQueue) {
                updateQValue(gridWorld, p);
            }
        }


    }

    private static void updateQValue(GridWorld gridWorld, PType p) {
        BigDecimal reward, currentValue, nextValue, qValue;
        reward = gridWorld.getQValue(p.getxAxis(), p.getyAxis());
        currentValue = gridWorld.getQValue(p.getPrevXAxis(), p.getPrevYAxis());
        nextValue = gridWorld.getGreatestNeighbourValue(p.getxAxis(), p.getyAxis());
        qValue = gridWorld.calculateQValue(nextValue, reward, currentValue);
        gridWorld.getqTable()[p.getPrevXAxis()][p.getPrevYAxis()].setValue(qValue);
    }
}
