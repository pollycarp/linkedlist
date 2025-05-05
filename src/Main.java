import java.io.*;
import java.util.Scanner;

// Node class representing a line of text in the editor
class Node {
    String text;
    Node next;

    Node(String text) {
        this.text = text;
        this.next = null;
    }
}

// LineEditor using singly linked list
class LineEditor {
    private Node head;
    private int currentLine = 1;
    private String filename;

    // Load lines from a file into the linked list
    public void loadFile(String filename) {
        this.filename = filename;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                appendLine(line.trim());
            }
        } catch (IOException e) {
            System.out.println("File not found. Starting with an empty document.");
        }
    }

    // Save current list content back to the file
    public void saveFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Node current = head;
            while (current != null) {
                writer.write(current.text);
                writer.newLine();
                current = current.next;
            }
            System.out.println("Saved and exited: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    // Insert a line at a specific position
    public void insertLine(String text, int position) {
        Node newNode = new Node(text);
        if (position <= 1 || head == null) {
            newNode.next = head;
            head = newNode;
            return;
        }

        Node prev = null;
        Node current = head;
        int index = 1;
        while (current != null && index < position) {
            prev = current;
            current = current.next;
            index++;
        }

        newNode.next = current;
        if (prev != null) {
            prev.next = newNode;
        }
    }

    // Append a line at the end of the list
    public void appendLine(String text) {
        Node newNode = new Node(text);
        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Delete lines from start to end (inclusive)
    public void deleteLines(int start, int end) {
        if (head == null) {
            System.out.println("No lines to delete.");
            return;
        }

        Node dummy = new Node("");
        dummy.next = head;
        Node prev = dummy;
        Node current = head;
        int index = 1;

        while (current != null && index < start) {
            prev = current;
            current = current.next;
            index++;
        }

        while (current != null && index <= end) {
            current = current.next;
            index++;
        }

        prev.next = current;
        head = dummy.next;
    }

    // List lines between start and end
    public void listLines(int start, int end) {
        Node current = head;
        int index = 1;

        while (current != null) {
            if (index >= start && index <= end) {
                System.out.println(index + ": " + current.text);
            }
            current = current.next;
            index++;
        }
    }

    public int getCurrentLine() {
        return currentLine;
    }
}

// Recursive multiplication function
class RecursiveMath {
    public static int multiply(int a, int b) {
        if (b == 0) return 0;
        if (b > 0) return a + multiply(a, b - 1);
        return -multiply(a, -b);
    }

    public static int sumSeries(int n) {
        if (n <= 0) return 0;
        return n + sumSeries(n - 1);
    }
}

public class Main {
    public static void runEditor() {
        Scanner scanner = new Scanner(System.in);
        LineEditor editor = new LineEditor();

        System.out.print("Enter EDIT <filename>: ");
        String command = scanner.nextLine().trim();
        if (!command.toLowerCase().startsWith("edit ")) {
            System.out.println("Invalid command. Start with: EDIT <filename>");
            return;
        }

        String filename = command.substring(5);
        editor.loadFile(filename);

        while (true) {
            System.out.print("[" + editor.getCurrentLine() + "]> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+");
            String cmd = parts[0].toUpperCase();

            switch (cmd) {
                case "I":
                    int pos = (parts.length > 1) ? Integer.parseInt(parts[1]) : editor.getCurrentLine();
                    System.out.print("Enter text to insert: ");
                    String insertText = scanner.nextLine();
                    editor.insertLine(insertText, pos);
                    break;
                case "A":
                    System.out.print("Enter text to append: ");
                    String appendText = scanner.nextLine();
                    editor.appendLine(appendText);
                    break;
                case "D":
                    if (parts.length == 3) {
                        editor.deleteLines(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    } else if (parts.length == 2) {
                        editor.deleteLines(Integer.parseInt(parts[1]), Integer.parseInt(parts[1]));
                    } else {
                        editor.deleteLines(editor.getCurrentLine(), editor.getCurrentLine());
                    }
                    break;
                case "L":
                    if (parts.length == 3) {
                        editor.listLines(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                    } else if (parts.length == 2) {
                        editor.listLines(Integer.parseInt(parts[1]), Integer.parseInt(parts[1]));
                    } else {
                        editor.listLines(editor.getCurrentLine(), editor.getCurrentLine());
                    }
                    break;
                case "E":
                    editor.saveFile();
                    return;
                default:
                    System.out.println("Unknown command. Use I, A, D, L, E.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Run Line Editor");
        System.out.println("2. Test Recursive Multiplication");
        System.out.println("3. Test Series Summation");
        System.out.print("Choose an option (1–3): ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                runEditor();
                break;
            case "2":
                System.out.print("Enter first number: ");
                int a = scanner.nextInt();
                System.out.print("Enter second number: ");
                int b = scanner.nextInt();
                System.out.println(a + " * " + b + " = " + RecursiveMath.multiply(a, b));
                break;
            case "3":
                System.out.print("Enter number of terms to sum: ");
                int n = scanner.nextInt();
                System.out.println("Sum of first " + n + " numbers: " + RecursiveMath.sumSeries(n));
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
