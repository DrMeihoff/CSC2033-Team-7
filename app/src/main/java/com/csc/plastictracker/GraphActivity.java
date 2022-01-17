package com.csc.plastictracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        String[][] testArray = {{"A","1"},{"B","2"},{"C","3"},{"D","4"},{"ugh this is gonna take ages","dasdasdas"},{"as","asda"},{"sdagsd","sfdhfd"},{"asdsad","ads"}};
        fillStats(testArray);
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

    //currently formatting stats as [name, count] while testing
    public void fillStats(String[][] stats) {
        LinearLayout linLay = this.findViewById(R.id.layoutStats);
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