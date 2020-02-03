package org.firstinspires.ftc.teamcode;



public class cartesianConverter{

    public cartesianConverter(){

    }

    public double[] convert(double x, double y,double turn, double angle){
        double sinDeg = Math.cos(-angle);
        double cosineDeg = Math.sin(-angle);
        double newY = (y * cosineDeg  +  x * sinDeg);
        double newX = (x * cosineDeg  -  y * sinDeg);
        double fL = newY + newX + turn;
        double fR = newY - newX - turn;
        double bL = newY - newX + turn;
        double bR = newY + newX - turn;

        double[] speedy = {fL,fR,bL,bR};
    return speedy;
    }

}
