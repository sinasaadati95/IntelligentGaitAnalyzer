package com.example.reverseengine;


public class Mathematics {


    public static double [] Derivative(double [] array){
        double [] result = new double[array.length] ;
        for (int i = 0 ; i < ( result.length - 1 ) ; i ++ ){
            result [i] = ( array[i + 1] - array[ i ] ) ;
        }
        result[result.length - 1] = ( array[0] - array[array.length - 1] ) ;
        return result ;
    }

    public static void print(double [ ] array){
        for(int i = 0 ; i < (array.length - 1 ) ; i ++ ){
            System.out.print(array[i] + "  ,  ");
        }
        System.out.println(array[array.length-1] + "  ");
    }


    public static void print(String Title , double [] array){
        System.out.println(Title);
        print(array);
        System.out.println("      ");
    }

    public static double [] duplicate(double [] array){
        double [] result = new double[array.length] ;
        for( int i = 0 ; i < result.length ; i ++ ){
            result[i] = array[i] ;
        }
        return result ;
    }

    public static double [] Multiply(double x , double [] array){
        double [] result = new double[array.length] ;
        for( int i = 0 ; i < result.length ; i ++ ){
            result[i] = ( x * array[i] ) ;
        }
        return result ;
    }

    public static double degToRadian(double angle){
        return ( angle * 0.01745329251 ) ;
    }

    public static double RadianToDeg(double angle){
        return ((angle / (Math.PI / 180.00))) ;
    }

    public static double [] degToRadian(double [] array){
        double [] result = new double[array.length] ;
        for(int i = 0 ; i < result.length ; i ++ ){
            result[i] = degToRadian(array[i]) ;
        }
        return result ;
    }


    public static double [] sum(double [] a , double [] b ) {
        if(a.length != b.length){
            System.err.println("ERROR \n\tSize of vectors are not equal.");
            return null ;
        }
        double [] result = new double[a.length] ;
        for (int i = 0 ; i < a.length ; i ++ ){
            result[i] = a[i] + b[i] ;
        }
        return result ;
    }



    public static double qadr(double value){
        if(value >= 0.0)
            return value ;
        return ( - value) ;
    }

    public static double removeNegative(double value){
        if(value >= 0.0)
            return value;
        return 0.0 ;
    }


    public static double [] removeNegatives(double [] array){
        double [] result = Mathematics.duplicate(array);
        for( int i = 0 ; i < result.length ; i ++ ){
            result [i] = removeNegative(array[i]);
        }
        return result ;
    }



    public static double [] sum_multiVector(double[]...arrays){
        double [] result = new double[arrays[0].length] ;
        for (double [] vector : arrays) {
            for (int i = 0 ; i < result.length ; i ++ ){
                result[i] += vector[i] ;
            }
        }
        return result ;
    }



}
