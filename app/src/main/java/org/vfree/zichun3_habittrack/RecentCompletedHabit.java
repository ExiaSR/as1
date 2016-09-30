package org.vfree.zichun3_habittrack;


import java.util.ArrayList;
import java.util.Calendar;

public class RecentCompletedHabit extends Habit {

    public RecentCompletedHabit(String habitName, Calendar date, ArrayList<String> habitOccurrence) throws InvalidHabitException {
        super(habitName, date, habitOccurrence);
    }

    public RecentCompletedHabit(String habitName, Calendar date, ArrayList<String> habitOccurance, ArrayList<Calendar> habitCompletion, ArrayList<Calendar> habitFailure) throws InvalidHabitException {
        super(habitName, date, habitOccurance, habitCompletion, habitFailure);
    }

    public RecentCompletedHabit(String habitName, Calendar date, ArrayList<String> habitOccurance, ArrayList<Calendar> habitFailure) throws InvalidHabitException {
        super(habitName, date, habitOccurance, habitFailure);
    }

    public RecentCompletedHabit(String habitName, Calendar date, ArrayList<String> habitOccurrence, Calendar createdDate) throws InvalidHabitException {
        super(habitName, date, habitOccurrence, createdDate);
    }
}
