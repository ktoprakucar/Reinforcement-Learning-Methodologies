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

  public static void main(String[] args) throws IOException, InterruptedException {
    rewardValue = 100L;
    actorImage = ImageIO.read(new File(Main.class.getClassLoader().getResource("cobain.jpg").getFile()));
    goalImage = ImageIO.read(new File(Main.class.getClassLoader().getResource("guitar.png").getFile()));
    actor = new Component(0, 0, actorImage, "actor");
    goal = new Component(5, 5, goalImage, "goal");
    gridWorld = new GridWorld(actor, goal, 10, rewardValue);
    for (int i = 0; i < 5; i++) {
      Thread.sleep(100);
      gridWorld.reloadWorldAfterMovement("down");
      System.out.println(i);
    }
  }
}
