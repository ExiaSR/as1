package org.vfree.zichun3_habittrack;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Michael on 16-09-24.
 */
public class NormalHabit extends Habit{

    public NormalHabit() {
    }

    public NormalHabit(String habitName, Calendar date, ArrayList<String> habitOccurrence) throws InvalidHabitException {
        super(habitName, date, habitOccurrence);
    }

    public NormalHabit(String habitName, Calendar date, ArrayList<String> habitOccurance, ArrayList<Calendar> habitCompletion, ArrayList<Calendar> habitFailure) throws InvalidHabitException {
        super(habitName, date, habitOccurance, habitCompletion, habitFailure);
    }
}
