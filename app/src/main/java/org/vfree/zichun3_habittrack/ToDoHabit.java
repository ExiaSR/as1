package org.vfree.zichun3_habittrack;

import java.util.Calendar;

public class ToDoHabit extends Habit {
    public ToDoHabit(String habitName) throws InvalidHabitException {
        super(habitName);
    }

    public ToDoHabit(String habitName, Calendar date) throws InvalidHabitException {
        super(habitName, date);
    }

}
