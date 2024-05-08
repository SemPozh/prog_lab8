package laba6.common.data;

import laba6.common.exeptions.InvalidObjectFieldException;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Integer x;
    private double y;
    public static final int MAX_Y = 518;
    public static final int MIN_X = -393;

    public Coordinates(Integer x, double y) throws InvalidObjectFieldException {
        this.setX(x);
        this.setY(y);
    }

    public void setX(Integer x) throws InvalidObjectFieldException {
        if (validateX(x)){
            this.x = x;
        } else {
            throw new InvalidObjectFieldException("X-coordinate can't be <= -393");
        }
    }

    public void setY(double y) throws InvalidObjectFieldException {
        if (validateY(y)){
            this.y = y;
        } else {
            throw new InvalidObjectFieldException("Y-coordinate can't be > 518");
        }
    }

    private boolean validateX(Integer x){
        return x > -393;
    }

    private boolean validateY(double y){
        return y <= 518;
    }

    public Integer getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
