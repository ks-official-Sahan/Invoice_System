package ewision.sahan.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ewision.sahan.customer.CustomerList;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author ksoff
 */
public class CSVFileReader {

    public File selectCSV(Component parent) {
        JFileChooser fc = new JFileChooser();

        fc.setFileFilter(new ExcelFileFilter());

        try {
            int option = fc.showOpenDialog(parent);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                return file;
                //readCSV(file);
                //readAll(file);
            }
        } catch (HeadlessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List getAll(File file) throws FileNotFoundException, IOException, CsvException {
        FileReader fileReader = new FileReader(file);

        // // create csvReader object and skip first Line
        //CSVReader csvReder = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
        // // create csvReader object passing 
        /* file reader as a parameter */
        CSVReader csvReader = new CSVReader(fileReader);

        List<String[]> dataList = csvReader.readAll();

        return dataList;
    }

    private void readAll(File file) throws FileNotFoundException, IOException, CsvException {
        FileReader fileReader = new FileReader(file);

        // // create csvReader object and skip first Line
        //CSVReader csvReder = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
        // // create csvReader object passing 
        /* file reader as a parameter */
        CSVReader csvReader = new CSVReader(fileReader);

        List<String[]> dataList = csvReader.readAll();

        // print Data 
        for (String[] dataRow : dataList) {
            for (String cell : dataRow) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    private void readLine(File file) throws FileNotFoundException, IOException, CsvException {
        FileReader fileReader = new FileReader(file);

        // // create csvReader object and skip first Line
        //CSVReader csvReder = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
        // // create csvReader object passing 
        /* file reader as a parameter */
        CSVReader csvReader = new CSVReader(fileReader);

        String[] nextRecord;

        // we are going to read data line by line 
        while ((nextRecord = csvReader.readNext()) != null) {
            for (String cell : nextRecord) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }

    private void readCSV(File file) throws FileNotFoundException, IOException {
        BufferedReader lineReader = new BufferedReader(new FileReader(file));

        String line = null;
        while ((line = lineReader.readLine()) != null) {
            String[] data = line.split(",");
            String id = data[0];
            System.out.println(id);
        }
    }

}
