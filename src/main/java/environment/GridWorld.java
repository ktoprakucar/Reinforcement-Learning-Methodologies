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
    public static final double gamma = 0.7;
    public static final double epsilon = 0.3;
    JFrame frame;
    JPanel panel;

    Random generator = new Random();

    public GridWorld(Component actor, Component goal, int size, long rewardValue) {
        this.actor = actor;
        this.goal = goal;
        this.size = size;
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
        actorLabel.setBounds(actor.getxAxis() * 50, actor.getyAxis() * 50, 50, 50);
        JLabel goalLabel = new JLabel(new ImageIcon(goal.getImage()));
        goalLabel.setBounds(goal.getxAxis() * 50, goal.getyAxis() * 50, 50, 50);
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
        qTable[previousX][previousY].setAccessed();
        appendRewardToStates(qTable[actor.getxAxis()][actor.getyAxis()].getValue());
        panel.getComponent(0).setBounds((actor.getxAxis()), (actor.getyAxis()) * 50, 50, 50);
        panel.updateUI();
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

    public void epsilonGreedyExploration() {
        BigDecimal right = null, left = null, up = null, down = null;
        Map<String, BigDecimal> actionMap = setActionValues(right, left, up, down);
        String greatestAction = greedySelection(actionMap);

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
            else if(actionMap.get(maxValues.get(0)).compareTo(entry.getValue()) == -1){
                maxValues.clear();
                maxValues.add(entry.getKey());
            }
        }
        if(maxValues.size() == 1)
            return maxValues.get(0);
        else{
            int randomGreatesIndex = generator.nextInt((maxValues.size() - 1) - 0 + 1) + 0;
            return maxValues.get(randomGreatesIndex);
        }
    }

    public Map<String, BigDecimal> setActionValues(BigDecimal right, BigDecimal left, BigDecimal up, BigDecimal down) {
        Map<String, BigDecimal> actionMap = new TreeMap<String, BigDecimal>();
        if (actor.getxAxis() - 1 >= 0)
            up = qTable[actor.getxAxis() - 1][actor.getyAxis()].getValue();
        if (actor.getxAxis() + 1 < size)
            down = qTable[actor.getxAxis() + 1][actor.getyAxis()].getValue();
        if (actor.getyAxis() - 1 >= 0)
            left = qTable[actor.getxAxis()][actor.getyAxis() - 1].getValue();
        if (actor.getyAxis() + 1 < size)
            right = qTable[actor.getxAxis()][actor.getyAxis() + 1].getValue();
        actionMap.put("up", up);
        actionMap.put("down", down);
        actionMap.put("rigth", right);
        actionMap.put("left", left);
        return actionMap;
    }

    public State[][] getqTable() {
        return qTable;
    }

    public BigDecimal getQValue(int xLocation, int yLocation) {
        return qTable[xLocation][yLocation].getValue();
    }

    public void initializeRewards(long rewardValue) {
        qTable = new State[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                qTable[i][j] = new State();
                if (i == actor.getxAxis() && j == actor.getyAxis())
                    qTable[i][j].setValue(BigDecimal.valueOf(rewardValue));
                else if (i == 0 || j == 0 || i == size - 1 || j == size - 1)
                    qTable[i][j].setValue(BigDecimal.valueOf(-20));
                else
                    qTable[i][j].setValue(BigDecimal.valueOf(-5));

            }
        }
        qTable[goal.getxAxis()][goal.getyAxis()].setValue(BigDecimal.valueOf(rewardValue));
    }
}
