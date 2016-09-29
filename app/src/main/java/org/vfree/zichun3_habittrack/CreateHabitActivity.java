package org.vfree.zichun3_habittrack;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitActivity extends AppCompatActivity implements View.OnClickListener {
    private Habit newHabit;
    private Calendar newHabitDate = Calendar.getInstance();
    private EditText habitNameEditText;
    private EditText habitDateEditText;
    private EditText habitRepeatEditText;
    private Button createHabitButton;
    final String[] daySelector = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private ArrayList<String> daySelected = new ArrayList<>();
    private ArrayList<Habit> habitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        // setup each EditText and their onClickListner
        habitDateEditText = (EditText) findViewById(R.id.habit_date);
        habitDateEditText.setFocusable(false);
        habitDateEditText.setOnClickListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
        habitDateEditText.setHint("Date (Default: " + sdf.format(newHabitDate.getTime()) + ")");

        habitNameEditText = (EditText) findViewById(R.id.habit_name);

        habitRepeatEditText = (EditText) findViewById(R.id.habit_repeat);
        habitRepeatEditText.setFocusable(false);
        habitRepeatEditText.setOnClickListener(this);

        createHabitButton = (Button) findViewById(R.id.create_habit_button);
        createHabitButton.setOnClickListener(this);

        habitDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateHabitActivity.this, onDateSetListener,
                        newHabitDate.get(Calendar.YEAR), newHabitDate.get(Calendar.MONTH),
                        newHabitDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            newHabitDate.set(Calendar.YEAR, year);
            newHabitDate.set(Calendar.MONTH, monthOfYear);
            newHabitDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            habitDateEditText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    };

    /**
     * OnClick event, when user click Create Habit button it would create a habit object, and save
     * it to a json file
     */
    protected void createHabit() {
        Intent intent = new Intent(this, MainActivity.class);
        // Get habit name from user input
        String habitName = habitNameEditText.getText().toString();
        // if habit name is not given
        if (habitName.isEmpty()) {
            habitNameEditText.setError("Habit name is empty!");
        } else if (checkHabitName(habitName)) {
            // if habitname already exist
            habitNameEditText.setError("Habit name has been taken!");
        } else if (daySelected.isEmpty()) {
            // if day repeat is not specify
            habitRepeatEditText.setError("Habit occurence is empty!");
        } else {
            try {
                Calendar current = Calendar.getInstance();
                // new a object (heyue)
                newHabit = new ToDoHabit(habitName, newHabitDate, daySelected, current);
                JsonFileHelper jsonFile = new JsonFileHelper(this);
                jsonFile.saveInFile(newHabit);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    /**
     * Open up a dialog for user to select repeat day of week
     */
    private void openRepeatDayPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateHabitActivity.this);
        daySelected = new ArrayList();
        builder.setTitle(R.string.repeat_dialog)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.day_selector, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            daySelected.add(daySelector[which]);
                        } else if (daySelected.contains(daySelector[which])) {
                            daySelected.remove(daySelector[which]);
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // print selected day of text field
                        habitRepeatEditText.setText(daySelected.toString().replace("[", "").replace("]", ""));
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
     * onClick event for EditText
     *
     * @param view ID
     */
    @Override
    public void onClick(View view) {
        if (view == habitRepeatEditText) {
            openRepeatDayPickerDialog();
        } else if (view == createHabitButton) {
            createHabit();
        }
    }

    /**
     * Check if the habit name has been taken or not
     *
     * @return true -> habit name already exist flase -> habit name does not exist
     */
    private Boolean checkHabitName(String habitName) {
        JsonFileHelper jsonFile = new JsonFileHelper(this);
        habitList = jsonFile.loadAllFile();
        for (Habit habit : habitList) {
            if (habitName.equals(habit.getHabitName())) {
                return true;
            }
        }
        return false;
    }
}
