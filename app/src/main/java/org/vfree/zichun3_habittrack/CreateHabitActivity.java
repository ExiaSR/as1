package org.vfree.zichun3_habittrack;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateHabitActivity extends AppCompatActivity implements View.OnClickListener{
    private Habit newHabit;
    private Calendar newHabitDate;
    private EditText habitNameText;
    private EditText habitDateText;
    private EditText habitRepeatText;
    private Button createHabitButton;
    private int mYear, mMonth, mDay;
    private DatePickerDialog datePickerDialog;
    final String[] daySelector = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private ArrayList<String> daySelected = new ArrayList<>();
    private ArrayList<Habit> habitList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);

        // setup each EditText and their onClickListner
        habitDateText = (EditText) findViewById(R.id.habit_date);
        habitDateText.setFocusable(false);
        habitDateText.setOnClickListener(this);

        habitNameText = (EditText) findViewById(R.id.habit_name);

        habitRepeatText = (EditText) findViewById(R.id.habit_repeat);
        habitRepeatText.setFocusable(false);
        habitRepeatText.setOnClickListener(this);

        createHabitButton = (Button) findViewById(R.id.create_habit_button);
        createHabitButton.setOnClickListener(this);


    }

    /**
     * OnClick event, when user click Create Habit button
     * it would create a habit object, and save it to a json
     * file
     *
     */
    protected void createHabit() {
        Intent intent = new Intent(this, MainActivity.class);
        // Get habit name from user input
        String habitName = habitNameText.getText().toString();
        // init Calendar object
        newHabitDate = new GregorianCalendar(mYear, mMonth, mDay);
        if (habitName.isEmpty()) {
            habitNameText.setError("Habit name is empty!");
        }
        else if (checkHabitName(habitName)) {
            habitNameText.setError("Habit name has been taken!");
        }
        else if (daySelected.isEmpty()) {
            habitRepeatText.setError("Habit occurence is empty!");
        }
        else {
            try {
                Calendar current = Calendar.getInstance();
                newHabit = new ToDoHabit(habitName, newHabitDate, daySelected, current);
                //newHabit.saveHabitToFile();
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
     * Open up a dialog for user to set start day
     *
     */
    private void openDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        // prefill with current date
        habitDateText.setHint(mYear + "-" + mMonth + "-" + mDay);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        habitDateText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    /**
     * Open up a dialog for user to select repeat day of week
     *
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
                        habitRepeatText.setText(daySelected.toString().replace("[", "").replace("]", ""));
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
     * @param view ID
     */
    @Override
    public void onClick(View view) {
        if (view == habitDateText ) {
            openDatePickerDialog();
        } else if (view == habitRepeatText) {
            openRepeatDayPickerDialog();
        } else if (view == createHabitButton) {
            createHabit();
        }
    }

    /**
     * Check if the habit name has been taken or not
     *
     * @param habitName
     * @return  true -> habit name already exist
     *          flase -> habit name does not exist
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
