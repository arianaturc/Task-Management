package graphicalUserInterface;

import businessLogic.TasksManagement;
import businessLogic.Utility;
import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.Task;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.Insets;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class TasksManagementView {

    private TasksManagement taskManager;
    private ObservableList<Employee> employees;
    private ObservableList<Task> tasks;

    public TasksManagementView(TasksManagement taskManager) {
        this.taskManager = taskManager;
        employees = FXCollections.observableArrayList();
        tasks = FXCollections.observableArrayList();
        Label messageLabel = new Label();

    }

    public void startView(Stage primaryStage) {
        loadEmployeesFromTaskManager();
        loadTasksFromTaskManager();

        ListView<Employee> employeeListView = new ListView<>(employees);
        ListView<Task> taskListView = new ListView<>(tasks);
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ListView<String> assignedTasksListView = new ListView<>();
        assignedTasksListView.setPlaceholder(new Label("No tasks assigned"));
        assignedTasksListView.setPrefHeight(150);

        loadTaskAssignments(assignedTasksListView);

        Button addEmployeeBtn = new Button("Add Employee");
        Button addTaskBtn = new Button("Add Task");
        Button assignTaskBtn = new Button("Assign Task");
        Button calculateEmployeeWDuration = new Button("Calculate Employee Work Duration");
        Button modifyTaskStatusBtn = new Button("Modify Task Status");
        Button viewStatsBtn = new Button("View Statistics");
        Button saveDataBtn = new Button("Save Data");


        addEmployeeBtn.setOnAction(e -> showAddEmployeeForm(primaryStage));
        addTaskBtn.setOnAction(e -> showAddTaskForm(primaryStage));
        assignTaskBtn.setOnAction(e -> showAssignTaskForm(primaryStage, assignedTasksListView));
        calculateEmployeeWDuration.setOnAction(e -> showCalculateEmployeeWorkDuration());
        modifyTaskStatusBtn.setOnAction(e -> showModifyTaskStatusForm(primaryStage));
        viewStatsBtn.setOnAction(e -> showViewStatisticsForm());
        //saveDataBtn.setOnAction(e -> saveData());


        HBox topButtons = new HBox(10, addEmployeeBtn, addTaskBtn, assignTaskBtn, calculateEmployeeWDuration, modifyTaskStatusBtn, viewStatsBtn);
        topButtons.setPadding(new Insets(10));

        VBox leftPanel = new VBox(new Label("Employees") {{setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");}});
        leftPanel.setPadding(new Insets(10));

        ScrollPane employeeScrollPane = new ScrollPane(employeeListView);
        employeeScrollPane.setFitToWidth(true);
        employeeScrollPane.setPrefWidth(700);
        employeeScrollPane.setPrefHeight(300);
        leftPanel.getChildren().add(employeeScrollPane);


        VBox rightPanel = new VBox(new Label("Tasks"){{setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");}});
        rightPanel.setPadding(new Insets(10));

        ScrollPane taskScrollPane = new ScrollPane(taskListView);
        taskScrollPane.setFitToWidth(true);
        taskScrollPane.setPrefWidth(700);
        taskScrollPane.setPrefHeight(300);
        rightPanel.getChildren().add(taskScrollPane);

        HBox mainPanels = new HBox(20, leftPanel, rightPanel);
        mainPanels.setPadding(new Insets(10));

        VBox assignedTasksPanel = new VBox(new Label("Tasks assigned to employees") {{setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");}}, assignedTasksListView);
        assignedTasksPanel.setPadding(new Insets(10));


        VBox centerPanel = new VBox(mainPanels, assignedTasksPanel);
        centerPanel.setSpacing(10);

        HBox bottomPanel = new HBox(saveDataBtn);
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.setAlignment(Pos.BOTTOM_RIGHT);

        BorderPane root = new BorderPane();
        root.setTop(topButtons);
        root.setCenter(centerPanel);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Task Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void loadEmployeesFromTaskManager() {
        employees.clear();
        employees.addAll(taskManager.getEmployees());
    }

    private void loadTasksFromTaskManager() {
        tasks.clear();
        tasks.addAll(taskManager.getTaskList());
    }


    private void loadTaskAssignments(ListView<String> assignedTasksListView) {
        assignedTasksListView.getItems().clear();

        Map<Integer, List<Integer>> employeeTasksMap = new HashMap<>();
        for (Employee employee : taskManager.getEmployees()) {
            List<Task> assignedTasks = taskManager.getTasks().get(employee);

            if (assignedTasks != null && !assignedTasks.isEmpty()) {
                List<Integer> taskIds = employeeTasksMap.computeIfAbsent(employee.getIdEmployee(), k -> new ArrayList<>());
                for (Task task : assignedTasks) {
                    taskIds.add(task.getIdTask());
                }
            } else {
                employeeTasksMap.putIfAbsent(employee.getIdEmployee(), new ArrayList<>());
            }
        }

        for (Map.Entry<Integer, List<Integer>> entry : employeeTasksMap.entrySet()) {
            int employeeId = entry.getKey();
            List<Integer> taskIds = entry.getValue();

            if (!taskIds.isEmpty()) {
                assignedTasksListView.getItems().add(
                        "Employee ID: " + employeeId + " -> Task: " + taskIds.stream()
                                .map(taskId -> {
                                    Task task = taskManager.getTaskById(taskId);
                                    return task != null ? "ID: " + task.getIdTask() + ", Duration: " + task.estimateDuration() + " hrs" : "Task not found";
                                })
                                .collect(Collectors.joining(", "))
                );

            } else {
                assignedTasksListView.getItems().add("Employee ID: " + employeeId + " -> No tasks assigned");
            }
        }
    }



    private void showAddEmployeeForm(Stage primaryStage) {
        Stage addEmployeeStage = new Stage();
        addEmployeeStage.setTitle("Add Employee");

        TextField idField = new TextField();
        idField.setPromptText("Employee ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Employee Name");

        Button addButton = new Button("Add");
        Label messageLabel = new Label();

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                taskManager.addEmployee(id, name);
                employees.add(new Employee(id, name));
                idField.clear();
                nameField.clear();

            } catch (NumberFormatException ex) {
                messageLabel.setText("Error: ID must be a number.");
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
            addEmployeeStage.close();
        });

        VBox formLayout = new VBox(10, new Label("Add New Employee"), idField, nameField, addButton, messageLabel);
        formLayout.setPadding(new Insets(20));
        Scene scene = new Scene(formLayout, 300, 200);
        addEmployeeStage.setScene(scene);
        addEmployeeStage.show();
    }


    private void showAddTaskForm(Stage primaryStage) {
        Stage addTaskStage = new Stage();
        addTaskStage.setTitle("Add Task");

        TextField taskIdField = new TextField();
        taskIdField.setPromptText("Task ID");

        TextField statusField = new TextField();
        statusField.setPromptText("Task Status");

        ComboBox<String> taskTypeBox = new ComboBox<>();
        taskTypeBox.getItems().addAll("Simple Task", "Complex Task");
        taskTypeBox.setPromptText("Select Task Type");

        TextField startHourField = new TextField();
        startHourField.setPromptText("Start Hour");
        startHourField.setVisible(false);

        TextField endHourField = new TextField();
        endHourField.setPromptText("End Hour");
        endHourField.setVisible(false);

        ListView<Task> subTaskListView = new ListView<>(tasks);
        subTaskListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        subTaskListView.setVisible(false);

        Label messageLabel = new Label();
        Button addButton = new Button("Add");

        taskTypeBox.setOnAction(e -> {
            boolean isSimpleTask = "Simple Task".equals(taskTypeBox.getValue());
            startHourField.setVisible(isSimpleTask);
            endHourField.setVisible(isSimpleTask);
            subTaskListView.setVisible(!isSimpleTask);
        });

        addButton.setOnAction(e -> {
            try {
                int taskId = Integer.parseInt(taskIdField.getText());
                String status = statusField.getText();
                String taskType = taskTypeBox.getValue();

                if (taskType == null) {
                    messageLabel.setText("Error: Please select a task type.");
                    return;
                }

                if (taskId <= 0) {
                    messageLabel.setText("Error: Please enter a valid positive Task ID.");
                    return;
                }

                if (status == null || status.isEmpty()) {
                    messageLabel.setText("Error: Please enter a status.");
                    return;
                }

                if ("Simple Task".equals(taskType)) {
                    if (startHourField.getText().isEmpty() || endHourField.getText().isEmpty()) {
                        messageLabel.setText("Error: Please enter both start and end hours.");
                        return;
                    }

                    int startHour = Integer.parseInt(startHourField.getText());
                    int endHour = Integer.parseInt(endHourField.getText());

                    if (startHour >= endHour) {
                        messageLabel.setText("Error: Start hour must be less than end hour.");
                        return;
                    }

                    SimpleTask simpleTask = new SimpleTask(taskId, status, startHour, endHour);
                    taskManager.addSimpleTask(taskId, status, startHour, endHour);
                    tasks.add(simpleTask);

                } else {

                    ComplexTask complexTask = new ComplexTask(taskId, status);

                    ObservableList<Task> selectedSubtasks = subTaskListView.getSelectionModel().getSelectedItems();
                    List<Task> subtasks = new ArrayList<>(selectedSubtasks);
                    taskManager.addComplexTask(taskId, status, subtasks);
                    tasks.add(complexTask);
                }

                subTaskListView.setItems(FXCollections.observableList(tasks));
                addTaskStage.close();
            } catch (NumberFormatException ex) {
                messageLabel.setText("Error: Please enter valid numeric values for task ID, start hour, and end hour.");
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        VBox formLayout = new VBox(10,
                new Label("Add New Task"), taskIdField, statusField, taskTypeBox, startHourField, endHourField,
                new Label("Select Subtasks (for Complex Task)"), subTaskListView, addButton, messageLabel
        );

        formLayout.setPadding(new Insets(20));
        Scene scene = new Scene(formLayout, 300, 400);
        addTaskStage.setScene(scene);
        addTaskStage.show();
    }


    private void showAssignTaskForm(Stage primaryStage, ListView<String> assignedTasksListView) {
        Stage assignTaskStage = new Stage();
        assignTaskStage.setTitle("Assign Task to Employee");

        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");

        ComboBox<Task> taskComboBox = new ComboBox<>();
        loadTasksFromTaskManager();
        taskComboBox.setItems(FXCollections.observableArrayList(tasks));
        taskComboBox.setPromptText("Select Task");

        Label messageLabel = new Label();
        Button assignButton = new Button("Assign Task");

        assignButton.setOnAction(e -> {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                Task selectedTask = taskComboBox.getValue();

                if (selectedTask == null) {
                    messageLabel.setText("Error: Please select a task.");
                    return;
                }

                taskManager.assignTaskToEmployee(employeeId, selectedTask);
                String assignedTaskInfo = "Employee ID: " + employeeId + " -> Task ID: " + selectedTask.getIdTask();
                assignedTasksListView.getItems().add(assignedTaskInfo);
                messageLabel.setText("Task assigned successfully!");

            } catch (NumberFormatException ex) {
                messageLabel.setText("Error: Employee ID must be a number.");
            } catch (Exception ex) {
                messageLabel.setText("Error: " + ex.getMessage());
            }
        });

        VBox layout = new VBox(10,
                new Label("Assign a Task to an Employee"), employeeIdField, taskComboBox, assignButton, messageLabel
        );
        layout.setPadding(new Insets(20));
        Scene scene = new Scene(layout, 300, 250);
        assignTaskStage.setScene(scene);
        assignTaskStage.show();
    }



    private void showCalculateEmployeeWorkDuration() {

        Stage calculateWorkDurationStage = new Stage();
        calculateWorkDurationStage.setTitle("Calculate Employee Work Duration");

        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");

        Label resultLabel = new Label();
        Button calculateButton = new Button("Calculate Work Duration");

        calculateButton.setOnAction(e -> {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                int workDuration = taskManager.calculateEmployeeWorkDuration(employeeId);

                if (workDuration > 0) {
                    resultLabel.setText("Total Work Duration: " + workDuration + " hours");
                } else {
                    resultLabel.setText("No completed tasks for the given employee.");
                }

            } catch (NumberFormatException ex) {
                resultLabel.setText("Error: Please enter a valid employee ID.");
            }
        });


        VBox layout = new VBox(10, new Label("Enter Employee ID"), employeeIdField, calculateButton, resultLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 200);
        calculateWorkDurationStage.setScene(scene);
        calculateWorkDurationStage.show();
    }



    private void showModifyTaskStatusForm(Stage primaryStage) {
        Stage modifyTaskStatusStage = new Stage();
        modifyTaskStatusStage.setTitle("Modify Task Status");

        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Enter Employee ID");

        TextField taskIdField = new TextField();
        taskIdField.setPromptText("Enter Task ID");

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Completed", "Uncompleted");
        statusComboBox.setPromptText("Select New Status");

        Label resultLabel = new Label();
        Button modifyButton = new Button("Modify Status");

        modifyButton.setOnAction(e -> {
            try {
                int employeeId = Integer.parseInt(employeeIdField.getText());
                int taskId = Integer.parseInt(taskIdField.getText());
                String newStatus = statusComboBox.getValue();

                if (newStatus == null) {
                    resultLabel.setText("Error: Please select a status.");
                    return;
                }

                taskManager.modifyTaskStatus(employeeId, taskId, newStatus);
                resultLabel.setText("Task status updated successfully!");

            } catch (NumberFormatException ex) {
                resultLabel.setText("Error: Please enter valid numeric values for IDs.");
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
            }
        });


        VBox layout = new VBox(10, new Label("Modify Task Status"), employeeIdField, taskIdField, statusComboBox, modifyButton, resultLabel);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 250);
        modifyTaskStatusStage.setScene(scene);
        modifyTaskStatusStage.show();
    }

    private void showViewStatisticsForm() {
        Stage statsStage = new Stage();
        statsStage.setTitle("Statistics");


        Label headingLabel = new Label("Employees that worked more than 40 hours");
        headingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> statisticsListView = new ListView<>();
        statisticsListView.setPrefWidth(340);
        statisticsListView.setPrefHeight(600);
        statisticsListView.setSelectionModel(null);
        statisticsListView.setMouseTransparent(true);
        statisticsListView.setFocusTraversable(false);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalOut = System.out;

        try {
            System.setOut(printStream);
            Utility.filterAndSortEmployeesByWorkDuration(taskManager);
            printStream.flush();
        } finally {
            System.setOut(originalOut);
        }


        String result = outputStream.toString().trim();
        String[] employees = result.split("\n");

        if (employees.length == 0 || employees[0].isEmpty()) {
            statisticsListView.getItems().add("No employees have worked more than 40 hours.");
        } else {
            statisticsListView.getItems().addAll(employees);
        }


        VBox leftVBox = new VBox(10, headingLabel, statisticsListView);
        leftVBox.setPadding(new Insets(10));

        Map<String, Map<String, Integer>> taskStatusCount = Utility.calculateTaskStatusCount(taskManager);
        TableView<Map.Entry<String, Map<String, Integer>>> taskStatusTable = new TableView<>();
        TableColumn<Map.Entry<String, Map<String, Integer>>, String> nameColumn = new TableColumn<>("Employee Name");
        TableColumn<Map.Entry<String, Map<String, Integer>>, Integer> completedColumn = new TableColumn<>("Completed Tasks");
        TableColumn<Map.Entry<String, Map<String, Integer>>, Integer> uncompletedColumn = new TableColumn<>("Uncompleted Tasks");

        nameColumn.setCellValueFactory(entry -> new SimpleStringProperty(entry.getValue().getKey()));
        completedColumn.setCellValueFactory(entry -> new SimpleIntegerProperty(entry.getValue().getValue().get("Completed")).asObject());
        uncompletedColumn.setCellValueFactory(entry -> new SimpleIntegerProperty(entry.getValue().getValue().get("Uncompleted")).asObject());

        taskStatusTable.getColumns().addAll(nameColumn, completedColumn, uncompletedColumn);
        taskStatusTable.setPrefWidth(340);
        taskStatusTable.setPrefHeight(603);
        taskStatusTable.getSelectionModel().clearSelection();
        taskStatusTable.setMouseTransparent(true);
        taskStatusTable.getItems().addAll(taskStatusCount.entrySet());
        taskStatusTable.setFocusTraversable(false);

        Label taskStatusLabel = new Label("Task Status of Employees");
        taskStatusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");


        VBox rightVBox = new VBox(10, taskStatusLabel, taskStatusTable);
        rightVBox.setPadding(new Insets(10));

        HBox mainLayout = new HBox(20);
        mainLayout.setPadding(new Insets(10));
        mainLayout.getChildren().addAll(leftVBox, rightVBox);

        Scene scene = new Scene(mainLayout, 800, 500);
        statsStage.setScene(scene);
        statsStage.show();
    }

}