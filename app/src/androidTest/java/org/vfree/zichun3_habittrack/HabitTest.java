package org.vfree.zichun3_habittrack;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Michael on 16-10-03.
 */
public class HabitTest extends ApplicationTestCase<Application> {
     public HabitTest() {
        super(Application.class);
    }

    /**
     * Test if the habit completion has been properly
     * added or not
     */
    public void addHabitCompletionTest() {
        ArrayList<String> occurance = new ArrayList<>();
        occurance.add("Mon");
        occurance.add("Tue");
        Habit habit;
        try {
            habit = new ToDoHabit("test", Calendar.getInstance(), occurance, Calendar.getInstance());
            Calendar completedTime = Calendar.getInstance();
            habit.addHabitCompletion(completedTime);
            assertEquals(habit.getHabitCompletion().get(0), completedTime);
        } catch (InvalidHabitException e) {
            e.printStackTrace();
        }
    }

}
