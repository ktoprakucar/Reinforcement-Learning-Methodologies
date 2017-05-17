package entity;

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by toprak on 4/26/2017.
 */
public class StateTest {
    State state = new State();

    @Ignore
    @Test
    public void test_calculate_average_return(){
        state.addReward(BigDecimal.valueOf(10.0));
        state.addReward(BigDecimal.valueOf(20.0));
        state.addReward(BigDecimal.valueOf(30.0));
        state.addReward(BigDecimal.valueOf(40.0));
        state.addReward(BigDecimal.valueOf(50.0));
        state.calculateAverageReturn();
        assertEquals(state.getValue().setScale(2, BigDecimal.ROUND_HALF_UP), BigDecimal.valueOf(9.02));
    }
}
