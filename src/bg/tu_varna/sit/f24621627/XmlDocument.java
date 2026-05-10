package bg.tu_varna.sit.f24621627;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class XmlDocument {
    private String currentFilePath;
    private XmlElement rootElement; // main tag
    private boolean isFileOpened;

    private Map<String, XmlElement> idRegistry;

    public XmlDocument() {
        this.isFileOpened = false;
        this.currentFilePath = null;
        this.rootElement = null;
    }

    public Map<String, XmlElement> getIdRegistry() {
        return idRegistry;
    }

    public void open(String filePath) {
        try {
            String content = Files.readString(Path.of(filePath));

            XmlParser parser = new XmlParser();
            XmlElement parsedRoot = parser.parse(content);

            // ПРОВЕРКА: Ако парсърът е върнал null, значи е имало грешка (напр. Mismatched tags)
            if (parsedRoot == null) {
                this.rootElement = null;
                this.isFileOpened = false;
                // Не печатаме успех, защото парсърът вече е изписал своята грешка
                return;
            }

            this.rootElement = parsedRoot;

            // Присвояваме ID-тата само ако имаме валиден корен
            IdAssigner assigner = new IdAssigner();
            this.idRegistry = assigner.assignIds(this.rootElement);

            this.currentFilePath = filePath;
            this.isFileOpened = true;
            System.out.println("Successfully opened " + filePath);

        } catch (IOException e) {
            System.out.println("Error: Could not read file " + filePath + ". Make sure the file exists.");
            this.isFileOpened = false;
        } catch (Exception e) {
            System.out.println("Error: Failed to open XML file. " + e.getMessage());
            this.rootElement = null;
            this.isFileOpened = false;
        }
    }

    public void close() {
        if (!isFileOpened) {
            System.out.println("No file is currently opened.");
            return;
        }
        this.currentFilePath = null;
        this.rootElement = null;
        this.isFileOpened = false;
        System.out.println("Successfully closed the document.");
    }

    public boolean isOpened() {
        return isFileOpened;
    }

    public void saveAs(String newFilePath) {
        if (!isFileOpened || rootElement == null) {
            System.out.println("Error: No document is currently opened.");
            return;
        }

        try (FileWriter writer = new FileWriter(newFilePath)) {

            String xmlContent = rootElement.toXml(0);

            writer.write(xmlContent);

            this.currentFilePath = newFilePath;
            System.out.println("Successfully saved " + newFilePath);

        } catch (IOException e) {
            System.out.println("Error saving file: Could not write to " + newFilePath);
        }
    }

    public void save() {
        if (!isFileOpened) {
            System.out.println("Error: No document is currently opened.");
            return;
        }
        saveAs(this.currentFilePath);
    }

    public XmlElement getRootElement() {
        return this.rootElement;
    }

    public XmlElement getElementById(String id) {
        if (idRegistry != null) {
            return idRegistry.get(id);
        }
        return null;
    }

    public void updateIdInRegistry(String oldId, String newId, XmlElement element) {
        if (idRegistry.containsKey(oldId)) {
            idRegistry.remove(oldId);
            idRegistry.put(newId, element);
        }
    }

    public void removeIdFromRegistry(String id) {
        if (idRegistry != null) {
            idRegistry.remove(id);
        }
    }
}
