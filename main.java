// written by Junnior Lucero Jaramillo (lucer045) && Jesus Romero Rivera (romer309)
//Import Section
import java.util.Random;
import java.util.Scanner;
/*
 * Provided in this class is the neccessary code to get started with your game's implementation
 * You will find a while loop that should take your minefield's gameOver() method as its conditional
 * Then you will prompt the user with input and manipulate the data as before in project 2
 *
 * Things to Note:
 * 1. Think back to project 1 when we asked our user to give a shape. In this project we will be asking the user to provide a mode. Then create a minefield accordingly
 * 2. You must implement a way to check if we are playing in debug mode or not.
 * 3. When working inside your while loop think about what happens each turn. We get input, user our methods, check their return values. repeat.
 * 4. Once while loop is complete figure out how to determine if the user won or lost. Print appropriate statement.
 */
public class main {

    public static void main(String[] args){
        // Let's declare and initialize the variables we will need to create our minefield && let's keep a flag variable (debugMode) to see if the user wants to play in the debug mode
        int row = 0;
        int col = 0;
        int flag = 0;
        boolean debugMode = false;

        // Let's start off by asking the user for the appropriate difficulty mode
        Scanner myScanner = new Scanner(System.in);
        System.out.println("What level of difficulty do you want to play? \n Please enter the game mode exactly as seen below:   \n\t 1) Easy  \n\t 2) Medium  \n\t 3) Hard");
        String difficultyInput = myScanner.nextLine();

        //Give the appropriate rows && cols && flags to the corresponding difficulty levels
        switch (difficultyInput){
            // case 1
            case "Easy":
                row = 5;
                col = 5;
                flag = 5;
                break;
            // case 2
            case "Medium":
                row = 9;
                col = 9;
                flag = 12;
                break;
            // case 3
            case "Hard":
                row = 20;
                col = 20;
                flag = 40;
                break;
        }

        // Let's ask if the user wants to play in the debug mode or not
        while(true) {
            System.out.println("Would you like to play in debug mode? \n Please enter the game mode exactly as seen below: \n\t Yes \n\t No");
            String modeDebug = myScanner.nextLine();

            if (modeDebug.equals("Yes")) {
                // update the flag accordingly
                debugMode = true;
                break;
            } else if (modeDebug.equals("No")) {
                // update the flag accordingly
                debugMode = false;
                break;
            } else {
                System.out.println("Please enter Yes or No");
            }
        }


        // Step 1: Make the appropriate minefield after asking for difficulty level && if they want to play in debug or not
        Minefield minefield = new Minefield(row, col, flag);

        // let's not put this in the !minefield.gaveOver while loop, because we should only ask this once.
        System.out.println("Enter your starting coordinates [x] [y]: ");
        int startingX = myScanner.nextInt();
        int startingY = myScanner.nextInt();
        // Step 2: placing the mines on the minefield
        minefield.createMines(row,col,flag);
        //Step 3: now that we have mines, we need to change the status of those surrounding cells
        minefield.evaluateField();
        //Step 4: we need to reveal the cells of the starting area to them
        minefield.revealStartingArea(startingX, startingY);

        // If the user wants to play in debug mode then we simply call debug
        if(debugMode){
            // The entire board should be displayed with all cells revealed each turn
            minefield.debug();
        }
        // Always print out the minefield
        System.out.println(minefield);


        // While the game is in session
        while(!minefield.gameOver()){
            // Ask for a user's turn
            System.out.println("Please enter a coordinate and if you wish to place a flag (flags remaining: " + minefield.getFlag() + ")" +" \n\t [x] --> enter your row  \n\t [y] --> enter your column \n\t [f(-1,else)] --> enter a 1 to place a flag or 0 to NOT place a flag");
            int myRow = myScanner.nextInt();
            int myCol= myScanner.nextInt();
            boolean inputFlag;
            int myFlag;

            //Let's make a while loop so that the user can only either enter a 1 (yes flag) or 0 (no flag)
            while (true) {
                myFlag = myScanner.nextInt();
                if(myFlag == 1 || myFlag == 0){
                    break;
                } else {
                    System.out.println("please enter 1) to place a flag or \n 0 to NOT place a flag");
                }
            }
            // let's update our boolean variable accordingly
            if(myFlag == 1){
                inputFlag = true;
            } else{
                inputFlag = false;
            }
            // Let's update the guess output
            boolean myReturn = minefield.guess(myRow, myCol, inputFlag);

            //Let's inform the user if their guess was made or not
            if(myReturn){
                System.out.println("Your guess was successfully made");
            } else {
                System.out.println("Guess was NOT successfully made");
            }

            // Say if the user wants to play using debug mode, then..
            if(debugMode){
                // the entire board should be displayed with all cells revealed each turn.
                minefield.debug();
            }
            System.out.println(minefield);

        }
        // Determine who won or lost .... and print that out
        if(!minefield.isOver) {
            System.out.println("You won");
        } else {
            System.out.println("You lost");
        }
    }
}
