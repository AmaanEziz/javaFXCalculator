//Amaan Eziz
/*The program runs a very basic calculator GUI. It shows experience in
JavaFX, collections, data encapsulation, threads, and more. */

import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class Calculator extends Application {

    
    private boolean isDouble(String s) { //Returns true if string can be converted to a double, false if not
        try {
            Double.parseDouble(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    
    /*Declare all the variables needed for the program*/
    public String textOnScreen;
    public TextField textArea = new TextField();
    private String operation = "-1";
    private double firstnumber;
    private double secondnumber;
    private double answer;
    private String b;
    
    
    @Override
    public void start(Stage stage) {
 
        
        //Thread that constantly updates the textOnScreen variable
        Thread updateText = new Thread() {
            public void run() {
                while (true) {
                    textOnScreen = textArea.getText();
                }
            }
        };
        updateText.start();
        
        
        //Deals with what to do when an operation button is clicked
        EventHandler < MouseEvent > operationClick = new EventHandler < MouseEvent > () {
            public void handle(MouseEvent e) {
              operation = ((Button) e.getSource()).getId();
                if (isDouble(textOnScreen)) {
                    firstnumber = Double.valueOf(textOnScreen);}
                textArea.setText(operation);
            }
        };
        
        
        /*Deals with what to do when a number OR decimal button is clicked */
        EventHandler < MouseEvent > numClick = new EventHandler < MouseEvent > () {
            @Override
            public void handle(MouseEvent e) {               
                if (isDouble(textOnScreen) || operation == "-1" || b == ".") {
                    b = ((Button) e.getSource()).getId();
                    textArea.appendText(b);
                } else {
                    b = ((Button) e.getSource()).getId();
                    textArea.setText(b);
                }
            }
        };
        
        
        /*Deals with the complexity of figuring out if the user intended to use the
        "-" button for subtraction or to signify a number is negative */
        EventHandler < MouseEvent > SubtractOrNegativeClick = new EventHandler < MouseEvent > () {
            public void handle(MouseEvent e) {
                if (isDouble(textOnScreen)) {
                    operation = ((Button) e.getSource()).getId();
                    firstnumber = Double.valueOf(textOnScreen);
                    textArea.setText(operation);
                } else {
                    textArea.setText("-");
                }
            }
        };
        
        
        /*Resets the calculator*/
        EventHandler < MouseEvent > clear = new EventHandler < MouseEvent > () {
            public void handle(MouseEvent e) {
                textArea.clear();
                operation="";
            }
        };
        
        
        /*Deals with deciding what calculation to do when the equals button is hit,
        if an operation was specified in the first place, and  resets the calculator
        while still displaying the result of the previous calculation*/
        EventHandler < MouseEvent > equals = new EventHandler < MouseEvent > () {
            public void handle(MouseEvent e) {
                if (isDouble(textOnScreen)) {
                    secondnumber = Double.valueOf(textOnScreen);
                    switch (operation) {
                        case "*":
                            answer = (firstnumber) * secondnumber;
                            break;
                        case "/":
                            answer = (firstnumber) / secondnumber;
                            break;
                        case "-":
                            answer = firstnumber - secondnumber;
                            break;
                        case "+":
                            answer = firstnumber + secondnumber;
                            break;
                        default:
                            answer = secondnumber;
                    }
                    
                    //Whole numbers are omitted of their trailing zeros
                    if (answer == Math.round(answer)) {
                        textArea.setText(Long.toString(Math.round(answer)));
                    } else {
                        textArea.setText(Double.toString(answer));
                    }
                }
                operation = "-1";
                firstnumber = 0;
                secondnumber = 0;
            }
        };

        
        
        
        
        /*Declaring the gridPane, buttons, the button IDs, adding their corresponding
         EventHandlers, their position on the gridPane */
        GridPane grid = new GridPane();
        Button decimalButton = new Button(".");
        Button multiplyButton = new Button("*");
        Button addButton = new Button("+");
        Button divideButton = new Button("/");
        Button subtractButton = new Button("-");
        Button clearButton = new Button("Clear");
        Button equalsButton = new Button("=");
        ArrayList < Button > buttonList = new ArrayList < Button > ();
        Collections.addAll(buttonList, decimalButton, multiplyButton, clearButton, equalsButton, addButton, divideButton, subtractButton, addButton);
        decimalButton.setId(".");
        multiplyButton.setId("*");
        addButton.setId("+");
        divideButton.setId("/");
        subtractButton.setId("-");
        multiplyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, operationClick);
        addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, operationClick);
        divideButton.addEventHandler(MouseEvent.MOUSE_CLICKED, operationClick);
        subtractButton.addEventHandler(MouseEvent.MOUSE_CLICKED, SubtractOrNegativeClick);
        decimalButton.addEventHandler(MouseEvent.MOUSE_CLICKED, numClick);
        clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, clear);
        equalsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, equals);
        grid.add(textArea, 0, 0, 4, 1);
        grid.add(clearButton, 0, 1, 2, 1);
        grid.add(multiplyButton, 3, 1, 1, 1);
        grid.add(divideButton, 3, 2, 1, 1);
        grid.add(addButton, 3, 3, 1, 1);
        grid.add(subtractButton, 3, 4, 1, 1);
        grid.add(equalsButton, 2, 6, 4, 1);
        grid.add(decimalButton, 0, 6, 2, 1);

        
        /*Creates the button 0-9 through the use of a for loop for convenience*/
        int row = 1;
        int column = 0;
        for (int i = 0; i < 10; i++) {
            String b = String.valueOf(i);
            Button c = new Button(b);
            c.setId(b);
            buttonList.add(c);
            c.addEventHandler(MouseEvent.MOUSE_CLICKED, numClick);
            if (i % 3 == 1) {
                row++;
                column = 0;
            } else {
                column++;
            }
            if (i != 0) {
                grid.add(c, column, row, 1, 1);
            } else {
                grid.add(c, 2, 1, 1, 1);
            }
        }
        
        
        /*Enhanced for loop that goes through every button and sets its size and font
         to make sure it fits the gridPane perfectly*/
        for (Button b: buttonList) {
            b.setMaxWidth(Double.MAX_VALUE);
            b.setFont(new Font(48));
        }
        
        //Set size of the textArea and initializes the scene's components
        textArea.setMinHeight(80);
        textArea.setFont(new Font(48));
        textArea.setMaxWidth(400);
        Scene scene = new Scene(grid, grid.getMinWidth(), grid.getMinHeight());
        stage.setTitle("Calculator");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler < WindowEvent > () {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        stage.show();
    }
    public static void main(String args[]) {
        launch(args); //Launches the javaFX program

    }


}