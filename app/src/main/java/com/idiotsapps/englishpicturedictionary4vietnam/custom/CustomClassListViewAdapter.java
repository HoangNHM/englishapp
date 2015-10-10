package com.idiotsapps.englishpicturedictionary4vietnam.custom;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.idiotsapps.englishpicturedictionary4vietnam.ClassTab;
import com.idiotsapps.englishpicturedictionary4vietnam.R;
import com.idiotsapps.englishpicturedictionary4vietnam.item.ClassItem;

import java.util.ArrayList;


/**
 * Created by vantuegia on 10/1/2015.
 */
public class CustomClassListViewAdapter extends ArrayAdapter<ClassItem> {

    private OnFriendsItemInteractionListener itemInteractionListener;
    private ArrayList<ClassItem> mArrClassItems;
    private Activity mActivity;
    private FragmentManager mFragmentManager;
    private String[] mParties = new String[] {
            "Party A", "Party B"
    };
    public CustomClassListViewAdapter(Activity activity, FragmentManager fragmentManager, ClassTab classTab, ArrayList<ClassItem> arrClassItems) {
        super(activity, R.layout.class_list_item_layout, arrClassItems);
        this.mActivity = activity;
        this.mArrClassItems = arrClassItems;
        this.mFragmentManager = fragmentManager;
        this.itemInteractionListener = (OnFriendsItemInteractionListener) classTab;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            // first time creation
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            view = inflater.inflate(R.layout.class_list_item_layout, null);
        } // else next time will recover

        // chart
        PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);
        // set percent
        setPieChart(pieChart, mArrClassItems.get(position).getClassPercent());

        // class 1, class 2, ...
        TextView mTextViewClassNumber = (TextView) view.findViewById(R.id.textViewClassNumber);
        mTextViewClassNumber.setText(mArrClassItems.get(position).getClassName());

        // btn Study
        ImageButton mBtnStudyClassItem = (ImageButton) view.findViewById(R.id.btnStudyClassItem);
        mBtnStudyClassItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInteractionListener.onFriendsItemInteractionListener(v.getId(), position, mArrClassItems.get(position).getClassName());
            }
        });

        // btn View More
        ImageButton mBtnViewMoreClassItem = (ImageButton) view.findViewById(R.id.btnViewMoreClassItem);
        mBtnViewMoreClassItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInteractionListener.onFriendsItemInteractionListener(v.getId(), position, mArrClassItems.get(position).getClassName());
                // Don't use dlg, use activity instead
                // Show Bar chart
//                DialogFragment horBarChartDlg = BarChartDlg.newInstance(mArrClassItems.get(position).getYVals(), mArrClassItems.get(position).getClassName());
//                horBarChartDlg.show(mFragmentManager, "BarChartDlg");
//                DialogFragment horBarChartDlg = HorizontalBarChartDlg.newInstance(mArrClassItems.get(position).getYVals(), mArrClassItems.get(position).getClassName());
//                horBarChartDlg.show(mFragmentManager, "HorBarChartDlg");

            }
        });

        return view;
    }

    private void setPieChart(PieChart pieChart, int percent) {
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");
        // hide legend
        pieChart.getLegend().setEnabled(false);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

//        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
//
//        pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);
        pieChart.setCenterText(percent + "%");

        pieChart.setRotationAngle(-90);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);

        // pieChart.setUnit(" €");
        // pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        // pieChart.setOnChartValueSelectedListener(this);

        setData(pieChart, percent);

        pieChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // pieChart.spin(2000, 0, 360);
    }

    private void setData(PieChart pieChart, int pecent) {

        int count = 2; // number of Parties
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        yVals1.add(new Entry((float) pecent, 0));
        yVals1.add(new Entry((float) 100 - pecent, 1));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);
//        xVals.addAll(Arrays.asList(mParties).subList(0, count + 1));

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        final int[] VORDIPLOM_COLORS = new int[]{Color.rgb(255, 100, 140), Color.rgb(100, 247, 140)};
        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        dataSet.setColors(colors);
        // parties percent text
        dataSet.setDrawValues(false);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
//        data.setValueTypeface(tf);
        pieChart.setData(data);
        // Parties name text
        pieChart.setDrawSliceText(false);

        // undo all highlights
        pieChart.highlightValues(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFriendsItemInteractionListener {
        // TODO: Update argument type and name
        public void onFriendsItemInteractionListener(int btnId, int position, String className);
    }
}
