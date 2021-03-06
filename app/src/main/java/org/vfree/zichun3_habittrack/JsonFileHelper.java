package org.vfree.zichun3_habittrack;

import com.google.gson.Gson;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A class that provide several methods to save and load habit object into json file which is store
 * at the internal storage
 */
public class JsonFileHelper {
    protected Context context;

    public JsonFileHelper(Context context) {
        this.context = context;
    }

    /**
     * Deserialize all saved json file into an ArrayList of objects
     *
     * @return an ArrayList content all past habit
     */
    protected ArrayList<Habit> loadAllFile() {
        ArrayList<Habit> habitList = new ArrayList<>();
        NormalHabit habitBuffer;
        String[] fileList = getFileList();
        for (int i = 1; i < fileList.length; ++i) {
            try {
                Gson gson = new Gson();
                FileInputStream fis = context.openFileInput(fileList[i]);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                habitBuffer = gson.fromJson(in, NormalHabit.class);
                habitList.add(habitBuffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return habitList;
    }

    /**
     * Serialize habit object into json string and write it into Android internal storage
     *
     * Save each habit as a file to prevent memory overflow in case there are too many habits
     *
     * @param habit habit to be saved
     */
    protected void saveInFile(Habit habit) {
        try {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(habit);
            // write json string into corresponding file
            FileOutputStream fos = context.openFileOutput(generateFileName(habit), 0);
            fos.write(jsonStr.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    protected void deleteFile(Habit habit) {
        String fileName = generateFileName(habit);
        context.deleteFile(fileName);
    }

    /**
     * Using timestamp when file is created as each habit's file name
     * since the chance of two file being creating at same the time impossible
     *
     * Also, separate each habit object into different files
     * provide the capability of indexing and avoid memory overflow
     *
     * This method return a filename of the habit object
     * @param habit habit object
     * @return filename
     */
    private String generateFileName(Habit habit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(habit.getCreatedDate().getTime()) + ".json";
    }

    /**
     * get a list of local files
     *
     * @return an array that contain file names
     */
    protected String[] getFileList() {
        return context.fileList();
    }
}
