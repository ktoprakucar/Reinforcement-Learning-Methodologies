package environment;

import entity.Component;

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
  private BigDecimal qTable[][];

  public GridWorld(Component actor, Component goal, int size, long rewardValue) {
    this.actor = actor;
    this.goal = goal;
    this.size = size;
    qTable = new BigDecimal[size][size];
    qTable[goal.getxAxis()][goal.getyAxis()] = BigDecimal.valueOf(rewardValue);
    display();
  }

  public void display() {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel(null);
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
    System.out.println(panel.getComponentCount());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
    return qTable[xLocation][yLocation];
  }
}
