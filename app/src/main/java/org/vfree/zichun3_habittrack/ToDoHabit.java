package org.vfree.zichun3_habittrack;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Habit that has to been done by today
 */
public class ToDoHabit extends Habit {

    public ToDoHabit(String habitName, Calendar date, ArrayList<String> habitOccurrence, Calendar createdDate) throws InvalidHabitException {
        super(habitName, date, habitOccurrence, createdDate);
    }
}
