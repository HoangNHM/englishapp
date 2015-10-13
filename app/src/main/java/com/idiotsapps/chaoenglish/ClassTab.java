package com.idiotsapps.chaoenglish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.idiotsapps.chaoenglish.custom.CustomClassListViewAdapter;
import com.idiotsapps.chaoenglish.item.ClassItem;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassTab extends Fragment implements CustomClassListViewAdapter.OnFriendsItemInteractionListener {

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
     * @return A new instance of fragment ClassTab.
     */
    // TODO: Rename and change types and number of parameters
    // Fragment required empty public constructor, nên muốn truyền cái gì vào phải dùng newInstance
    public static ClassTab newInstance(String param1, String param2) {
        ClassTab fragment = new ClassTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ClassTab() {
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
    public void onFriendsItemInteractionListener(int btnId, int position, int className) {
        switch (btnId) {
            case R.id.btnStudyClassItem:
                Toast.makeText(getContext(), "Study, item: " + position, Toast.LENGTH_SHORT).show();
                Intent intentQues = new Intent(getContext(), QuesActivity.class);
                startActivity(intentQues);
                break;
            case R.id.btnViewMoreClassItem:
                // call ViewMoreActivity
                Toast.makeText(getContext(), "View More, item: " + position, Toast.LENGTH_SHORT).show();
                Intent intentViewMore = new Intent(getContext(), ViewMoreActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ClassName", className);
                intentViewMore.putExtra("ClassPackage", bundle);
                startActivity(intentViewMore);
                break;
            default:
                break;
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Percent of Class, read from database
        ArrayList<Integer> arrClassPercent = new ArrayList<Integer>();
        arrClassPercent.add(55);
        arrClassPercent.add(80);
        arrClassPercent.add(64);
        arrClassPercent.add(36);
        // Database retrieve
//        DBHandler db = new DBHandler(getContext());
//        Cursor cursor = db.getData(DBHandler.CLASS_TABLE_NAME, 0);
//        if (cursor.moveToFirst()) {
//            do {
//                // get  the  data into array,or class variable
//                arrClassPercent.add(cursor.getInt(cursor.getColumnIndex(DBHandler.CLASS_COLUMN_PERCENT)));
//            } while (cursor.moveToNext());
//        }
//        db.close();

        // add list view
        ListView listView = (ListView) view.findViewById(R.id.listViewClass);
        // TODO ArrayList ClassItem
        ArrayList<ClassItem> arr = new ArrayList<ClassItem>();
        // unit + percent of that unit
        SparseIntArray unitPercent = new SparseIntArray();
        unitPercent.append(2, 100);
        unitPercent.append(3, 30);
        arr.add(new ClassItem(6, arrClassPercent.get(0), unitPercent));
        arr.add(new ClassItem(7, arrClassPercent.get(1), unitPercent));
        arr.add(new ClassItem(8, arrClassPercent.get(2), unitPercent));
        arr.add(new ClassItem(9, arrClassPercent.get(3), unitPercent));
        // adapter
        CustomClassListViewAdapter adapter = new CustomClassListViewAdapter(getActivity(), getFragmentManager(), ClassTab.this, arr);
        listView.setAdapter(adapter);
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
