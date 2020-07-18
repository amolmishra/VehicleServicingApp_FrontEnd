package com.example.smartbits.vehicleservicingapp.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.smartbits.vehicleservicingapp.ExpandableListAdapter;
import com.example.smartbits.vehicleservicingapp.R;
import com.example.smartbits.vehicleservicingapp.loginandregistration.SQLiteHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceHistory extends Fragment {

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private SQLiteHandler db;
    private ProgressDialog pDialog;

    public ServiceHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_history, container, false);

        db = new SQLiteHandler(getContext());
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // get the listview
        ExpandableListView expListView = v.findViewById(R.id.lvExp);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Getting History...");
        pDialog.show();
        // preparing list data
        getHistory();
        // create a history table while logging in itself. and add to sqlite then get from sqlite at this point
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                String clickedOn = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                if(clickedOn.contains("Click here to navigate")) {
                    List<String> values = listDataChild.get(listDataHeader.get(groupPosition));
                    String centerID = values.get(3);
                    if(centerID != null) {
                        centerID = centerID.split(": ")[1];
                        Map<String, String> centerDetails = db.getCenterById(Integer.parseInt(centerID));
                        String latitude = centerDetails.get("lat");
                        String longitude = centerDetails.get("lon");
                        Uri navigationIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }
                return false;
            }
        });

        // Inflate the layout for this fragment
        if(pDialog.isShowing()) {
            pDialog.hide();
        }
        return v;
    }

    public void getHistory() {
        Map<String, List<String>> histories = db.getHistory();
        Iterator it = histories.keySet().iterator();
        while (it.hasNext()) {
            String carid = (String) it.next();
            listDataHeader.add(carid);
            List<String> value = histories.get(carid);
            value.add("Click here to navigate to the Service Center.");
            listDataChild.put(carid, value);
        }
    }

}
