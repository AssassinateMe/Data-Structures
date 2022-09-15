package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;


    public HuffmanCoding(String f) { 
        fileName = f; 
    }


    public void makeSortedList() {
        StdIn.setFile(fileName);
        int[] arr = new int[128];
        int totalChar = 0;
        sortedCharFreqList = new ArrayList<CharFreq>();

        while(StdIn.hasNextChar())
        {
            arr[StdIn.readChar()]++;
            totalChar++;
        }

        for(int i = 0; i < arr.length; i++)
        {
            if(arr[i] > 0)
                sortedCharFreqList.add(new CharFreq((char)i, (double)arr[i]/totalChar));

        }
        if(sortedCharFreqList.size() == 1)
        {
            int i = (int)sortedCharFreqList.get(0).getCharacter() + 1;
            if(i > 127)
            {
                i =0;
            }
            sortedCharFreqList.add(new CharFreq((char)i, 0));
        }
        Collections.sort(sortedCharFreqList); 
        

    }


    public void makeTree() {

        Queue<CharFreq> source = new Queue<CharFreq>();
        Queue<TreeNode> target = new Queue<TreeNode>();
        TreeNode n1;
        TreeNode n2;

        for(int i = 0; i < sortedCharFreqList.size(); i++)
        {
            source.enqueue(sortedCharFreqList.get(i));
        }


        n1 = new TreeNode(source.dequeue(),null,null);
        n2 = new TreeNode(source.dequeue(),null,null);
        target.enqueue(new TreeNode(new CharFreq(null, n1.getData().getProbOcc()+n2.getData().getProbOcc()),n1,n2));

        
        while(!source.isEmpty())
        {
            if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()) 
            {
                n1 = new TreeNode(source.dequeue(),null,null);

                if(source.isEmpty()) 
                {
                    n2 = target.dequeue();
                }
                else 
                {
                    if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc()) 
                    {
                        n2 = new TreeNode(source.dequeue(),null,null);
                    }
                    else 
                    {
                        n2 = target.dequeue();
                    }
                }
            }
            

            else 
            {
                n1 = target.dequeue();
                if(target.isEmpty())
                    n2 = new TreeNode(source.dequeue(),null,null);
                else if((source.peek().getProbOcc() <= target.peek().getData().getProbOcc()))
                {
                    n2 = new TreeNode(source.dequeue(),null,null);
                }

                else 
                {
                    n2 = target.dequeue();
                }

            }
            
            target.enqueue(new TreeNode(new CharFreq(null, n2.getData().getProbOcc()+n1.getData().getProbOcc()), n1, n2));
        }

        while(target.size() > 1)
        {
            n1 = target.dequeue();
            n2 = target.dequeue();
            
            target.enqueue(new TreeNode(new CharFreq(null, n2.getData().getProbOcc()+n1.getData().getProbOcc()), n1, n2));
        }

        huffmanRoot = target.peek();
    }

    public void makeEncodings() {

        encodings = new String[128];
        TreeNode ptr = huffmanRoot;
        String bitstring = "";
        helper(ptr, bitstring);
        
    }

    private void helper( TreeNode ptr, String s)
    {
        if(ptr.getData().getCharacter() != null)
        {
            encodings[ptr.getData().getCharacter()] = s;
            return;
        }
        if(ptr.getLeft() != null)
            helper(ptr.getLeft(), s+0);
        if(ptr.getRight() != null)
            helper(ptr.getRight(), s+1);
    }


    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String bitstring = "";
        while(StdIn.hasNextChar())
        {
            char c = StdIn.readChar();
            bitstring += encodings[c];
        }

        writeBitString(encodedFile, bitstring);
    }

    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        String str = readBitString(encodedFile);
        System.out.println(str);
        TreeNode ptr = huffmanRoot;
        for(int i = 0; i < str.length(); i++)
        {
            
            if(ptr.getData().getCharacter() != null)
            {
                StdOut.print(ptr.getData().getCharacter());
                ptr = huffmanRoot;
                i--;
            }

            else
            {
                if(str.charAt(i) == ('0'))
                {
                    ptr = ptr.getLeft();
                    
                }
                else
                {
                    ptr = ptr.getRight();
                    
                }
            }
        }
        
                StdOut.print((ptr.getData().getCharacter()));
        
    }

    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}