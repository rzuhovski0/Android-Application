package com.btproject.barberise;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.cosmocalendar.dialog.CalendarDialog;
import com.applikeysolutions.cosmocalendar.dialog.OnDaysSelectionListener;
import com.applikeysolutions.cosmocalendar.model.Day;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
import com.applikeysolutions.cosmocalendar.selection.SingleSelectionManager;
import com.applikeysolutions.cosmocalendar.settings.appearance.ConnectedDayIconPosition;
import com.applikeysolutions.cosmocalendar.settings.lists.connected_days.ConnectedDays;
import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class testing_activity extends AppCompatActivity {

    Button button;
    String startDate, endDate;
    CalendarView calendarView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

//        calendarView = findViewById(R.id.cosmo_calendar);
//        button = findViewById(R.id.selectButton);

        //Set First day of the week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Set Orientation 0 = Horizontal | 1 = Vertical
        calendarView.setCalendarOrientation(0);

        calendarView.setWeekendDays(new HashSet(){{
            add(Calendar.SATURDAY);
            add(Calendar.SUNDAY);
        }});

        //Range Selection
        calendarView.setSelectionType(SelectionType.SINGLE);

        Set<Long> days = new TreeSet<>();
        String date1 = "02-02-2023";
        String date2 = "02-09-2023";
        //........

        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date mDate1 = sdf1.parse(date1);
            long timeInMilliseconds1 = mDate1.getTime();


            Date mDate2 = sdf1.parse(date2);
            long timeInMilliseconds2 = mDate2.getTime();

            days.add(timeInMilliseconds1);
            days.add(timeInMilliseconds2);
            // can add more days to set

        } catch (Exception e) {
            Log.e("DateException", e.getMessage());
        }

//        int textColor = Color.parseColor("#0063B0");
        int selectedTextColor = Color.parseColor("#FFFFFF");
//        int disabledTextColor = Color.parseColor("#ff8000");
//        ConnectedDays connectedDays = new ConnectedDays(days, textColor, selectedTextColor, disabledTextColor);

        int orangeColor = Color.parseColor("#ea6545");
        int darkModeColor = Color.parseColor("#333333");

        int textColor = Color.parseColor("#B3B3B3");
        int disabledTextColor = Color.parseColor("#616161");


        //Add Connect days to calendar
//        calendarView.addConnectedDays(connectedDays);

//        calendarView.setWeekDayTitleTextColor(orangeColor);
        calendarView.setWeekendDayTextColor(orangeColor);
        calendarView.setBackgroundColor(darkModeColor);
        calendarView.setSelectedDayBackgroundColor(orangeColor);

        calendarView.setMonthTextColor(orangeColor);
        calendarView.setDayTextColor(textColor);
        calendarView.setDisabledDayTextColor(disabledTextColor);

        calendarView.setDisabledDays(days);
        calendarView.setCurrentDayIconRes(R.drawable.red_dot);

//        calendarView.setConnectedDayIconRes(R.drawable.red_dot);   // Drawable
        calendarView.setConnectedDayIconPosition(ConnectedDayIconPosition.BOTTOM);// TOP & BOTTOM
        calendarView.update();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Calendar> selectedDates = calendarView.getSelectedDates();

                // Iterate over the selected dates
                for (Calendar selectedDate : selectedDates) {
                    // Do something with the selected date
                    Toast.makeText(getApplicationContext(), selectedDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                    days.add(selectedDate.getTimeInMillis());
                    calendarView.setDisabledDays(days);
                    calendarView.update();
                }

            }
        });


    }
}