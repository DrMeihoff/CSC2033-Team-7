package com.csc.plastictracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Class responsible for generating the Graph view that displays the amount
// of items the user has recycled throughout the week.
public class GraphActivity extends AppCompatActivity {

    private GraphView graphView;
    private DbHandler dbHandler = new DbHandler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fillData(getDaysOfYear());
        /*dbHandler.getAllRecyclable(new DbHandler.onGetRecyclables() {
            @Override
            public void onSuccess(Recyclable[] recs) {
                fillStats(recs);
            }
        });*/
    }

    // Finds the day of the year, as well as all of the days which have passed this week
    //                                 ^ this is so that the appropriate days are displayed
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Integer> getDaysOfYear(){
        ArrayList<Integer> daysOfYear = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int currentDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        ArrayList<String> daysOfWeek = new ArrayList<String>(7);
        daysOfWeek.add(0, "MONDAY");
        daysOfWeek.add(1, "TUESDAY");
        daysOfWeek.add(2, "WEDNESDAY");
        daysOfWeek.add(3, "THURSDAY");
        daysOfWeek.add(4, "FRIDAY");
        daysOfWeek.add(5, "SATURDAY");
        daysOfWeek.add(6, "SUNDAY");
        String currentDayOfWeek = LocalDate.now().getDayOfWeek().name();
        for(int i=0; i<daysOfWeek.indexOf(currentDayOfWeek)+1;i++){
            int newDate = currentDayOfYear-i;
            daysOfYear.add(newDate);
        }
        Collections.reverse(daysOfYear);
        return daysOfYear;
    }

    // Generates and formats the graphView, as well as fills it with all of the appropriate data
    public void fillData(ArrayList<Integer> dates){
        dbHandler.getAllUserRecyclable(FirebaseAuth.getInstance().getCurrentUser().getUid(), new DbHandler.onGetUserRecyclables() {
            @Override
            public void onSuccess(UserRecyclable[] uRecs) {
                {
                    DataPoint[] dataPoints = new DataPoint[7];
                    for (int i=0; i < 7; i++){
                        dataPoints[i] = new DataPoint(i, 0);
                    }


                    for (UserRecyclable uRec: uRecs){
                        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(uRec.getUid())){
                            if (dates.contains(uRec.getDayOfYear())){
                                dataPoints[dates.indexOf(uRec.getDayOfYear())] = new DataPoint(dates.indexOf(uRec.getDayOfYear()), dataPoints[dates.indexOf(uRec.getDayOfYear())].getY() + 1);
                            }
                        }
                    }

                    setContentView(R.layout.activity_graph);
                    GraphView graphView = (GraphView) findViewById(R.id.graphview);
                    graphView.setTitle("Weekly Graph");
                    graphView.getGridLabelRenderer().setVerticalAxisTitle("Amount of items Recycled");
                    graphView.getGridLabelRenderer().setHorizontalAxisTitle("Day Of The Week");
                    StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
                    staticLabelsFormatter.setHorizontalLabels(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
                    graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    LineGraphSeries<DataPoint> currentWeek = new LineGraphSeries<>(dataPoints);
                    graphView.addSeries(currentWeek);
                }
            }
        });
    }

    // Displays User Statistics
    public void fillStats(Recyclable[] recs) {
        LinearLayout linLay = this.findViewById(R.id.layoutStats);
        //format of stats will be name, amount, date
        String[][] stats = {};
        for (Recyclable rec : recs) {
            boolean found = false;
            for (String[] stat : stats) {
                if (rec.getName().equals(stat[0])) {
                    found = true;
                    try {
                        Date oldDate = new SimpleDateFormat("dd/MM/yyyy").parse(stat[3]);
                        Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse(
                                rec.getDayOfMonth()+"-"+rec.getMonth()+"-"+rec.getYear());
                        if (newDate != null && newDate.after(oldDate)) {
                            stat[3] = newDate.toString();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            if (!found) {
                String[][] tempArray = new String[stats.length + 1][3];
                System.arraycopy(stats, 0, tempArray, 0, stats.length);
                tempArray[stats.length][0] = rec.getName();
                tempArray[stats.length][1] = "1"; //just putting 1 pending database change
                tempArray[stats.length][2] = rec.getDayOfMonth()+"-"+rec.getMonth()+"-"+rec.getYear();
                stats = tempArray;
            }
        }
        for (String[] stat : stats) {
            Date statDate = new Date();
            TextView tv = new TextView(this);
            String tvText = "Name: " + stat[0] + ", amount: " + stat[1]+", most recent recycle: "+stat[2];
            tv.setText(tvText);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tv.setTextColor(0xFF000000); // hex color 0xAARRGGBB
            tv.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
            tv.setLayoutParams(params);
            linLay.addView(tv);
        }
    }
}