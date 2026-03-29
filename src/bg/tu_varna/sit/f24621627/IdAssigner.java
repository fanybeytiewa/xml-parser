package bg.tu_varna.sit.f24621627;

import java.util.HashMap;
import java.util.Map;

public class IdAssigner {

    private int generatedIdCounter = 1;

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

    // idFrequency is updated with counts of each id value found in the document
    private void collectIdFrequencies(XmlElement element, Map<String, Integer> idFrequency) {
        String id = element.getId();
        if (id != null && !id.isEmpty()) {
            idFrequency.put(id, idFrequency.getOrDefault(id, 0) + 1);
        }
        for (XmlElement child : element.getChildren()) {
            collectIdFrequencies(child, idFrequency);
        }
    }

    //idFrequency decides what to do, idCounters track suffixes
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
