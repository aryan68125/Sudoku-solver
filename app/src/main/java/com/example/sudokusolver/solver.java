package com.example.sudokusolver;

import java.util.ArrayList;

public class solver {
    //create a 2D Array to store the values of sudoku game board
    int [][] board;
    //Make a 2D ArrayList
    ArrayList<ArrayList<Object>> emptyBoxIndex;

    //selected row
    int selected_row;
    //selected column
    int selected_column;

    //creating a constructor
    solver()
    {
        selected_row = -1;
        selected_column=-1;
        board = new int[9][9]; //because our sudoku board is divided in 9x9 cells
        //fill in the gameboard with a bunch of zeros
        for(int row=0; row<9;row++)
        {
            for(int column = 0;column<9;column++)
            {
                board[row][column] = 0;
            }
        }
        //emptyBoxIndex will store all the rows and columns with the value zero in them
        //every zero within the sudoku board will be assigned a row and a column within this emptyBoxIndex
        //any integer value entered by the user will not be stored in emptyBoxIndex
        emptyBoxIndex = new ArrayList<>();
    }

    //method to extract the rows and columns of an empty box
    public void getemptyBoxIndexs()
    {
        //creating two for loops
        //these for loops will iterate through our game board and identify zero values which would be an empty box on our sudoku board
        for(int row=0; row<9;row++)
        {
            for(int column = 0;column<9;column++)
            {
                if(this.board[row][column]==0)
                {
                    //if true then add a new Array in emptyBoxIndex ArrayList and then assign that array a row and column within it
                    this.emptyBoxIndex.add(new ArrayList<>());
                    //now we need to grab this new Array in emptyBoxIndex that we just added
                    //here refer to the very last arrayList because when we place this in it gets tacked on to the very end
                    //this.emptyBoxIndex.size() - 1 => this gonna grab the index value of the last element that we just placed in
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(row); //.add(row) will add in the corresponding row
                    this.emptyBoxIndex.get(this.emptyBoxIndex.size() - 1).add(column); //.add(column) will add in the corresponding column
                }

            }
        }
    }

    //this method will check the entered number by the computer is according to the sudoku rules or not
    private boolean check(int rows, int columns)
    {
        //make sure that we are not checking an empty cell
        if(this.board[rows][columns]>0)
        {
            //now we can check horizontal and vertical boxes
            for(int i=0;i<9;i++)
            {
                //here we will perform vertical and horizontal checks these cells that were passed into these check method

                //this is gonna be for our rows
                //checking the cells in the same row
                //this.board[i][columns] == this.board[rows][columns]
                //i!=rows prevent the check from happening if the cell and the box have the same row number
                if(this.board[i][columns] == this.board[rows][columns] && i!=rows)
                {
                    return false;
                }

                //this is gonna be for our columns
                if(this.board[rows][i] == this.board[rows][columns] && i!=columns)
                {
                    return false;
                }
            }


            //now performing our box check
            //we have to prevent the same numbers from inserting into the same box
            int boxrow = rows/3;
            int boxcolumn = columns/3;
            //now that we've got the rows and columns for each individual boxes in the sudoku table we can just iterate through the cells of each boxes in the sudoku table
            for(int r = boxrow*3; r<boxrow*3+3;r++)
            {
                for(int c = boxcolumn*3; c<boxcolumn*3+3;c++)
                {
                    //check if a specific cell in a column has a number within that box
                    if(this.board[r][c] == this.board[rows][columns] && rows!=r && columns!=c)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    //this method is resposible for solving our sudoku board for us
    public boolean Solve(sudoku_board display)
    {
        //here we are initializing the row and column with a -1 because
        //if we do not find an empty cell then that means we will return true
        int row= -1;
        int column= -1;

        //here we will iterate through the entire sudoku board and try to find 0 values
        //this for loop will only gets executed when the row and column is not equal to -1
        for(int r=0;r<9;r++)
        {

            for(int c=0;c<9;c++)
            {
                   //this if statement will determine if the cells in the board have a zero value or not
                //if yes then save the row and column so that we can use it for later logical calculations
                if(this.board[r][c] == 0)
                {
                    row = r;
                    column = c;
                    break;
                }
            }

        }

        //if we do not find an empty cell then that means we will return true and the situation where this will happen is when either the row = -1 or the column = -1
        //which means that our sudoku board is solved
        if(row == -1 || column == -1)
        {
            return true;
        }

        //we are gonna use this for loop to place values into the empty cells of the sudoku board
        //this is what solving our sudoku board recursively

        for(int i=1; i<10;i++)
        {
            this.board[row][column] = i; //place in the value of i in the sudoku board cells that we just got earlier
            display.invalidate(); //this will refresh the sudoku board

            //check the validity of the number that we just placed in the empty cell of sudoku board
            if(check(row,column))
            {
                if(Solve(display)) //call the Solve function recursively if the check method returns false
                {
                    return true;
                }
            }

            //here we will implement the backtracking logic
            //this code below will set the cell that we've found earlier if the number is not valid and it will start backtracking
            this.board[row][column] = 0;
        }
        return false; //if we ddon't ever hit any of these true statements
    }

    //now create a reset board method that will reset our sudoku board
    public void resetBoard()
    {
        //reset the gameboard
        //reset the 2D Array that we've created
        for(int row=0; row<9;row++)
        {
            for(int column = 0;column<9;column++)
            {
                board[row][column] = 0;
            }
        }

        //reset the ArrayList that contains all the empty cells
        this.emptyBoxIndex = new ArrayList <>();
    }

    //this function will delete a number from the selected cell on the sudoku board
    public void resetNumberPosition()
    {
        //first make sure that the user actually clicked a row and a column
        //if the default value of selected_row and selected_column = -1 then we don't want anything to happen because this means the user did not click on any cell in our sudoku board
        if(this.selected_row!=-1 && this.selected_column!=-1)
        {
                this.board[this.selected_row-1][this.selected_column-1] = 0;

        }
    }

    //This method is going to set a new number position within our sudoku board
    //this is gonna update our gameboard with a new number that the user wants to add in to the sudoku board
    public void setNumberPosition(int num)
    {
        //first make sure that the user actually clicked a row and a column
        //if the default value of selected_row and selected_column = -1 then we don't want anything to happen because this means the user did not click on any cell in our sudoku board
        if(this.selected_row!=-1 && this.selected_column!=-1)
        {
            //here we need to take into consideration that the user can make a mistake in entering a number
            //so in order to tackle this problem I am going to implement so when the user double taps a cell in a sudoku board it deletes the entered number in that cell
            //the way it will delet a number is by replacing the number with a zero
            if(this.board[this.selected_row-1][this.selected_column-1] == num)
            {
                this.board[this.selected_row-1][this.selected_column-1] = 0;
            }
            else {
                this.board[this.selected_row-1][this.selected_column-1] = num;
            }
        }
    }

    //create getter for our game board
    public int[][] getBoard()
    {
        return this.board;
    }

    public ArrayList<ArrayList<Object>> getemptyBoxIndex(){
        return this.emptyBoxIndex;
    }

    //create a getters for our constructor
    public int getSelectedRow(){
        return selected_row;
    }
    public int getSelectedColumn(){
        return selected_column;
    }

    //create setters for our constructor
    public void setSelectedRow(int row)
    {
        selected_row = row;
    }
    public void setSelectedColumn(int column)
    {
        selected_column = column;
    }
}
