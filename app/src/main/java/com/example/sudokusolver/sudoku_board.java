package com.example.sudokusolver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

//this class is responsible for creating a custom sudoku board with all it's display attributes in it
public class sudoku_board extends View {
    //note that the attributes when first defined here will show red error mark if not defined inside the constructor

    private final int boardColor; //color attribute of our sudoku board
    private final Paint boardColorPaint = new Paint(); //now here we need to create a variable that is an instance of our paint class
    private int cellsize; //size of each cells in a sudoku board

    private final int cellFillColor; //attributes of sudoku board in attrs.xml file
    private final int cellsHighlightColor; //attributes of sudoku board in attrs.xml file
    //create two new instances for our paint class for our two attributes that we just extracted from attrs.xml file
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellsHighlightColorPaint = new Paint();

    private final int letterColor; //color of default number in sudoku board
    private final int letterColorSolve; //color of computer generated numbers in the sudoku board
    //create two new instances for our paint class for our two attributes that we just extracted from attrs.xml file
    private final Paint letterColorPaint = new Paint();
    private final Paint letterColorSolvePaint = new Paint();
    //now we need to create a rectangle object we need this because when we draw a number into out sudoku board
    //it's very difficult to grab the correct width and height of our text so I am gonna use this rectangle
    //to grab the height of our text
    private final Rect letterColorPaintBounds = new Rect();

    //create an instance for our solver class
    private final solver solver = new solver();

    //this constructor will allow us to define our own attributes in our view
    //now create an attributes resource file
    //go to this location res > values > create an attribute.xml file
    public sudoku_board(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //creating an array that will store all the attribute of our sudoku app
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,R.styleable.sudoku_board, 0,0);
        //now we need to extract all the attributes from this typedArray and store them in their individual variables in this java class
        try {
            //extract the individual attributes and set them to variables
            //extracting the board color from our typed array and setting it to the variable named boardColor
            boardColor = array.getInteger(R.styleable.sudoku_board_boardColor,0);

            cellFillColor = array.getInteger(R.styleable.sudoku_board_cellFillColor,0);
            cellsHighlightColor = array.getInteger(R.styleable.sudoku_board_cellsHighlightColor,0);

            letterColor = array.getInteger(R.styleable.sudoku_board_letterColor,0);
            letterColorSolve = array.getInteger(R.styleable.sudoku_board_letterColorSolve,0);
        }
        finally {
            //free up some memory for our application
            array.recycle();
        }
    }

    //now we need to determine the dimension of our sudoku board depending upon which device the application is running on
    //for that we need to override the on measure method
    @Override
    protected void onMeasure(int width,int height)
    {
        super.onMeasure(width, height);

        /*
        difference between getwidth and getMeasuredWidth is that gerWidth will give us the width of the view when
        it's displayed on the screen and getMeasured width is going to display what the view is actually wants to be
        it's not gonna be dependent on the parent's size
        */

        //pass the width and height of the user's device
        //we need to make sure that our sudoku board is a perfect square no matter what is the screen size of the device is
        //now this will allow our sudoku board to be perfect square and still fit on the device's screen
        int rel_height = MeasureSpec.getSize(height);
        int rel_width = MeasureSpec.getSize(width);

        int dimension = Math.min(rel_width, rel_height);

        //define individual cell size
        cellsize = dimension/9; //because there are 9 cells per row and comlumn in a sudoku board

        //this is defining the width and height of the view according to the device's screen size
        setMeasuredDimension(dimension, dimension);

        //now that we have our sudoku board dimensions define we need to start drawing in our sudoku board

    }

    //This method will allow us to draw our sudoku board in our view
    @Override
    protected void onDraw(Canvas canvas){
        //now here we need to create a variable that is an instance of our paint class
        boardColorPaint.setStyle(Paint.Style.STROKE); //so this is gonna be like a paint brush
        //define a width for the paint brush stroke in the paint class
        boardColorPaint.setStrokeWidth(16);
        //call in a paint bucket
        boardColorPaint.setColor(boardColor);
        //the code below will ensure that the line drawn on the canvas are all crisp and do not look all funky
        boardColorPaint.setAntiAlias(true);

        //setting up paint classes for our cellFillColor attribute
        //now here we need to create a variable that is an instance of our paint class
        cellFillColorPaint.setStyle(Paint.Style.FILL); //so this is gonna be like a paint brush
        //the code below will ensure that the line drawn on the canvas are all crisp and do not look all funky
        cellFillColorPaint.setAntiAlias(true);
        //call in a paint bucket
        cellFillColorPaint.setColor(cellFillColor); //cellFillColor gets its color from activity_main.xml

        //setting up paint classes for our cellsHighlightColor attribute
        //now here we need to create a variable that is an instance of our paint class
        cellsHighlightColorPaint.setStyle(Paint.Style.FILL); //so this is gonna be like a paint brush
        //the code below will ensure that the line drawn on the canvas are all crisp and do not look all funky
        cellsHighlightColorPaint.setAntiAlias(true);
        //call in a paint bucket
        cellsHighlightColorPaint.setColor(cellsHighlightColor); //cellsHighlightColor gets its color from activity_main.xml

        //drawNumbers method
        //set the color of the numbers entered in the sudoku board
        letterColorPaint.setStyle(Paint.Style.FILL);
        letterColorPaint.setAntiAlias(true);
        letterColorPaint.setColor(letterColor);

        //call the colorCells method which handels the highlighting of the cell of the sudoku board whne the user clicks on it
        colorCells(canvas,solver.getSelectedRow(), solver.getSelectedColumn());

        //draw a rectangle around the perimeter of our view
        canvas.drawRect(0,0,getWidth(),getHeight(),boardColorPaint);
        //after all this is done you can place this in our xml file and see for ourselves how it looks

        //call the drawBoard method so that we can draw the sudoku board onto our canvas
        drawBoard(canvas);

        //drawNumbers method
        drawNumbers(canvas);
    }

    //method to draw Thick lines
    private void drawThickLines()
    {
        //now here we need to create a variable that is an instance of our paint class
        boardColorPaint.setStyle(Paint.Style.STROKE); //so this is gonna be like a paint brush
        //define a width for the paint brush stroke in the paint class
        boardColorPaint.setStrokeWidth(10);

        //call in a paint bucket
        boardColorPaint.setColor(boardColor);
    }

    //method to draw thin lines
    private void drawThinLines()
    {
        //now here we need to create a variable that is an instance of our paint class
        boardColorPaint.setStyle(Paint.Style.STROKE); //so this is gonna be like a paint brush
        //define a width for the paint brush stroke in the paint class
        boardColorPaint.setStrokeWidth(4);

        //call in a paint bucket
        //here we are getting our boardColor from our activity_main.xml file
        boardColorPaint.setColor(boardColor);
    }

    //creating a new custom method that's gonna draw in the individual lines that will makeup the sudoku board
    //so to add in these lines we are gonna make our own custom methods
    private void drawBoard(Canvas canvas)
    {
        //here we are gonna have two for loops
        //one that will take care of drawing the rows and one that will take care of drawing the columns
        for(int columns = 0; columns<10; columns++ )
        {
            //we need an if statement here to determine weather we need to draw a thick or thin line
            if(columns%3==0)
            {
                drawThickLines();
            }
            else {
                drawThinLines();
            }
            //pass in the starting point and an end point here
            //it sets up our paint bucket and paint brush and then it will draw onto our view
            canvas.drawLine(cellsize * columns,0,cellsize * columns, getWidth(),boardColorPaint);
        }
        for(int rows = 0; rows<10; rows++ )
        {
            //we need an if statement here to determine weather we need to draw a thick or thin line
            if(rows%3==0)
            {
                drawThickLines();
            }
            else {
                drawThinLines();
            }
            //pass in the starting point and an end point here
            //it sets up our paint bucket and paint brush and then it will draw onto our view
            canvas.drawLine(0,cellsize * rows, getWidth(), cellsize * rows ,boardColorPaint);
        }

    }

    //override the on touch event method
    //this method is responsible for giving us data about touch events that happens on the user's screen
    //so if the user swipes the screen we can get data about that
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Boolean isValid;
        //extract x and y coordinate from a tap on the screen from an event
        float x = event.getX();
        float y = event.getY();
        //this will allow us to different between the types of taps that can occour on a user's device's screen
        int action = event.getAction();

        //so here we can define an if statement that we can weed out all the other tap events that we don't want
        //MotionEvent.ACTION_DOWN = so this is gonna be a press or a click on the screen
        if(action == MotionEvent.ACTION_DOWN)
        {
            //convert the x and y coordinate to their corresponding rows and column values of the sudoku board
            //set the values of row and column in a solver class with our setters that we just created
            //ath.ceil() will allow us to obtain an integer value rather than a decimal value
            solver.setSelectedRow((int)Math.ceil(y/cellsize));
            solver.setSelectedColumn((int)Math.ceil(x/cellsize));
            isValid = true;
        }
        else {
            isValid = false;
        }

        return isValid;
    }

    //this method will draw numbers in our sudoku board
    private void drawNumbers(Canvas canvas)
    {
        //define a text size for a letter
        letterColorPaint.setTextSize(cellsize);

        //this is gonna consists of two main parts
        //one part is gonna loop through the 2D array which is our game board
        //and the other part is gonna iterate through the empty indexes

        //for the first part
        //create two for loops that is gonna iterate through our sudoku game board
        for(int row = 0;row < 9 ; row++)
        {
            for(int column = 0; column<9;column++)
            {
                //check if the corresponding row and column is a zero or not
                if(solver.getBoard()[row][column]!=0)
                {
                    //update the sudoku game board accordingly
                    //first get the number that the user assigned for that box
                    String text = Integer.toString(solver.getBoard()[row][column]);
                    //grab the width and height of this number
                    float width, height ;

                    //this is where our rectangle that we've created earlier is gonna come into play
                    letterColorPaint.getTextBounds(text,0,text.length(),letterColorPaintBounds);
                    //width
                    width = letterColorPaint.measureText(text);
                    height = letterColorPaintBounds.height();

                    //inorder to actually place the numbers onto the sudoku board we need to draw the numbers onto the canvas
                    canvas.drawText(text, (column*cellsize)+((cellsize- width)/2), (row*cellsize+cellsize) - ((cellsize-height)/2), letterColorPaint);


                }
            }
        }

        //second part of the code where we will handle the number inserted by the computer after the user pressed solve button
        //change the color of the text
        letterColorPaint.setColor(letterColorSolve);
        for(ArrayList<Object> letter : solver.getemptyBoxIndex())
        {
            //here we have the ArrayList that holds the specific row and column values of that empty box
            int row = (int)letter.get(0);
            int column = (int)letter.get(1);

            //update the sudoku game board accordingly
            //first get the number that the user assigned for that box
            String text = Integer.toString(solver.getBoard()[row][column]);
            //grab the width and height of this number
            float width, height ;

            //this is where our rectangle that we've created earlier is gonna come into play
            letterColorPaint.getTextBounds(text,0,text.length(),letterColorPaintBounds);
            //width
            width = letterColorPaint.measureText(text);
            height = letterColorPaintBounds.height();

            //inorder to actually place the numbers onto the sudoku board we need to draw the numbers onto the canvas
            canvas.drawText(text, (column*cellsize)+((cellsize- width)/2), (row*cellsize+cellsize) - ((cellsize-height)/2), letterColorPaint);

        }

    }

    //draw in the rectangle to color in the corresponding cells when the user clicked
    private void colorCells(Canvas canvas, int row, int column){
        //this if statement will allow us to determine weather our row and column is valid or not
        //in solver java file we've set our row and column to -1
        if(solver.getSelectedColumn() !=-1 && solver.getSelectedRow() != -1)
        {
            //place in the code that's going to highlight the cell that the user clicked along with the corresponding row and column
            //color the column of a selected cell
            canvas.drawRect((column-1)*cellsize,0,column*cellsize, cellsize*9,cellsHighlightColorPaint);
            //color the row of a selected cell
            canvas.drawRect(0,(row-1)*cellsize,cellsize*9, row*cellsize,cellsHighlightColorPaint);
            //color the sepcific cell that the user clicked on
            canvas.drawRect((column-1)*cellsize,(row-1)*cellsize,column*cellsize, row*cellsize,cellsHighlightColorPaint);
        }

        //now that all of our rows and columns and the selected cell is highlighted all we need to do is refresh our sudoku board
        invalidate(); //refresh our sudoku board
    }

    public solver getsolver()
    {
         return this.solver;
    }

}
