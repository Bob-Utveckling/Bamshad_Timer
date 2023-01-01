//Sugestions for more completion:
// - time can be set by changing the text fields instead of always keeping the time change from inside the controller
// done. 1. see line 96 ############### TO BE COMPLETED, where a status image can be set at start and pause
// 2. the save feature can save to text file, a database, or to clipboad

package se.bamshad.timerapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.net.URL;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

import static java.lang.Thread.sleep;
import static javafx.application.Platform.exit;

public class Controller_Buttons_TextFields_And_TheThread extends RuntimeException {

    String TheThreadStatus;
    int theSeconds = 0;
    int theMinutes = 0;
    int theHours = 0;
    int theDays = 0;
    int pressedPauseButtonHowManyTimes = 0;

    boolean timerStarted = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                //TheThreadStatus = String.valueOf(t1.getState());
                //System.out.println("Right now the thread status is: " + TheThreadStatus);
                //while (TheThreadStatus == "RUNNABLE") {
                //if (TheThreadStatus == "RUNNABLE") {
                try {
                    sleep(1000);
                    theSeconds += 1;
                    System.out.println(theSeconds);


                    //final int sec = theSeconds;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            updateTheGUI(theSeconds);
                            //secondsField.setText(String.valueOf(sec));
                            //System.out.println("seconds should be: " + sec);
                        }
                    });

                    if (interrupted()) { //not sure
                        System.out.println("now interrupt, I guess");
                        break;
                    }
                } catch (InterruptedException ie) {
                    System.out.println("- cought interrupt exception");
                    System.out.println("- the thread status is now: " + t1.getState());
                    System.out.println("- thought 1: if there is a break out of the loop, but I do want to use the same thread, so hmm.......... I should find a way not to terminate it, but to have a loop where some waiting for a notification and a start is set");
                    System.out.println("- Now pause/stop the timer");
                    break;
                }
                //}
            }
            System.out.println("borke out of the time counter loop");
        }
    };

    //========================    the thread
    Thread t1 = new Thread(runnable);



    //=====================================buttons ==============

    @FXML private Button startButton;
    @FXML private Button quit;

    @FXML
    //protected void onStartButtonClick() throws InvocationTargetException{
    protected synchronized void onStartButtonClick() throws RuntimeException{
        if (!timerStarted) {
            timerStarted = true;
            System.out.println("Start the timer...");
            try { theSeconds = Integer.parseInt(secondsField.getText()); } catch (NumberFormatException nfe) { theSeconds = 0;}
            try { theMinutes = Integer.parseInt(minutesField.getText()); } catch (NumberFormatException nfe) { theMinutes = 0;}
            try { theHours = Integer.parseInt(hoursField.getText()); } catch (NumberFormatException nfe) { theHours = 0;}
            try { theDays = Integer.parseInt(daysField.getText()); } catch (NumberFormatException nfe) { theDays = 0;}

            try {
                //System.out.println("notify...");
                //t1.notify();
                t1.start();
            } catch (RuntimeException rte) {
                System.out.println("cought a runtime exception - calling an already stopped thread?");
                //start a fresh thread?
                t1 = new Thread(runnable);
                t1.start();
            }

            URL imageUrl = getClass().getResource("play.jpg");
            Image image = new Image(imageUrl.toExternalForm());
            statusImageView.setImage(image); //this works!
            informThatTimerIsRunning();

            System.out.println("Thread name: " + t1.getName());
            System.out.println("state: " + t1.getState());
        }
    }

    @FXML
    protected synchronized void onPauseButtonClick() throws InterruptedException {
        //t1.interrupt();
        System.out.println("Pause requested" + "-- Thread name: " + t1.getName());
        pressedPauseButtonHowManyTimes += 1;
        updateHowManyPauseClicks();
        /*synchronized (t1) {
            t1.wait();
            System.out.println("Gave. Thread status now: " + t1.getState());  }*/
        //t1.sleep(5000);

        System.out.println("Request Interrupt...");
        //t1.stop();
        t1.interrupt();
        System.out.println("state after this interrupt request: " + t1.getState());

        URL imageUrl = getClass().getResource("pause.png");
        Image image = new Image(imageUrl.toExternalForm());
        statusImageView.setImage(image);
        timerStarted = false;

    }

    @FXML
    protected void onResetButtonClick() {
        theSeconds=0;
        theMinutes=0;
        theHours=0;
        theDays=0;
        secondsField.setText(String.valueOf(theSeconds));
        minutesField.setText(String.valueOf(theMinutes));
        hoursField.setText(String.valueOf(theHours));
        daysField.setText(String.valueOf(theDays));
        informThatTimerHasBeenReset();
    }


    @FXML
    protected void onQuitButtonClick() {
        //BL see if there is a better way, or a way to free memory or to avoid memory leackage
        informTimerIsQuitting();
        t1.interrupt();
        exit();
    }

    @FXML
    protected void onCopyButtonClick() {
        System.out.println("save time and notes");
        System.out.println("days: " + theDays + ", hours: " + theHours + ", minutes: " + theMinutes + ", seconds: " + theSeconds);
        System.out.println("date note: " + dateField.getText() + ", note: " + notesField.getText());

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(
                (dateField.getText() == "" ? "" : dateField.getText() + "\n") +
                        (notesField.getText() == "" ? "" : notesField.getText() + "\n\n") +
                        (theDays==0?"":theDays+" days ") +(theHours==0?"":theHours+" hr. ") +(theMinutes==0?"":theMinutes+" min. ")+(theSeconds==0?"":theSeconds+" sec. ")
        );
        clipboard.setContent(content);

        informThatDetailsAreCopied();

    }

    //@FXML private TextField myMillis; not needed
    private void increaseMilliseconds() {
    }


    // #################### notes fields
    @FXML   public TextField dateField;
    @FXML   public TextArea notesField;


    // #################### the text fields
    @FXML public TextField secondsField;
    @FXML public TextField minutesField;
    @FXML public TextField hoursField;
    @FXML public TextField daysField;

    @FXML
    private void setDate() {
        //dateField.setText("[4aug2022]");
    }

    public void updateTheGUI(int getSeconds) {
        System.out.println("now update the GUI");

        if (getSeconds >= 0 && getSeconds <= 59) {
            secondsField.setText(String.valueOf(theSeconds));
        } else if (getSeconds > 59) {
            theSeconds = 0;
            secondsField.setText(String.valueOf(theSeconds));
            theMinutes += 1;
            if (theMinutes < 59) {
                minutesField.setText(String.valueOf(theMinutes));
            } else {
                theMinutes = 0;
                minutesField.setText(String.valueOf(theMinutes));
                theHours += 1;
                if (theHours < 24) {
                    hoursField.setText(String.valueOf(theHours));
                } else {
                    theHours = 0;
                    hoursField.setText(String.valueOf(theHours));
                    theDays += 1;
                    daysField.setText(String.valueOf(theDays));
                }
            }
        }
    }

    public void updateTheSecondsGUI(int getSeconds) {
        System.out.println("now update the GUI - set seconds to: "+getSeconds);
        try {
            //secondsField.setText("test");
            secondsField.setText(String.valueOf(getSeconds));
        } catch (NullPointerException npe) {
            System.out.println(npe);
        }
    }

    @FXML
    public void initialize() {
        System.out.println("Basically_The_Buttons_The_TextFields_And_The_Thread Controller Started");
        setDate();
    }


    //============= label and status iamge
    @FXML
    public Label statusMessages;
    @FXML public ImageView statusImageView;

    public void updateHowManyPauseClicks() {
        if (pressedPauseButtonHowManyTimes==1) {
            statusMessages.setText("Have pressed Pause Button once");
        } else if (pressedPauseButtonHowManyTimes==2) {
            statusMessages.setText("Have pressed Pause Button twice");
        } else if (pressedPauseButtonHowManyTimes>2) {
            statusMessages.setText("Have pressed Pause Button " + String.valueOf(pressedPauseButtonHowManyTimes) + " times");
        }
    }

    public void informThatDetailsAreCopied() {
        statusMessages.setText("Copied Notes and Timer to Clipboard");
    }

    public void informThatTimerIsRunning() {
        statusMessages.setText("The Timer is running");
    }

    public void informThatTimerHasBeenReset() {
        statusMessages.setText("Timer is reset");
    }

    public void informTimerIsQuitting() {
        statusMessages.setText("Quit Timer...");
    }
}
