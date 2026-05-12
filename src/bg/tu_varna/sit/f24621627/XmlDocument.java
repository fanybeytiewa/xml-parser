package bg.tu_varna.sit.f24621627;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Represents an XML document.
 * Manages opening, closing, and saving XML files
 * and provides access to elements via an ID registry.
 */
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

    /** @return the ID registry */
    public Map<String, XmlElement> getIdRegistry() {
        return idRegistry;
    }

    /**
     * Opens an XML file. If the file does not exist, creates a new empty file.
     * The content is loaded into memory and the original file is not modified until saved.
     * @param filePath path to the file
     */
    public void open(String filePath) {
        if (isFileOpened) {
            System.out.println("Error: A file is already opened. Please close it first.");
            return;
        }

        try {
            Path path = Path.of(filePath);

            // If the file does not exist, create a new empty file
            if (!Files.exists(path)) {
                Files.createFile(path);
                this.currentFilePath = filePath;
                this.rootElement = null;
                this.idRegistry = buildIdRegistry(null);
                this.isFileOpened = true;
                System.out.println("Successfully created and opened new file " + filePath);
                return;
            }

            String content = Files.readString(path);

            // If the file is empty, open it without parsing
            if (content.trim().isEmpty()) {
                this.currentFilePath = filePath;
                this.rootElement = null;
                this.idRegistry = buildIdRegistry(null);
                this.isFileOpened = true;
                System.out.println("Successfully opened " + filePath);
                return;
            }

            // Create objects via factory methods
            XmlElement parsedRoot = parseContent(content);

            this.rootElement = parsedRoot;

            // Assign IDs only if we have a valid root
            this.idRegistry = buildIdRegistry(this.rootElement);

            this.currentFilePath = filePath;
            this.isFileOpened = true;
            System.out.println("Successfully opened " + filePath);

        } catch (XmlParseException e) {
            System.out.println("XML Error: " + e.getMessage());
            this.rootElement = null;
            this.isFileOpened = false;
        } catch (IOException e) {
            System.out.println("Error: Could not read file " + filePath + ". " + e.getMessage());
            this.isFileOpened = false;
        } catch (Exception e) {
            System.out.println("Error: Failed to open XML file. " + e.getMessage());
            this.rootElement = null;
            this.isFileOpened = false;
        }
    }

    /** Closes the currently opened document and releases resources. */
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

    /** @return true if a file is currently opened */
    public boolean isOpened() {
        return isFileOpened;
    }

    /**
     * Saves the document to a new file.
     * @param newFilePath path to the new file
     */
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

    /** Saves changes to the current file. */
    public void save() {
        if (!isFileOpened) {
            System.out.println("Error: No document is currently opened.");
            return;
        }
        saveAs(this.currentFilePath);
    }

    /** @return the root element of the document or null */
    public XmlElement getRootElement() {
        return this.rootElement;
    }

    /**
     * Finds an element by its unique identifier.
     * @param id the identifier to search for
     * @return the found element or null
     */
    public XmlElement getElementById(String id) {
        if (idRegistry != null) {
            return idRegistry.get(id);
        }
        return null;
    }

    /**
     * Updates the ID of an element in the registry.
     * @param oldId the old ID
     * @param newId the new ID
     * @param element the element to re-register
     */
    public void updateIdInRegistry(String oldId, String newId, XmlElement element) {
        if (idRegistry != null && idRegistry.containsKey(oldId)) {
            idRegistry.remove(oldId);
            idRegistry.put(newId, element);
        }
    }

    /**
     * Removes an element from the registry by ID.
     * @param id the identifier to remove
     */
    public void removeIdFromRegistry(String id) {
        if (idRegistry != null) {
            idRegistry.remove(id);
        }
    }

    /**
     * Registers an element in the ID registry.
     * Used instead of direct access to idRegistry.
     */
    public void registerElement(String id, XmlElement element) {
        if (idRegistry != null) {
            idRegistry.put(id, element);
        }
    }

    /**
     * Factory method — creates a parser and parses the content.
     * Encapsulates the creation of XmlParser.
     */
    private XmlElement parseContent(String content) throws XmlParseException {
        XmlParser parser = new XmlParser();
        return parser.parse(content);
    }

    /**
     * Factory method — creates an IdAssigner and assigns IDs.
     * Encapsulates the creation of IdAssigner.
     */
    private Map<String, XmlElement> buildIdRegistry(XmlElement root) {
        IdAssigner assigner = new IdAssigner();
        return assigner.assignIds(root);
    }
}
