package entity;

import java.math.BigDecimal;

/**
 * Created by toprak on 5/23/2017.
 */
public class PType {
    private int prevXAxis;
    private int prevYAxis;
    private int xAxis;
    private int yAxis;
    private BigDecimal pValue;

    public PType(int prevXAxis, int prevYAxis, int xAxis, int yAxis, BigDecimal pValue) {
        this.prevXAxis = prevXAxis;
        this.prevYAxis = prevYAxis;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.pValue = pValue;
    }

    public int getPrevXAxis() {
        return prevXAxis;
    }

    public void setPrevXAxis(int prevXAxis) {
        this.prevXAxis = prevXAxis;
    }

    public int getPrevYAxis() {
        return prevYAxis;
    }

    public void setPrevYAxis(int prevYAxis) {
        this.prevYAxis = prevYAxis;
    }

    public int getxAxis() {
        return xAxis;
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public BigDecimal getpValue() {
        return pValue;
    }

    public void setpValue(BigDecimal pValue) {
        this.pValue = pValue;
    }
}
