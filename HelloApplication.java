package org.example.pt2025_30422_turc_ariana_assignment_1;

import businessLogic.TasksManagement;
import dataAccess.DataPersistance;
import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.Task;
import graphicalUserInterface.TasksManagementView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {

    private DataPersistance dataPersistance = new DataPersistance();
    private TasksManagement taskManager;

    public void start(Stage primaryStage) throws IOException {

        // Load the task manager from the file
        taskManager = dataPersistance.loadData("C:\\Users\\arian\\Documents\\an2_utcn\\sem2\\fpt\\PT2025_30422_Turc_Ariana_Assignment_1\\src\\main\\java\\data.txt");

        // Iterate over all employees and print their tasks
        getLists(taskManager);
        System.out.println();
        printEmployeeTasks(taskManager);

        // Start the GUI view
        TasksManagementView tasksManagementView = new TasksManagementView(taskManager);
        tasksManagementView.startView(primaryStage);

        // Save the data when the application is closed
        primaryStage.setOnCloseRequest(event -> {
            try {
                dataPersistance.saveData(taskManager, "C:\\Users\\arian\\Documents\\an2_utcn\\sem2\\fpt\\PT2025_30422_Turc_Ariana_Assignment_1\\src\\main\\java\\data.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void printEmployeeTasks(TasksManagement taskManager) {
        // Iterate through the employees and print their tasks
        for (Employee employee : taskManager.getTasks().keySet()) {
            System.out.println("Employee: " + employee.getName() + " (ID: " + employee.getIdEmployee() + ")");
            List<Task> tasks = taskManager.getTasks().get(employee);

            if (tasks != null && !tasks.isEmpty()) {
                System.out.print("Tasks Assigned: ");
                for (Task task : tasks) {
                    System.out.print("Task ID: " + task.getIdTask() + "status" + task.getStatusTask() + " | ");
                    if(task instanceof ComplexTask) {
                        ComplexTask complexTask = (ComplexTask) task;
                        for(Task cTask : complexTask.getTasks()) {
                            System.out.print("SubTask ID: " + cTask.getIdTask() + " | ");
                        }
                    }
                }
                System.out.println();  // Move to the next line after listing tasks
            } else {
                System.out.println("No tasks assigned.");
            }
        }
    }

    public void getLists(TasksManagement taskManager) {
        for(Task task : taskManager.getTaskList()) {
            System.out.println("Task ID: " + task.getIdTask() + "status" + task.getStatusTask() + " | ");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


    /*@Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        TasksManagement tasksManagement = new TasksManagement();

        // Add Employees
        Employee emp1 = new Employee(1, "Alice");
        Employee emp2 = new Employee(2, "Bob");
        Employee emp3 = new Employee(3, "Charlie");
        tasksManagement.getTasks().put(emp1, new java.util.ArrayList<>());
        tasksManagement.getTasks().put(emp2, new java.util.ArrayList<>());
        tasksManagement.getTasks().put(emp3, new java.util.ArrayList<>());

        // Add Tasks
        Task task1 = new SimpleTask(101, "Completed", 1, 24);
        Task task2 = new ComplexTask(102, "Uncompleted");
        Task task3 = new SimpleTask(103, "Completed", 1, 24);
        Task task4 = new SimpleTask(104, "Completed", 2, 20);

        try {
            tasksManagement.assignTaskToEmployee(emp1.getIdEmployee(), task1);
            tasksManagement.assignTaskToEmployee(emp1.getIdEmployee(), task3);
            tasksManagement.assignTaskToEmployee(emp2.getIdEmployee(), task2);
            tasksManagement.assignTaskToEmployee(emp3.getIdEmployee(), task4);
            tasksManagement.assignTaskToEmployee(emp3.getIdEmployee(), task1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Modify Task Status
        try {
            tasksManagement.modifyTaskStatus(emp1.getIdEmployee(), 102, "Completed");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Calculate Work Duration
        int workDurationAlice = tasksManagement.calculateEmployeeWorkDuration(emp1.getIdEmployee());
        int workDurationBob = tasksManagement.calculateEmployeeWorkDuration(emp2.getIdEmployee());
        int workDurationCharlie = tasksManagement.calculateEmployeeWorkDuration(emp3.getIdEmployee());

        System.out.println("Alice's work duration: " + workDurationAlice + " hours");
        System.out.println("Bob's work duration: " + workDurationBob + " hours");
        System.out.println("Charlie's work duration: " + workDurationCharlie + " hours");

        Utility.filterAndSortEmployeesByWorkDuration(tasksManagement);
    }*/

}