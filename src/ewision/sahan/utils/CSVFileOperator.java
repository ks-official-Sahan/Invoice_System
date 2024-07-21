package ewision.sahan.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.awt.Component;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author ksoff
 */
public class CSVFileOperator {

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

    public File selectCSVPath(Component parent) {
        JFileChooser fc = new JFileChooser();

        fc.setDialogTitle("Select Destination");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);

        try {
            int option = fc.showOpenDialog(parent);
            if (option == JFileChooser.APPROVE_OPTION) {
                System.out.println(fc.getCurrentDirectory());
                return fc.getCurrentDirectory();
            }
        } catch (HeadlessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void writeCSV(File file, List<String[]> data) throws IOException {
        // first create file object for file placed at location
        // specified by filepath
        file.createNewFile();

        // create FileWriter object with file as parameter 
        FileWriter outputfile = new FileWriter(file);

        // create CSVWriter with '|' as separator 
        CSVWriter writer = new CSVWriter(outputfile);

        // use a List which contains String array 
        writer.writeAll(data);

        // closing writer connection 
        writer.close();
    }

    public List getAll(File file) throws FileNotFoundException, IOException, CsvException {
        FileReader fileReader = new FileReader(file);

        // // create csvReader object and skip first Line
        CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
        // // create csvReader object passing 
        /* file reader as a parameter */
        //CSVReader csvReader = new CSVReader(fileReader);

        List<String[]> dataList = csvReader.readAll();

        return dataList;
    }

    // <editor-fold defaultstate="collapsed" desc="Unused">
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
    // </editor-fold>

}
