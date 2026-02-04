package edu.jsu.mcis.cs310;

import com.opencsv.CSVReaderHeaderAware;
import java.util.*;
import java.io.StringReader;
import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.opencsv.CSVWriter;
import java.io.IOException;
import java.io.StringWriter;


public class Converter {
    String csvFilePath = "input.csv"; // Replace with your file path
    String jsonFilePath = "input.json";
    String jsonOutput = csvToJson(csvFilePath);
    String csvOutput = jsonToCsv(jsonFilePath);
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        JsonArray jArray = new JsonArray();
        //System.out.println(csvString);
        JsonObject finished = new JsonObject();
      
       
        
        String result = "{}"; // default return value; replace later!
        
        try {
        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new StringReader(csvString));
        List<String[]> csvArray = reader.readAll();
        String[] headers = csvArray.get(0);
        JsonArray headings = new JsonArray();
        JsonArray rowData = new JsonArray();
        JsonArray pNums = new JsonArray();
        
        headings.addAll(Arrays.asList(headers));
         
        
        for (int i = 1; i <csvArray.size(); i++){
            String[] row = csvArray.get(i);
            pNums.add(row[0]);
            JsonObject Text = new JsonObject();
            for(int x = 0; x < headers.length && x< row.length; x++){
                Text.put(headers[x], row[x]);
                
            }
            rowData.add(Text);
        }
          
        finished.put("ColHeadings", headings);
        finished.put("ProdNums", pNums);
        finished.put("Data", rowData);
        
        
        return Jsoner.serialize(finished);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
// "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
// "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        String result = ""; // default return value; replace later!
        
        try {
            
          JsonObject mainObj = (JsonObject)Jsoner.deserialize(jsonString);
          StringWriter sWriter = new StringWriter();
          
          try(CSVWriter csvWriter = new CSVWriter(sWriter)){
              JsonArray headings = (JsonArray)mainObj.get("ColHeadings");
              JsonArray data = (JsonArray)mainObj.get("Data");
              JsonArray pNums = (JsonArray)mainObj.get("ProdNums");
              
              if (headings != null) {
        String[] headerRow = headings.stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        csvWriter.writeNext(headerRow);
    }
            for (Object rowObj : data) {
                if (rowObj instanceof JsonArray & pNums != null) {
                    JsonArray row = (JsonArray) rowObj;
                    String[] entries = new String[row.size()];
                    String[] numRow = new String[pNums.size()];
                    List<String> fullLine = new ArrayList<>();
                    
                    
                    for (int i = 0; i < row.size(); i++) {
                        entries[i] = String.valueOf(row.get(i));
                        numRow[i] = String.valueOf(pNums.get(i));
                        fullLine.add(numRow[i]);
                        fullLine.add(entries[i]);
        
        }
        csvWriter.writeNext(fullLine.toArray(new String[0]));
    }
}
      
              
              //I used these lists to parse the JsonArrays
//              List<String> dataList = new ArrayList<>();
//              for(Object item : data){
//                  dataList.add((String)item);
//              }
//              List<String> numList = new ArrayList<>();
//              for(Object item : pNums){
//                  numList.add((String)item);
//              }



              
//              System.out.println(headings);
//              System.out.println(data);
//              System.out.println(pNums);
              //System.out.println(dataList);
              
//              String[] headers = headings.toArray(new String[0]);
              //String[] text = data.toArray(new String[0]);
//              String[] nums = pNums.toArray(new String[1]);
// Created these thinking they would work, they in fact, did not.
              
              //System.out.println(sWriter.toString());
//              for (Object text : data) {
//                  System.out.println(text);
//                  System.out.println(nums.toString());
//                  Object fullData = new Object();
//                  for(int i = 0; i < data.size(); i++){
//                      
//                  }
//              }
//              for(Object text : data){
//                  if (!(text instanceof JsonObject)) continue;
//              JsonObject rows = (JsonObject)text;
//              String[] row = new String[headers.length]; // This one was wierd because its one of the only . commands that doesnt have () at the end.
//                for (int i = 0; i < headers.length; i++) {
//    // 1. Get the value as a generic Object (could be Integer, String, or Boolean)
//    Object val = rows.get(headers[i]);
//                    System.out.println("carl" + i);
//
//    // 2. Convert to String safely
//    // If it's an int (e.g., 1), .toString() makes it "1"
//    // If it's null, we use "" to avoid a crash
//    row[i] = (val != null) ? String.valueOf(val) : "";
//}
//                csvWriter.writeNext(row, true);
//              }
              
          }
          String finalCSV = sWriter.toString();
          return finalCSV;
            
            
        }
        catch (JsonException | IOException e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
