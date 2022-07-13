/*
Author: Tony Pham
Date: 9/22/2021

Description: Asks user for # of pills and simulations. Each day half a pill is eaten and the other half is put back into the bottle. Outputs
    a table showing days and amount of whole/half pills.

Resources used: DecimalFormat to round to 2 decimal places, a little about ArrayList, and printf text space formatting.
 */


import java.util.Random;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.text.DecimalFormat;


public class Main {

    private static boolean reEnter;
    private static int pillNum;     //User input for pill number
    private static int simNum;      //User input for simulation number
    private static int day = 0;
    private static final Scanner input = new Scanner(System.in);
    private static final DecimalFormat df = new DecimalFormat("0.00"); //Round to 2 decimal places

    static ArrayList<Integer> pillBottle = new ArrayList<>();       //Used for keeping track whole/half pills
    static ArrayList<Integer> dailyWholePills = new ArrayList<>();  //Used for keeping track of whole pills for 1 run
    static ArrayList<Integer> dailyHalfPills = new ArrayList<>();   //Used for keeping track of half pills for 1 run
    static ArrayList<Integer> totalHalfPills = new ArrayList<>();   //Used for keeping track of half pills for entire simulation
    static ArrayList<Integer> totalWholePills = new ArrayList<>();  //Used for keeping track of whole pills for entire simulation
    static ArrayList<Float> totalRatio = new ArrayList<>();         //Used for keeping track of ratio for entire simulation

    static void menu() {
        System.out.println("Input number of pills and number of simulations to empty pills in the bottle. Half a pill is consumed each day.");
    }

    static void pillValidate(){ //Validates user input for pills, has to be a integer between 1 to 1000
        System.out.println("How many pills will there be?:");
        do {
            reEnter = false;
            try {
                pillNum = input.nextInt();
                if (pillNum < 1 || pillNum > 1000) {
                    throw new Exception();
                }
            } catch (InputMismatchException ex) {
                reEnter = true;
                System.out.println("Not an integer, re-enter:");
                input.next();
            } catch (Exception ex) {
                reEnter = true;
                System.out.println("Number of pills should between 1 to 1000, re-enter:");
            }
        } while (reEnter);
    }

    static void simValidate(){ //Validates user input for simulations, has to be a integer between 1 to 1000
        System.out.println("How many simulations will be run?:");
        do {
            reEnter = false;
            try {
                simNum = input.nextInt();
                if (simNum < 1 || simNum > 1000) {
                    throw new Exception();
                }
            } catch (InputMismatchException ex) {
                reEnter = true;
                System.out.println("Not an integer, re-enter:");
                input.next();
            } catch (Exception ex) {
                reEnter = true;
                System.out.println("Number of simulations should between 1 to 1000, re-enter:");
            }
        } while (reEnter);
    }

    static void countPills(){ //Keeps track of whole/half pills in pillBottle ArrayList
        int wholePills = 0;
        int halfPills = 0;
        for (int i = 0; i < pillBottle.size(); i++){
            int count = pillBottle.get(i);
            if (count == 1){
                wholePills++;
            }
            else if (count == 0){
                halfPills++;
            }
        }
        day++;
        dailyWholePills.add(wholePills);
        dailyHalfPills.add(halfPills);
    }

    static void emptyBottle(){ //Main function to draw a whole or half pill
        Random rand = new Random();
        int randomNum;
        int isThereWholePill = pillBottle.indexOf(1);
        int isThereHalfPill = pillBottle.indexOf(0);

        while(isThereHalfPill != -1 || isThereWholePill != -1){
            randomNum = rand.nextInt(2);
            if (randomNum == 1){
                isThereWholePill = pillBottle.indexOf(1);
                if (isThereWholePill == -1){
                    isThereHalfPill = pillBottle.indexOf(0);
                    if (isThereHalfPill == -1){
                        continue;
                    }
                    else{
                        countPills();
                        pillBottle.remove(isThereHalfPill);
                    }
                }
                else{
                    countPills();
                    pillBottle.set(isThereWholePill, 0);
                }
            }

            if (randomNum == 0){
                isThereHalfPill = pillBottle.indexOf(0);
                if (isThereHalfPill == -1){
                    isThereWholePill = pillBottle.indexOf(1);
                    if (isThereWholePill == -1){
                        continue;
                    }
                    else{
                        countPills();
                        pillBottle.set(isThereWholePill, 0);
                    }
                }
                else{
                    countPills();
                    pillBottle.remove(isThereHalfPill);
                }
            }
        }
    }

    public static void main(String[] args) {
        int simTimes = 0;

        menu();
        pillValidate();
        simValidate();

        System.out.println("Number of pills in the bottle: " + pillNum);
        System.out.println("Number of simulations you want to run: " + simNum + "\n");

        for (int i = 0; i < (2*pillNum); i++){  //Initialize ArrayList
            totalWholePills.add(0);
            totalHalfPills.add(0);
            totalRatio.add(0F);
        }

        while(simTimes < simNum){
            day = 0;
            for (int i = 0; i < pillNum; i++){ //Fill pillBottle ArrayList full of whole pills
                pillBottle.add(1);
            }

            emptyBottle();

            //Print Header
            System.out.println("\nSimulation #" + (simTimes + 1));
            System.out.printf("%-10s%-15s%-15s%-15s\n", "Day", "Whole Pills", "Half Pills", "Ratio of Half pills to total");
            System.out.println("---------|--------------|-------------|------------------------------------");

            //Print information for 1 simulation after pillBottle is empty
            for (int i = 0; i < day; i++){
                String ratio = df.format(((float)dailyHalfPills.get(i) / pillNum));
                System.out.printf("%-10s%-15s%-15s%-15s\n", (i+1), dailyWholePills.get(i), dailyHalfPills.get(i), ratio);

                totalWholePills.set(i, (totalWholePills.get(i) + dailyWholePills.get(i)));
                totalHalfPills.set(i, (totalHalfPills.get(i) + dailyHalfPills.get(i)));
                totalRatio.set(i, (totalRatio.get(i) + ((float)dailyHalfPills.get(i) / pillNum)));
            }

            simTimes++;
            pillBottle.clear();
            dailyHalfPills.clear();
            dailyWholePills.clear();
        }

        //Print header for after all simulations are done
        System.out.println("\nTotal:");
        System.out.printf("%-10s%-20s%-20s%-20s\n", "Day", "AVG Whole Pills", "AVG Half Pills", "AVG Ratio of Half pills to total");
        System.out.println("---------|-------------------|-------------------|--------------------------");

        //Print information on averages of all simulations
        for (int i = 0; i < (2*pillNum); i++){
            int avgWholePills = totalWholePills.get(i) / simNum;
            int avgHalfPills = totalHalfPills.get(i) / simNum;
            String avgRatio = df.format(totalRatio.get(i) / simNum);

            System.out.printf("%-10s%-20s%-20s%-20s\n", (i+1), avgWholePills, avgHalfPills, avgRatio);
        }

        //Wait user to press ENTER to end
        System.out.println("\nPress \"ENTER\" to continue...");
        input.nextLine();
        input.nextLine();
        input.close();
    }
}
