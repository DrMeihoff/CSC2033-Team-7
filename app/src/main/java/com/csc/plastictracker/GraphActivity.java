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
import java.util.concurrent.atomic.AtomicReference;

public class GraphActivity extends AppCompatActivity {

    private GraphView graphView;
    private DbHandler dbHandler = new DbHandler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            fillData(getDaysOfYear());
            dbHandler.getAllUserRecyclable(FirebaseAuth.getInstance().getCurrentUser().getUid(), uRecs -> fillStats(uRecs));
        }
    }

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

    //this function is used to fill in the linear layout beneath the graph with the user's recyclables
    public void fillStats(UserRecyclable[] uRecs) {
        //format of stats will be barcodeID, name, amount, date
        String[][] stats = {};
        //this loop will fill stats with user recyclables. If there are any duplicates, it updates the date to the most recent one.
        for (UserRecyclable uRec : uRecs) {
            boolean found = false;
            for (String[] stat : stats) {
                if (uRec.getBarcodeId().equals(stat[0])) {
                    found = true;
                    try {
                        //if this recyclable exists, chooses the most recent one
                        Date oldDate = new SimpleDateFormat("dd/MM/yyyy").parse(stat[3]);
                        Date newDate = new SimpleDateFormat("dd/MM/yyyy").parse(
                                uRec.getDayOfYear() + "-" + uRec.getMonth() + "-" + uRec.getYear());
                        if (newDate != null && newDate.after(oldDate)) {
                            stat[3] = newDate.toString();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //increases the amount value in the stats array
                    stat[2] = String.valueOf((Integer.parseInt(stat[2]) + 1));
                    break;
                }
            }
            //if this recyclable is new, increases array length by 1 and adds it.
            if (!found) {
                final int statsLength = stats.length;
                String[][] tempArray = new String[statsLength + 1][4];
                System.arraycopy(stats, 0, tempArray, 0, statsLength);
                tempArray[statsLength][0] = uRec.getBarcodeId();
                tempArray[statsLength][2] = "1";
                tempArray[statsLength][3] = uRec.getDayOfYear() + "-" + uRec.getMonth() + "-" + uRec.getYear();
                dbHandler.getRecyclable(uRec.getBarcodeId(), new DbHandler.onGetRecyclable() {
                    @Override
                    public void onSuccess(Recyclable rec) {
                        tempArray[statsLength][1] = rec.getName();
                        //this part was originally synchronous, but has been moved to be asynchronous to
                        //match the database returns and display correct item names
                        addTV(tempArray[statsLength]);
                    }
                });
                stats = tempArray; //the final array is stored just in case
            }
        }
    }

    //this function dynamically creates new TextViews in the linear layout for each recyclable
    public void addTV(String[] stat) {
        LinearLayout linLay = this.findViewById(R.id.layoutStats);
        TextView tv = new TextView(this);
        String tvText = "Name: " + stat[1] + ", amount: " + stat[2]+"x, most recent recycle: "+stat[3];
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