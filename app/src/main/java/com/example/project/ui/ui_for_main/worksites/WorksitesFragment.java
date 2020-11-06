package com.example.project.ui.ui_for_main.worksites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.model.Database;
import com.example.project.model.User;
import com.example.project.model.siteAttendance.Attendance;
import com.example.project.model.siteAttendance.attendanceDatabase;
import com.example.project.model.workSite.SiteDatabase;
import com.example.project.model.workSite.WorkSite;

import java.util.ArrayList;
import java.util.List;

public class WorksitesFragment extends Fragment {

    Context globalContext = null;
    SiteDatabase siteDB;
    attendanceDatabase attendanceDB;
    User user;
    Database db;
    List<WorkSite> userSites = new ArrayList<>();
    ListView listView;


    public class siteListAdapter extends ArrayAdapter<WorkSite> {
        public siteListAdapter() {
            super(globalContext, R.layout.item_view, userSites);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            // get site
            WorkSite ws = userSites.get(position);

            // site name
            TextView textSiteName = itemView.findViewById(R.id.textSiteName);
            TextView textSiteId = itemView.findViewById(R.id.textSiteID);
            textSiteName.setText(ws.getName());
            textSiteId.setText("Site ID: " + ws.getSiteId());

            // fill image
            ImageView imageView = itemView.findViewById(R.id.listView_image);
            imageView.setImageResource(R.drawable.ic_worksite_logo);

            return itemView;

        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_worksites, container, false);
        globalContext = root.getContext().getApplicationContext();
        listView = root.findViewById(R.id.listViewWorkSite);
        siteDB = new SiteDatabase(root.getContext());
        attendanceDB = new attendanceDatabase(root.getContext());
        db = new Database(root.getContext());
        getUser();
        getWorkSiteForUser();
        System.out.println("size = " + userSites.size());
        for (WorkSite ws : userSites)
            System.out.println("WorkSite name = " + ws.getName());
        populateListView();

        return root;
    }

    private void populateListView() {
        ArrayAdapter<WorkSite> adapter = new siteListAdapter();
        listView.setAdapter(adapter);
    }

    private void getUser() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "user", Context.MODE_PRIVATE);

        String email = prefs.getString("email", "NONE");
        String password = prefs.getString("password", "NONE");
        user = db.getUser(email, password);
    }

    private void getWorkSiteForUser() {

        if (userSites.size() > 0) // for the special case when user presses back instead of navigation panel
            return;

        for (Attendance a : attendanceDB.getAllAttendanceList())
            if (a.getWorkerEmail().equals(user.getEmail()))
                for (WorkSite ws : siteDB.getAllWorkSite())
                    if (a.getSiteID().equals(ws.getSiteId())) {
                        System.out.println(ws.getName());
                        userSites.add(ws);
                    }

    }

}