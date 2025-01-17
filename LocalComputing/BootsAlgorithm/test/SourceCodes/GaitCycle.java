package com.example.reverseengine;


import javax.swing.*;

public class GaitCycle {

    private static double [] thighAngles = { -28 , -25 , -21 , -16 , -10 , -2 , 5   , 10 , 15  , 19 , 19  , 17  ,  11 ,  1  , -8  , -17 , -24  , -29 , -29 , -29   }  ;
    private static double [] kneeAngles =  { -1  , -7  , -12 , -12 , -10 , -6 , 0.0 , 2  , 0.0 , -5 , -10 , -19 , -29 , -40 , -48 , -45 , -40  , -25 , -12 , -5    }  ;
    private static double [] ankleAngles = { 0.0 , -4  , -4  , -1  ,  2  ,  3 , 5   , 7  , 8   ,  7 , -1  , -14 , -22 , -21 , -12 , -6  ,  0.0 ,  3  ,  6  ,  7    }  ;



    public static double [] getThighAngles(){
        return Mathematics.duplicate((thighAngles)) ;
    }

    public static double [] getKneeAngles(){
        return Mathematics.duplicate((kneeAngles)) ;
    }

    public static double [] getAnkleAngles(){
        return Mathematics.duplicate((ankleAngles)) ;
    }


    public static double [] getThighVelocities(){
        return Mathematics.duplicate((Mathematics.Derivative(thighAngles))) ;
    }
    public static double [] getKneeVelocities(){
        return Mathematics.duplicate((Mathematics.Derivative(kneeAngles))) ;
    }
    public static double [] getAnkleVelocities(){
        return Mathematics.duplicate((Mathematics.Derivative(ankleAngles))) ;
    }


    public static double [] getThighAccelerations(){
        return Mathematics.duplicate((Mathematics.Derivative(getThighVelocities()))) ;
    }
    public static double [] getKneeAccelerations(){
        return Mathematics.duplicate((Mathematics.Derivative(getKneeVelocities()))) ;
    }
    public static double [] getAnkleAccelerations(){
        return Mathematics.duplicate((Mathematics.Derivative(getAnkleVelocities()))) ;
    }

    public static void setThighAngles(double [] angles){
        if(angles.length != 20){
            JOptionPane.showMessageDialog(null , "Number of items must be 20.");
            System.err.println("Number of items must be 20.");
            return ;
        }
        thighAngles = angles ;
    }

    public static void setKneeAngles(double [] angles){
        if(angles.length != 20){
            JOptionPane.showMessageDialog(null , "Number of items must be 20.");
            System.err.println("Number of items must be 20.");
            return ;
        }
        kneeAngles = angles ;
    }

    public static void setAnkleAngles(double [] angles){
        if(angles.length != 20){
            JOptionPane.showMessageDialog(null , "Number of items must be 20.");
            System.err.println("Number of items must be 20.");
            return ;
        }
        ankleAngles = angles ;
    }



    private static double [] groundVerticalForce = { 0.50 , 0.95 , 1.0 , 1.0 , 1.0 , 1.0 , 1.0 , 1.0 , 1.0 , 1.0 , 0.50 , 0.05 ,0.0 , 0.0 , 0.0 , 0.0 , 0.0 , 0.0 , 0.0 , 0.10 } ;

    public static double getVerticalGroundForceReaction(int timeStep){
        double result = groundVerticalForce[ timeStep % 20 ] ;
        return result ;
    }

    public static double [] getVerticalGroundForceReactions(){
        return Mathematics.duplicate(groundVerticalForce) ;
    }

    public static void setGroundVerticalForceReaction(double [] array){
        if(array.length != 20){
            JOptionPane.showMessageDialog(null , "Number of items must be 20.");
            System.err.println("Number of items must be 20.");
            return ;
        }
        groundVerticalForce = array ;
    }


    public static double getThighVerticalAngle(int timeStep){
        int step = timeStep % 20 ;
        return ( 0.0 + thighAngles[ step ] ) ;
    }

    public static double getKneeVerticalAngle(int timeStep){
        int step = timeStep % 20 ;
        return ( kneeAngles[ step ] - thighAngles[ step ] ) ;
    }

    public static double getAnkleHorizontalAngle(int timeStep){
        int step = timeStep % 20 ;
        return ( getKneeVerticalAngle(step) + ankleAngles[ step ] ) ;
    }



    public static void setTibialisDisorderInAgnles(){
        double [] r = new double[20] ;
        for(int i = 0 ; i < 20 ; i ++ ){
            if(groundVerticalForce[i] > 0)
                r[i] = ankleAngles[i] - GaitCycle.getAnkleHorizontalAngle(i) ;
            else
                r[i] = 0.0 ;
        }
        ankleAngles = r ;
    }



    public static void setKneeDisorderInAgnles(){
        double [] r = new double[20] ;
        for(int i = 0 ; i < 20 ; i ++ ){
            if(groundVerticalForce[i] > 0)
                r[i] = 0.0 ;
        }
        kneeAngles = r ;
    }


    public static void reset(){
        double [] thighAnglesValues = { -28 , -25 , -21 , -16 , -10 , -2 , 5   , 10 , 15  , 19 , 19  , 17  ,  11 ,  1  , -8  , -17 , -24  , -29 , -29 , -29   }  ;
        double [] kneeAnglesValues =  { -1  , -7  , -12 , -12 , -10 , -6 , 0.0 , 2  , 0.0 , -5 , -10 , -19 , -29 , -40 , -48 , -45 , -40  , -25 , -12 , -5    }  ;
        double [] ankleAnglesValues = { 0.0 , -4  , -4  , -1  ,  2  ,  3 , 5   , 7  , 8   ,  7 , -1  , -14 , -22 , -21 , -12 , -6  ,  0.0 ,  3  ,  6  ,  7    }  ;
        GaitCycle.setAnkleAngles(ankleAnglesValues);
        GaitCycle.setKneeAngles(kneeAnglesValues);
        GaitCycle.setThighAngles(thighAnglesValues);
    }



}

