//Yizhou Wang
//669026
//DS project1
package Server;

import java.io.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;


public class Dictionary {
    public static HashMap<String, String> dictionary;

    public Dictionary() throws IOException
    {
        HashMap<String,String>full_dictionary = new HashMap<>();
        String key=null;
        String value=null;
        InputStream in = Dictionary.class.getResourceAsStream("result2.xlsm");
        File targetFile= new File("Target.tmp");
        FileUtils.copyInputStreamToFile(in, targetFile);
        FileInputStream excelFile = new FileInputStream(targetFile);
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        Sheet Sheet = workbook.getSheetAt(0);

        for (int rowIndex = 0; rowIndex <= Sheet.getLastRowNum(); rowIndex++) {
            Row row = Sheet.getRow(rowIndex);
            if (row != null) {
                Cell cell1 = row.getCell(0);
                if (cell1.getCellTypeEnum() == CellType.STRING)
                {
                    key = cell1.getStringCellValue();
                    key = key.toLowerCase();
                }
                Cell cell2 = row.getCell(1);
                if (cell2.getCellTypeEnum() == CellType.STRING)
                {
                    value = cell2.getStringCellValue();
                    value = value.replace("[","").replace("]","");
                }
                full_dictionary.put(key, value);
            }
        }
        dictionary = full_dictionary;
    }

    public String remove (String key)
    {
        if (dictionary.containsKey(key))
        {
            dictionary.remove(key);
            return("Remove successfully!");
        }
        else
            {
                return("Not found in the dictionary!");
            }
    }

    public String add (String key, String value)
    {
        if (dictionary.containsKey(key))
        {
            return ("Already existed!");
        }
        else
            {
                dictionary.put(key, value);
                return ("Add successfully!");
            }
    }

    public String lookup (String key)
    {
        return dictionary.getOrDefault(key, "Not found in the dictionary!");
    }
}

