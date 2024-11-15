package services;/* Summary: Converts a CSV file to a JSON file.*/

//import java.util.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;

/**
 * CSVtoJSON
 * <p>
 * parses CSV data into a JSONObject.
 *
 * @author Chris Trumpet, Matthew Lee, Mohit Ambe, Shrinand Perumal, Vraj Patel
 * @version December 11, 2023
 */
public class CSVtoJSON extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String CSVFile;

    public CSVtoJSON(String csvFile) {
        this.CSVFile = csvFile;
    }

    public String convert() {
        /*Converts a .csv file to .json. Assumes first line is header with columns*/
        try {
            BufferedReader read = new BufferedReader(new FileReader(CSVFile));

            StringBuilder stringBuilder = new StringBuilder();

            String line;
            String[] columns; //contains column names
            int num_cols;
            String[] tokens;

            int progress = 0; //check progress

            //initialize columns
            line = read.readLine();
            columns = line.split(",");
            num_cols = columns.length;


            stringBuilder.append("["); //begin file as array
            line = read.readLine();


            while (true) {
                tokens = line.split(",");

                if (tokens.length == num_cols) { //if number columns equal to number entries
                    stringBuilder.append("{");

                    for (int k = 0; k < num_cols; ++k) { //for each column
                        if (tokens[k].matches("^-?[0-9]*\\.?[0-9]*$")) { //if a number
                            stringBuilder.append("\"").append(columns[k]).append("\": ").append(tokens[k]);
                            if (k < num_cols - 1) stringBuilder.append(", ");
                        } else { //if a string
                            stringBuilder.append("\"").append(columns[k]).append("\": \"").append(tokens[k]).append("\"");
                            if (k < num_cols - 1) stringBuilder.append(", ");
                        }
                    }

                    ++progress; //progress update
                    if (progress % 10000 == 0) System.out.println(progress); //print progress           


                    if ((line = read.readLine()) != null) {//if not last line
                        stringBuilder.append("},");
                        stringBuilder.append("\n");
                    } else {
                        stringBuilder.append("}]");//if last line
                        stringBuilder.append("\n");
                        break;
                    }
                } else {
                    System.out.println("ERROR: Formatting error line \" + (progress + 2)\n" + " + \". Failed to parse.\"");
                }
            }

            read.close();
            return stringBuilder.toString();
        } catch (IOException ignore) {
            // TODO Auto-generated catch block
        }
        return null;
    }
}