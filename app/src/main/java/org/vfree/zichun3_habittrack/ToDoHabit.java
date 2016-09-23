package org.vfree.zichun3_habittrack;

import java.util.ArrayList;
import java.util.Calendar;

public class ToDoHabit extends Habit {
    public ToDoHabit(String habitName) throws InvalidHabitException {
        super(habitName);
    }

    public ToDoHabit(String habitName, Calendar date) throws InvalidHabitException {
        super(habitName, date);
    }

    public ToDoHabit(String habitName, Calendar date, ArrayList<String> habitOccurrence) throws InvalidHabitException {
        super(habitName, date, habitOccurrence);
    }


}
