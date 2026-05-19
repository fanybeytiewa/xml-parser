package bg.tu_varna.sit.f24621627.models;

import bg.tu_varna.sit.f24621627.io.FileHandler;
import bg.tu_varna.sit.f24621627.parsers.IdAssigner;
import bg.tu_varna.sit.f24621627.parsers.XmlParser;
import bg.tu_varna.sit.f24621627.exceptions.XmlParseException;
import bg.tu_varna.sit.f24621627.io.XmlSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * Represents an XML document.
 * Manages the document state (root element, ID registry) and
 * delegates file operations to {@link FileHandler} and
 * serialization to {@link XmlSerializer}.
 */
public class XmlDocument {
    /** Path to the currently opened file. */
    private String currentFilePath;

    /** Root element of the XML tree. */
    private XmlElement rootElement;

    /** Whether a file is currently opened. */
    private boolean isFileOpened;

    /** Registry mapping unique IDs to their corresponding elements. */
    private Map<String, XmlElement> idRegistry;

    /** Serializer for converting elements to XML strings. */
    private final XmlSerializer serializer;

    /** Handler for file system operations (read, write, create). */
    private final FileHandler fileHandler;

    /** Initializes the document with no file opened and creates service instances. */
    public XmlDocument() {
        this.isFileOpened = false;
        this.currentFilePath = null;
        this.rootElement = null;
        this.serializer = new XmlSerializer();
        this.fileHandler = new FileHandler();
    }

    /**
     * Returns the ID registry.
     * @return the ID registry
     */
    public Map<String, XmlElement> getIdRegistry() {
        return idRegistry;
    }

    /**
     * Returns the XML serializer instance.
     * @return the XML serializer instance
     */
    public XmlSerializer getSerializer() {
        return serializer;
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
            String content = loadFileContent(filePath);
            XmlElement parsedRoot = (content != null) ? parseContent(content) : null;

            initDocument(filePath, parsedRoot);
            System.out.println("Successfully opened " + filePath);

        } catch (XmlParseException e) {
            System.out.println("XML Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error: Could not read file " + filePath + ". " + e.getMessage());
        }
    }

    /**
     * Loads the file content, creating the file if it does not exist.
     * @param filePath path to the file
     * @return the file content, or null if the file is new or empty
     * @throws IOException if the file cannot be read or created
     */
    private String loadFileContent(String filePath) throws IOException {
        if (!fileHandler.fileExists(filePath)) {
            fileHandler.createFile(filePath);
            return null;
        }

        String content = fileHandler.readFile(filePath);
        return content.trim().isEmpty() ? null : content;
    }

    /**
     * Initializes the document state with the given file path and root element.
     * @param filePath the path to the file
     * @param root the root element (can be null for empty documents)
     */
    private void initDocument(String filePath, XmlElement root) {
        this.currentFilePath = filePath;
        this.rootElement = root;
        this.idRegistry = buildIdRegistry(root);
        this.isFileOpened = true;
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

    /**
     * Checks if a file is currently opened.
     * @return true if a file is currently opened
     */
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

        try {
            String xmlContent = serializer.serialize(rootElement, 0);
            fileHandler.writeFile(newFilePath, xmlContent);
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

    /**
     * Returns the root element of the document.
     * @return the root element of the document or null
     */
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
     * @param id the unique identifier for the element
     * @param element the element to register
     */
    public void registerElement(String id, XmlElement element) {
        if (idRegistry != null) {
            idRegistry.put(id, element);
        }
    }

    /**
     * Factory method — creates a parser and parses the content.
     * Encapsulates the creation of XmlParser.
     * @param content the XML content string to parse
     * @return the root element of the parsed tree
     * @throws XmlParseException if the XML structure is invalid
     */
    private XmlElement parseContent(String content) throws XmlParseException {
        XmlParser parser = new XmlParser();
        return parser.parse(content);
    }

    /**
     * Factory method — creates an IdAssigner and assigns IDs.
     * Encapsulates the creation of IdAssigner.
     * @param root the root element of the document (can be null)
     * @return map of id to element for fast lookup
     */
    private Map<String, XmlElement> buildIdRegistry(XmlElement root) {
        IdAssigner assigner = new IdAssigner();
        return assigner.assignIds(root);
    }
}
