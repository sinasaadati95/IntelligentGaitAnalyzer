package com.company;

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


    public static double [] getThighVelocities(){ return Mathematics.duplicate((Mathematics.Derivative(thighAngles))) ; }

    public static double [] getKneeVelocities(){
        return Mathematics.duplicate((Mathematics.Derivative(kneeAngles))) ;
    }

    public static double [] getAnkleVelocities(){  return Mathematics.duplicate((Mathematics.Derivative(ankleAngles))) ; }


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
        //double [] thighAnglesValues = { -28 , -25 , -21 , -16 , -10 , -2 , 5   , 10 , 15  , 19 , 19  , 17  ,  11 ,  1  , -8  , -17 , -24  , -29 , -29 , -29   }  ;


        ///Unhealthy walking:
        double [] thighAnglesValues = { 97 , 97.4 , 98.60000000000001 , 99.5 , 100.4 , 100.5 , 100.7 , 100.30000000000001 , 99.5 , 97 , 83.30000000000001 , 81.9 , 79.7 , 78 , 77.30000000000001 , 77.9 , 79.5 , 81.5 , 82.30000000000001 , 82.9 } ;
        //double [] thighAnglesValues = { 98.5 , 99.60000000000001 , 100.80000000000001 , 101.4 , 102 , 102.9 , 103.2 , 103 , 100.9 , 98.7 , 81.7 , 80.10000000000001 , 77.9 , 76.10000000000001 , 74.8 , 74.3 , 76.9 , 79 , 80.2 , 80.9 } ;
        //double [] thighAnglesValues = {96.5 , 97.7 , 98.7 , 100 , 100.9 , 101.4 , 101.10000000000001 , 100.60000000000001 , 98.30000000000001 , 83.2 , 80 , 76.10000000000001 , 73.9 , 73.3 , 73.7 , 75.60000000000001 , 78.80000000000001 , 80.9 , 82.2 , 83.30000000000001} ;
        //double [] thighAnglesValues = {95.80000000000001 , 96.4 , 96.9 , 98.10000000000001 , 99.10000000000001 , 100 , 100.2 , 99.4 , 99.2 , 97.60000000000001 , 82.10000000000001 , 80.10000000000001 , 78.80000000000001 , 78.5 , 79.10000000000001 , 79.80000000000001 , 80.10000000000001 , 81.4 , 82.7 , 82.7} ;
        //double [] thighAnglesValues = {95.30000000000001 , 95.9 , 96.10000000000001 , 96.7 , 97.5 , 97.9 , 82.10000000000001 , 81.10000000000001 , 79.5 , 78.7 , 78.7 , 78.4 , 77.9 , 77.7 , 78.4 , 80.5 , 83.2 , 84.10000000000001 , 84.80000000000001 , 84.60000000000001} ;
        //double [] thighAnglesValues  = {98.10000000000001 , 98.80000000000001 , 100.10000000000001 , 101.2 , 102.10000000000001 , 102.60000000000001 , 103 , 102.7 , 102.30000000000001 , 101.60000000000001 , 100.60000000000001 , 79.7 , 77.9 , 77.4 , 78.10000000000001 , 78.80000000000001 , 79.9 , 81.5 , 82.9 , 82.9} ;
        //double [] thighAnglesValues = {97.80000000000001 , 98.2 , 98.80000000000001 , 99.30000000000001 , 99.60000000000001 , 100 , 99.80000000000001 , 99.60000000000001 , 99.4 , 99.4 , 80.4 , 80.10000000000001 , 80.2 , 80.30000000000001 , 80.4 , 80.30000000000001 , 80.30000000000001 , 81.5 , 83.60000000000001 , 84.4} ;
        //double [] thighAnglesValues = {100.9 , 101 , 101.60000000000001 , 102.2 , 103.10000000000001 , 103.2 , 103.5 , 103.2 , 103.2 , 102.9 , 77.80000000000001 , 77.9 , 78 , 77.9 , 78.80000000000001 , 79 , 79 , 79.80000000000001 , 80.4 , 81} ;
        //double [] thighAnglesValues = {100 , 100.30000000000001 , 100.80000000000001 , 100.9 , 101.10000000000001 , 101.10000000000001 , 79.10000000000001 , 79.30000000000001 , 78.9 , 77.80000000000001 , 77 , 76.60000000000001 , 76.9 , 78 , 79 , 79.7 , 80.60000000000001 , 81 , 81.10000000000001 , 80.80000000000001} ;
        //double [] thighAnglesValues = {95.5 , 95.9 , 96.60000000000001 , 97.30000000000001 , 97.9 , 98.2 , 98.2 , 97.9 , 97.7 , 97.2 , 96.2 , 83.7 , 82.7 , 83.2 , 83.60000000000001 , 84.5 , 84.4 , 84.60000000000001 , 85.5 , 85.80000000000001} ;



        ///Healthy walking:
        //double [] thighAnglesValues = {98.5 , 101 , 105.2 , 107.10000000000001 , 107.30000000000001 , 102.5 , 94.9 , 83.80000000000001 , 75.8 , 69.2 , 63.800000000000004 , 62 , 62.5 , 62.7 , 61.1 , 61 , 64.4 , 67.7 , 73.10000000000001 , 79.9} ;
        //double [] thighAnglesValues = {104.2 , 105.5 , 107.7 , 109.60000000000001 , 110.80000000000001 , 109.9 , 105.5 , 99.10000000000001 , 79.7 , 74.7 , 68.7 , 63.6 , 60 , 58.2 , 58.6 , 61.2 , 63 , 64.5 , 67 , 71.9} ;
        //double [] thighAnglesValues = {105.10000000000001 , 106.7 , 107.9 , 109 , 109.2 , 108.5 , 104.7 , 98.4 , 78.9 , 72.7 , 66 , 62.900000000000006 , 60.900000000000006 , 61.1 , 61 , 61.900000000000006 , 65.7 , 68.2 , 72.8 , 75.4};
        //double [] thighAnglesValues = {98.80000000000001 , 100.80000000000001 , 103.60000000000001 , 106.2 , 108.60000000000001 , 108.7 , 105.60000000000001 , 93.80000000000001 , 82.10000000000001 , 74 , 68.5 , 64.8 , 63.6 , 63.5 , 62.6 , 62.2 , 61.800000000000004 , 65.7 , 70.5 , 76.7} ;
        //double [] thighAnglesValues = {103.2 , 103.80000000000001 , 105.9 , 106.9 , 106.80000000000001 , 104.4 , 98.80000000000001 , 80.7 , 76.80000000000001 , 69.9 , 65.5 , 62.400000000000006 , 60.5 , 60.2 , 60 , 61.5 , 65 , 67.5 , 71.3 , 75.4} ;
        //double [] thighAnglesValues = {104 , 105.30000000000001 , 106.9 , 108.30000000000001 , 109.2 , 108.10000000000001 , 105 , 79 , 76.80000000000001 , 72 , 64.10000000000001 , 59.900000000000006 , 57.5 , 56.7 , 58.2 , 61.5 , 64.8 , 69.5 , 73.7 , 76.80000000000001} ;
        //double [] thighAnglesValues = {97.80000000000001 , 98.9 , 100.60000000000001 , 102 , 101.80000000000001 , 97.10000000000001 , 87.5 , 81.60000000000001 , 73.8 , 69 , 64.2 , 61.6 , 60.800000000000004 , 60.7 , 61 , 62.7 , 67.5 , 71.5 , 77.30000000000001 , 80.30000000000001} ;
        //double [] thighAnglesValues = {103.5 , 104.4 , 105.80000000000001 , 107.30000000000001 , 107.80000000000001 , 106.80000000000001 , 102.5 , 79.5 , 74 , 69 , 62.5 , 58.800000000000004 , 57.900000000000006 , 58.800000000000004 , 60 , 60.800000000000004 , 64.3 , 66.60000000000001 , 71.7 , 75.2} ;
        //double [] thighAnglesValues = {99.80000000000001 , 100.2 , 102.2 , 104 , 104.10000000000001 , 103.2 , 97.5 , 82.4 , 74.9 , 70.8 , 66.10000000000001 , 64.2 , 63.400000000000006 , 62.900000000000006 , 62.1 , 61.7 , 63.7 , 68.7 , 73.3 , 78.80000000000001} ;
        //double [] thighAnglesValues = {102.9 , 103.7 , 105.9 , 107.30000000000001 , 108.30000000000001 , 107.5 , 105.5 , 99.4 , 82.9 , 76.4 , 70.60000000000001 , 65.5 , 61.2 , 60.1 , 61.300000000000004 , 62.6 , 63.1 , 67.3 , 70.4 , 75.4} ;



        //double [] kneeAnglesValues =  { -1  , -7  , -12 , -12 , -10 , -6 , 0.0 , 2  , 0.0 , -5 , -10 , -19 , -29 , -40 , -48 , -45 , -40  , -25 , -12 , -5    }  ;

        //Healthy:
        //double [] kneeAnglesValues = { 98.10000000000001 , 99.4 , 100.80000000000001 , 102.7 , 105.5 , 109.9 , 116.2 , 125.2 , 136.3 , 146 , 141.3 , 133.8 , 118.9 , 102 , 77 , 63.900000000000006 , 63.400000000000006 , 70.5 , 76.5 , 79.60000000000001 } ;
        //double [] kneeAnglesValues = { 96.60000000000001 , 96.9 , 98.2 , 100.7 , 104.7 , 110.5 , 118.5 , 128 , 137.5 , 139.1 , 132.3 , 124.60000000000001 , 109.80000000000001 , 95.80000000000001 , 73.3 , 63.2 , 65.5 , 75.3 , 81.2 , 83 } ;
        //double [] kneeAnglesValues = { 101 , 102 , 102.60000000000001 , 103.9 , 105 , 107.4 , 111.30000000000001 , 116.5 , 123.9 , 132 , 142.4 , 144.6 , 140.20000000000002 , 129.4 , 115.10000000000001 , 104.30000000000001 , 69.9 , 64 , 62.800000000000004 , 70.3 } ;

        //Unhealthy:
        double [] kneeAnglesValues = {91.80000000000001 , 94.2 , 96.4 , 97.9 , 98.80000000000001 , 99.9 , 100.4 , 100.5 , 100.4 , 100.30000000000001 , 100.2 , 100.30000000000001 , 101.10000000000001 , 104 , 104.80000000000001 , 101.5 , 96.60000000000001 , 93 , 90.60000000000001 , 88.4 } ;
        //double [] kneeAnglesValues = {94.7 , 95.7 , 96.5 , 97.5 , 98.30000000000001 , 98.9 , 98.9 , 98.7 , 98.60000000000001 , 98.7 , 98.5 , 100.2 , 103.5 , 106 , 101.10000000000001 , 93.10000000000001 , 85.4 , 83.4 , 84.2 , 85.80000000000001} ;
        //double [] kneeAnglesValues = {93.80000000000001 , 94.7 , 97.4 , 99 , 100.5 , 101.10000000000001 , 101.60000000000001 , 101.60000000000001 , 101.5 , 101.7 , 101.60000000000001 , 101.60000000000001 , 101.60000000000001 , 102.80000000000001 , 106.80000000000001 , 109.30000000000001 , 104.30000000000001 , 96.7 , 95 , 84 } ;


        //double [] ankleAnglesValues = { 0.0 , -4  , -4  , -1  ,  2  ,  3 , 5   , 7  , 8   ,  7 , -1  , -14 , -22 , -21 , -12 , -6  ,  0.0 ,  3  ,  6  ,  7    }  ;
        double [] ankleAnglesValues = { 0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0 ,  0.0  } ;


        for (int i = 0; i < thighAnglesValues.length; i++) {
            thighAnglesValues[i] -= 90.0 ;
        }

        for (int i = 0; i < thighAnglesValues.length; i++) {
            kneeAnglesValues[i] -= 90.0 ;
        }


        GaitCycle.setAnkleAngles(ankleAnglesValues);
        GaitCycle.setKneeAngles(kneeAnglesValues);
        GaitCycle.setThighAngles(thighAnglesValues);
    }


}
