package businessLogic;

import dataModel.ComplexTask;
import dataModel.Employee;
import dataModel.SimpleTask;
import dataModel.Task;

import java.io.Serializable;
import java.util.List;
import java.util.*;

public class TasksManagement implements Serializable {
    private static final long serialVersionUID = 2314448046272300455L;
    private Map<Employee, List<Task>> taskMap;
    private List<Task> taskList;

    public TasksManagement() {
        taskMap = new HashMap<Employee, List<Task>>();
    }

    public Employee findEmployeeById(int id) {
        for(Employee employee : taskMap.keySet()) {
            if(employee.getIdEmployee() == id) {
                return employee;
            }
        }
        return null;
    }

    public boolean findTaskById(int id) {
        for(Task task : taskList) {
            if(task.getIdTask() == id)
                return true;
        }
        return false;
    }

    public void addEmployee(int idEmployee, String name) throws Exception{
        if(findEmployeeById(idEmployee) == null) {
            Employee employee = new Employee(idEmployee, name);
            taskMap.put(employee, new ArrayList<>());
        } else {
            throw new Exception("Employee already exists");
        }
    }

    public void addSimpleTask(int idTask, String statusTask, int startHour, int endHour) throws Exception {
        if(!findTaskById(idTask)) {
            Task task = new SimpleTask(idTask, statusTask, startHour, endHour);
            taskList.add(task);
        } else {
            throw new Exception("Task with the given id already exists");
        }
    }

    public void addComplexTask(int idTask, String statusTask, List<Task> subtasks) throws Exception {
        if(!findTaskById(idTask)) {
            ComplexTask task = new ComplexTask(idTask, statusTask);
            if(!subtasks.isEmpty()) {
                for(Task subtask : subtasks) {
                    task.addTask(subtask);
                }
            }
            taskList.add(task);
        } else {
            throw new Exception("Task with the given id already exists");
        }

    }

    public void assignTaskToEmployee(int idEmployee, Task task) throws Exception {
        Employee employee = findEmployeeById(idEmployee);

        if(employee != null){
            List<Task> tasks = taskMap.computeIfAbsent(employee, k -> new ArrayList<>());
            tasks.add(task);
        } else {
            throw new Exception("Employee with the given id was not found");
        }

    }

    public int calculateEmployeeWorkDuration(int idEmployee)  {
        Employee employee = findEmployeeById(idEmployee);

        if(employee != null){
            List<Task> tasks = taskMap.get(employee);

            int totalWorkDuration = 0;
            for(Task task : tasks){
                if(task.getStatusTask().equals("Completed")){
                    totalWorkDuration += task.estimateDuration();
                }
            }
            return totalWorkDuration;
        } else {
            return 0;
        }
    }



    public void modifyTaskStatus(int idEmployee, int idTask, String newStatus) throws Exception {
        Employee employee = findEmployeeById(idEmployee);

        if (employee != null) {
            List<Task> tasks = taskMap.get(employee);

            boolean taskFound = false;

            for (Task task : tasks) {
                if (task.getIdTask() == idTask) {
                    taskFound = true;

                    if (task instanceof ComplexTask complexTask) {
                        updateSubtaskStatus(complexTask, newStatus);
                    } else {
                        task.setStatusTask(newStatus);
                    }
                    break;
                }
            }

            if (!taskFound) {
                throw new Exception("Task with the given id was not found for the given employee");
            }
        } else {
            throw new Exception("Employee with the given id was not found");
        }
    }


    private void updateSubtaskStatus(ComplexTask complexTask, String newStatus) {
        complexTask.setStatusTask(newStatus);
        for(Task task : complexTask.getTasks()) {
            if(task instanceof ComplexTask) {
                updateSubtaskStatus((ComplexTask) task, newStatus);
            }
            task.setStatusTask(newStatus);
        }

    }

    public Map<Employee, List<Task>> getTasks() {
        if(taskMap == null) {
            taskMap = new HashMap<>();
        }
        return taskMap;
    }

    public List<Task> getTaskList() {
        if(taskList == null) {
            taskList = new ArrayList<>();
        }
        return taskList;
    }


    public List<Employee> getEmployees() {
        return new ArrayList<>(taskMap.keySet());
    }

}
