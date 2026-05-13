package bg.tu_varna.sit.f24621627;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles all file system operations for the XML document.
 * Responsible for reading, writing, and creating files.
 * Separates I/O concerns from the document model.
 */
public class FileHandler {

    /**
     * Reads the content of a file as a string.
     * @param filePath path to the file
     * @return the file content
     * @throws IOException if the file cannot be read
     */
    public String readFile(String filePath) throws IOException {
        return Files.readString(Path.of(filePath));
    }

    /**
     * Writes content to a file, creating it if it does not exist.
     * @param filePath path to the file
     * @param content the content to write
     * @throws IOException if the file cannot be written
     */
    public void writeFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

    /**
     * Checks whether a file exists at the given path.
     * @param filePath path to check
     * @return true if the file exists
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }

    /**
     * Creates a new empty file at the given path.
     * @param filePath path for the new file
     * @throws IOException if the file cannot be created
     */
    public void createFile(String filePath) throws IOException {
        Files.createFile(Path.of(filePath));
    }
}
