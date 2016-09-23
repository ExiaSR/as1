package org.vfree.zichun3_habittrack;

import android.content.Context;
import android.util.Log;

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
    protected Context context;
    private Gson gson;

    public JsonFileHelper(Context context) {
        this.context = context;
    }
    /**
     * Deserialize all saved json file into
     * an ArrayList of objects
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

    /**
     * Serialize habit object into json string
     * and write it into Android internal storage
     *
     * Save each habit as a file to prevent memory
     * overflow in case there are too many habits
     *
     * @param habit habit to be saved
     */
    public void saveInFile(Habit habit) {
        gson = new Gson();
        // serialize habit object into json string
        String jsonStr = gson.toJson(habit);
        try  {
            // write json string into corresponding file
            FileOutputStream fos = context.openFileOutput(generateFileName(habit), Context.MODE_APPEND);
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

    // using timestamp as each habit's file name
    // at least in this app, the chance of two file being
    // creating at same the time as impossible
    private String generateFileName(Habit habit) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        //Log.d("file_name", sdf.format(habit.getCreatedDate().getTime()) + ".json");
        return sdf.format(habit.getCreatedDate().getTime()) + ".json";
    }

    // get all private insternal storage files' name
    public String[] getFileList() {
        return context.fileList();
    }
}
