package environment;

import entity.Component;
import entity.State;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

/**
 * Created by toprak on 07-Apr-17.
 */
public class GridWorld {
    private Component actor;
    private Component goal;
    private int size;
    private State qTable[][];
    JFrame frame;
    JPanel panel;

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

    public void reloadWorlAfterMovement(String direction) {
        actor.moveComponent(direction);
        panel.getComponent(0).setBounds((actor.getxAxis()), (actor.getyAxis()) * 50, 50, 50);
        panel.updateUI();
    }


    public Component getActor() {
        return actor;
    }

    public void setActor(Component actor) {
        this.actor = actor;
    }

    public Component getGoal() {
        return goal;
    }

    public void setGoal(Component goal) {
        this.goal = goal;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
