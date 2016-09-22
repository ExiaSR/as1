package org.vfree.zichun3_habittrack;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText habitNameText;
    private EditText habitDateText;
    private EditText habitRepeatText;
    private int mYear, mMonth, mDay;
    private DatePickerDialog datePickerDialog;
    final String[] daySelector = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private ArrayList<String> daySelected;

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

    }

    /**
     * OnClick event, when user click Create Habit button
     * it would create a habit object, and save it to a json
     * file
     *
     * @param view whatever
     */
    protected void createHabit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        // Get habit name from user input
        String habitName = habitNameText.getText().toString();
        if (habitName.isEmpty()) {

        }
        startActivity(intent);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        habitDateText.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

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
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

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
        }
    }
}
