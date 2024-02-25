package hello;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Random;

public class FileIOGenerator {
    public static String TARGET_FILE = "dummy.txt";
    public static void generateFileIO() {
        try {
            File myObj = new File(TARGET_FILE);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 100000;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();

            System.out.println(generatedString);

            FileWriter myWriter = new FileWriter(TARGET_FILE);
            myWriter.append(generatedString);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void deleteFile(){
        File myObj = new File(TARGET_FILE);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }
}
