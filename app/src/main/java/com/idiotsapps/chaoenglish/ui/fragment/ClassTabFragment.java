package com.idiotsapps.chaoenglish.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.idiotsapps.chaoenglish.Grade;
import com.idiotsapps.chaoenglish.R;
import com.idiotsapps.chaoenglish.Unit;
import com.idiotsapps.chaoenglish.helper.HelperApplication;
import com.idiotsapps.chaoenglish.ui.activity.ViewMoreActivity;
import com.idiotsapps.chaoenglish.ui.adapter.CustomClassListViewAdapter;
import com.idiotsapps.chaoenglish.item.ClassItem;
import com.idiotsapps.chaoenglish.ui.activity.QuesActivity;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassTabFragment extends Fragment implements CustomClassListViewAdapter.OnClassItemInteractionListener {

    private ArrayList<Grade> mGrades;
    ListView mListViewGrade;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    // Fragment required empty public constructor, nên muốn truyền cái gì vào phải dùng newInstance
    public static ClassTabFragment newInstance(String param1, String param2) {
        ClassTabFragment fragment = new ClassTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ClassTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_tab, container, false);
        //TODO code in here or onViewCreated is nearly the same, still not figure out which is better
        return view;
    }

    // btn Study & View More click listener
    @Override
    public void onClassItemInteractionListener(int btnId, int position, int className) {
        switch (btnId) {
            case R.id.btnStudyClassItem:
                Toast.makeText(getContext(), "Study, item: " + position, Toast.LENGTH_SHORT).show();
                Intent intentQues = new Intent(getContext(), QuesActivity.class);
//                intentQues.putParcelableArrayListExtra("key_grades", mGrades);
                intentQues.putExtra("position",position);
                startActivity(intentQues);
                break;
            case R.id.btnViewMoreClassItem:
                // call ViewMoreActivity
                Toast.makeText(getContext(), "View More, item: " + position, Toast.LENGTH_SHORT).show();
                Intent intentViewMore = new Intent(getContext(), ViewMoreActivity.class);
//                ArrayList<Unit> units = mGrades.get(position).getUnits(); // pass only list of units of clicked_class to ViewMoreActivity
//                intentViewMore.putParcelableArrayListExtra("key_units", units);
                Bundle args = new Bundle();
                args.putInt("ClassName", className);
                intentViewMore.putExtra("ClassPackage", args);
                intentViewMore.putExtra("position", position);

                startActivity(intentViewMore);
                break;
            default:
                break;
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // add list view
        mListViewGrade = (ListView) view.findViewById(R.id.listViewClass);
        mListViewGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), "Study, item: " + position, Toast.LENGTH_SHORT).show();
                Intent intentQues = new Intent(getContext(), QuesActivity.class);
//                intentQues.putParcelableArrayListExtra("key_grades", mGrades);
                intentQues.putExtra("position",position);
                startActivity(intentQues);
            }
        });
//        ArrayList<ClassItem> arr = new ArrayList<ClassItem>();// Percent of Classes, read from database
//        mGrades = HelperApplication.sMySQLiteHelper.getClasses();
//        for (int i = 0; i < mGrades.size(); i++) {
//            int classId = mGrades.get(i).getGrade();
//            int percent = (int) mGrades.get(i).getPercent();
//            arr.add(new ClassItem(classId, percent, null));
//        }
//        // adapter
//        CustomClassListViewAdapter adapter = new CustomClassListViewAdapter(getActivity(), getFragmentManager(), ClassTabFragment.this, arr);
//        mListViewGrade.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // add list view
        ArrayList<ClassItem> arr = new ArrayList<ClassItem>();// Percent of Classes, read from database
        mGrades = HelperApplication.sMySQLiteHelper.getClasses();
        for (int i = 0; i < mGrades.size(); i++) {
            int classId = mGrades.get(i).getGrade();
            int percent = (int) mGrades.get(i).getPercent();
            arr.add(new ClassItem(classId, percent, null));
        }
        // adapter
        CustomClassListViewAdapter adapter = new CustomClassListViewAdapter(getActivity(), getFragmentManager(), ClassTabFragment.this, arr);
        mListViewGrade.setAdapter(adapter);
    }
//    dùng để callback to parent
//    private OnFragmentInteractionListener mListener;
//      // TODO: Rename method, update argument and hook method into UI event
//        public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
