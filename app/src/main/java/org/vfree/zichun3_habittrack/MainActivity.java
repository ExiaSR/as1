package org.vfree.zichun3_habittrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Display current completed and todo habit that should be done today
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Habit> habitList = new ArrayList<>();
    private ArrayList<Habit> recentCompleteHabitList = new ArrayList<>();
    private ArrayList<Habit> toDoHabitList = new ArrayList<>();
    private ArrayAdapter<Habit> recentCompletedHabitAdapter;
    private ArrayAdapter<Habit> toDoHabitAdapter;
    private ListView recentCompletedHabitListView;
    private ListView toDoHabitListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recentCompletedHabitListView = (ListView) findViewById(R.id.main_activity_recent_completed_list);
        toDoHabitListView = (ListView) findViewById(R.id.main_activity_todo_list);

        // enable click event for ListView item
        // when it's click, a dialog will popup
        // user may click confirm to complete the habit
        recentCompletedHabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String v = parent.getItemAtPosition(position).toString();
                openConfirmCompleteDialog(v);

            }
        });

        toDoHabitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String v = parent.getItemAtPosition(position).toString();
                openConfirmCompleteDialog(v);

            }
        });

        // load habits
        loadAllHabit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recentCompletedHabitAdapter = new ArrayAdapter<>(this, R.layout.habit_list_view_item, recentCompleteHabitList);
        toDoHabitAdapter = new ArrayAdapter<>(this, R.layout.habit_list_view_item, toDoHabitList);

        recentCompletedHabitListView.setAdapter(recentCompletedHabitAdapter);
        toDoHabitListView.setAdapter(toDoHabitAdapter);
    }

    public void createHabit(View v) {
        Intent intent = new Intent(MainActivity.this, CreateHabitActivity.class);
        Log.d("File_list", Arrays.toString(fileList()));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Go to habithistoryactivity
        if (id == R.id.action_history) {
            Intent intent = new Intent(this, HabitHistoryActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_acknowledgments) {
            // maybe add a license dialog
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * load all saved habit from json file and catagorized them into RecentCompletedHabit and
     * ToDoHabit
     */
    private void loadAllHabit() {
        JsonFileHelper jsonFile = new JsonFileHelper(this);
        habitList = jsonFile.loadAllFile();
        // check if there is any file exist
        if (!habitList.isEmpty()) {
            // consider the habit which is finished within today
            // as recent completed
            Calendar current = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
            for (Habit habit : habitList) {

                // if the habit should be done today
                if (current.after(habit.getDate()) || sdf.format(current.getTime()).equals(habit.getDate())) {
                    if (habit.getHabitOccurance().contains(current.getDisplayName
                            (Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CANADA))) {
                        if (habit.getHabitCompletion().isEmpty()) {
                            toDoHabitList.add(habit);
                        } else {
                            for (Calendar date : habit.getHabitCompletion()) {
                                current = Calendar.getInstance();
                                // if the habit has been fullfiled today
                                if (sdf.format(date.getTime()).equals(sdf.format(current.getTime()))) {
                                    recentCompleteHabitList.add(habit);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Find corespond habit object from habitList
     *
     * @return habit object
     */
    private Habit findHabitFromHabitList(String habitName) {
        Habit habit = new NormalHabit();
        for (Habit habitTmp : habitList) {
            if (habitTmp.getHabitName().equals(habitName)) {
                habit = habitTmp;
                break;
            }
        }
        return habit;
    }

    /**
     * Open a dialog to promot user to confirm habit is done
     */
    private void openConfirmCompleteDialog(final String habitName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Confirm habit is completed")
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addCompletionToHabit(habitName);

                    }
                })
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
     * Add completion timestamp to habit completionlist keep record and then reload all corepond
     * adapter and lists
     *
     * @param habitName habit name
     */
    private void addCompletionToHabit(String habitName) {
        Habit habit = findHabitFromHabitList(habitName);
        Calendar current = Calendar.getInstance();
        habit.addHabitCompletion(current);

        JsonFileHelper jsonFile = new JsonFileHelper(this);
        // update correponding json file, serialize
        // habit object into that file
        jsonFile.saveInFile(habit);

        // cleanup everything and reload everthing again
        habitList.clear();
        recentCompleteHabitList.clear();
        toDoHabitList.clear();
        loadAllHabit();

        // notify arrayadapter the data is changed
        recentCompletedHabitAdapter.notifyDataSetChanged();
        toDoHabitAdapter.notifyDataSetChanged();
    }

    /**
     * Reload files to refresh this activity when user press back buttom from other activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        habitList.clear();
        recentCompleteHabitList.clear();
        toDoHabitList.clear();
        loadAllHabit();

        recentCompletedHabitAdapter.notifyDataSetChanged();
        toDoHabitAdapter.notifyDataSetChanged();
    }
}
