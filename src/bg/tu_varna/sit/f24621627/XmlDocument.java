package bg.tu_varna.sit.f24621627;

public class XmlDocument {
    private String currentFilePath;
    private XmlElement rootElement; // main tag
    private boolean isFileOpened;

    public XmlDocument() {
        this.isFileOpened = false;
        this.currentFilePath = null;
        this.rootElement = null;
    }

    public void open(String filePath) {
        this.currentFilePath = filePath;
        this.isFileOpened = true;
        System.out.println("Successfully opened " + filePath);
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
}
