package org.vfree.zichun3_habittrack;


import java.util.ArrayList;
import java.util.Calendar;

public class RecentCompletedHabit extends Habit {
    public RecentCompletedHabit(String habitName) throws InvalidHabitException {
        super(habitName);
    }

    public RecentCompletedHabit(String habitName, Calendar date) throws InvalidHabitException {
        super(habitName, date);
    }

    public RecentCompletedHabit(String habitName, Calendar date, ArrayList<String> habitOccurrence) throws InvalidHabitException {
        super(habitName, date, habitOccurrence);
    }

}
