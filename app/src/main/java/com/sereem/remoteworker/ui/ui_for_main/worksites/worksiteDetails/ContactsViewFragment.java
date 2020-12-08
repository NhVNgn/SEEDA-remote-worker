package com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.siteAttendance.attendanceDatabase;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ui_for_main.worksites.SiteDetailActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ContactsViewFragment, used for displaying all users assigned to a chosen worksite.
 */
public class ContactsViewFragment extends Fragment {
    Context globalContext = null;
    SiteDatabase siteDB;
    attendanceDatabase attendanceDB;
    Database db;
    WorkSite userWorkSite = null;
    List<User> userList = new ArrayList<>();
    ListView listView;
    User me;
    private ProgressBar progressBar;
    private View root;




    public class userListAdapter extends ArrayAdapter<User>{
        private HashMap<String, User> userMap;
        public userListAdapter() {super(globalContext, R.layout.contact_parent_item, userList);}

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.contact_parent_item, parent, false);
            userMap = SiteDetailActivity.getUserList();
            // get user
            User user = userList.get(position);
            System.out.println(user.getFirstName());
            // user name
            TextView userTextName = itemView.findViewById(R.id.parentHeader);
            userTextName.setText(user.getFirstName() + " " + user.getLastName());
            ImageView profileIcon = itemView.findViewById(R.id.userAvatar);
            File iconFile = new File(getContext().getCacheDir() + "/" +
                    user.getUID() + ".jpeg");
            if(iconFile.exists()) {
                profileIcon.setImageURI(Uri.fromFile(iconFile));
            }
            // fill image
            return itemView;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_contacts_view, container, false);
        progressBar = root.findViewById(R.id.progressBarContacts);
        progressBar.setVisibility(View.VISIBLE);
        globalContext = root.getContext().getApplicationContext();
        listView = root.findViewById(R.id.contactListView);
        siteDB = new SiteDatabase(root.getContext());
        db = new Database(root.getContext());



        getUser();
        attendanceDB = new attendanceDatabase(root.getContext());
        getSite();
        if (userList.isEmpty()) {
            getConnectedUser();
        } else {
            populateListView();
            setUpListClick(root);
            progressBar.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    private void getSite() {
        userWorkSite = WorkSite.getChosenWorksite();
    }

    private void getConnectedUser(){
        userList = new ArrayList<>(SiteDetailActivity.getUserList().values());
        populateListView();
        setUpListClick(root);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void populateListView(){
        ArrayAdapter<User> adapter = new userListAdapter();
        listView.setAdapter(adapter);
    }

    private void getUser(){
        me = User.getInstance();
    }

    private void setUpListClick(View root){
        ListView list = root.findViewById(R.id.contactListView);
        list.setOnItemClickListener((adapterView, view, position, l) -> {
            FragmentManager manager = getFragmentManager();
            User user = userList.get(position);
            PopupFragment dialog = new PopupFragment();
            dialog.showDialog(user.getEmail(), user.getPhone(), manager);
        });
    }
}