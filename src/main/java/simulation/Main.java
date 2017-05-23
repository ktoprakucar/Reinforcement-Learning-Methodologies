package simulation;

import entity.Component;
import environment.GridWorld;
import learningType.MonteCarlo;
import learningType.PrioritizedSweeping;
import learningType.QLearning;
import learningType.Sarsa;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by toprak on 10-Apr-17.
 */
public class Main {
    public static final int SIZE = 4;
    public static final double epsilon = 0.3;
    public static final boolean hasWind = false;
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
        goal = new Component(3, 3, goalImage, "goal");
        gridWorld = new GridWorld(actor, goal, SIZE, rewardValue, epsilon);
        if(hasWind)
            gridWorld.generateWind();
        /*
        montecarlo:
        gamma=0.7 edgeReward=-15 movementReward=-5
         */
        //MonteCarlo.simulateMonteCarlo(actor, goal, gridWorld);
        /*
        qlearning:
        gamma=0 alpha =0.9 edgeReward=0 movementReward=0
         */
        //QLearning.simulateQLearning(actor,goal,gridWorld);
        Sarsa.simulateSarsa(actor,goal,gridWorld);
        //PrioritizedSweeping.simulatePS(actor, goal,gridWorld);
    }



    private static void printQTable() {
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                System.out.print(gridWorld.getqTable()[i][j].getValue() + "    ");
            }
            System.out.print("\n");
        }
    }
}
