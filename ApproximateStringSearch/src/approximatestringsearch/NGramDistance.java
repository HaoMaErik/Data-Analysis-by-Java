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
public class NGramDistance {

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

            System.out.println("Please input the required N for N-Gram: ");

            Scanner keyboard = new Scanner(System.in);
            int n = keyboard.nextInt();

            System.out.println("Searching locations......");
            System.out.println("*********************************************************");
            
            // Read file line by line
            while ((lineQ = bufferReader1.readLine()) != null) {

                wordQ = lineQ.split(" ");

                FileReader inputFile2 = new FileReader(fileName2);
                BufferedReader bufferReader2 = new BufferedReader(inputFile2);

                while ((lineT = bufferReader2.readLine()) != null) {

                    wordT = lineT.split(" ");

                    int i = 0;
                    int j = 0;

                    while (i < wordQ.length && j < wordT.length) {
                        
                        countCompare++;
                        
                        if (NGramDistances(wordQ[i], wordT[j], n) > 0.7) {

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

    public static float NGramDistances(String s, String t, int n) {

        final int sl = s.length();
        final int tl = t.length();
        
        if (sl == 0 || tl == 0) {
            if (sl == tl) {
                return 1;
            } else {
                return 0;
            }
        }

        int cost = 0;
        if (sl < n || tl < n) {
            for (int i = 0; i < Math.min(sl, tl); i++) {
                if (s.charAt(i) == t.charAt(i)) {
                    cost++;
                }
            }
            return (float) cost / Math.max(sl, tl);
        }

        char[] sa = new char[sl + n - 1];
        float p[]; //'previous' cost array, horizontally
        float d[]; // cost array, horizontally
        float _d[]; //placeholder to assist in swapping p and d

        //construct sa with prefix
        for (int i = 0; i < sa.length; i++) {
            if (i < n - 1) {
                sa[i] = 0; //add prefix
            } else {
                sa[i] = s.charAt(i - n + 1);
            }
        }
        p = new float[sl + 1];
        d = new float[sl + 1];

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char[] t_j = new char[n]; // jth n-gram of t

        for (i = 0; i <= sl; i++) {
            p[i] = i;
        }

        for (j = 1; j <= tl; j++) {
            //construct t_j n-gram 
            if (j < n) {
                for (int ti = 0; ti < n - j; ti++) {
                    t_j[ti] = 0; //add prefix
                }
                for (int ti = n - j; ti < n; ti++) {
                    t_j[ti] = t.charAt(ti - (n - j));
                }
            } else {
                t_j = t.substring(j - n, j).toCharArray();
            }
            d[0] = j;
            for (i = 1; i <= sl; i++) {
                cost = 0;
                int tn = n;
                //compare sa to t_j
                for (int ni = 0; ni < n; ni++) {
                    if (sa[i - 1 + ni] != t_j[ni]) {
                        cost++;
                    } else if (sa[i - 1 + ni] == 0) { //discount matches on prefix
                        tn--;
                    }
                }
                float ec = (float) cost / tn;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + ec);
            }
            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return 1.0f - (p[sl] / Math.max(tl, sl));
    }

}
