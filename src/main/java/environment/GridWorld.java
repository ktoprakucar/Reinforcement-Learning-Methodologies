package environment;

import entity.Component;

import java.awt.*;
import java.math.BigDecimal;

/**
 * Created by toprak on 07-Apr-17.
 */
public class GridWorld {
  private Component actor;
  private Component goal;
  private int size;
  private BigDecimal qTable[][];

  public GridWorld(Component actor, Component goal, int size, long rewardValue){
    this.actor = actor;
    this.goal = goal;
    this.size = size;
    qTable = new BigDecimal[size][size];
    qTable[goal.getxAxis()][goal.getyAxis()] = BigDecimal.valueOf(rewardValue);
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

  public BigDecimal getQValue(int xLocation, int yLocation){
    return qTable[xLocation][yLocation];
  }
}
