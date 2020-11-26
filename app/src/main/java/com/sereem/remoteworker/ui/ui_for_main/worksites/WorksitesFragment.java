package com.sereem.remoteworker.ui.ui_for_main.worksites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ItemViewBinding;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.siteAttendance.attendanceDatabase;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;
import com.sereem.remoteworker.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WorksitesFragment extends Fragment {

    Context globalContext = null;
    SiteDatabase siteDB;
    attendanceDatabase attendanceDB;
    User user;
    Database db;
    List<WorkSite> userSites = new ArrayList<>();
    ListView listView;
    private View itemView;
    View root;

    public static final String PROJECT_ID = "project_id";

    private ItemViewBinding binding;
    private ColorPalette colorPalette;
    private ProgressBar progressBar;

    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public class siteListAdapter extends ArrayAdapter<WorkSite> {
        public siteListAdapter() {
            super(globalContext, R.layout.item_view, userSites);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            itemView = convertView;
            if (itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            // get site
            binding = ItemViewBinding.bind(itemView);
            colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.ITEM_VIEW);
            binding.setColorPalette(colorPalette);
            binding.setLifecycleOwner(getViewLifecycleOwner());

            colorPalette.registerListener();

            WorkSite ws = userSites.get(position);

            // site name
            TextView textSiteName = itemView.findViewById(R.id.textSiteName);
            TextView textSiteId = itemView.findViewById(R.id.textSiteID);
            textSiteName.setText(ws.getName());
            textSiteId.setText("Site ID: " + ws.getSiteID());

            // fill image

            return itemView;

        }


    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_worksites, container, false);

        progressBar = root.findViewById(R.id.progressBarWorksites);
        progressBar.setVisibility(View.VISIBLE);
        globalContext = root.getContext().getApplicationContext();
        listView = root.findViewById(R.id.listViewWorkSite);
        siteDB = new SiteDatabase(root.getContext());
        attendanceDB = new attendanceDatabase(root.getContext());
        db = new Database(root.getContext());
//        user = User.getInstance();
//        getWorkSiteForUser();
        if(User.isNull()) {
            initializeDocumentReference();
        } else {
            user = User.getInstance();
            getWorkSiteForUser();
        }
        System.out.println("size = " + userSites.size());
        for (WorkSite ws : userSites)
            System.out.println("WorkSite name = " + ws.getName());

        return root;
    }

    private void populateListView() {
        ArrayAdapter<WorkSite> adapter = new siteListAdapter();
        listView.setAdapter(adapter);
    }

//    private void getUser() {
//        SharedPreferences prefs = getActivity().getSharedPreferences(
//                "user", Context.MODE_PRIVATE);
//
//        String email = prefs.getString("email", "NONE");
//        String password = prefs.getString("password", "NONE");
//        user = db.getUser(email, password);
//    }

    public void getWorkSiteForUser() {

        if (userSites.size() > 0) // for the special case when user presses back instead of navigation panel
            return;
        
//        for (Attendance a : attendanceDB.getAllAttendanceList()) {
//            if (a.getWorkerEmail().equals(user.getEmail())) {
//                for (WorkSite ws : siteDB.getAllWorkSite()) {
//                    if (a.getSiteID().equals(ws.getSiteId())) {
//                        System.out.println(ws.getName());
//                        userSites.add(ws);
//                    }
//                }
//            }
//        }
        if(user.getWorksites() == null) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        DocumentReference documentReference;
        for(int i = 0; i < user.getWorksites().size(); i++) {
            final int position = i;
            documentReference = fStore.document("/worksites/" + user.getWorksites().get(i));
            documentReference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    userSites.add(task.getResult().toObject(WorkSite.class));
                    if(position == user.getWorksites().size() - 1) {
                        populateListView();
                        setupListClick(root);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), task.getException().getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
//        user = User.getInstance();
//        Toast.makeText(getContext(), user.getFirstName(), Toast.LENGTH_LONG).show();
    }
    private void setupListClick(View root){
        ListView list = root.findViewById(R.id.listViewWorkSite);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(globalContext, SiteDetailActivity.class);
                String site_id = userSites.get(position).getSiteID();
                intent.putExtra(PROJECT_ID, site_id);
                WorkSite.setChosenWorksite(userSites.get(position));
                saveInSharedPrefs(site_id, root);
                startActivity(intent);
            }
        });
    }

    private void saveInSharedPrefs(String site_id, View root) {
        SharedPreferences prefs = root.getContext().getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit().putString("last_accessed_site_id", site_id).apply();
    }

    private void initializeDocumentReference() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String UID = prefs.getString("UID", "");
        DocumentReference documentReference = FirebaseFirestore.getInstance().document(
                "/users/" + UID + "/");

        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                user = User.createNewInstance(task.getResult().toObject(User.class));
                getWorkSiteForUser();
            } else {
                Toast.makeText(getContext(), task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
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