package com.csc.plastictracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Random;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        GraphView graphView = (GraphView) findViewById(R.id.graphview);
        graphView.setTitle("Weekly Graph");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Amount Recycled (kg)");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("Day Of The Week");
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        LineGraphSeries<DataPoint> currentWeek = new LineGraphSeries<DataPoint>(getSampleData());
        graphView.addSeries(currentWeek);
    }


    /* temporary */
    protected DataPoint[] getSampleData() {
        DataPoint[] dataPoints = {
                new DataPoint(1, new Random().nextDouble()),
                new DataPoint(2, new Random().nextDouble()),
                new DataPoint(3, new Random().nextDouble()),
                new DataPoint(4, new Random().nextDouble()),
                new DataPoint(5, new Random().nextDouble()),
                new DataPoint(6, new Random().nextDouble()),
                new DataPoint(7, new Random().nextDouble()),
        };
        return dataPoints;
    }
}