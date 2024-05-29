// written by Junnior Lucero Jaramillo (lucer045) && Jesus Romero Rivera (romer309)
import java.util.Queue;
import java.util.Random;
public class Minefield {
    /**
     Global Section
     */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";
    // added new String
    public static final String ANSI_WHITE = "\u001B[37m";


    /*
     * Class Variable Section
     *
     */

    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */

    /**
     * Minefield
     *
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        this.rows = rows;
        this.columns = columns;
        this.flags = flags;
        this.minefield = new Cell[this.rows][this.columns];

        //Go through the entire board
        // Hide every status and set it to -
        for(int i = 0; i < minefield.length; i++){
            for(int j = 0; j < minefield[0].length; j++){
                minefield[i][j] = new Cell(false, "-");
            }
        }
    }

    /**
     * evaluateField
     *
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     *
     */

    public void evaluateField() {
        for (int i = 0; i < minefield.length; i++) { // loop through the array of provided input
            for (int j = 0; j < minefield[0].length; j++) {
                if(minefield[i][j].getStatus().equals("-")){ // case 1 say if we encounter a -, then let's swap it to a 0
                    minefield[i][j].setStatus("0");
                }
                // Let's check if we run into a number in the String type "0-9"
                if(!minefield[i][j].getStatus().equals("-") && !minefield[i][j].getStatus().equals("M")) {
                    // let's start off incrementing  creating a value that will store the amount of nearby mines
                    int totalMines = 0;
                    totalMines += helperChecker(i - 1, j - 1); // let's check the top left cell
                    totalMines += helperChecker(i - 1, j); // cell right above it
                    totalMines += helperChecker(i - 1, j + 1); // cell to the right of the curr cell
                    totalMines += helperChecker(i, j - 1); // cell to the left of curr cell
                    totalMines += helperChecker(i, j + 1); // cell to the right of curr cell
                    totalMines += helperChecker(i + 1, j - 1); // bottom left cell
                    totalMines += helperChecker(i + 1, j); // cell right under curr cell
                    totalMines += helperChecker(i + 1, j + 1); // cell to the bottom right

                    // Since we are working with strings, let's convert the string type to an int type
                    int myNum = Integer.parseInt(minefield[i][j].getStatus());
                    myNum += totalMines; // let's now simply increase the number at that cell with the amount of mines nearby
                    minefield[i][j].setStatus(String.valueOf(myNum)); // let's reassign the same spot with the value of the int as a Stirng.
                }
            }
        }
    }

    // This helper method will check if the row and col are in bounds
    // once we encounter a mine, then we can place a 1 in the respective 8 positions near the cell
    // then we can increment them above
    public int helperChecker(int row, int col){
        // Case 1: we can only add to adjacent cells if we are within bounds so...
        // Let's make a conditional statement checking for bounds
        if(row >= 0 && row < this.rows && col >= 0 && col < this.columns){
            // Case 2: we have found a mine
            if (minefield[row][col].getStatus().equals("M")) { // if the cell we are looking at has a mine, then
                return 1; // let's say we have 1
                // Case 3: if we don't find a mine then we also return 0
            } else {
                return 0;
            }
        }
        return 0; // if it's not in bounds, then just return 0.
    }

    /**
     * createMines
     *
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {

        int minesLeft = mines;
        // Let's use random to randomly create mines
        Random rand = new Random();

        while(minesLeft > 0){
            // randomly create a row && col
            int randomRow = rand.nextInt(rows);
            int randomCol = rand.nextInt(columns);
            // as long as our randomly generated row and col do not equal our input && we the current status is not a number or a flag
            if((randomRow != x && randomCol != y) && (minefield[randomRow][randomCol].getStatus().equals("-"))){
                // setting random cells to the mine
                minefield[randomRow][randomCol].setStatus("M");
                // decreasing form our total amount of mines possible based on the input
                minesLeft--;
            }
        }
    }


    /**
     * guess
     *
     * Check if the guessed cell is inbounds (if not done in the Main class).
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     *
     *
     * @param x       The x value the user entered.
     * @param y       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int x, int y, boolean flag) {
        //First, let's check if the guess is within the bounds of our board
        if((x >= 0 && x < minefield.length) || (y >= 0  && y < minefield[0].length)) if (flag == true) {
            // let the user place down flags, if they have enough flags, change status, change revealed, decrease from our flags, and return false
            if (this.flags > 0) {
                minefield[x][y].setStatus("F");
                minefield[x][y].setRevealed(true);
                this.flags--;
                return false;
            }// once they run out of flags
            else if(flag == true && this.flags < 0){
                System.out.println("Cannot place flag, insufficient amount");
            }
        } else {
            // let's check if the cell is a 0 and if so, lets reveal it and the nearby 0s
            if (minefield[x][y].getStatus().equals("0")) {
                minefield[x][y].setRevealed(true);
                revealZeroes(x,y);
                return false;
            }
            // if the user hits a mine lets also reveal it AND let's update our isOver variable ( indicating the game is over and they lost to true)
            else if (minefield[x][y].getStatus().equals("M")) {
                minefield[x][y].setRevealed(true);
                isOver = true;
                return isOver;
            } // for hte reaming numbers let's just reveal them
            else{
                minefield[x][y].setRevealed(true);
            }
        } // finally return true
        return true;
    }
    /**
     * gameOver
     *
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     *
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
        // as long as our variable keeping track if the game is over or not let's check if the game is still in sesions
        if(!isOver) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    // if the player has not already revealed all cells, then the game is not yet over
                    // we do not have to account if they guessed a mine, because that's already done in guess and we are using the same variable isOver
                    if (!minefield[i][j].getRevealed()) {
                        return false;
                    }

                }
            }
        } return true;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param x      The x value the user entered.
     * @param y      The y value the user entered.
     */
    public void revealZeroes(int x, int y) {
        // create the stack and push in the x and y cord
        Stack1Gen<int[]> newStack = new Stack1Gen<>();
        newStack.push(new int[]{x, y});

        while(!newStack.isEmpty()){
            // let's get the cell
            int[] currCell = newStack.pop();
            // let's save the row and col appropriately
            int currX = currCell[0];
            int currY = currCell[1];
            //let's update each cell to revealed
            minefield[currX][currY].setRevealed(true);
            // check to the left and as long as it's not already revealed
            if(currY - 1 >= 0 && minefield[currX][currY - 1].getStatus().equals("0") &&
                    minefield[currX][currY - 1].getRevealed() == false){
                // "add" aka push it into the stack
                newStack.push(new int[]{currX,currY-1});
            }
            // check to the right and as long as it's not already revealed
            if(currY + 1 < rows && minefield[currX][currY + 1].getStatus().equals("0") &&
                    minefield[currX][currY + 1].getRevealed() == false){
                // "add" aka push it into the stack
                newStack.push(new int[]{currX,currY+1});
            }
            // check the top and as long as it's not already revealed
            if(currX - 1 >= 0 && minefield[currX - 1][currY].getStatus().equals("0") &&
                    minefield[currX - 1][currY].getRevealed() == false){
                // "add" aka push it into the stack
                newStack.push(new int[]{currX-1, currY});
            }
            //check the bottom and as long as it's not already revealed
            if(currX + 1 < columns && minefield[currX + 1][currY].getStatus().equals("0") &&
                    minefield[currX + 1][currY].getRevealed() == false) {
                // "add" aka push it into the stack
                newStack.push(new int[]{currX + 1, currY});
            }
        }
    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the initial cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int x, int y) {
        // let's create the queue and add in the appropriate row and col
        Q1Gen<int[]> newQ = new Q1Gen<>();
        newQ.add(new int[]{x, y});
        // as long as we have cord pts in our queue
        while(newQ.length() > 0){
            //let's save them appropriately
            int[] currCell = newQ.remove();
            int currX = currCell[0];
            int currY = currCell[1];
            // let's reveal the nearby cells
            minefield[currX][currY].setRevealed(true);
            // if we run into a mine, let's reveal the first one, only the first one.
            if(minefield[currX][currY].getStatus().equals("M")){
                break;
            }
            else{ // check to the left if not already revealed, then do so
                if(currY - 1 >= 0 &&
                        minefield[currX][currY - 1].getRevealed() == false){
                    newQ.add(new int[]{currX,currY-1});
                } // check to the right if not already revealed, then do so
                if(currY + 1 < rows  &&
                        minefield[currX][currY + 1].getRevealed() == false){
                    newQ.add(new int[]{currX,currY+1});
                } // check the top if not already revealed, then do so
                if(currX - 1 >= 0  &&
                        minefield[currX - 1][currY].getRevealed() == false) {
                    newQ.add(new int[]{currX-1, currY});
                } // check the bottom if not already revealed, then do so
                if(currX + 1 < columns &&
                        minefield[currX + 1][currY].getRevealed() == false) {
                    newQ.add(new int[]{currX + 1, currY});
                }

            }
        }
    }
    /**
     * For both printing methods utilize the ANSI colour codes provided!
     *
     *
     *
     *
     *
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected.
     */
    public void debug() {
        // let's use a StrinbBuilder instead of a normal string, so we can make changes as needed
        StringBuilder minefieldStr = new StringBuilder();
        minefieldStr.append("  ");
        for (int j = 0; j < columns; j++) {
            minefieldStr.append(j);
            minefieldStr.append(" ");
        }
        minefieldStr.append("\n"); //get columns separated by newlines

        for (int i = 0; i < rows; i++) {
            minefieldStr.append(i);
            minefieldStr.append(" "); //get rows separated by spaces
            for (int j = 0; j < columns; j++) {
                if (minefield[i][j].getStatus().equals("0")) {
                    minefieldStr.append(ANSI_YELLOW + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("1")) {
                    minefieldStr.append(ANSI_BLUE + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("2")) {
                    minefieldStr.append(ANSI_GREEN + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("3")) {
                    minefieldStr.append(ANSI_RED + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("4")) {
                    minefieldStr.append(ANSI_PURPLE + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("5")) {
                    minefieldStr.append(ANSI_CYAN + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("6")) {
                    minefieldStr.append(ANSI_BLUE_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("7")) {
                    minefieldStr.append(ANSI_WHITE + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("8")) {
                    minefieldStr.append(ANSI_YELLOW_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("9")) {
                    minefieldStr.append(ANSI_RED_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("M")) {
                    minefieldStr.append(ANSI_RED + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                } else if (minefield[i][j].getStatus().equals("F")) {
                    minefieldStr.append(ANSI_YELLOW_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                }
                minefieldStr.append(" ");
            }
            minefieldStr.append("\n");
        }
        System.out.println(minefieldStr);
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
        // like the debug, let's create a stringbuilder to save our cells into
        StringBuilder minefieldStr = new StringBuilder();
        minefieldStr.append("  ");
        for (int j = 0; j < columns; j++) {
            minefieldStr.append(j);
            minefieldStr.append(" ");
        }
        minefieldStr.append("\n"); //get columns separated by newlines

        for(int i = 0; i < rows; i++){
            minefieldStr.append(i);
            minefieldStr.append(" "); //get rows separated by spaces
            for(int j = 0; j < columns; j++){
                if(minefield[i][j].getRevealed() == true){ //if the revealed status is true or we guessed it with flag

                    if(minefield[i][j].getStatus().equals("0")) {
                        minefieldStr.append(ANSI_YELLOW + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("1")) {
                        minefieldStr.append(ANSI_BLUE + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("2")) {
                        minefieldStr.append(ANSI_GREEN + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("3")) {
                        minefieldStr.append(ANSI_RED + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("4")) {
                        minefieldStr.append(ANSI_PURPLE + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("5")) {
                        minefieldStr.append(ANSI_CYAN + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("6")) {
                        minefieldStr.append(ANSI_BLUE_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("7")) {
                        minefieldStr.append(ANSI_WHITE + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("8")) {
                        minefieldStr.append(ANSI_YELLOW_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("9")) {
                        minefieldStr.append(ANSI_RED_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("M")) {
                        minefieldStr.append(ANSI_RED + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }
                    else if(minefield[i][j].getStatus().equals("F")) {
                        minefieldStr.append(ANSI_YELLOW_BRIGHT + minefield[i][j].getStatus() + ANSI_GREY_BACKGROUND);
                    }

                }
                else{
                    minefieldStr.append("-"); //we can append cell values that haven't been revealed yet to their default values
                }
                minefieldStr.append(" ");
            }
            minefieldStr.append("\n");
        }
        return minefieldStr.toString();
    }

    public int getFlag(){
        return this.flags;
    }

    private Cell[][] minefield;
    private int rows;
    private int columns;
    private int flags;
    boolean isOver = false;
}