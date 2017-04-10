package environment;

import entity.Component;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by toprak on 07-Apr-17.
 */
public class GridWorldTest {
  GridWorld gridWorld;
  Component actor;
  Component goal;
  BufferedImage actorImage;
  BufferedImage goalImage;
  Long rewardValue;

  @Before
  public void setUp() throws IOException {
    rewardValue = 100L;
    actorImage = ImageIO.read(new File(GridWorldTest.class.getClassLoader().getResource("cobain.jpg").getFile()));
    goalImage = ImageIO.read(new File(GridWorldTest.class.getClassLoader().getResource("guitar.png").getFile()));
    actor = new Component(0, 0, actorImage, "actor");
    goal = new Component(3, 3, goalImage, "goal");
  }

  @Test
  public void test_generate_grid_world(){
    gridWorld = new GridWorld(actor, goal, 5, rewardValue);
  }

}
