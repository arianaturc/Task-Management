package dataAccess;

import businessLogic.TasksManagement;

import java.io.*;

public class DataPersistance {
    public void saveData(TasksManagement tasksManagement, String fileName) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(tasksManagement);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public TasksManagement loadData(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            // If the file doesn't exist or is empty, return a new TasksManagement instance
            return new TasksManagement();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (TasksManagement) ois.readObject(); // Read the object from the file
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // In case of any error while reading the file, return a new TasksManagement instance
            return new TasksManagement();
        }
    }
}
