/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sebastian_pohl
 */
public class LogFile {

    private BufferedWriter logWriter = null;

    public LogFile(String logName) throws IOException {

        File logFile = new File(logName);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        logWriter = new BufferedWriter(new FileWriter(logFile));

        writeToLog("Log file reinstantiated.");

    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (logWriter != null) {
                logWriter.flush();
                logWriter.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void writeToLog(String str) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            logWriter.write(dateFormat.format(date) + ": " + str + "\n");
            logWriter.flush();
        } catch(Exception e) {
            System.out.println("log system down.");
        }
    }
}
