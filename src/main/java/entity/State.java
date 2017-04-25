package entity;

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

    public void setRewards(List<BigDecimal> rewards) {
        this.rewards = rewards;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public boolean isAccessed() {
        return isAccessed;
    }

    public void setAccessed(boolean accessed) {
        isAccessed = accessed;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
