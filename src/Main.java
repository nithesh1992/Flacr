/**
 * Created by Nithesh on 4/30/2016.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main
        extends Application {

    private Text actionStatus;
    private Stage savedStage;
    private static final String titleTxt = "Convert to Flac";

    public static void main(String [] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle(titleTxt);

        // Window label
        Label label = new Label("Select File Choosers");
        label.setTextFill(Color.SALMON);
        label.setFont(Font.font("Cambria", FontWeight.BOLD, 28));
        HBox labelHb = new HBox();
        labelHb.setAlignment(Pos.CENTER);
        labelHb.getChildren().add(label);

        // Buttons
        Button btn1 = new Button("Choose a file...");
        btn1.setOnAction(new SingleFcButtonListener());
        HBox buttonHb1 = new HBox(10);
        buttonHb1.setAlignment(Pos.CENTER);
        buttonHb1.getChildren().addAll(btn1);

        Button btn2 = new Button("Choose Directory...");
        btn2.setOnAction(new MultipleFcButtonListener());
        HBox buttonHb2 = new HBox(10);
        buttonHb2.setAlignment(Pos.CENTER);
        buttonHb2.getChildren().addAll(btn2);

        // Status message text
        actionStatus = new Text();
        actionStatus.setFont(Font.font("Cambria", FontWeight.NORMAL, 20));
        actionStatus.setFill(Color.FIREBRICK);

        // Vbox
        VBox vbox = new VBox(30);
        vbox.setPadding(new Insets(25, 25, 25, 25));;
        vbox.getChildren().addAll(labelHb, buttonHb1, buttonHb2, actionStatus);

        // Scene
        Scene scene = new Scene(vbox, 500, 300); // w x h
        primaryStage.setScene(scene);
        primaryStage.show();

        savedStage = primaryStage;
    }

    private class SingleFcButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            showSingleFileChooser();
        }
    }

    private void showSingleFileChooser() {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            actionStatus.setText("File selected: " + selectedFile.getName());
            String[] fname = selectedFile.getName().split("\\.",2);
            System.out.println("Base Name: " +fname[0]);
            String command = " ffmpeg -i " + selectedFile.getAbsolutePath() +" "+selectedFile.getParent()+"/"+ fname[0] +".flac";
            System.out.println(command);
            try {
                Process p = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            actionStatus.setText("File selection cancelled.");
        }
    }

    private class MultipleFcButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

            showMultipleFileChooser();

        }
    }

    private void showMultipleFileChooser() {

        DirectoryChooser folderChooser = new DirectoryChooser();
        File selectedFile1 = folderChooser.showDialog(null);

        if (selectedFile1 != null) {

            actionStatus.setText("Directory selected: " + selectedFile1.getAbsolutePath());
            String[] fname = selectedFile1.getName().split(".",2);
            System.out.println(fname[0]);
//            String dirName =  selectedFile1.getParent() + "/"+ fname[0] +"_flac";
//            System.out.println(dirName);
//            File dir = new File(dirName);
//            boolean successful = dir.mkdir();
//            System.out.println("Directory Created: " + successful);


            new Thread()
            {
                public void run() {
                    File folder = new File(selectedFile1.getAbsolutePath());
                    File[] listOfFiles = folder.listFiles();

                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("wav"))
                        {
                            System.out.println("File " + listOfFiles[i].getName());
                            String command1 = " ffmpeg -i " + listOfFiles[i];
                            String command2 = " "+ listOfFiles[i] +"2.flac";
                            String FinalCommand = command1+command2;
                            System.out.println(FinalCommand);
                            try {
                                Process flacr = Runtime.getRuntime().exec(FinalCommand);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }
            }.start();



        }
        else {
            actionStatus.setText("File selection cancelled.");
        }
    }
}