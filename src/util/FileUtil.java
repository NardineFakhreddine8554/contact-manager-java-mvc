package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileUtil {
	public static void saveToFile(String filename, Object object) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			oos.writeObject(object);
			System.out.println("Saved successfully to " + filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object loadFromFile(String filename) {
		File file = new File(filename);
		if (!file.exists()) {
			System.out.println("File not found: " + filename);
			return null;
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
