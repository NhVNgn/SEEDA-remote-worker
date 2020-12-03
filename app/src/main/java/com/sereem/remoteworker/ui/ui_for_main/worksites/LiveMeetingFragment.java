package com.sereem.remoteworker.ui.ui_for_main.worksites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.FragmentLiveMeetingBinding;
import com.sereem.remoteworker.databinding.FragmentLocationViewBinding;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LiveMeetingFragment extends Fragment {

    DatabaseReference reference;



    Button createMeetingButton;
    Button linkButton;
    TextView linkContainerTextView;
    String urlGoogleMeet = null;
    WorkSite workSite;
    List<GoogleMeetLink> linkList;
    User user;
    private ColorPalette colorPalette;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_live_meeting, container, false);
        FragmentLiveMeetingBinding binding = FragmentLiveMeetingBinding.bind(root);
        colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.MEETINGS);
        binding.setColorPalette(colorPalette);
        createMeetingButton = root.findViewById(R.id.meetingButton);
        linkButton = root.findViewById(R.id.joinButton);
        linkContainerTextView = root.findViewById(R.id.linkContainerText);
        workSite = WorkSite.getChosenWorksite();
        reference = FirebaseDatabase.getInstance().getReference().child(
                "lives/" + workSite.getSiteID());
        user = User.getInstance();



        createMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://meet.google.com/new";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (urlGoogleMeet == null)
                    Toast.makeText(getContext(), "There is not meeting link now", Toast.LENGTH_SHORT).show();
                else{
                    String url = urlGoogleMeet;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        receiveGoogleMeetLink();

        return root;
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        if (sharedText != null) {
            String[] arr = sharedText.split("\n");
            linkContainerTextView.setText(arr[1].trim());
            urlGoogleMeet = arr[1].trim();
            linkContainerTextView.setMovementMethod(LinkMovementMethod.getInstance());
            GoogleMeetLink googleMeetLink = new GoogleMeetLink(user.getUID(), urlGoogleMeet, Calendar.getInstance().getTime().toString(), user.getFirstName());
            sendLink(googleMeetLink);


        }
    }

    private void sendLink(GoogleMeetLink googleMeetLink) {



        reference.push().setValue(googleMeetLink).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(getContext(), "Sent to other user", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(),
                        task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
                
            }
        });
    }

    private void receiveGoogleMeetLink(){
        linkList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linkList.clear();
                String host = "";
                for (DataSnapshot snapshotItem : snapshot.getChildren()){
                    GoogleMeetLink googleMeetLink = snapshotItem.getValue(GoogleMeetLink.class);
                    linkList.add(googleMeetLink);
                }

                if (!linkList.isEmpty())
                {
                    host =linkList.get(linkList.size()-1).getHost();
                    urlGoogleMeet = linkList.get(linkList.size()-1).getLink();
                    linkContainerTextView.setText(urlGoogleMeet);
                    Toast.makeText(getContext(), "new live meeting created: " + linkList.get(linkList.size()-1).getLink() + " by " + host, Toast.LENGTH_SHORT).show();
                }

                int counters = linkList.size()-1;
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    counters--;
                    if (counters == 0)
                        break;
                    appleSnapshot.getRef().removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }






    @Override
    public void onPause() {
        super.onPause();
        colorPalette.unregisterListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        colorPalette.registerListener();
        Intent intent = getActivity().getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }
    }
}