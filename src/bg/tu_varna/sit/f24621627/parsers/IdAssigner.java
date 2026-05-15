package bg.tu_varna.sit.f24621627.parsers;

import bg.tu_varna.sit.f24621627.models.XmlElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Assigns unique identifiers to all elements in the XML tree.
 * If an element has an id but it is not unique — appends a suffix ( e.g. "1_1", "1_2").
 * If an element has no id — generates a new one ( e.g. "gen-1", "gen-2").
 */
public class IdAssigner {

    /** Counter for generating unique IDs (gen-1, gen-2, ...). */
    private int generatedIdCounter = 1;

    /**
     * Assigns unique IDs to all elements and returns a registry.
     * @param root the root element of the document (can be null)
     * @return map of id to element for fast lookup
     */
    public Map<String, XmlElement> assignIds(XmlElement root) {
        if (root == null) return new HashMap<>();

        // Pass 1: count how many times each id value appears in the whole document
        Map<String, Integer> idFrequency = new HashMap<>();
        collectIdFrequencies(root, idFrequency);

        // Pass 2: assign the final unique ids
        Map<String, Integer> idCounters = new HashMap<>();

        // create registry
        Map<String, XmlElement> registry = new HashMap<>();

        assignUniqueIds(root, idFrequency, idCounters, registry);

        return registry;
    }

    /**
     * Recursively counts how many times each ID appears in the tree.
     * @param element the current element being examined
     * @param idFrequency map to update with ID occurrence counts
     */
    private void collectIdFrequencies(XmlElement element, Map<String, Integer> idFrequency) {
        String id = element.getId();
        if (id != null && !id.isEmpty()) {
            idFrequency.put(id, idFrequency.getOrDefault(id, 0) + 1);
        }
        for (XmlElement child : element.getChildren()) {
            collectIdFrequencies(child, idFrequency);
        }
    }

    /**
     * Recursively assigns unique IDs to elements, adding suffixes for duplicates
     * and generating new IDs for elements without one.
     * @param element the current element being processed
     * @param idFrequency map of original ID occurrence counts
     * @param idCounters map tracking suffix counters for duplicate IDs
     * @param registry output map of final ID to element mappings
     */
    private void assignUniqueIds(XmlElement element, Map<String, Integer> idFrequency, Map<String, Integer> idCounters, Map<String, XmlElement> registry) {
        String rawId = element.getId();

        if (rawId == null || rawId.isEmpty()) {
            // generate id if none exists
            element.setId("gen-" + generatedIdCounter++);
        } else {
            int freq = idFrequency.getOrDefault(rawId, 1);
            if (freq > 1) {
                int counter = idCounters.getOrDefault(rawId, 0) + 1;
                idCounters.put(rawId, counter);
                element.setId(rawId + "_" + counter);
            }
        }

        // save the final id -> element mapping in the registry
        registry.put(element.getId(), element);

        for (XmlElement child : element.getChildren()) {
            assignUniqueIds(child, idFrequency, idCounters, registry);
        }
    }
}
