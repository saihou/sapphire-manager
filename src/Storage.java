import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Teck Sheng (Dex)
 *	This class is the storage that that deals with the reading of file and writing to file
 */

public class Storage {
	private ArrayList<Task> taskList;
	private UserInterface userInterface;
	private String fileName;
	
	public Storage(String fileName, UserInterface userInterface) {
		this.fileName = fileName;
		this.userInterface = userInterface;
		taskList = new ArrayList<Task>();
		readFromFile();
	}
	
	public boolean readFromFile() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
			String line = bufferedReader.readLine();
			String[] splitedTaskInfo;
			while(line != null) {
				splitedTaskInfo = line.split("\\|");
				taskList.add(new Task(splitedTaskInfo[0].trim(), splitedTaskInfo[1].trim(), splitedTaskInfo[2].trim(), splitedTaskInfo[3].trim(), splitedTaskInfo[4].trim(), splitedTaskInfo[5].trim(), splitedTaskInfo[6].trim()));
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			return true;
		} catch(Exception ex) {
			userInterface.displayMessage("Error reading file! Please re-start program."); 
			return false;
		} 
	}
	
	public ArrayList<Task> getTaskList() {
		return taskList;
	}
	
	public boolean setTaskList(Task task) {
		if(task == null) {
			return false;
		} else {
			taskList.add(task);
			return true;
		}
	}
	
	public boolean writeTaskListToFile() {
		if(taskList.size() == 0) { //clear text file
			//BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), false));
			//bufferedWriter.write(task.getType()+"|"+task.getName()+"|"+task.getDate()+"|"+task.getStartTime()+"|"+task.getEndTime()+"|"+task.getDeadline()+"|"+task.getLocation());
			//bufferedWriter.close();
			return true;
		} else if(taskList.size() > 0) { //write text file
			return true;
		} else { 
			userInterface.displayMessage("Error writing to file!");
			return false;
		}
	}
	
	public boolean writeATaskToFile(Task task) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(fileName), true));
			bufferedWriter.write(task.getType()+"|"+task.getName()+"|"+task.getDate()+"|"+task.getStartTime()+"|"+task.getEndTime()+"|"+task.getDeadline()+"|"+task.getLocation());
			bufferedWriter.newLine();
			bufferedWriter.close();
			return true;
		} catch(Exception ex) {
			userInterface.displayMessage("Error writing to file!");
			return false;
		}
	}
	
	public boolean clearFile(String fileName) {
		return false;
	}
}