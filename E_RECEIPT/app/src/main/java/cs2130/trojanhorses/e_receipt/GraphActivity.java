package cs2130.trojanhorses.e_receipt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphActivity extends AppCompatActivity implements Callbackable {

    private ArrayList<BarEntry> mEntries;
    private ArrayList<String> mLabels;
    private BarChart mBarChart;
    private BarDataSet mDataSet;
    private BarData mData;
    private ReceiptLab mReceiptLab;
    private ArrayList<Double> prices;
    private List<Receipt> receipts;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        mBarChart = (BarChart) findViewById(R.id.bar_chart);

        mReceiptLab = ReceiptLab.get(GraphActivity.this, this, false);

        receipts = mReceiptLab.getReceipts();
        prices = new ArrayList<>();

        for(int i=0; i<receipts.size(); i++){
            Log.d("CHECK", "Date: "+receipts.get(i).getDate()+ "Price: "+receipts.get(i).getTotal());
        }

        mLabels = new ArrayList<>();
        mLabels.add("Nov");
        mLabels.add("Oct");
        mLabels.add("Sept");
        mLabels.add("Aug");
        mLabels.add("Jul");

        mEntries = new ArrayList<>();
        mEntries.add(new BarEntry(1f,getTotalByMonth(receipts, barEntryDates(0)), mLabels.get(0)));
        mEntries.add(new BarEntry(2f,getTotalByMonth(receipts, barEntryDates(1)), mLabels.get(1)));
        mEntries.add(new BarEntry(3f,getTotalByMonth(receipts, barEntryDates(2)), mLabels.get(2)));
        mEntries.add(new BarEntry(4f,getTotalByMonth(receipts, barEntryDates(3)), mLabels.get(3)));
        mEntries.add(new BarEntry(5f,getTotalByMonth(receipts, barEntryDates(4)), mLabels.get(4)));

        mDataSet = new BarDataSet(mEntries, "Month");
        mDataSet.setValueTextSize(16);
        mDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        mDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        mDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return entry.getData().toString()+ "(" + entry.getY() + ")";
            }
        } );

        mData = new BarData(mDataSet);

        mBarChart.setData(mData);
        mBarChart.invalidate();
        mBarChart.animateY(3000);

        /** Set labels on */
        //mBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mLabels));

        /** Set a LimitLine */
        LimitLine line = new LimitLine(600f, "$ LimitPerMonth");
        line.setLineColor(Color.RED);
        line.setLineWidth(4f);
        line.setTextColor(Color.BLACK);
        line.setTextSize(14);
        YAxis yAxis = mBarChart.getAxisLeft();
        yAxis.addLimitLine(line);

        /** Set a Description */
        Description description = new Description();
        description.setText("My Budget");
        description.setTextSize(13);
        mBarChart.setDescription(description);
    }

    @Override
    public void update() {
        System.out.print("Hello Graph!");
    }

    public int getTotalByMonth(List<Receipt> list, String date){
        int count = 0;
        double value = 0;

        while ( count < list.size()) {
            if (list.get(count).getDate().substring(4,6).equals(date.substring(4,6))) {
                prices.add(Double.parseDouble(list.get(count).getTotal().substring(1)));
                value += Double.parseDouble(list.get(count).getTotal().substring(1));
            }

            count ++;
        }
        return (int)value;
    }

    public String barEntryDates (int i) {
        String date;
        mCalendar = Calendar.getInstance();

        if ((mCalendar.get(Calendar.MONTH)+1-i) < 10)
            date = ""+mCalendar.get(Calendar.YEAR) + "0" +(mCalendar.get(Calendar.MONTH)+1-i);
        else
            date = ""+mCalendar.get(Calendar.YEAR) + (mCalendar.get(Calendar.MONTH)+1-i);
        return date+"00";
    }

}
