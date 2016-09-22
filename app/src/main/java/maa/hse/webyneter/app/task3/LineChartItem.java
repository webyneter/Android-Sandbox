package maa.hse.webyneter.app.task3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

import maa.hse.webyneter.app.R;

public class LineChartItem extends ChartItem {
    public LineChartItem(ChartData<?> cd, Context c) {
        super(cd);
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(R.layout.fragment_task3_list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.task3_linechart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.chart.setDescriptionTypeface(typeface);
//        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
//        xAxis.setTypeface(typeface);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = holder.chart.getAxisLeft();
//        leftAxis.setTypeface(typeface);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinValue(0f);

        YAxis rightAxis = holder.chart.getAxisRight();
//        rightAxis.setTypeface(typeface);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMaxValue(0f);

        holder.chart.setData((LineData) mChartData);

        holder.chart.invalidate();
        holder.chart.animateX(750);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }
}
