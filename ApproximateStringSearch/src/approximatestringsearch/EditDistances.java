/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package approximatestringsearch;

import java.io.*;
import java.util.Scanner;

/**
 *
 * @author haom1
 */
public class EditDistances {

    public static void main(String[] args) {
        
        String fileName1 = "queries.1K.txt";
        String fileName2 = "tweets.3K.txt";
        
        try {

            //Create object of FileReader
            FileReader inputFile1 = new FileReader(fileName1);

            //Instantiate the BufferedReader Class
            BufferedReader bufferReader1 = new BufferedReader(inputFile1);

            //Variable to hold the one line data
            String lineQ;
            String lineT;
            String[] wordQ;
            String[] wordT;
            int countCompare = 0;
            int countMatch = 0;

            System.out.println("Please input the required minimum Edit Distance: ");

            Scanner keyboard = new Scanner(System.in);
            int minEditDistance = keyboard.nextInt();

            System.out.println("Searching locations......");
            System.out.println("*********************************************************");

            // Read file line by line
            while ((lineQ = bufferReader1.readLine()) != null) {

                wordQ = lineQ.split(" ");

                FileReader inputFile2 = new FileReader(fileName2);
                BufferedReader bufferReader2 = new BufferedReader(inputFile2);

                while ((lineT = bufferReader2.readLine()) != null) {

                    wordT = lineT.split("\\s+");

                    int i = 0;
                    int j = 0;

                    while (i < wordQ.length && j < wordT.length) {
                        
                        countCompare++;

                        if (editDistances(wordQ[i], wordT[j]) <= minEditDistance) {

                            if (i == wordQ.length - 1) {
                                
                                countMatch++;
                                String found = "";

                                String id = lineT.substring(0, 10);

                                for (int m = 0; m < wordQ.length; m++) {
                                    
                                    found = found + wordT[j - wordQ.length + 1 + m] + " ";
                                    
                                }
                                System.out.println("Location: " + lineQ + "\t" + " Match found in: " + id + "\t" + "Key words: " + found);

                                i = 0;
                                j = j + 2 - wordQ.length;
                                
                            } else {
                                
                                i++;
                                j++;
                                
                            }
                        } else {
                            
                            if (i > 0) {
                                i = 0;
                            } else {
                                j++;
                            }

                        }
                    }
                }

            }
            System.out.println("After compared " + countCompare + " times:");
            System.out.println("Approximate match count: " + countMatch);

            //Close the buffer reader
            bufferReader1.close();
            
        } catch (Exception e) {
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }
    }


    public static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public static int editDistances(String q, String t) {

        q = q.toLowerCase();
        t = t.toLowerCase();

        int[][] distance = new int[q.length() + 1][t.length() + 1];

        for (int i = 0; i <= q.length(); i++) {
            distance[i][0] = i;
        }

        for (int j = 0; j <= t.length(); j++) {
            distance[0][j] = j;
        }

        for (int i = 1; i <= q.length(); i++) {

            for (int j = 1; j <= t.length(); j++) {

                int a = 0;
                char charq = q.charAt(i - 1);
                char chart = t.charAt(j - 1);
                if (charq == chart) {
                    a = 0;
                } else {
                    a = 1;
                }

                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + a);
            }
        }

        return distance[q.length()][t.length()];
    }

}
