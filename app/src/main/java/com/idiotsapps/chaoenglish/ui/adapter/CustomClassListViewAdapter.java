package com.idiotsapps.chaoenglish.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.idiotsapps.chaoenglish.ui.fragment.ClassTabFragment;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.item.ClassItem;

import java.util.ArrayList;


/**
 * Created by vantuegia on 10/1/2015.
 */
public class CustomClassListViewAdapter extends ArrayAdapter<ClassItem> {

    private static int[] mClassName =
            {R.drawable.class_1,
                    R.drawable.class_2,
                    R.drawable.class_3,
                    R.drawable.class_4,
                    R.drawable.class_5,
                    R.drawable.class_6,
                    R.drawable.class_7,
                    R.drawable.class_8,
                    R.drawable.class_9,
                    R.drawable.class_10,
                    R.drawable.class_11,
                    R.drawable.class_12};

    private OnClassItemInteractionListener itemInteractionListener;
    private ArrayList<ClassItem> mArrClassItems;
    private Activity mActivity;
    private FragmentManager mFragmentManager;
    private String[] mParties = new String[] {
            "Party A", "Party B"
    };
    public CustomClassListViewAdapter(Activity activity, FragmentManager fragmentManager, ClassTabFragment classTab, ArrayList<ClassItem> arrClassItems) {
        super(activity, R.layout.item_class_list, arrClassItems);
        this.mActivity = activity;
        this.mArrClassItems = arrClassItems;
        this.mFragmentManager = fragmentManager;
        this.itemInteractionListener = (OnClassItemInteractionListener) classTab;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            // first time creation
            LayoutInflater inflater = this.mActivity.getLayoutInflater();
            view = inflater.inflate(R.layout.item_class_list, null);
        } // else next time will recover

        // chart
        PieChart pieChart = (PieChart) view.findViewById(R.id.pieChart);
        // set percent
        setPieChart(pieChart, mArrClassItems.get(position).getClassPercent());

        // class 1, class 2, ...
        ImageView imgClassName = (ImageView) view.findViewById(R.id.imgClassName);
        imgClassName.setImageResource(mClassName[mArrClassItems.get(position).getClassName()]);
//        TextView mTextViewClassNumber = (TextView) view.findViewById(R.id.textViewClassNumber);
//        mTextViewClassNumber.setText(mArrClassItems.get(position).getClassName());

        // btn Study
        ImageView mBtnStudyClassItem = (ImageView) view.findViewById(R.id.btnStudyClassItem);
        mBtnStudyClassItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInteractionListener.onClassItemInteractionListener(v.getId(), position, mArrClassItems.get(position).getClassName());
            }
        });

        // btn View More
        ImageView mBtnViewMoreClassItem = (ImageView) view.findViewById(R.id.btnViewMoreClassItem);
        mBtnViewMoreClassItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInteractionListener.onClassItemInteractionListener(v.getId(), position, mArrClassItems.get(position).getClassName());
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
        final int[] VORDIPLOM_COLORS = new int[]{Color.rgb(255, 90, 0), Color.rgb(151, 200, 101)};
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
    public interface OnClassItemInteractionListener {
        // TODO: Update argument type and name
        public void onClassItemInteractionListener(int btnId, int position, int className);
    }
}
