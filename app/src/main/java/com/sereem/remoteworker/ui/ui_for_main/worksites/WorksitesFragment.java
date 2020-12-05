package com.sereem.remoteworker.ui.ui_for_main.worksites;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.sereem.remoteworker.ui.ui_for_main.worksites.LiveMeetingFragment.linkList;
import static com.sereem.remoteworker.ui.ui_for_main.worksites.LiveMeetingFragment.urlGoogleMeet;

public class WorksitesFragment extends Fragment {

    Context globalContext = null;
    SiteDatabase siteDB;
    attendanceDatabase attendanceDB;
    User user;
    Database db;
    List<WorkSite> userSites = new ArrayList<>();
    ListView listView = null;
    private View itemView;
    View root;

    public static final String PROJECT_ID = "project_id";

    private ItemViewBinding binding;
    private ColorPalette colorPalette;
    private ProgressBar progressBar;
    ArrayAdapter<WorkSite> adapter = null;

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

        return root;
    }

    private void populateListView() {
        adapter = new siteListAdapter();
        listView.setAdapter(adapter);
        receiveGoogleMeetLink();

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

        if (userSites.size() > 0) {
            populateListView();
            setupListClick(root);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        
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
        System.out.println("getWorkSiteForUser is called");
        DocumentReference documentReference;
        for(int i = 0; i < user.getWorksites().size(); i++) {
            final int position = i;
            documentReference = fStore.document("/worksites/" + user.getWorksites().get(i));
            documentReference.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    userSites.add(task.getResult().toObject(WorkSite.class));
                    System.out.println("userSites is updated" + userSites.get(0) + "  " + user.getEmail());
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
        System.out.println("current user UID is" + UID);
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



    private void receiveGoogleMeetLink(){
        linkList = new ArrayList<>();
        List<DatabaseReference> databaseReferenceList = new ArrayList<>();
        // each databse will refer to a worksite


        for (WorkSite ws : userSites){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(
                    "lives/" + ws.getSiteID());
            databaseReferenceList.add(reference);
            System.out.println("add reference:" + ws.getName());
        }

        // for each database, add a listener
        for (DatabaseReference reference : databaseReferenceList){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    linkList.clear();
                    System.out.println("OnDataChange is called");
                    String host = "";
                    for (DataSnapshot snapshotItem : snapshot.getChildren()){
                        GoogleMeetLink googleMeetLink = snapshotItem.getValue(GoogleMeetLink.class);
                        linkList.add(googleMeetLink);
                    }

                    if (!linkList.isEmpty())
                    {
                        GoogleMeetLink lastLink = linkList.get(linkList.size()-1);
                        host = lastLink.getHost();

                        urlGoogleMeet = lastLink.getLink();
                        if (urlGoogleMeet.equals("Meeting has ended")) {
                            if (getActivity() != null)
                                Toast.makeText(getContext(), "WorksitesFragment: No meeting available ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (!lastLink.getHost().equals(user.getFirstName())) {
                                showNotification(host, lastLink);
                            }

                        }
                    }

                    int counters = linkList.size()-1;
                    for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                        counters--;
                        if (counters == 0)
                            break;
                        appleSnapshot.getRef().removeValue();
                    }

                }

                private void showNotification(String host, GoogleMeetLink lastLink) {
                    if(getActivity() != null){
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle("Notification")
                                .setMessage(host + " hosted a meeting right now!")
                                .setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (urlGoogleMeet == null || urlGoogleMeet.equals("No meeting available"))
                                            Toast.makeText(getContext(), "Meeting has just ended", Toast.LENGTH_SHORT).show();
                                        else{
                                            String url = urlGoogleMeet;
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            if (!url.contains("Meeting has ended"))
                                                startActivity(i);
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .create();
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            private static final int AUTO_DISMISS_MILLIS = 6000;
                            @Override
                            public void onShow(final DialogInterface dialog) {
                                final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                                final CharSequence negativeButtonText = defaultButton.getText();
                                new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        defaultButton.setText(String.format(
                                                Locale.getDefault(), "%s (%d)",
                                                negativeButtonText,
                                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                        ));
                                    }
                                    @Override
                                    public void onFinish() {
                                        if (((AlertDialog) dialog).isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                }.start();
                            }
                        });
                        if(!getActivity().isFinishing())
                        {
                            dialog.show();
                        }

                    }

                }







                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

}