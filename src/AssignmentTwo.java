import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/*
Elizabeth West
CS 405
Assignment 2
Arguments: input file consists of one line made up of: true, false, and, or, xor
Objective: calculate how many ways the statement can be true or false
 */
public class AssignmentTwo {
    public static void main(String args[]) {
        try {
            ArrayList<Boolean> booleans = new ArrayList<>();
            ArrayList<String> operand = new ArrayList<>();
            File file = new File(args[0]);
            Scanner scanner = new Scanner(file);
            parseArgs(scanner, booleans, operand);
            int length = booleans.size();

            int true_matrix[][] = new int[length][length];
            int false_matrix[][] = new int[length][length];
            int total_matrix[][] = new int[length][length];
            initializeMatrices(booleans, true_matrix,false_matrix,total_matrix);

            int k;
            for (int i = 1; i < length; i++) {
                for (int j = 0, y = i; y < length; j++, y++) {
                    k = j;
                    int t = calculateTrue(j, y, k, operand, true_matrix, false_matrix, total_matrix, 0);
                    true_matrix[j][y] = t;
                    int f = calculateFalse(j, y, k, operand, true_matrix, false_matrix, total_matrix, 0);
                    false_matrix[j][y] = f;

                    total_matrix[j][y] = (true_matrix[j][y] + false_matrix[j][y]);
                }
            }
            System.out.println("True: " + true_matrix[0][length - 1]);
            System.out.println("False: " + false_matrix[0][length - 1]);
            System.out.println("Total: " + total_matrix[0][length - 1]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void parseArgs(Scanner scanner, ArrayList<Boolean> booleans, ArrayList<String> operand) {
        while (scanner.hasNextLine()) {
            String temp = scanner.next().toLowerCase();
            if (temp.equals("true")) {
                booleans.add(true);
            } else if (temp.equals("false")) {
                booleans.add(false);
            } else if (temp.equals("xor") || temp.equals("or") || temp.equals("and")) {
                operand.add(temp);
            }
        }
        scanner.close();
    }

    private static void initializeMatrices(ArrayList<Boolean> booleans, int[][] a, int[][] b, int[][] c) {
        for (int i = 0; i < booleans.size(); i++) {
            boolean temp = booleans.get(i);
            if (temp) {
                a[i][i] = 1;
                b[i][i] = 0;
                c[i][i] = 1;
            } else {
                a[i][i] = 0;
                b[i][i] = 1;
                c[i][i] = 1;
            }
        }
    }

    private static int calculateTrue(int i, int j, int k, ArrayList<String> operand, int[][] a, int[][] b, int[][] c, int temp) {
        if (k == j) {
            return temp;
        } else {
            String op = operand.get(k);
            if (op.equals("and")) {
                temp += (a[i][k] * a[k + 1][j]);
            } else if (op.equals("or")) {
                temp += (c[i][k] * c[k + 1][j]) - (b[i][k] * b[k + 1][j]);
            } else {
                temp += (a[i][k] * b[k + 1][j]) + (b[i][k] * a[k + 1][j]);
            }
        }
        return calculateTrue(i, j, k + 1, operand,a , b, c, temp);
    }

    private static int calculateFalse(int i, int j, int k, ArrayList<String> operand, int[][] a, int[][] b, int[][] c, int temp) {
        if (k == j) {
            return temp;
        } else {
            String op = operand.get(k);
            if (op.equals("or")) {
                temp += (b[i][k] * b[k + 1][j]);
            } else if (op.equals("and")) {
                temp += (c[i][k] * c[k + 1][j]) - (a[i][k] * a[k + 1][j]);
            } else {
                temp += (a[i][k] * a[k + 1][j]) + (b[i][k] * b[k + 1][j]);
            }
            return calculateFalse(i, j, k + 1, operand, a, b, c, temp);
        }
    }
}