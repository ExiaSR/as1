package org.vfree.zichun3_habittrack;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Abstract class of Habit provide all the attributes
 */
public abstract class Habit {
    private String habitName;
    private Calendar date;
    private Calendar createdDate; // timestamp when the file is created
    private ArrayList<String> habitOccurance = new ArrayList<>();
    private ArrayList<Calendar> habitCompletion = new ArrayList<>();
    private ArrayList<Calendar> habitFailure = new ArrayList<>();

    /**
     * Empty consturctor
     */
    public Habit() {

    }

    /**
     * Constructor when user specify habit name, date, and event repeat occurance
     *
     * @param habitName       user specify habit name
     * @param date            user specify start date
     * @param habitOccurrence user specify event occurance
     */
    public Habit(String habitName, Calendar date, ArrayList<String> habitOccurrence)
            throws InvalidHabitException {
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }
        this.habitName = habitName;
        this.date = date;
        //this.createdDate = Calendar.getInstance();
        this.habitOccurance = habitOccurrence;
    }

    /**
     * Constructor
     *
     * @param habitName
     * @param date
     * @param habitOccurrence
     * @param createdDate
     * @throws InvalidHabitException
     */
    public Habit(String habitName, Calendar date, ArrayList<String> habitOccurrence, Calendar createdDate)
            throws InvalidHabitException {
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }
        this.habitName = habitName;
        this.date = date;
        this.createdDate = createdDate;
        this.habitOccurance = habitOccurrence;
    }

    /**
     * Constructor
     * @param habitName
     * @param date
     * @param habitOccurance
     * @param habitFailure
     * @throws InvalidHabitException
     */
    public Habit(String habitName, Calendar date, ArrayList<String> habitOccurance, ArrayList<Calendar> habitFailure) throws InvalidHabitException {
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }

        // set habitName
        // set date to the given date
        this.habitName = habitName;
        this.date = date;
        this.habitOccurance = habitOccurance;
        this.habitFailure = habitFailure;
    }

    /**
     * @param habitName       user specify habit name
     * @param date            user specify start date
     * @param habitOccurance  user specify event ouccrance
     * @param habitCompletion a list of tiemstamp that user complete the habit
     * @param habitFailure    a list of timestamp that user fail to complete the habit
     * @throws InvalidHabitException if habit name is not specify
     */
    public Habit(String habitName, Calendar date, ArrayList<String> habitOccurance,
                 ArrayList<Calendar> habitCompletion, ArrayList<Calendar> habitFailure) throws InvalidHabitException {
        if (habitName.isEmpty()) {
            throw new InvalidHabitException();
        }

        // set habitName
        // set date to the given date
        this.habitName = habitName;
        this.date = date;
        //this.createdDate = Calendar.getInstance();
        this.habitOccurance = habitOccurance;
        this.habitCompletion = habitCompletion;
        this.habitFailure = habitFailure;
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

    public void setHabitCompletion(ArrayList<Calendar> habitCompletion) {
        this.habitCompletion = habitCompletion;
    }

    public void addHabitCompletion(Calendar date) {
        this.habitCompletion.add(date);
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return this.habitName;
    }
}
