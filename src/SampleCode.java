import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.File; // Import the File class
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileWriter;

// A Tree node
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
            // System.out.print(root.ch);
            sb6.append(root.ch);
            return index;
        }

        index++;

        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decode(root, index, sb, dest);
        return index;
    }

    // Utility function to check if Huffman Tree contains only a single node
    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    // Builds Huffman Tree and decodes the given input text
    static PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

    public static StringBuilder buildHuffmanTree(String text) {
        String str2 = "File is empty";
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        // Base case: empty string
        if (text == null || text.length() == 0) {
            return sb2;
        }

        // Count the frequency of appearance of each character
        // and store it in a map

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // create a priority queue to store live nodes of the Huffman tree.
        // Notice that the highest priority item has the lowest frequency

        // create a leaf node for each character and add it
        // to the priority queue.

        for (var entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        // do till there is more than one node in the queue
        while (pq.size() != 1) {
            // Remove the two nodes of the highest priority
            // (the lowest frequency) from the queue

            Node left = pq.poll();
            Node right = pq.poll();

            // create a new internal node with these two nodes as children
            // and with a frequency equal to the sum of both nodes'
            // frequencies. Add the new node to the priority queue.

            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        // `root` stores pointer to the root of Huffman Tree
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
        // System.out.print("The decoded string is: ");

        // if (isLeaf(root))
        // {
        // // Special case: For input like a, aa, aaa, etc.
        // while (root.freq-- > 0) {
        // System.out.print(root.ch);
        // }
        // }
        // else {
        // // Traverse the Huffman Tree again and this time,
        // // decode the encoded string
        // int index = -1;
        // while (index < sb.length() - 1) {
        // index = decode(root, index, sb);
        // }
        // }

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
            // FileWriter myWriter = new FileWriter(dest);
            File myObj = new File(dest);
            String str4;
            // String builder made
            sb4 = buildHuffmanTree(data);
            BitSet b = ByteData(sb4);
            b.toByteArray();
            FileOutputStream fos = new FileOutputStream(myObj);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.write(b.toByteArray());
            // System.out.println("Bitset once again : " + b);
            oos.close();

            // BitSet.valueOf(b.toByteArray());
            // String binaryString = "";
            // for (int i = 0; i <= b.length() - 1; i++) {
            // if (b.get(i)) {
            // binaryString += "1";
            // } else {
            // binaryString += "0";
            // }
            // }
            // System.out.println("String after bitset : " + binaryString);
            // System.out.println(sb4.toString().equals(binaryString));
            // str4 = sb4.toString();
            // String rs = convetBinarytoChar(str4);
            // myWriter.write(rs);
            // myWriter.close();
            System.out.println("File compressed.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static BitSet ByteData(StringBuilder s) {
        // byte[] byteArr = s.toString().getBytes();
        // // print the byte[] elements
        // System.out.println("String to byte array: " + Arrays.toString(byteArr));
        BitSet bitSet = new BitSet(s.toString().length());
        int bitcounter = 0;
        for (Character c : s.toString().toCharArray()) {
            if (c.equals('1')) {
                bitSet.set(bitcounter);

            }
            bitcounter++;
        }
        // System.out.println("Bitset is : " + bitSet);
        return bitSet;

    }

    static String convetBinarytoChar(String data) {
        String s = data;
        String str = "";
        int c = 0;
        for (int i = 0; i < s.length() / 32; i++) {
            c += 32;
            int a = Integer.parseInt(s.substring(32 * i, (i + 1) * 32), 2);
            str += (char) (a);
        }

        if (s.length() - c > 0) {
            int a = Integer.parseInt(s.substring(c, s.length() - 1), 2);
            str += (char) (a);
        }
        return str;
    }

    public static StringBuilder readBits() {
        BitSet b = ByteData(sb4);
        BitSet.valueOf(b.toByteArray());
        StringBuilder binaryString = new StringBuilder();
        // changed code here
        for (int i = 0; i <= b.length(); i++) {
            if (b.get(i)) {
                binaryString.append("1");
            } else {
                binaryString.append("0");
            }
        }
        // System.out.println("String after bitset in readBits : " + binaryString);
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
                // Special case: For input like a, aa, aaa, etc.
                while (root.freq-- > 0) {
                    // System.out.print(root.ch);
                }
            } else {
                // Traverse the Huffman Tree again and this time,
                // decode the encoded string
                int index = -1;
                while (index < st.length() - 1) {
                    index = decode(root, index, st, dest);
                }
            }

            myWriter.write(sb6.toString());
            myWriter.close();
            System.out.println("File decompressed.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("INDEX OUT OF BOND>");
        }

    }

    // Huffman coding algorithm implementation in Java
    public static void main(String[] args) {

        String text = "Whereas the thesis is the main point of the essay the topic sentence.\n Whereas the";
        // buildHuffmanTree(text);
        // Whereas the thesis is the main point of the essay the topic sentence.
        compress(text, "CompressedFile");
        String path = "C:\\Users\\Bilal\\Desktop\\DSAProject\\Huffman encoding\\src\\CompressedFile";
        // Decompress(path, "DecompressedFile");

        readBits();
        Decompress("DecompressedFile");
    }
}