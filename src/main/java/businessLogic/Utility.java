package businessLogic;

import dataModel.Employee;
import dataModel.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

    public static void filterAndSortEmployeesByWorkDuration(TasksManagement tasksManagement) {
        tasksManagement.getTasks().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), tasksManagement.calculateEmployeeWorkDuration(entry.getKey().getIdEmployee())))
                .filter(entry -> entry.getValue() > 40)
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> System.out.println(entry.getKey().getName() + " has worked " + entry.getValue() + " hours"));
    }

    public static Map<String, Map<String, Integer>> calculateTaskStatusCount(TasksManagement tasksManagement) {
        Map<String, Map<String, Integer>> taskStatusCount = new HashMap<>();

        for (Map.Entry<Employee, List<Task>> entry : tasksManagement.getTasks().entrySet()) {
            Map<String, Integer> countTasks = new HashMap<>();

            countTasks.put("Completed", 0);
            countTasks.put("Uncompleted", 0);

            for(Task task : entry.getValue()) {
                countTasks.put(task.getStatusTask(), countTasks.get(task.getStatusTask()) + 1);
            }
            taskStatusCount.put(entry.getKey().getName(), countTasks);
        }
        return taskStatusCount;
    }

}
