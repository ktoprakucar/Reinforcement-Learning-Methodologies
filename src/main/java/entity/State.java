package entity;

import environment.GridWorld;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toprak on 4/25/2017.
 */
public class State {
    private List<BigDecimal> rewards = new ArrayList<BigDecimal>();
    private int counter = 0;
    private boolean isAccessed = false;
    private BigDecimal value = BigDecimal.ZERO;

    public List<BigDecimal> getRewards() {
        return rewards;
    }

    public boolean isAccessed() {
        return isAccessed;
    }

    public void setAccessed() {
        this.isAccessed = true;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void addReward(BigDecimal value) {
        this.counter++;
        rewards.add(BigDecimal.valueOf(Math.pow(GridWorld.gamma, counter)).multiply(value));
    }

    public void calculateAverageReturn() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal reward : rewards)
            sum = sum.add(reward);
        if (this.counter != 0)
            value = value.add(sum.divide(BigDecimal.valueOf(counter), 10, BigDecimal.ROUND_HALF_UP));
    }
}
