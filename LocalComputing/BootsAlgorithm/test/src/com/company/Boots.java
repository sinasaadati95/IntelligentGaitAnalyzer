package com.company;

public class Boots {



    private static double feetLength  = 0.26 ; // from toe to heel
    private static double legLength   = 0.38 ; // from ankle to knee
    private static double thighLength = 0.48 ; // from knee to basin


    private static double Ankle_I() {//from proximal
        return (feetMass() * 0.69 * 0.69 * feetLength * feetLength) ;  //based on Biomechanics and motor control, by David Winter.
    }

    private static double Knee_I(){
        return ( legMass() * 0.528 * 0.528 * legLength * legLength ) ;  //based on Biomechanics and motor control, by David Winter.
    }

    private static double Thigh_I_proximal(){
        return (thighMass() * 0.540 * 0.540 * thighLength * thighLength ) ; //based on Biomechanics and motor control, by David Winter.
    }

    private static double Thigh_I_distal(){
        return (thighMass() * 0.653 * 0.653 * thighLength * thighLength ) ; //based on Biomechanics and motor control, by David Winter.
    }

    private static double bodyMass = 60.0 ;

    public static double feetMass(){
        return (0.0145 * bodyMass) ; //based on Biomechanics and motor control, by David Winter.
    }

    public static double legMass(){
        return (0.0465 * bodyMass) ; //based on Biomechanics and motor control, by David Winter.
    }

    public static double thighMass(){
        return (0.100 * bodyMass) ; // //based on Biomechanics and motor control, by David Winter.
    }



    /**
     An Algorithm to do a reverse engineering of human gait.
     By  Sina Saadati
     sina.saadati@aut.ac.ir
     mss.kamran@gmail.com
     */
    public static void Boots(){
        Boots_Ankle_Gastro(); //Correct.
        Boots_Ankle_Tibia();  //Correct.
        //---------------------------
        Boots_Quadriceps();   //Correct.
        //---------------------------
        Boots_Hamstring() ; //Correct.
        ///--------------------------
        Boots_Guteus(); //Correct.
        Boots_Iliopsoas(); //Correct.
    }



    public static double [] Boots_Ankle_Gastro(){
        double [] Gastro_muscleForce = new double[ 20 ] ;
        double [] Torque = Mathematics.Multiply( Ankle_I() , GaitCycle.getAnkleAccelerations());
        double [] groundForce = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions());
        for( int i = 0 ; i < 20 ; i ++ ){
            double AnkleVerticalAngle = Mathematics.degToRadian( Mathematics.qadr(90.0 - GaitCycle.getAnkleHorizontalAngle(i)) ) ;
            ///System.out.println("Ankle Vertical angle = " + (AnkleVerticalAngle / (Math.PI / 180.0)) );
            //double Moment_groundReactionForce = + ( groundForce[i]  * 0.15 * Math.sin(AnkleVerticalAngle) )  ;
            double Moment_groundReactionForce = + ( ( get_Ground_ReactionForce_In_Toe( groundForce[i] , 0.18 , 0.8 , GaitCycle.getKneeVerticalAngle(i) , 0.37 , GaitCycle.getThighVerticalAngle(i) , 0.44 , GaitCycle.getAnkleHorizontalAngle(i) ) )  * 0.15 * Math.sin(AnkleVerticalAngle) )  ;
            double t1 = get_Ground_ReactionForce_In_Toe( groundForce[i] , 0.18 , 0.8 , GaitCycle.getKneeVerticalAngle(i) , 0.37 , GaitCycle.getThighVerticalAngle(i) , 0.44 , GaitCycle.getAnkleHorizontalAngle(i) )  ;
            ///System.out.println("Ground_reaction in toe = " + t1 );
            double Moment_feetWeight          = - ( (feetMass() * 9.8) * 0.06 * Math.sin(AnkleVerticalAngle)  );
            double Moment_total               =   Torque[i] ;
            /*
                1) Moment_total = I * a
                2) Moment_total = Moment_groundReactionForce + Moment_feetWeight + Moment_muscle
                so:
                Moment_muscle = Moment_total - ( Moment_groundReactionForce + Moment_feetWeight )
                and:
                Moment_muscle = Force_muscle * 0.06 * Sin(AnkleVerticalAngle)
                So:
                Force_muscle = Moment_muscle / ( 0.06 * Sin(AnkleVerticalAngle) )
             */
            double Moment_muscle = Moment_total - ( Moment_groundReactionForce + Moment_feetWeight ) ;
            double Force_muscle  = Moment_muscle / ( 0.06 * Math.sin(AnkleVerticalAngle) ) ;
            Gastro_muscleForce[i] = - Force_muscle ;
        }
        ///Mathematics.print(Gastro_muscleForce);
        return Gastro_muscleForce ;
    }




    public static double [] Boots_Ankle_Tibia(){
        double [] phase1 = Boots_Ankle_Tibia_phase1() ;
        double [] phase2 = Boots_Ankle_Tibia_phase2() ;
        //return Mathematics.sum( phase1 , phase2 ) ;
        double [] result = Mathematics.sum( Mathematics.Multiply(40.0 , phase1 ) , phase2 ); // Multiply is only to make it appear in the graph. NOTICE: Activity of muscle when heel wants to fly the ground is much less than when toes are impacted by the ground.
        ///Mathematics.print(result);
        return result ;
    }



    public static double [] Boots_Ankle_Tibia_phase1(){
        double [] Tibialis_muscleForce = new double[ 20 ] ;
        double [] Torque = Mathematics.Multiply( Ankle_I() , GaitCycle.getAnkleAccelerations());
        double [] groundForce = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions());
        for( int i = 0 ; i < 20 ; i ++ ){
            /*
                Tibialis is active ONLY if weight of feet is positive.
                In other words, Tibialis ONLY Controls the weight force of feet. So
                If Ground reaction force is negating the weight force of feet, Tibialis will be passive.(No activity and no contraction force)

                This can be calculated by:
                        Moment_outer = | Moment_feetWeight |  -  | Moment_groundReactionForce |
                        if( Moment_outer > 0.0 ) {
                            Tibialis is active to negate the outerMoment.
                        }else{
                            Tibialis is NOT active. Because there is NO need to be active.
                        }

                        NOTICE : If Moment_outer is negative, it does NOT mean that Gastro must be active.
                        It means that, WeightForce of feet has been negated by the ground. (Newton's third law: Reaction force is equal to action force, but in different sides.)

             */
            double AnkleVerticalAngle = Mathematics.degToRadian( Mathematics.qadr(90.0 - GaitCycle.getAnkleHorizontalAngle(i)) ) ;
            double Moment_groundReactionForce = + ( groundForce[i]     * 0.06 * Math.sin(AnkleVerticalAngle) )  ;
            double Moment_feetWeight          = - ( (feetMass() * 9.8) * 0.06 * Math.sin(AnkleVerticalAngle)  );
            double Moment_outer               = Mathematics.qadr( Moment_feetWeight )  -  Mathematics.qadr( Moment_groundReactionForce ) ;
            if(Moment_outer >= 0.0){
                /*
                    Moment_total = I * a
                    Moment_total = Moment_muscle - Moment_outer ;
                    So:
                    Moment_muscle = Moment_total + Moment_outer ;
                    Then we have:
                    Moment_muscle = Muscle_Force * 0.15 * Sin(15 degrees)
                        So:
                        Muscle_Force = Moment_muscle / ( 0.15 * Sin(15 degrees) )
                 */
                double Moment_total  = Torque[i] ;
                double Moment_muscle = Moment_total + Moment_outer ;
                double Muscle_Force  = Moment_muscle / ( 0.15 * Math.sin( Mathematics.degToRadian( 15 ) ) ) ;
                Tibialis_muscleForce[i] = Muscle_Force ;
            }else{
                Tibialis_muscleForce[i] = 0.0 ;
            }
        } /// End of FOR circle.
        ///Mathematics.print(Tibialis_muscleForce);
        return Tibialis_muscleForce ;
    }





    public static double [] Boots_Ankle_Tibia_phase2(){
        double [] Tibialis_muscleForce = new double[ 20 ] ;
        double [] Torque = Mathematics.Multiply( Ankle_I() , GaitCycle.getAnkleAccelerations());
        double [] groundForce = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions());
        for( int i = 0 ; i < 20 ; i ++ ){
            /*
                Tibialis is active ONLY to negate the groundReactionForce which is pushing the heel. (First of Stance phase)
                In this situation, we can claim that:
                    1) groundForce > 0.0
                    2) AnkleHorizontalAngle > 0.0
                        NOTICE : It is OK if the value of AnkleHorizontalAngle is small. (Because we have used sin function.)

             */
            double AnkleHorizontalAngle = GaitCycle.getAnkleHorizontalAngle(i) ;
            if( groundForce[i] > 0.0 && AnkleHorizontalAngle > 0.0 ){
                /*
                    We have:
                    AnkleVerticalAngle = 90.0 - AnkleHorizontalAngle (degrees)
                    Moment_ground     = - ( groundForce * 0.06 * Sin( AnkleVerticalAngle ) )
                    NOTICE : AnkleVerticalAngle must be non-negative. So, If  AnkleHorizontalAngle is negative, we would have: Moment_ground = 0.0
                    Moment_feetWeight = + (feetMass * 0.06 * Sin( AnkleVerticalAngle (degrees) ) )

                    We will have:
                    Moment_total = I * a
                    Moment_total = Moment_muscle + Moment_ground + Moment_feetWeight ((Note that Moment_ground is negative.))
                    So:
                    Moment_muscle = Moment_total - ( Moment_ground + Moment_feetWeight )

                    Then:
                    Moment_muscle = Muscle_Force * 0.15 * Sin( 15 degrees )
                    So:
                    Muscle_Force  = Moment_muscle / ( 0.15 * Sin( 15 degrees) )

                 */
                double AnkleVerticalAngle = 90.0 - AnkleHorizontalAngle ;
                double Moment_ground     = - ( groundForce[i] * 0.06 * Math.sin( Mathematics.degToRadian( AnkleVerticalAngle ) ) ) ;
                double Moment_feetWeight = + ( feetMass()     * 0.06 * Math.sin( Mathematics.degToRadian( AnkleVerticalAngle ) ) ) ;
                double Moment_total = Torque[i] ;

                double Moment_muscle = Moment_total - ( Moment_ground + Moment_feetWeight ) ;
                double Muscle_Force  = Moment_muscle / ( 0.15 * Math.sin( Mathematics.degToRadian(15) ) ) ;
                Tibialis_muscleForce[i] = Muscle_Force ;
            }else{
                Tibialis_muscleForce[i] = 0.0 ;
            }
        } /// End of FOR circle.
        ///Mathematics.print(Tibialis_muscleForce);
        return Tibialis_muscleForce ;
    }



    public static double getHorizontalDistanceBetweenKneeAndBodyMass( double thighVerticalAngle ,  double thighLength){
        double distance_at_knee = - ( thighLength * Math.sin(Mathematics.degToRadian(thighVerticalAngle)) ) ;
        return distance_at_knee ;
    }

    public static double getHorizontalDistanceBetweenAnkleAndKnee( double kneeVerticalAngle ,  double kneeLength ){
        double r = + ( kneeLength * Math.sin(Mathematics.degToRadian(kneeVerticalAngle)) ) ;
        return r ;
    }

    public static double getHorizontalDistanceBetweenAnkleAndBodyMass( double kneeVerticalAngle ,  double kneeLength , double thighVerticalAngle ,  double thighLength ){
        return getHorizontalDistanceBetweenKneeAndBodyMass(thighVerticalAngle , thighLength) + getHorizontalDistanceBetweenAnkleAndKnee(kneeVerticalAngle , kneeLength) ;
    }

    public static double get_Ground_ReactionForce_In_Toe(double bodyMass , double distance_toe_to_ankle , double distance_ankle_to_heel ,  double kneeVerticalAngle ,  double kneeLength , double thighVerticalAngle ,  double thighLength , double ankleHorizontalAngle ){
        double t2 = getHorizontalDistanceBetweenAnkleAndBodyMass(kneeVerticalAngle, kneeLength, thighVerticalAngle, thighLength);
        //System.out.println("HorizontalDistance (ankle , bodyMass) = " + t2 );

        if( -2.0 < ankleHorizontalAngle && ankleHorizontalAngle < +2.0 ) {
            double total_distance = distance_ankle_to_heel + distance_toe_to_ankle;
            double bodyMass_horizontal_distance_to_ankle = getHorizontalDistanceBetweenAnkleAndBodyMass(kneeVerticalAngle, kneeLength, thighVerticalAngle, thighLength);
            double distance_to_toe = distance_toe_to_ankle - bodyMass_horizontal_distance_to_ankle;
            //System.out.println("Distance (bodyMass , toe) = " + distance_to_toe );
            if (distance_to_toe < 0.0) {
                return bodyMass;
            } else {
                double g_t = (distance_to_toe / total_distance) * bodyMass;
                return g_t;
            }
        }else if(ankleHorizontalAngle >= 2.0){
            return 0.0 ;
        }else if(ankleHorizontalAngle <= -2.0){
            return bodyMass ;
        }
        System.err.println("ERROR \n\tUnexpected Situation. \n\terror code : 01.0100 ");
        return 0.0 ;
    }



    public static double get_Ground_ReactionForce_In_Heel(double bodyMass , double distance_toe_to_ankle , double distance_ankle_to_heel ,  double kneeVerticalAngle ,  double kneeLength , double thighVerticalAngle ,  double thighLength , double ankleHorizontalAngle ){
        if( -2.0 < ankleHorizontalAngle && ankleHorizontalAngle < +2.0 ) {
            double total_distance = distance_ankle_to_heel + distance_toe_to_ankle;
            double bodyMass_horizontal_distance_to_ankle = getHorizontalDistanceBetweenAnkleAndBodyMass(kneeVerticalAngle, kneeLength, thighVerticalAngle, thighLength);
            double distance_to_toe = distance_toe_to_ankle - bodyMass_horizontal_distance_to_ankle;
            if (distance_to_toe < 0.0) {
                return bodyMass;
            } else {
                double g_t = (1.0 - (distance_to_toe / total_distance)) * bodyMass;
                return g_t;
            }
        }else if(ankleHorizontalAngle >= 2.0){
            return bodyMass ;
        }else if(ankleHorizontalAngle <= 2.0){
            return 0.0 ;
        }
        System.err.println("ERROR \n\tUnexpected Situation. \n\terror code : 01.0102 ");
        return 0.0 ;
    }



    public static double [] Boots_Quadriceps(){
        double [] phase1 = Boots_Quadriceps_phase1();
        double [] phase2 = Boots_Quadriceps_phase2();
        double [] result =  Mathematics.sum(phase1 , phase2) ;
        ///Mathematics.print(result);
        return result ;
    }


    /**
     * This algorithm concentrates the quadriceps muscle activity in knee joint.
     * @return Quadriceps muscle activity during gait cycle
     */
    public static double [] Boots_Quadriceps_phase1(){
        double [] result = new double[20] ;
        double [] Knee_Torque = Mathematics.Multiply(Knee_I() , GaitCycle.getKneeAccelerations()) ;
        double [] GroundReact = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions()) ;
        for(int i = 0 ; i < 20 ; i ++ ){
            double Moment_total = Knee_Torque[i] ;
            double Moment_ground = GroundReact[i] * 0.39 * Math.sin(Mathematics.degToRadian(GaitCycle.getKneeVerticalAngle(i))) ;
            double Moment_legWeight = - ( legMass() * 9.8 * 0.15 * Math.sin(Mathematics.degToRadian(GaitCycle.getKneeVerticalAngle(i))) ) ;
            double Moment_muscle = Moment_total - ( Moment_ground + Moment_legWeight ) ;
            double Muscle_Force ;
            if(Moment_muscle > 0.0){
                Muscle_Force = Moment_muscle / ( 0.10 * Math.sin(Mathematics.degToRadian(85)) ) ;
            }else{
                Muscle_Force = 0.0 ;
            }
            result[i] = Muscle_Force ;
        }
        ///Mathematics.print(result);
        return result ;
    }




    /**
     * This algorithm concentrates on the Quadriceps muscle in Hip joint that is holding the upper body.
     * @return Quadriceps activity during gait cycle.
     */
    public static double [] Boots_Quadriceps_phase2(){
        double [] result = new double[20] ;
        double [] GroundReact = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions()) ;
        for(int i = 0 ; i < 20 ; i ++ ){
            double Moment_total  = 0.0 ; //Because Upper body is always fixed, The Moment is always equal to zero.
            double Moment_Ground = ( GroundReact[i] * getDistanceBetweenBodyMassCenterAndHipJoint(i) ) ;
            double Moment_muscle = Moment_total - Moment_Ground ;
            double Muscle_Force  = Moment_muscle / ( -1.0 * 0.02 * Math.sin(Mathematics.degToRadian(Mathematics.qadr(90.0 - GaitCycle.getKneeVerticalAngle(i)))) ) ;
            if(Muscle_Force < 0.0){
                Muscle_Force = 0.0 ; // Because the body mass center is in the interior side of hip joint.
            }
            result[i] = Muscle_Force ;
        }
        ///Mathematics.print(result);
        return result ;
    }



    /**
     * Computes the location of body mass center related to the hip joint.
     * @param timeStep Time of the gait cycle.
     * @return Horizontal distance between body mass center and hip joint. (Anterior is positive, Posterior is negative.)
     */
    public static double getDistanceBetweenBodyMassCenterAndHipJoint(int timeStep){
        double thisThithVerticalAngle = GaitCycle.getThighVerticalAngle(timeStep) ;
        double anotherThithVerticalAngle = GaitCycle.getThighVerticalAngle((timeStep + 10) % 20) ;
        double UpperBodyMass = 0.578 * bodyMass ; //Based on Biomechanics and motor control by David Winter.
        double FullLegMass   = 0.161 * bodyMass ; //Based on Biomechanics and motor control by David Winter.
        double UpperBodyMassCenterDistanceFromHip = 0.02 ; // Based on the accepted information, while standing, Body mass center is in the posterior part of the hip. So it has a rotation torque.
        double AnotherFullLegMassCenterDistanceFromHip = legLength * Math.sin(Mathematics.degToRadian(GaitCycle.getThighVerticalAngle((timeStep + 10) % 20))) ; //We have supposed that knee is the mass center of each leg(from thigh to ankle).
        double bodyMassCenter = ( ( UpperBodyMass * UpperBodyMassCenterDistanceFromHip) + (FullLegMass * AnotherFullLegMassCenterDistanceFromHip ) ) / ( UpperBodyMass + FullLegMass ) ;
        return bodyMassCenter ;
    }





    public static double [] Boots_Hamstring(){
        double [] phase1 = Boots_Hamstring_phase1() ; //Correct.
        double [] phase2 = Boots_Hamstring_phase2() ; //Correct.
        double [] result = Mathematics.sum(phase1 , phase2) ;
        ///Mathematics.print(result);
        return result ;
    }



    /**
     * In this algorithm, we will focus on the hamstring muscle rotating the knee joint.
     * @return Hamstring muscle activity during gait cycle
     */
    public static double [] Boots_Hamstring_phase1(){
        double [] result = new double[20] ;
        double [] Knee_Torque = Mathematics.Multiply(Knee_I() , GaitCycle.getKneeAccelerations()) ;
        double [] GroundReact = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions());
        for(int i = 0 ; i < 20 ; i ++ ){
            double Moment_total = Knee_Torque[i] ;
            double Moment_ground = GroundReact[i] * 0.39 * Math.sin(Mathematics.degToRadian(GaitCycle.getKneeVerticalAngle(i))) ;
            double Moment_Weight = - ( ( legMass() * 9.8 ) * 0.15 * Math.sin(Mathematics.degToRadian(GaitCycle.getKneeVerticalAngle(i))) ) ;
            double Moment_muscle = Moment_total - ( Moment_ground + Moment_Weight ) ;
            /*
                Notice:
                    Hmastring Muscle rotates the knee joint in the negative rotation side.
                    So: when the moment of muscle is negative, it has been created by Hamstring muscle.
                    We have divided the Moment_muscle into the negative for this reason.
             */
            double Muscle_Force = Moment_muscle / ( ( - 0.1 ) * Math.sin(Mathematics.degToRadian(Mathematics.qadr(90.0 - GaitCycle.getKneeAngles()[i]))) ) ;
            if(Muscle_Force < 0.0){
                Muscle_Force = 0.0 ;
            }
            result[i] = Muscle_Force ;
        }
        ///Mathematics.print(result);
        return result ;
    }



    /**
     * This Algorithm focuses on the behavior of Hamstring Muscle in the Hip joint that holds the upper body.
     * @return Muscle activity during gait cycle.
     */
    public static double [] Boots_Hamstring_phase2(){
        double [] result = new double[20] ;
        double [] GroundReact = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions()) ;
        for(int i = 0 ; i < 20 ; i ++ ){
            double Moment_total  = 0.0 ; //Because Upper body is always fixed, The Moment is always equal to zero.
            double Moment_Ground = ( GroundReact[i] * getDistanceBetweenBodyMassCenterAndHipJoint(i) ) ;
            double Moment_muscle = Moment_total - Moment_Ground ;
            double Muscle_Force  = Moment_muscle / ( -1.0 * 0.12 * Math.sin(Mathematics.degToRadian(Mathematics.qadr(90.0 - GaitCycle.getKneeVerticalAngle(i)))) ) ;
            if(Muscle_Force < 0.0){
                Muscle_Force *= -1.0  ;
            }else{
                Muscle_Force = 0.0 ;
            }
            result[i] = Muscle_Force ;
        }
        ///Mathematics.print(result);
        return result ;
    }





    public static double [] Boots_Guteus(){
        double [] result = new double[20] ;
        double [] Torque = Mathematics.Multiply(Thigh_I_proximal() , GaitCycle.getThighAccelerations()) ;
        double [] Ground = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions()) ;
        double [] Hamstring = Boots_Hamstring() ;
        double [] Quadriceps = Boots_Quadriceps() ;
        for(int i = 0 ; i < 20 ; i ++ ){
            double Moment_total     = Torque[i] ;
            double Moment_Ground    = + ( Ground[i] * (legLength + thighLength) * Math.sin(Mathematics.degToRadian(GaitCycle.getThighVerticalAngle(i))) ) ;
            double Moment_weight    = - ( ( legMass() + thighMass() + feetMass() ) * ( thighLength / 2.0 ) * Math.sin(Mathematics.degToRadian(GaitCycle.getThighVerticalAngle(i))) ) ; // The center of a fullLeg(from ankle to hip joint) is supposed to be between the knee joint and hip joint.
            double Moment_Hamstring = + ( Hamstring[i]  * thighLength * Math.sin(Mathematics.degToRadian(5.0)) ) ; ///Hamstring only can rotate the hip joint in the positive side.
            double Moment_Quadricep = - ( Quadriceps[i] * thighLength * Math.sin(Mathematics.degToRadian(5.0)) ) ;
            /*
                NOTICE:
                    Hamstring force is being used in knee joint. Only a small fraction of Hamstring force will be applied in Hip joint.
                    So, It has been multplied in Math.Sin(10 degrees) to mention this fact.
                    The real angle between Hamstring and leg is about 20 degrees.
                    This is also true for quadriceps muscle.
                    The real angle between Quadriceps and leg is 90 degrees in calculations.
             */
            double Moment_muscle = Moment_total - ( Moment_Ground + Moment_weight + Moment_Hamstring + Moment_Quadricep ) ;
            double Muscle_Force = 0.0 ;
            if(Moment_muscle > 0.0){
                Muscle_Force = Moment_muscle / ( 0.18 * Math.sin(Mathematics.degToRadian(40.0)) ) ;
            }
            result[i] = Muscle_Force ;
        }
        ///Mathematics.print(result);
        return result ;
    }









    public static double [] Boots_Iliopsoas(){
        double [] result = new double[20] ;
        double [] Torque = Mathematics.Multiply(Thigh_I_proximal() , GaitCycle.getThighAccelerations()) ;
        double [] Ground = Mathematics.Multiply(bodyMass * 9.8 , GaitCycle.getVerticalGroundForceReactions()) ;
        double [] Hamstring = Boots_Hamstring() ;
        double [] Quadriceps = Boots_Quadriceps() ;
        for(int i = 0 ; i < 20 ; i ++ ){
            double Moment_total     = Torque[i] ;
            double Moment_Ground    = + ( Ground[i] * (legLength + thighLength) * Math.sin(Mathematics.degToRadian(GaitCycle.getThighVerticalAngle(i))) ) ;
            double Moment_weight    = - ( ( legMass() + thighMass() + feetMass() ) * ( thighLength / 2.0 ) * Math.sin(Mathematics.degToRadian(GaitCycle.getThighVerticalAngle(i))) ) ; // The center of a fullLeg(from ankle to hip joint) is supposed to be between the knee joint and hip joint.
            double Moment_Hamstring = + ( Hamstring[i]  * thighLength * Math.sin(Mathematics.degToRadian(5.0)) ) ; ///Hamstring only can rotate the hip joint in the positive side.
            double Moment_Quadricep = - ( Quadriceps[i] * thighLength * Math.sin(Mathematics.degToRadian(5.0)) ) ;
            /*
                NOTICE:
                    Hamstring force is being used in knee joint. Only a small fraction of Hamstring force will be applied in Hip joint.
                    So, It has been multplied in Math.Sin(10 degrees) to mention this fact.
                    The real angle between Hamstring and leg is about 20 degrees.
                    This is also true for quadriceps muscle.
                    The real angle between Quadriceps and leg is 90 degrees in calculations.
             */
            double Moment_muscle = Moment_total - ( Moment_Ground + Moment_weight + Moment_Hamstring + Moment_Quadricep ) ;
            double Muscle_Force = 0.0 ;
            if(Moment_muscle < 0.0){
                Muscle_Force = Moment_muscle / ( -0.18 * Math.sin(Mathematics.degToRadian(40.0)) ) ;
            }
            result[i] = Muscle_Force ;
        }
        ///Mathematics.print(result);
        return result ;
    }




    public static void Boots_printResult(){
        Mathematics.print( "Gastrocnemius:" , Boots_Ankle_Gastro()); //Correct.
        Mathematics.print("Tibialis Anterior:" , Boots_Ankle_Tibia());  //Correct.
        //---------------------------
        Mathematics.print("Quadricep:" , Boots_Quadriceps());   //Correct.
        //---------------------------
        Mathematics.print("Hamstring:" , Boots_Hamstring()); ; //Correct.
        ///--------------------------
        Mathematics.print("Gluteus:" , Boots_Guteus()); //Correct.
        Mathematics.print("Iliopsoas:" , Boots_Iliopsoas()); //Correct.
    }










}
