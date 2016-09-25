package org.vfree.zichun3_habittrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;
/**
 * List all habit in this activity
 */
public class HabitHistoryActivity extends AppCompatActivity {
    private ListView habitHistoryListView;
    private ArrayAdapter<Habit> adapter;
    private ArrayList<Habit> habitList = new ArrayList<Habit>();
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


                Log.d("click", habit.getHabitName());
            }
        });

        JsonFileHelper jsonFile = new JsonFileHelper(this);
        habitList = jsonFile.loadAllFile();
        // load all existing file into habit list
        //habitList = jsonFile.loadAllFile();
        //loadFromFile();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new ArrayAdapter<>(this, R.layout.habit_history_list_item, habitList);
        //adapter = new HabitAdapter(this, habitList);
        habitHistoryListView.setAdapter(adapter);
    }

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
        List<CharSequence> completionList = new ArrayList<>();
        completionList = getCompletionList(habit);
        AlertDialog.Builder builder = new AlertDialog.Builder(HabitHistoryActivity.this);
        builder.setTitle("Completed: " + habit.getHabitCompletion().size() + " Failed: " + habit.getHabitFailure().size())
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
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

    private ArrayList<CharSequence> getCompletionList(Habit habit) {
        ArrayList<CharSequence> completionList = new ArrayList<>();
        try {

            for (Calendar date : habit.getHabitCompletion()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                Log.d("debug", habit.toString() + sdf.format(date.getTime()));
                completionList.add(sdf.format(date.getTime()));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return completionList;
    }

    private void deleteHabitCompletion(Habit habit) {
        originalCompletion = habit.getHabitCompletion();
        Log.d("debug", "deleting record");
        Log.d("debug", originalCompletion.toString());
        Gson gson = new Gson();
        Log.d("debug", gson.toJson(habit));
        Log.d("debug", "to delete index" + toDeleteCompletion.toString());
        for (int index : toDeleteCompletion) {
            originalCompletion.remove(index);
        }
        Log.d("debug", gson.toJson(habit));
        habit.setHabitCompletion(originalCompletion);

        JsonFileHelper jsonFile = new JsonFileHelper(this);
        jsonFile.saveInFile(habit);

        habitList.clear();
        originalCompletion.clear();
        toDeleteCompletion.clear();
        habitList = jsonFile.loadAllFile();
        adapter.notifyDataSetChanged();
        recreate();
    }

    private void deleteHabit(Habit habit) {
        JsonFileHelper jsonFile = new JsonFileHelper(this);
        jsonFile.deleteFile(habit);
        recreate();
    }
}
