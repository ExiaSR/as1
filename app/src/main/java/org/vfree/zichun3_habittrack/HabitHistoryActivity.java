package org.vfree.zichun3_habittrack;

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
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
/**
 * List all habit in this activity
 */
public class HabitHistoryActivity extends AppCompatActivity {
    private ListView habitHistoryListView;
    private ArrayAdapter<Habit> adapter;
    private ArrayList<Habit> habitList = new ArrayList<Habit>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_history);

        habitHistoryListView = (ListView) findViewById(R.id.habit_history_list);

        habitHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String v = parent.getItemAtPosition(position).toString();
                Habit habit = findHabit(v);
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

}
