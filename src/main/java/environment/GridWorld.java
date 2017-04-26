package environment;

import entity.Component;
import entity.State;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by toprak on 07-Apr-17.
 */
public class GridWorld {
    private Component actor;
    private Component goal;
    private int size;
    private State qTable[][];
    private boolean hasWind = false;
    public static final double gamma = 0.7;
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

    public void reloadWorldAfterMovement(String direction) {
        int previousX = actor.getxAxis();
        int previousY = actor.getyAxis();
        actor.moveComponent(direction);
        if (hasWind) {
            flyActor();
        }
        qTable[previousX][previousY].setAccessed();
        appendRewardToStates(qTable[actor.getxAxis()][actor.getyAxis()].getValue());
        panel.getComponent(0).setBounds((actor.getxAxis()) * 29, (actor.getyAxis()) * 29, 50, 50);
        panel.updateUI();
    }

    private void flyActor() {
        int xValue = generator.nextInt(((size - 1) - 0) + 1) + 0;
        int yValue = generator.nextInt(((size - 1) - 0) + 1) + 0;
        actor.setxAxis(xValue);
        actor.setyAxis(yValue);
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

    public String epsilonGreedyExploration() {
        Map<String, BigDecimal> actionMap = setActionValues();
        String greatestAction = greedySelection(actionMap);
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

    public Map<String, BigDecimal> setActionValues() {
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

    public void decreaseEpsilon(double value) {
        if (epsilon > 0)
            this.epsilon -= value;
    }

    public void generateWind() {
        this.hasWind = true;
    }

    public void initializeRewards(long rewardValue) {
        qTable = new State[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                qTable[i][j] = new State();
                if (i == goal.getxAxis() && j == goal.getyAxis())
                    qTable[i][j].setValue(BigDecimal.valueOf(rewardValue));
                else if (i == 0 || j == 0 || i == size - 1 || j == size - 1)
                    qTable[i][j].setValue(BigDecimal.valueOf(-15));
                else
                    qTable[i][j].setValue(BigDecimal.valueOf(-5));

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
}
