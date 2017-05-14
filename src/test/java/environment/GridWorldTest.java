package environment;

import entity.Component;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

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
  double epsilon;

  @Before
  public void setUp() throws IOException {
    rewardValue = 100L;
    epsilon = 0.3;
    actorImage = ImageIO.read(new File(GridWorldTest.class.getClassLoader().getResource("cobain.jpg").getFile()));
    goalImage = ImageIO.read(new File(GridWorldTest.class.getClassLoader().getResource("guitar.png").getFile()));
    actor = new Component(0, 0, actorImage, "actor");
    goal = new Component(3, 3, goalImage, "goal");
  }

  @Test
  public void test_generate_grid_world(){
    gridWorld = new GridWorld(actor, goal, 5, rewardValue, epsilon);
  }

  @Test
  public void test_initialize_rewards_for_each_state(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue, epsilon);
    assertEquals(gridWorld.getQValue(0,0), BigDecimal.valueOf(-15));
    assertEquals(gridWorld.getQValue(0,1),BigDecimal.valueOf(-15));
    assertEquals(gridWorld.getQValue(1,1),BigDecimal.valueOf(-5));
    assertEquals(gridWorld.getQValue(5,5), BigDecimal.valueOf(-15));
  }

  @Test
  public void test_append_reward_values_to_state(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue, epsilon);
    gridWorld.reloadWorldAfterMovementForMonteCarlo("down");
    gridWorld.reloadWorldAfterMovementForMonteCarlo("down");
    gridWorld.reloadWorldAfterMovementForMonteCarlo("rigth");
    assertEquals(gridWorld.getqTable()[0][0].getRewards().size(), 3);
    assertEquals(gridWorld.getqTable()[0][1].getRewards().size(), 2);
    assertEquals(gridWorld.getqTable()[0][2].getRewards().size(), 1);
  }

  @Test
  public void test_select_right(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue, epsilon);
    BigDecimal up = BigDecimal.valueOf(10);
    BigDecimal down = BigDecimal.valueOf(20);
    BigDecimal left = BigDecimal.valueOf(30);
    BigDecimal right = BigDecimal.valueOf(40);
    Map<String, BigDecimal> actionMap = new TreeMap<String, BigDecimal>();
    actionMap.put("up", up);
    actionMap.put("down", down);
    actionMap.put("right", right);
    actionMap.put("left", left);
    String action = gridWorld.greedySelection(actionMap);
    assertEquals(action, "right");
  }

  @Test
  public void test_select_left(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue, epsilon);
    BigDecimal up = BigDecimal.valueOf(10);
    BigDecimal down = BigDecimal.valueOf(20);
    BigDecimal left = BigDecimal.valueOf(60);
    BigDecimal right = BigDecimal.valueOf(40);
    Map<String, BigDecimal> actionMap = new TreeMap<String, BigDecimal>();
    actionMap.put("up", up);
    actionMap.put("down", down);
    actionMap.put("right", right);
    actionMap.put("left", left);
    String action = gridWorld.greedySelection(actionMap);
    assertEquals(action, "left");
  }

  @Test
  public void test_select_down(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue, epsilon);
    BigDecimal up = BigDecimal.valueOf(10);
    BigDecimal down = BigDecimal.valueOf(80);
    BigDecimal left = BigDecimal.valueOf(60);
    BigDecimal right = BigDecimal.valueOf(40);
    Map<String, BigDecimal> actionMap = new TreeMap<String, BigDecimal>();
    actionMap.put("up", up);
    actionMap.put("down", down);
    actionMap.put("right", right);
    actionMap.put("left", left);
    String action = gridWorld.greedySelection(actionMap);
    assertEquals(action, "down");
  }

  @Test
  public void test_select_up(){
    gridWorld = new GridWorld(actor, goal, 6, rewardValue, epsilon);
    BigDecimal up = BigDecimal.valueOf(100);
    BigDecimal down = BigDecimal.valueOf(80);
    BigDecimal left = BigDecimal.valueOf(60);
    BigDecimal right = BigDecimal.valueOf(40);
    Map<String, BigDecimal> actionMap = new TreeMap<String, BigDecimal>();
    actionMap.put("up", up);
    actionMap.put("down", down);
    actionMap.put("right", right);
    actionMap.put("left", left);
    String action = gridWorld.greedySelection(actionMap);
    assertEquals(action, "up");
  }

}
