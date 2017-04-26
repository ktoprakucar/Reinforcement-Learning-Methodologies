package simulation;

import entity.Component;
import environment.GridWorld;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by toprak on 10-Apr-17.
 */
public class Main {
  static GridWorld gridWorld;
  static Component actor;
  static Component goal;
  static BufferedImage actorImage;
  static BufferedImage goalImage;
  static Long rewardValue;
  static int stepNumber;

  public static void main(String[] args) throws IOException, InterruptedException {
    rewardValue = 100L;
    actorImage = ImageIO.read(new File(Main.class.getClassLoader().getResource("cobain.jpg").getFile()));
    goalImage = ImageIO.read(new File(Main.class.getClassLoader().getResource("guitar.png").getFile()));
    actor = new Component(0, 0, actorImage, "actor");
    goal = new Component(5, 5, goalImage, "goal");
    gridWorld = new GridWorld(actor, goal, 10, rewardValue);
    for(int i = 0; i < 50; i++) {
      boolean isGoal = false;
      actor.setxAxis(0);
      actor.setyAxis(0);
      while (!isGoal) {
        String direction = gridWorld.epsilonGreedyExploration();
        System.out.println(direction);
        gridWorld.reloadWorldAfterMovement(direction);
        gridWorld.decreaseEpsilon(0.01);
        stepNumber++;
        Thread.sleep(100);
        if(actor.getxAxis() == goal.getxAxis() && actor.getyAxis() == goal.getyAxis()){
          System.out.println(stepNumber);
          gridWorld.recalculateReturnValues();
          continue;
        }

      }
    }
  }
}
