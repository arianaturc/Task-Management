package org.example.pt2025_30422_turc_ariana_assignment_1;

import businessLogic.TasksManagement;
import dataAccess.DataPersistence;
import graphicalUserInterface.TasksManagementView;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    private DataPersistence dataPersistence = new DataPersistence();
    private TasksManagement taskManager;

    public void start(Stage primaryStage) throws IOException {

        taskManager = dataPersistence.loadData("C:\\Users\\arian\\Documents\\an2_utcn\\sem2\\fpt\\PT2025_30422_Turc_Ariana_Assignment_1\\src\\main\\java\\data.txt");

        TasksManagementView tasksManagementView = new TasksManagementView(taskManager);
        tasksManagementView.startView(primaryStage);

        primaryStage.setOnCloseRequest(event -> {
            try {
                dataPersistence.saveData(taskManager, "C:\\Users\\arian\\Documents\\an2_utcn\\sem2\\fpt\\PT2025_30422_Turc_Ariana_Assignment_1\\src\\main\\java\\data.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}