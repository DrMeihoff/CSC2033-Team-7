package com.csc.plastictracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class GraphActivity extends AppCompatActivity {

    private DbHandler dbHandler = new DbHandler();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        GraphView graphView = (GraphView) findViewById(R.id.graphview);
        graphView.setTitle("Weekly Graph ");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Amount Recycled (kg)");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Day Of The Week");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        LineGraphSeries<DataPoint> currentWeek = new LineGraphSeries<DataPoint>(getData(day()));
        graphView.addSeries(currentWeek);

        dbHandler.getAllRecyclable(new DbHandler.onGetRecyclables() {
            @Override
            public void onSuccess(Recyclable[] recs) {
                fillStats(recs);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String day() {
        String day = LocalDate.now().getDayOfWeek().name();
        return day;
    }

    protected DataPoint[] getData(String day) {
        ArrayList<String> daysOfWeek = new ArrayList<String>(7);
        daysOfWeek.add(0, "MONDAY");
        daysOfWeek.add(1, "TUESDAY");
        daysOfWeek.add(2, "WEDNESDAY");
        daysOfWeek.add(3, "THURSDAY");
        daysOfWeek.add(4, "FRIDAY");
        daysOfWeek.add(5, "SATURDAY");
        daysOfWeek.add(6, "SUNDAY");
        DataPoint[] dataPoints = new DataPoint[7];

        for (String d: daysOfWeek){
            if(daysOfWeek.indexOf(d) <= daysOfWeek.indexOf(day)) {
                dataPoints[daysOfWeek.indexOf(d)] = new DataPoint(daysOfWeek.indexOf(d), new Random().nextDouble());
            }else{
                dataPoints[daysOfWeek.indexOf(d)] = new DataPoint(daysOfWeek.indexOf(d), 0);
            }
        }


        return dataPoints;
    }

    public void fillStats(Recyclable[] recs) {
        LinearLayout linLay = this.findViewById(R.id.layoutStats);
        String[][] stats = {};
        for (Recyclable rec : recs) {
            Boolean found = false;
            for (String[] stat : stats) {
                if (rec.getName() == stat[0]) {
                    found = true;
                    stat[1] = String.valueOf(Integer.parseInt(stat[1]) + 1);
                    break;
                }
            }
            if (!found) {
                String[][] tempArray = new String[stats.length + 1][2];
                for (int i = 0; i<stats.length;i++) {
                    tempArray[i] = stats[i];
                }
                tempArray[stats.length][0] = rec.getName();
                tempArray[stats.length][1] = "1";
                stats = tempArray;
            }
        }
        for (String[] stat : stats) {
            TextView tv = new TextView(this);
            String tvText = "Name: " + stat[0] + ", amount: " + stat[1];
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