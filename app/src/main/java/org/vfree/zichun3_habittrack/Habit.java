package org.vfree.zichun3_habittrack;

import android.text.BoringLayout;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public abstract class Habit{
    private String habitName;
    private Calendar date;
    private Calendar createdDate;
    private ArrayList<String> habitOccurance;
    private ArrayList<Calendar> habitCompletion;
    private ArrayList<Calendar> habitFailure;

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
        this.createdDate = Calendar.getInstance();
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
        this.createdDate = Calendar.getInstance();
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
        this.habitName = habitName;
        this.date = date;
        this.createdDate = Calendar.getInstance();
        this.habitOccurance = habitOccurrence;
    }

    /**
     *
     * @param habitName user specify habit name
     * @param date user specify start date
     * @param habitOccurance user specify event ouccrance
     * @param habitCompletion a list of tiemstamp that user complete the habit
     * @param habitFailure a list of timestamp that user fail to complete the habit
     * @throws InvalidHabitException if habit name is not specify
     */
    public Habit(String habitName, Calendar date, ArrayList<String> habitOccurance,
                 ArrayList<Calendar> habitCompletion, ArrayList<Calendar> habitFailure) throws InvalidHabitException{
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }

        // set habitName
        // set date to the given date
        this.habitName = habitName;
        this.date = date;
        this.createdDate = Calendar.getInstance();
        this.habitOccurance = habitOccurance;
        this.habitCompletion = habitCompletion;
        this.habitFailure = habitFailure;
    }

    /**
     * A method to determine if the habit has been completed or not
     *
     * @return TRUE or FALSE
     */
    protected boolean isCompleted() {
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

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }
}
