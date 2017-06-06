package environment;

import entity.Component;
import entity.State;
import entity.PType;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by toprak on 07-Apr-17.
 */
public class GridWorld {
    private Component actor;
    private Component goal;
    private int size;
    private State qTable[][];
    public State rewardTable[][];
    private boolean hasWind = false;
    public static final double gamma = 0.95;
    public static final double alpha = 0.3;
    public CopyOnWriteArrayList<PType> pQueue = new CopyOnWriteArrayList<PType>();

    private double epsilon;
    JFrame frame;
    JPanel panel;

    Random generator = new Random();

    public GridWorld(Component actor, Component goal, int size, long rewardValue, double epsilon) {
        this.actor = actor;
        this.goal = goal;
        this.size = size;
        this.epsilon = epsilon;
        initializeRewards(rewardValue);
        display();
    }

    public void display() {
        frame = new JFrame();
        panel = new JPanel(null);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setSize(55 * size, 55 * size);
        panel.setSize(55 * size, 55 * size);
        JLabel actorLabel = new JLabel(new ImageIcon(actor.getImage()));
        actorLabel.setBounds(actor.getxAxis() * 29, actor.getyAxis() * 29, 50, 50);
        JLabel goalLabel = new JLabel(new ImageIcon(goal.getImage()));
        goalLabel.setBounds(goal.getxAxis() * 29, goal.getyAxis() * 29, 50, 50);
        panel.add(actorLabel);
        panel.add(goalLabel);
        panel.setVisible(true);
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void reloadWorldAfterMovementForMonteCarlo(String direction) {
        int previousX = actor.getxAxis();
        int previousY = actor.getyAxis();
        actor.moveComponent(direction);
        if (hasWind) {
            flyActor();
        }
        qTable[previousX][previousY].setAccessed();
        appendRewardToStates(rewardTable[actor.getxAxis()][actor.getyAxis()].getValue());
        panel.getComponent(0).setBounds((actor.getxAxis()) * 29, (actor.getyAxis()) * 29, 50, 50);
        panel.updateUI();
    }

    public void reloadWorldAfterMovementForPS(String direction) throws InterruptedException {
        int previousX = actor.getxAxis();
        int previousY = actor.getyAxis();
        actor.moveComponent(direction);
        if (hasWind) {
            flyActor();
        }
        BigDecimal bestNeighbourValue = getGreatestNeighbourValue(actor.getxAxis(), actor.getyAxis());
        BigDecimal reward = getRValue(actor.getxAxis(), actor.getyAxis());
        BigDecimal currentValue = getQValue(previousX, previousY);
        BigDecimal pValue = calculatePValue(bestNeighbourValue, reward, currentValue);
        if (pValue.compareTo(BigDecimal.ZERO) == 1) {
            checkPQueueThenAdd(previousX, previousY, pValue);
        }
        qTable[previousX][previousY].setAccessed();
        //panel.getComponent(0).setBounds((actor.getxAxis()) * 29, (actor.getyAxis()) * 29, 50, 50);
        //panel.updateUI();
    }

    public void reloadWorldAfterMovementForQLearning(String direction) {
        int previousX = actor.getxAxis();
        int previousY = actor.getyAxis();
        actor.moveComponent(direction);

        BigDecimal bestNeighbourValue = getGreatestNeighbourValue(actor.getxAxis(), actor.getyAxis());
        BigDecimal reward = getRValue(actor.getxAxis(), actor.getyAxis());
        BigDecimal currentValue = getQValue(previousX, previousY);
        BigDecimal qValue = calculateQValue(bestNeighbourValue, reward, currentValue);
        qTable[previousX][previousY].setValue(qValue);
        panel.getComponent(0).setBounds((actor.getxAxis()) * 29, (actor.getyAxis()) * 29, 50, 50);
        panel.updateUI();
    }

    public String reloadWorldAfterMovementForSarsa(String direction) {
        int previousX = actor.getxAxis();
        int previousY = actor.getyAxis();
        actor.moveComponent(direction);
        if (hasWind) {
            flyActor();
        }
        String nextDirection = epsilonGreedyExploration(epsilon, actor);
        BigDecimal reward = getRValue(actor.getxAxis(), actor.getyAxis());
        BigDecimal currentValue = getQValue(previousX, previousY);
        BigDecimal nextReward = getNextReward(nextDirection);
        BigDecimal qValue = calculateQValue(nextReward, reward, currentValue);
        qTable[previousX][previousY].setValue(qValue);
        panel.getComponent(0).setBounds((actor.getxAxis()) * 29, (actor.getyAxis()) * 29, 50, 50);
        panel.updateUI();
        return nextDirection;
    }

    public void checkPQueueThenAdd(int previousX, int previousY, BigDecimal pValue) throws InterruptedException {
        boolean isFound = false;
        for (PType p : pQueue) {
            if (p.getPrevXAxis() == previousX && p.getPrevYAxis() == previousY) {
                isFound = true;
                if (pValue.compareTo(p.getpValue()) > 0) {
                    pQueue.remove(p);
                    pQueue.add(new PType(previousX, previousY, actor.getxAxis(), actor.getyAxis(), pValue));
                    break;
                }
            }
        }
        if (!isFound)
            pQueue.add(new PType(previousX, previousY, actor.getxAxis(), actor.getyAxis(), pValue));
    }

    private BigDecimal getNextReward(String nextDirection) {
        Component fakeActor = new Component(actor.getxAxis(), actor.getyAxis(), null, null);
        fakeActor.moveComponent(nextDirection);
        return getQValue(fakeActor.getxAxis(), fakeActor.getyAxis());
    }

    public BigDecimal calculatePValue(BigDecimal bestNeighbourValue, BigDecimal reward, BigDecimal currentValue) {
        BigDecimal pValue = BigDecimal.valueOf(gamma).multiply(bestNeighbourValue);
        pValue = reward.add(pValue);
        pValue = pValue.subtract(currentValue);
        return pValue;
    }

    public BigDecimal calculateQValue(BigDecimal bestNeighbourValue, BigDecimal reward, BigDecimal currentValue) {
        BigDecimal n = BigDecimal.valueOf(gamma).multiply(bestNeighbourValue);
        BigDecimal a = n.add(reward);
        a = a.subtract(currentValue);
        BigDecimal p = BigDecimal.valueOf(alpha).multiply(a);
        BigDecimal value = currentValue.add(p);
        return value;
    }

    public BigDecimal getGreatestNeighbourValue(int xAxis, int yAxis) {
        Component fakeActor = new Component(xAxis, yAxis, null, null);
        String directionForBestState = epsilonGreedyExploration(0.0, fakeActor);
        fakeActor.moveComponent(directionForBestState);
        return getQValue(fakeActor.getxAxis(), fakeActor.getyAxis());
    }

    public BigDecimal getGreatestNeighbourRewardValue(int xAxis, int yAxis) {
        Component fakeActor = new Component(xAxis, yAxis, null, null);
        String directionForBestState = epsilonGreedyExploration(0.0, fakeActor);
        fakeActor.moveComponent(directionForBestState);
        return getRValue(fakeActor.getxAxis(), fakeActor.getyAxis());
    }

    private void flyActor() {
        if (actor.getxAxis() < size - 1 && actor.getyAxis() < size - 1) {
            actor.setxAxis(actor.getxAxis() + 1);
            actor.setyAxis(actor.getyAxis() + 1);
        }
    }

    private void appendRewardToStates(BigDecimal value) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (qTable[i][j].isAccessed()) {
                    qTable[i][j].addReward(value);
                }
            }
        }
    }

    public String epsilonGreedyExploration(Double epsilon, Component actor) {
        Map<String, BigDecimal> actionMap = setActionValues(actor);
        String greatestAction = greedySelection(actionMap);
        if(greatestAction.charAt(0) == 'x') {
            greatestAction = greatestAction.replace('x', ' ');
            return greatestAction.trim();
        }
        double number = generator.nextDouble();
        if (number < 1 - epsilon)
            return greatestAction;
        else {
            List<String> actions = new ArrayList<String>();
            for (Map.Entry<String, BigDecimal> entry : actionMap.entrySet()) {
                if (entry.getValue().compareTo(actionMap.get(greatestAction)) < 0 || areAllEqual(actionMap)) {
                    actions.add(entry.getKey());
                }
            }
            int randomAction = generator.nextInt((actions.size() - 1) - 0 + 1) + 0;
            return actions.get(randomAction);
        }
    }

    private boolean areAllEqual(Map<String, BigDecimal> actionMap) {
        BigDecimal actionValue = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : actionMap.entrySet()) {
            if (entry.getValue() != null) {
                actionValue = entry.getValue();
                break;
            }
        }
        for (Map.Entry<String, BigDecimal> entry : actionMap.entrySet()) {
            if (entry.getValue() != null && actionValue.compareTo(entry.getValue()) != 0) {
                return false;
            }
        }
        return true;
    }

    public String greedySelection(Map<String, BigDecimal> actionMap) {
        List<String> maxValues = new ArrayList<String>();
        boolean isFirst = true;
        for (Map.Entry<String, BigDecimal> entry : actionMap.entrySet()) {

            if (actor.getxAxis() + 1 == goal.getxAxis() && actor.getyAxis() == goal.getyAxis())
                return "xrigth";
            if (actor.getxAxis() - 1 == goal.getxAxis() && actor.getyAxis() == goal.getyAxis())
                return "xleft";
            if (actor.getxAxis() == goal.getxAxis() && actor.getyAxis() + 1 == goal.getyAxis())
                return "xdown";
            if (actor.getxAxis() == goal.getxAxis() && actor.getyAxis() - 1 == goal.getyAxis())
                return "xup";

            if (isFirst) {
                maxValues.add(entry.getKey());
                isFirst = false;
                continue;
            } else if (actionMap.get(maxValues.get(0)).equals(entry.getValue()))
                maxValues.add(entry.getKey());
            else if (actionMap.get(maxValues.get(0)).compareTo(entry.getValue()) < 0) {
                maxValues.clear();
                maxValues.add(entry.getKey());
            }
        }
        if (maxValues.size() == 1)
            return maxValues.get(0);
        else {
            int randomGreatesIndex = generator.nextInt((maxValues.size() - 1) - 0 + 1) + 0;
            return maxValues.get(randomGreatesIndex);
        }
    }

    public Map<String, BigDecimal> setActionValues(Component actor) {
        Map<String, BigDecimal> actionMap = new TreeMap<String, BigDecimal>();
        if (actor.getxAxis() - 1 >= 0) {
            actionMap.put("left", qTable[actor.getxAxis() - 1][actor.getyAxis()].getValue());

        }
        if (actor.getxAxis() + 1 < size) {
            actionMap.put("rigth", qTable[actor.getxAxis() + 1][actor.getyAxis()].getValue());

        }
        if (actor.getyAxis() - 1 >= 0) {
            actionMap.put("up", qTable[actor.getxAxis()][actor.getyAxis() - 1].getValue());

        }
        if (actor.getyAxis() + 1 < size) {
            actionMap.put("down", qTable[actor.getxAxis()][actor.getyAxis() + 1].getValue());

        }
        return actionMap;
    }

    public State[][] getqTable() {
        return qTable;
    }

    public BigDecimal getQValue(int xLocation, int yLocation) {
        return qTable[xLocation][yLocation].getValue();
    }

    public BigDecimal getRValue(int xLocation, int yLocation) {
        return rewardTable[xLocation][yLocation].getValue();
    }

    public void decreaseEpsilon(double value) {
        if (epsilon > 0)
            this.epsilon -= value;
    }

    public void generateWind() {
        this.hasWind = true;
    }

    public void initializeRewards(long rewardValue) {
        rewardTable = new State[size][size];
        qTable = new State[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rewardTable[i][j] = new State();
                qTable[i][j] = new State();
                if (i == goal.getxAxis() && j == goal.getyAxis())
                    rewardTable[i][j].setValue(BigDecimal.valueOf(rewardValue));
                else if (i == 0 || j == 0 || i == size - 1 || j == size - 1)
                    rewardTable[i][j].setValue(BigDecimal.valueOf(0));
                else
                    rewardTable[i][j].setValue(BigDecimal.valueOf(0));

            }
        }
    }

    public void recalculateReturnValues() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                qTable[i][j].calculateAverageReturn();
            }
        }
    }

    public double getEpsilon() {
        return epsilon;
    }

    public int getSize() {
        return size;
    }
}
