package org.vfree.zichun3_habittrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private JsonFileHelper jsonFile;
    private ArrayList<Habit> habitList;
    private ArrayList<Habit> RecentCompleteHabitList;
    private ArrayList<Habit> ToDoHabitList;
    private ArrayAdapter<Habit> recentCompletedAdapter;
    private ArrayAdapter<Habit> toDoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, CreateHabitActivity.class);
//                startActivity(intent);
//            }
//        });

        //loadAllHabit();
    }

    public void createHabit(View v) {
        Intent intent = new Intent(MainActivity.this, CreateHabitActivity.class);
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

        return super.onOptionsItemSelected(item);
    }

    /**
     * load all saved habit from json file
     * and catagorized them into RecentCompletedHabit
     * and ToDoHabit
     */
    protected void loadAllHabit() {
        jsonFile = new JsonFileHelper(this);
        habitList = jsonFile.loadAllFile();
        // check if there is any file exist
        if (!habitList.isEmpty()) {
            RecentCompleteHabitList = new ArrayList<>();
            ToDoHabitList = new ArrayList<>();

            for(Habit habit : habitList) {
                if (habit.isCompleted()) {
                    this.RecentCompleteHabitList.add(habit);
                } else {
                    this.ToDoHabitList.add(habit);
                }
            }
        }
    }

    protected void listRecentCompletedHabit() {

    }

    protected void listToDoHabit() {

    }
}
