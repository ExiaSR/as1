package org.vfree.zichun3_habittrack;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * List all habit in this activity
 */
public class HabitHistoryActivity extends AppCompatActivity {
    private ListView habitHistoryListView;
    private ArrayAdapter<Habit> adapter;
    private ArrayList<Habit> habitList = new ArrayList<>();
    private ArrayList<Calendar> originalCompletion = new ArrayList<>();
    private List<Integer> toDeleteCompletion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        habitHistoryListView = (ListView) findViewById(R.id.habit_history_list);

        habitHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String habitName = parent.getItemAtPosition(position).toString();
                Habit habit = findHabit(habitName);
                if (habit.getHabitCompletion().isEmpty()) {
                    openUncompletedAlertDialog(habit);
                } else {
                    openHabitCompletionsDialog(habit);
                }
            }
        });

        JsonFileHelper jsonFile = new JsonFileHelper(this);
        habitList = jsonFile.loadAllFile();
        adapter = new ArrayAdapter<>(this, R.layout.habit_list_view_item, habitList);
        habitHistoryListView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        habitList.clear();
        JsonFileHelper jsonFile = new JsonFileHelper(this);
        habitList = jsonFile.loadAllFile();
    }

    /**
     * find habit object from habitList
     *
     * @param habitName habit name
     * @return habit object
     */
    private Habit findHabit(String habitName) {
        Habit habit = new NormalHabit();
        for (Habit habitTmp : habitList) {
            if (habitTmp.getHabitName() == habitName) {
                habit = habitTmp;
                break;
            }
        }
        return habit;
    }

    private void openHabitCompletionsDialog(final Habit habit) {
        List<CharSequence> completionList;
        completionList = getCompletionList(habit);
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitHistoryActivity.this);
        // indicate amount of completion and failure
        builder.setTitle("Completed: " + habit.getHabitCompletion().size() + " Failed: " + habit.getHabitFailure().size())
                // add index of selected item to arraylist
                .setMultiChoiceItems(completionList.toArray(new CharSequence[completionList.size()]), null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            toDeleteCompletion.add(which);
                        } else if (toDeleteCompletion.contains(which)) {
                            toDeleteCompletion.remove(which);
                        }
                    }
                })
                // Set the action buttons
                // delete selected habit button
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        deleteHabitCompletion(habit);
                    }
                })
                // delete habit button
                .setNeutralButton("DELETE Habit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHabit(habit);
                    }
                })
                // cancel button
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    /**
     * Open a dialog, indication user that the habit having been fullfilled yet
     * also allow user to delete that habit
     * @param habit habit
     */
    private void openUncompletedAlertDialog(final Habit habit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitHistoryActivity.this);
        builder.setTitle("This habit has not been completed yet")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                // delete habit button
                .setPositiveButton("DELETE Habit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteHabit(habit);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Return an arraylist of habitcompletion
     * @param habit
     * @return habitcompletion
     */
    private ArrayList<CharSequence> getCompletionList(Habit habit) {
        ArrayList<CharSequence> completionList = new ArrayList<>();
        try {
            for (Calendar date : habit.getHabitCompletion()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                completionList.add(sdf.format(date.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return completionList;
    }

    /**
     * Delete the habit completion according to the user choice
     * @param habit
     */
    private void deleteHabitCompletion(Habit habit) {
        originalCompletion = habit.getHabitCompletion();
        // deal with IndexOutOfRange Exception
        // just clear the ArrayList~~
        if (toDeleteCompletion.size() == originalCompletion.size()) {
            originalCompletion.clear();
        } else {
            for (int index : toDeleteCompletion) {
                originalCompletion.remove(index);
            }
        }

        // set the new completion back to the habit
        habit.setHabitCompletion(originalCompletion);

        // write changes to json file
        JsonFileHelper jsonFile = new JsonFileHelper(this);
        jsonFile.saveInFile(habit);

        // reload everything
        habitList.clear();
        originalCompletion.clear();
        toDeleteCompletion.clear();
        habitList = jsonFile.loadAllFile();
        adapter.notifyDataSetChanged();
        //recreate();
    }

    // delete the habit from internal storage
    private void deleteHabit(Habit habit) {
        JsonFileHelper jsonFile = new JsonFileHelper(this);
        jsonFile.deleteFile(habit);
        //recreate();
    }
}
