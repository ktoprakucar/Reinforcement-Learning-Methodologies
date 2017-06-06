package learningType;

import entity.Component;
import entity.PType;
import entity.State;
import environment.GridWorld;
import simulation.Main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toprak on 5/23/2017.
 */
public class PrioritizedSweeping {

    public static final double GAMMA = 0.1;

    public static void simulatePS(Component actor, Component goal, GridWorld gridWorld) throws InterruptedException {
        int stepNumber = 0;
        int counter = 0;
        for (int i = 0; i < 1000; i++) {
            boolean isGoal = false;
            actor.setxAxis(0);
            actor.setyAxis(0);
            while (!isGoal) {
                String direction = gridWorld.epsilonGreedyExploration(gridWorld.getEpsilon(), actor);
                gridWorld.reloadWorldAfterMovementForPS(direction);
                planning(gridWorld, 4);
                stepNumber++;
                Thread.sleep(1);
                if (actor.getxAxis() == goal.getxAxis() && actor.getyAxis() == goal.getyAxis()) {
                    System.out.println(counter++ + ": " + stepNumber);
                    isGoal = true;
                    if (gridWorld.getEpsilon() > 0.001)
                        gridWorld.decreaseEpsilon(0.001);
                    continue;
                }
            }
            setStatesNonAccessed(gridWorld);
            stepNumber = 0;
        }
    }

    private static void setStatesNonAccessed(GridWorld gridWorld) {
        for (int i = 0; i < gridWorld.getSize(); i++) {
            for (int j = 0; j < gridWorld.getSize(); j++) {
                gridWorld.getqTable()[i][j].setAccessed(false);

            }
        }
    }

    private static void planning(GridWorld gridWorld, int stepSize) throws InterruptedException {
        for (int i = 0; i < stepSize; i++) {
            List<PType> pList = new ArrayList<PType>();
            for (PType p : gridWorld.pQueue) {
                updateQValue(gridWorld, p);
                checkNeighboursThenAddPQueue(gridWorld, p);
            }

            gridWorld.pQueue.addAll(pList);
        }


    }

    private static void checkNeighboursThenAddPQueue(GridWorld gridWorld, PType p) throws InterruptedException {
        List<PType> pList = new ArrayList<PType>();
        BigDecimal currentValue;
        if (p.getPrevXAxis() - 1 > 0 && gridWorld.getqTable()[p.getPrevXAxis() - 1][p.getPrevYAxis()].isAccessed()) {
            currentValue = gridWorld.getGreatestNeighbourValue(p.getPrevXAxis(), p.getPrevYAxis()).multiply(BigDecimal.valueOf(GAMMA));
            currentValue = currentValue.subtract(gridWorld.getqTable()[p.getPrevXAxis() - 1][p.getPrevYAxis()].getValue());
            currentValue = currentValue.add(gridWorld.getRValue(p.getPrevXAxis() - 1, p.getPrevYAxis()));
            if (currentValue.compareTo(BigDecimal.ZERO) > 0)
                gridWorld.checkPQueueThenAdd(p.getPrevXAxis() - 1, p.getPrevYAxis(), currentValue);
        }
        if (p.getPrevXAxis() + 1 < gridWorld.getSize() && gridWorld.getqTable()[p.getPrevXAxis() + 1][p.getPrevYAxis()].isAccessed()) {
            currentValue = gridWorld.getGreatestNeighbourValue(p.getPrevXAxis(), p.getPrevYAxis()).multiply(BigDecimal.valueOf(GAMMA));
            currentValue = currentValue.subtract(gridWorld.getqTable()[p.getPrevXAxis() + 1][p.getPrevYAxis()].getValue());
            currentValue = currentValue.add(gridWorld.getRValue(p.getPrevXAxis() + 1, p.getPrevYAxis()));
            if (currentValue.compareTo(BigDecimal.ZERO) > 0)
                gridWorld.checkPQueueThenAdd(p.getPrevXAxis() + 1, p.getPrevYAxis(), currentValue);
            pList.add(new PType(p.getPrevXAxis() + 1, p.getPrevYAxis(), p.getPrevXAxis(), p.getPrevYAxis(), currentValue));
        }
        if (p.getPrevYAxis() + 1 < gridWorld.getSize() && gridWorld.getqTable()[p.getPrevXAxis()][p.getPrevYAxis() + 1].isAccessed()) {
            currentValue = gridWorld.getGreatestNeighbourValue(p.getPrevXAxis(), p.getPrevYAxis()).multiply(BigDecimal.valueOf(GAMMA));
            currentValue = currentValue.subtract(gridWorld.getqTable()[p.getPrevXAxis()][p.getPrevYAxis() + 1].getValue());
            currentValue = currentValue.add(gridWorld.getRValue(p.getPrevXAxis(), p.getPrevYAxis() + 1));
            if (currentValue.compareTo(BigDecimal.ZERO) > 0)
                gridWorld.checkPQueueThenAdd(p.getPrevXAxis(), p.getPrevYAxis() + 1, currentValue);
            pList.add(new PType(p.getPrevXAxis(), p.getPrevYAxis() + 1, p.getPrevXAxis(), p.getPrevYAxis(), currentValue));

        }
        if (p.getPrevYAxis() - 1 > 0 && gridWorld.getqTable()[p.getPrevXAxis()][p.getPrevYAxis() - 1].isAccessed()) {
            currentValue = gridWorld.getGreatestNeighbourValue(p.getPrevXAxis(), p.getPrevYAxis()).multiply(BigDecimal.valueOf(GAMMA));
            currentValue = currentValue.subtract(gridWorld.getqTable()[p.getPrevXAxis()][p.getPrevYAxis() - 1].getValue());
            currentValue = currentValue.add(gridWorld.getRValue(p.getPrevXAxis(), p.getPrevYAxis() - 1));
            if (currentValue.compareTo(BigDecimal.ZERO) > 0)
                gridWorld.checkPQueueThenAdd(p.getPrevXAxis(), p.getPrevYAxis() - 1, currentValue);
            pList.add(new PType(p.getPrevXAxis(), p.getPrevYAxis() - 1, p.getPrevXAxis(), p.getPrevYAxis(), currentValue));
        }
    }

    private static void updateQValue(GridWorld gridWorld, PType p) {
        BigDecimal reward, currentValue, nextValue, qValue;
        reward = gridWorld.getRValue(p.getxAxis(), p.getyAxis());
        currentValue = gridWorld.getQValue(p.getPrevXAxis(), p.getPrevYAxis());
        nextValue = gridWorld.getGreatestNeighbourValue(p.getPrevXAxis(), p.getPrevYAxis());
        qValue = gridWorld.calculateQValue(nextValue, reward, currentValue);
        gridWorld.getqTable()[p.getPrevXAxis()][p.getPrevYAxis()].setValue(qValue);
    }
}
