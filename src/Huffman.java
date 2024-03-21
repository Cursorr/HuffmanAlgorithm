
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

    // Node implementation for a binary tree
    public static class Node<T> {
        private final T value;
        private final int occurrences;
        private final Node<T> leftChild;
        private final Node<T> rightChild;

        public Node(T value, int occurrences, Node<T> leftChild, Node<T> rightChild) {
            this.value = value;
            this.occurrences = occurrences;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
    }

    // Implementation of a result class containing
    // the encoded string and the binary tree made.
    public static class Result {
        private final String encodedText;
        private final Node<Character> root;

        public Result(String encodedText, Node<Character> root) {
            this.encodedText = encodedText;
            this.root = root;
        }

        public String getEncodedText() { return this.encodedText;}
        public Node<Character> getRoot() { return this.root; }

    }

    // Simple function to count how many characters are in a string.
    public static HashMap<Character, Integer> countCharacters(String input) {
        HashMap<Character, Integer> charCountMap = new HashMap<>();

        for (char c : input.toCharArray()) charCountMap.merge(c, 1, Integer::sum);

        return charCountMap;
    }

    // The creation of binary trees and their assembly "Huffman's algorithm"
    public static Node<Character> buildHuffmanTree(HashMap<Character, Integer> charOccurrences) {
        PriorityQueue<Node<Character>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.occurrences));

        for (Map.Entry<Character, Integer> entry : charOccurrences.entrySet()) {
            Node<Character> node = new Node<>(entry.getKey(), entry.getValue(), null, null);
            priorityQueue.offer(node);
        }

        while (priorityQueue.size() > 1) {
            Node<Character> leftChild = priorityQueue.poll();
            Node<Character> rightChild = priorityQueue.poll();
            assert rightChild != null;
            int sumOccurrences = leftChild.occurrences + rightChild.occurrences;
            Node<Character> parent = new Node<>(null, sumOccurrences, leftChild, rightChild);
            priorityQueue.offer(parent);
        }

        return priorityQueue.poll();
    }

    // As its name says
    public static void generateHuffmanCodes(Node<Character> root, String codePrefix, HashMap<Character, String> huffmanCodes) {
        if (root == null) return;

        if (root.value != null) huffmanCodes.put(root.value, codePrefix);

        generateHuffmanCodes(root.leftChild, codePrefix + "0", huffmanCodes);
        generateHuffmanCodes(root.rightChild, codePrefix + "1", huffmanCodes);
    }

    public static Result encode(String input) {
        StringBuilder encodedText = new StringBuilder();

        HashMap<Character, Integer> charOccurrences = Huffman.countCharacters(input);

        Huffman.Node<Character> root = Huffman.buildHuffmanTree(charOccurrences);

        HashMap<Character, String> huffmanCodes = new HashMap<>();
        Huffman.generateHuffmanCodes(root, "", huffmanCodes);

        for (char c : input.toCharArray()) encodedText.append(huffmanCodes.get(c));

        return new Result(encodedText.toString(), root);
    }

    public static String decode(String encodedText, Node<Character> root) {
        StringBuilder decodedText = new StringBuilder();
        Node<Character> current = root;

        for (char bit : encodedText.toCharArray()) {
            current = (bit == '0') ? current.leftChild : current.rightChild;

            if (current.value != null) {
                decodedText.append(current.value);
                current = root;
            }
        }

        return decodedText.toString();
    }
}
