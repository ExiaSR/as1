package org.vfree.zichun3_habittrack;

import com.google.gson.Gson;

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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
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
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, CreateHabitActivity.class);
//                startActivity(intent);
//            }
//        });

        loadAllHabit();
        //loadFromFile();
        // for testing only, delete everything
        //JsonFileHelper jsonFile = new JsonFileHelper(this);
        //jsonFile.deleteAllFile();
        //Log.d("debug", Arrays.toString(fileList()));
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
        //JsonFileHelper jsonFile = new JsonFileHelper(this);
        //habitList = jsonFile.loadAllFile();
        loadFromFile();
        // check if there is any file exist
        if (!habitList.isEmpty()) {

            // consider the habit which is finished within today
            // as recent completed
            Calendar current = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
            //Log.d("debug", "size of habitList" + habitList.size());
            for (Habit habit : habitList) {
                //Log.d("debug", habit.toString());
                Gson gson = new Gson();
                Log.d("debug", gson.toJson(habit));
                // if the habit should be done today
                if (current.after(habit.getDate()) || sdf.format(current.getTime()).equals(habit.getDate())) {
                    Log.d("debug", "yes");
                    if (habit.getHabitOccurance().contains(current.getDisplayName
                            (Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.CANADA))) {
                        //Log.d("debug", "contains");
                        if (habit.getHabitCompletion().isEmpty()) {
                            //Log.d("debug", "todo " + habit.getHabitName());
                            toDoHabitList.add(habit);
                        } else {
                            for (Calendar date : habit.getHabitCompletion()) {
                                current = Calendar.getInstance();
                                // if the habit has been fullfiled today
                                if (sdf.format(date.getTime()).equals(sdf.format(current.getTime()))) {
                                    //Log.d("debug", "recent completeed " + habit.toString());
                                    recentCompleteHabitList.add(habit);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            //Log.d("debug", "recent completed list" + recentCompleteHabitList.toString());
            //Log.d("debug", "todo list" + toDoHabitList.toString());
        }
    }

    /**
     * load all objects into habitList
     */
    private void loadFromFile() {
        String[] fileList = fileList();
        try {
            for (int i = 1; i < fileList.length; ++i) {
                Gson gson = new Gson();//Builder().registerTypeAdapter(Habit.class, new InterfaceAdapter<>);
                FileInputStream fis = openFileInput(fileList[i]);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));
                //Type habitType = new TypeToken<Habit>(){}.getType();
                Habit habit = gson.fromJson(in, NormalHabit.class);
                Log.d("habit_name", habit.getHabitName());
                habitList.add(habit);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException();
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
        jsonFile.saveInFile(habit);
        habitList.clear();
        recentCompleteHabitList.clear();
        toDoHabitList.clear();
        loadAllHabit();

        recentCompletedHabitAdapter.notifyDataSetChanged();
        toDoHabitAdapter.notifyDataSetChanged();
    }

    /**
     * Reload files to refresh this activity when user press back buttom
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
