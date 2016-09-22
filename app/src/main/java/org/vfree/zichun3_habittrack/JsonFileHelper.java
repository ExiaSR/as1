package org.vfree.zichun3_habittrack;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class JsonFileHelper {
    private Context context;
    private Gson gson;

    /**
     * Read all saved habit records from internal storage
     *
     * @return an ArrayList content all past habit
     */
    public ArrayList<Habit> loadAllFile() {
        gson = new Gson();
        ArrayList<Habit> habitList = new ArrayList<>();
        Habit habitBuffer;
        String[] fileList = getFileList();
        for (String fileName : fileList) {
            try {
                FileInputStream fis = context.openFileInput(fileName);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                habitBuffer = gson.fromJson(in, Habit.class);
                habitList.add(habitBuffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return habitList;
    }

    // save a habit record to internal storage
    public void saveInFile(Habit habit) {
        gson = new Gson();
        // serialize habit object into json string
        String jsonStr = gson.toJson(habit);
        try  {
            // write json string into corresponding file
            FileOutputStream fos = context.openFileOutput(generateFileName(habit), Context.MODE_PRIVATE);
            fos.write(jsonStr.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // using timestamp as each habit's file name
    // at least in this app, the chance of two file being
    // creating at same the time as impossible
    private String generateFileName(Habit habit) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(habit.getDate().getTime()) + ".json";
    }

    // get all private insternal storage files' name
    private String[] getFileList() {
        return context.fileList();
    }
}
