package environment;

import entity.Component;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;

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

  @Test
  public void test_initialize_rewards_for_each_state(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue);
    assertEquals(gridWorld.getQValue(0,0), BigDecimal.valueOf(100));
    assertEquals(gridWorld.getQValue(0,1),BigDecimal.valueOf(-20));
    assertEquals(gridWorld.getQValue(1,1),BigDecimal.valueOf(-5));
    assertEquals(gridWorld.getQValue(5,5), BigDecimal.valueOf(-20));
  }

  @Test
  public void test_append_reward_values_to_state(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue);
    gridWorld.reloadWorldAfterMovement("down");
    gridWorld.reloadWorldAfterMovement("down");
    gridWorld.reloadWorldAfterMovement("rigth");
    assertEquals(gridWorld.getqTable()[0][0].getRewards().size(), 3);
    assertEquals(gridWorld.getqTable()[0][1].getRewards().size(), 2);
    assertEquals(gridWorld.getqTable()[0][2].getRewards().size(), 1);
  }

}
