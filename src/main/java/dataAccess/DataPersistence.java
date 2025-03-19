package dataAccess;

import businessLogic.TasksManagement;

import java.io.*;

public class DataPersistence {
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
            return new TasksManagement();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (TasksManagement) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new TasksManagement();
        }
    }
}