package bg.tu_varna.sit.f24621627.xpath;

import bg.tu_varna.sit.f24621627.models.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for resolving XPath axes (e.g., ancestor, descendant, child, parent).
 */
public class AxisResolver {

    /**
     * Resolves an axis and tag name against a list of context nodes.
     * @param contextNodes the parent/context elements
     * @param stepBase the string that might contain an axis (e.g., "ancestor::book" or "title")
     * @return the list of matched elements
     */
    public static List<XmlElement> resolve(List<XmlElement> contextNodes, String stepBase) {
        String axis = "child";
        String nodeTest = stepBase;

        if (stepBase.contains("::")) {
            String[] parts = stepBase.split("::", 2);
            axis = parts[0].trim();
            nodeTest = parts[1].trim();
        }

        List<XmlElement> result = new ArrayList<>();
        for (XmlElement node : contextNodes) {
            List<XmlElement> axisNodes = getAxisNodes(node, axis);
            for (XmlElement axisNode : axisNodes) {
                if (matchesNodeTest(axisNode, nodeTest)) {
                    if (!result.contains(axisNode)) {
                        result.add(axisNode);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Checks if the element matches the node test (tag name or *).
     */
    private static boolean matchesNodeTest(XmlElement node, String nodeTest) {
        if ("*".equals(nodeTest)) {
            return true;
        }
        // Match either the full tag name (including namespace) or just the local name
        return node.getTag().equals(nodeTest) || node.getLocalName().equals(nodeTest);
    }

    /**
     * Retrieves all nodes belonging to the specified axis for a single context node.
     */
    private static List<XmlElement> getAxisNodes(XmlElement node, String axis) {
        List<XmlElement> result = new ArrayList<>();
        switch (axis) {
            case "child":
                collectChildren(node, result);
                break;
            case "parent":
                collectParent(node, result);
                break;
            case "ancestor":
                collectAncestors(node, result);
                break;
            case "ancestor-or-self":
                collectAncestorOrSelf(node, result);
                break;
            case "descendant":
                collectDescendants(node, result);
                break;
            case "descendant-or-self":
                collectDescendantOrSelf(node, result);
                break;
            case "self":
                collectSelf(node, result);
                break;
            default:
                // Unsupported axis; fallback to empty list
                break;
        }
        return result;
    }

    /** Collects the immediate children of the node. */
    private static void collectChildren(XmlElement node, List<XmlElement> result) {
        result.addAll(node.getChildren());
    }

    /** Collects the immediate parent of the node, if it exists. */
    private static void collectParent(XmlElement node, List<XmlElement> result) {
        if (node.getParent() != null) {
            result.add(node.getParent());
        }
    }

    /** Collects all ancestors of the node (parent, grandparent, etc.). */
    private static void collectAncestors(XmlElement node, List<XmlElement> result) {
        XmlElement curr = node.getParent();
        while (curr != null) {
            result.add(curr);
            curr = curr.getParent();
        }
    }

    /** Collects the node itself and all its ancestors. */
    private static void collectAncestorOrSelf(XmlElement node, List<XmlElement> result) {
        result.add(node);
        collectAncestors(node, result);
    }

    /** Collects the node itself and all its descendants. */
    private static void collectDescendantOrSelf(XmlElement node, List<XmlElement> result) {
        result.add(node);
        collectDescendants(node, result);
    }

    /** Collects only the node itself. */
    private static void collectSelf(XmlElement node, List<XmlElement> result) {
        result.add(node);
    }

    /**
     * Recursively collects all descendants of a node.
     */
    private static void collectDescendants(XmlElement node, List<XmlElement> result) {
        for (XmlElement child : node.getChildren()) {
            result.add(child);
            collectDescendants(child, result);
        }
    }
}
