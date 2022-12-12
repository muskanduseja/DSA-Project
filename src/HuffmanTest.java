import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileWriter;

class Node {
    Character ch;
    Integer freq;
    Node left = null, right = null;

    Node(Character ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(Character ch, Integer freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

class HuffmanTest {
    Node root;

    // String builder made
    public static StringBuilder sb4 = new StringBuilder();
    public static StringBuilder sb6 = new StringBuilder();

    // Traverse the Huffman Tree and store Huffman Codes in a map.
    public static void encode(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }

        // Found a leaf node
        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }

        encode(root.left, str + '0', huffmanCode);
        encode(root.right, str + '1', huffmanCode);

    }

    static Boolean check = true;

    // Traverse the Huffman Tree and decode the encoded string
    public static int decode(Node root, int index, StringBuilder sb, String dest) {

        if (root == null) {
            return index;
        }

        // Found a leaf node
        if (isLeaf(root)) {
            // System.out.println(root.ch);
            sb6.append(root.ch);
            return index;
        }

        index++;

        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decode(root, index, sb, dest);
        return index;
    }

    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    static PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

    // Huffman tree
    public static StringBuilder buildHuffmanTree(String text) {
        String str2 = "File is empty";
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        // Base case: empty string
        if (text == null || text.length() == 0) {
            return sb2;
        }
        // counts frequency and stores in map: freq
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // create a priority queue to store nodes of the Huffman tree.
        for (var entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() != 1) {
            // Remove the two nodes of the highest priority

            Node left = pq.poll();
            Node right = pq.poll();

            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        Node root = pq.peek();

        // Traverse the Huffman tree and store the Huffman codes in a map
        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);

        // Print the Huffman codes
        System.out.println("Huffman Codes are: " + huffmanCode);
        // System.out.println("The original string is: " + text);

        // Print encoded string
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }
        System.out.println("The encoded string is: " + sb);

        return sb;
    }

    public static void compress(String data, String dest) {

        try {
            File myObj = new File(dest);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            File myObj = new File(dest);

            // Object to store encoded string.
            sb4 = buildHuffmanTree(data);
            BitSet b = ByteData(sb4);
            b.toByteArray();
            FileOutputStream fos = new FileOutputStream(myObj);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.write(b.toByteArray());
            // System.out.println("Bitset once again : " + b);
            oos.close();
            System.out.println("File compressed.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static BitSet ByteData(StringBuilder s) {
        // converts encoded string to bytes.
        BitSet bitSet = new BitSet(s.toString().length());
        int bitcounter = 0;
        for (Character c : s.toString().toCharArray()) {
            if (c.equals('1')) {
                bitSet.set(bitcounter);

            }
            bitcounter++;
        }
        return bitSet;

    }

    public static StringBuilder readBits() {
        BitSet b = ByteData(sb4);
        BitSet.valueOf(b.toByteArray());
        // converts back byte to encoded string
        StringBuilder binaryString = new StringBuilder();
        for (int i = 0; i <= b.length(); i++) {
            if (b.get(i)) {
                binaryString.append("1");
            } else {
                binaryString.append("0");
            }
        }
        return binaryString;
    }

    public static void Decompress(String dest) {
        try {
            File myObj = new File(dest);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(dest);
            Node root = pq.peek();
            StringBuilder st = readBits();
            if (isLeaf(root)) {
                while (root.freq-- > 0) {
                    System.out.print(root.ch);
                }
            } else {
                // decode the encoded string
                int index = -1;
                while (index < st.length() - 2) {
                    index = decode(root, index, st, dest);
                }
            }
            String variable = sb6.toString();
            myWriter.write(variable);
            myWriter.close();
            System.out.println("File decompressed.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("INDEX OUT OF BOND>");
        }

    }

    public static void main(String[] args) {
        // Text to be compressed.
        String text = "(1) the original words and form of a written or printed work, (2)an edited or emended copy of an original work";
        compress(text, "CompressedFile");
        readBits();
        Decompress("DecompressedFile");
    }
}