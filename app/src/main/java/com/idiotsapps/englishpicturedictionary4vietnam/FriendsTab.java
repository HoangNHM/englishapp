package com.idiotsapps.englishpicturedictionary4vietnam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.idiotsapps.englishpicturedictionary4vietnam.custom.CustomFriendsListViewAdapter;
import com.idiotsapps.englishpicturedictionary4vietnam.item.FriendsItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsTab extends Fragment {
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
     * @return A new instance of fragment FriendsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsTab newInstance(String param1, String param2) {
        FriendsTab fragment = new FriendsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendsTab() {
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
        View view = inflater.inflate(R.layout.fragment_friends_tab, container, false);
        //TODO code in here or onViewCreated is nearly the same, still not figure out which is better
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // add list view
        ListView listView = (ListView) view.findViewById(R.id.listViewFriends);
        // ArrayList ClassItem
        ArrayList<FriendsItem> arr = new ArrayList<FriendsItem>();
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.image);
        arr.add(new FriendsItem(bitmap, "trung huynh", "69"));
        arr.add(new FriendsItem(bitmap, "trung huynh", "69"));
        arr.add(new FriendsItem(bitmap, "trung huynh", "69"));
        arr.add(new FriendsItem(bitmap, "trung huynh", "69"));
        // adapter
        CustomFriendsListViewAdapter adapter = new CustomFriendsListViewAdapter(getActivity(), arr);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mOnItemClickListener);

        Button btnRateUs = (Button) view.findViewById(R.id.btnRateUs);
        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Rate us", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnInviteFriend = (Button) view.findViewById(R.id.btnInviteFriend);
        btnInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Invite Friend", Toast.LENGTH_SHORT).show();
            }
        });
    }

    AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(), "Friends list item: " + position, Toast.LENGTH_SHORT).show();
        }
    };

}
