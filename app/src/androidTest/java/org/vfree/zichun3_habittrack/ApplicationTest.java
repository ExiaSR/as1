package org.vfree.zichun3_habittrack;

import com.google.gson.Gson;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * Test whether serializing is working
     * properly or not
     */
    public void createHabit_isCorrect() {
        String haitName = "Test";
        ArrayList<String> habitOccurance = new ArrayList<>();
        habitOccurance.add("Monday");
        Calendar createdDate = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        Habit habit;
        try {
            habit = new ToDoHabit(haitName, date, habitOccurance, createdDate);
            JsonFileHelper jsonFile = new JsonFileHelper(getContext());
            jsonFile.saveInFile(habit);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String expected = sdf.format(habit.getCreatedDate().getTime()) + ".json";
            String actual = getContext().fileList()[1];
            assertEquals(expected, actual);

        } catch (InvalidHabitException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serialize habit object into json file
     * and then deserilize habit object from
     * json file, compare them
     */
    public void serialize_and_deserialize() {
        Gson gson = new Gson();
        String haitName = "Test";
        ArrayList<String> habitOccurance = new ArrayList<>();
        habitOccurance.add("Monday");
        Calendar createdDate = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        Habit habit;
        ArrayList<Habit> habitList = new ArrayList<>();
        try {
            habit = new ToDoHabit(haitName, date, habitOccurance, createdDate);
            String expected = gson.toJson(habit);

            JsonFileHelper jsonFile = new JsonFileHelper(getContext());
            jsonFile.saveInFile(habit);

            habitList = jsonFile.loadAllFile();

            assertEquals(expected, habitList.get(0));

        } catch (InvalidHabitException e) {
            e.printStackTrace();
        }
    }
}