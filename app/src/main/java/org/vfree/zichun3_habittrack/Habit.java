package org.vfree.zichun3_habittrack;

import android.text.BoringLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public abstract class Habit{
    private String habitName;
    private Calendar date;
    private ArrayList<String> habitOccurance;
    private ArrayList<Calendar> habitCompletion;
    private ArrayList<Calendar> habitFailure;
    private JsonFileHelper jsonFile;

    /**
     * constructor when user only specify the habit name
     *
     * @param habitName
     * @throws InvalidHabitException
     */
    public Habit(String habitName) throws InvalidHabitException {
        // if habitName is not given
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }

        // set habitName
        // if date is not given, default is current date
        this.habitName = habitName;
        this.date = Calendar.getInstance();
    }

    /**
     * constructor when user specify the habit name and date
     *
     * @param habitName user specify habit name
     * @param date user specify start date
     * @throws InvalidHabitException
     */
    public Habit(String habitName, Calendar date) throws InvalidHabitException {
        // if habitName is not given
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }

        // set habitName
        // set date to the given date
        this.habitName = habitName;
        this.date = date;
    }

    /**
     * Constructor when user specify habit name, date, and event repeat occurance
     *
     * @param habitName user specify habit name
     * @param date user specify start date
     * @param habitOccurrence user specify event occurance
     * @throws InvalidHabitException
     */
    public Habit(String habitName, Calendar date, ArrayList<String> habitOccurrence)
            throws InvalidHabitException{
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }
    }

    /**
     * A method to determine if the habit has been completed or not
     *
     * @return TRUE or FALSE
     */
    public boolean isCompleted() {
        if (this.habitCompletion.isEmpty()) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    /**
     * Keep a record of habit completed timestamp
     *
     * @param time when user is done with a habit
     */
    public void habitCompleted(Calendar time) {
        this.habitCompletion.add(time);
    }

    public String getHabitName() {
        return habitName;
    }

    public Calendar getDate() {
        return date;
    }


    public ArrayList<String> getHabitOccurance() {
        return habitOccurance;
    }

    public ArrayList<Calendar> getHabitCompletion() {
        return habitCompletion;
    }

    public ArrayList<Calendar> getHabitFailure() {
        return habitFailure;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setHabitCompletion(ArrayList<Calendar> habitCompletion) {
        this.habitCompletion = habitCompletion;
    }

    public void setHabitOccurance(ArrayList<String> habitOccurance) {
        this.habitOccurance = habitOccurance;
    }


    public void setHabitFailure(ArrayList<Calendar> habitFailure) {
        this.habitFailure = habitFailure;
    }
}
