package com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ContactParentItemBinding;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.siteAttendance.Attendance;
import com.sereem.remoteworker.model.siteAttendance.attendanceDatabase;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;

import java.util.ArrayList;
import java.util.List;


public class ContactsViewFragment extends Fragment {
    Context globalContext = null;
    private FragmentActivity myContext;
    SiteDatabase siteDB;
    attendanceDatabase attendanceDB;
    Database db;
    WorkSite userWorkSite = null;
    List<Attendance> attendances;
    List<User> userList = new ArrayList<>();
    ListView listView;
    User me;
    private ColorPalette colorPalette;
    private ProgressBar progressBar;
    private View root;




    public class userListAdapter extends ArrayAdapter<User>{
        public userListAdapter() {super(globalContext, R.layout.contact_parent_item, userList);}

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.contact_parent_item, parent, false);
            ContactParentItemBinding binding = ContactParentItemBinding.bind(itemView);
            colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.CONTACT);
            binding.setColorPalette(colorPalette);

            // get user
            User user = userList.get(position);
            System.out.println(user.getFirstName());
            // user name
            TextView userTextName = itemView.findViewById(R.id.parentHeader);
            userTextName.setText(user.getFirstName() + user.getLastName());
            colorPalette.registerListener();

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
//        SharedPreferences prefs = getActivity().getSharedPreferences(
//                "user", Context.MODE_PRIVATE);
//
//        String id = prefs.getString("last_accessed_site_id", "NONE");
        userWorkSite = WorkSite.getChosenWorksite();
    }

    private void getConnectedUser(){
//        attendances = attendanceDB.getAllAttendanceList();
//        for (Attendance a : attendances)
//            if (a.getSiteID().equals(userWorkSite.getSiteID())){
//                User user = db.getUserByEmail(a.getWorkerEmail());
//                System.out.println(user.getEmail() + " " + me.getEmail());
//                if (!user.getEmail().equals(me.getEmail()))
//                    {
//                        userList.add(user);
//                    }
//            }

        DocumentReference documentReference;
        if(userWorkSite.getWorkers() == null) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        final int size = userWorkSite.getWorkers().size();
        for(int i = 0; i < userWorkSite.getWorkers().size(); i++) {
            final int index = i;
            documentReference = FirebaseFirestore.getInstance().document("/users/" +
                    userWorkSite.getWorkers().get(i));
            documentReference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    User user = task.getResult().toObject(User.class);
                    if(user != null && !user.getUID().equals(me.getUID())) {
                        userList.add(user);
                    }
//                    if(index == size - 1) {
                        populateListView();
                        setUpListClick(root);
                        progressBar.setVisibility(View.INVISIBLE);
//                    }
                } else {
                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
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
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FragmentManager manager = getFragmentManager();
                User user = userList.get(position);
                PopupFragment dialog = new PopupFragment();
                dialog.showDialog(user.getEmail(), user.getPhone(), manager);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if(colorPalette != null) {
            colorPalette.unregisterListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(colorPalette != null) {
            colorPalette.registerListener();
        }
    }

}