package com.example.sudokusolver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private sudoku_board gameBoard;
    private solver gameBoardSolver;

    Button solveBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameBoard = findViewById(R.id.SudokuBoard);
        //grab the instance of the solver class that was assign to the sudoku board
        //and set to this gameBoardSolver variable here
        gameBoardSolver = gameBoard.getsolver();

        solveBTN = findViewById(R.id.button_solve);

    }

    public void BTNOnePress(View view)
    {
        gameBoardSolver.setNumberPosition(1); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNTwoPress(View view)
    {
        gameBoardSolver.setNumberPosition(2); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNThreePress(View view)
    {
        gameBoardSolver.setNumberPosition(3); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNFourPress(View view)
    {
        gameBoardSolver.setNumberPosition(4); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNFivePress(View view)
    {
        gameBoardSolver.setNumberPosition(5); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNSixPress(View view)
    {
        gameBoardSolver.setNumberPosition(6); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNSevenPress(View view)
    {
        gameBoardSolver.setNumberPosition(7); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNEightPress(View view)
    {
        gameBoardSolver.setNumberPosition(8); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNNinePress(View view)
    {
        gameBoardSolver.setNumberPosition(9); //function in solver java file
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNDeletePress(View view)
    {
        gameBoardSolver.resetNumberPosition();
        gameBoard.invalidate(); //function gameboard java file
    }

    public void BTNSolvePress(View view)
    {
       //grab the text that is being currently displayed on the button
       if(solveBTN.getText().toString().equals(getString(R.string.solve)))
       {
           //after pressing the button flip the button text from solve to clear
           solveBTN.setText(getString(R.string.clear));

           //need to execute the solver . java process here but we need to run this in aseperate thread so that the user can still interact with the application even it's solving the sudoku board
           //grab the emptyBoxIndexes before we run our solve method in solver.java file
           gameBoardSolver.getemptyBoxIndexs();

           //run the Solve method here via inner class so that it can run on a saperate thread
           //the actual logic to run a process in a different process from the application so that we can improve the performance
           SolveBoardThread solveBoardThread = new SolveBoardThread();
           new Thread(solveBoardThread).start();
           gameBoard.invalidate(); //update the gameboard
       }
       else {
           //after pressing the button flip the button text from solve to clear
           solveBTN.setText(getString(R.string.solve));
           gameBoardSolver.resetBoard();

           //refresh the gameboard
           gameBoard.invalidate();
       }
    }

    //create a inner class that will help us run the solve method in the solve.have file on a separate thread
    class SolveBoardThread implements Runnable{
        @Override
        public void run(){
            gameBoardSolver.Solve(gameBoard);
        }
    }

    //this function will allow us to set up menue item on the app title bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        //onCreateOptionsMenu(Menu menu object)
        //menuInflater.inflate(R.menu.menue xml file name, menu object);
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //this function is used to listen for click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        //item.getItemId() will get the item id selected by the user
        switch (item.getItemId()){
            case R.id.dev_info:
                Log.i("item","quad_eque selected");
                Intent intent = new Intent(MainActivity.this,devActivity.class);
                startActivity(intent);
                //adding animation when opening new activity
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                //return true; works same as a break statement it prevents the fall through of the switch case
                return true;
            default:
                return false;
        }
    }
}