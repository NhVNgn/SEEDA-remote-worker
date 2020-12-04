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

import java.nio.channels.CancelledKeyException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LiveMeetingFragment extends Fragment {

    DatabaseReference reference;



    Button createMeetingButton;
    Button linkButton;
    Button endButton;
    private boolean userStartAMeeting = false;
    TextView linkContainerTextView;
    TextView hostTextView;
    public static String urlGoogleMeet = null;
    WorkSite workSite;
    public static List<GoogleMeetLink> linkList;
    boolean linkIsSent = false;
    User user;
    public static String hostName = null;
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
        endButton = root.findViewById(R.id.endMeeting);
        hostTextView = root.findViewById(R.id.HostingInfoTextView);
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
                if (urlGoogleMeet == null || urlGoogleMeet.equals("No meeting available"))
                    Toast.makeText(getContext(), "There is not meeting link now", Toast.LENGTH_SHORT).show();
                else{
                    String url = urlGoogleMeet;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });

        endButton.setVisibility(View.INVISIBLE);
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
            userStartAMeeting = true;
            if (!linkIsSent)
                sendLink(googleMeetLink);


        }
    }

    private void sendLink(GoogleMeetLink googleMeetLink) {


        linkIsSent = true;
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
                    System.out.println("LinkList is not empty");
                    GoogleMeetLink lastLink = linkList.get(linkList.size()-1);
                    urlGoogleMeet = lastLink.getLink();
                    hostName = lastLink.getHost();
                    System.out.println(hostName);
                    hostTextView.setText(hostName);
                    linkContainerTextView.setText(urlGoogleMeet);
                    System.out.println("HOST NAME IS: " + hostName);

                    if (hostName != null && !urlGoogleMeet.equals("Meeting has ended")){
                        System.out.println("HOST NAME IS NOT NULL");
                        if (hostName.equals(user.getFirstName())){
                            endButton.setVisibility(View.VISIBLE);
                            endButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    GoogleMeetLink googleMeetLink = new GoogleMeetLink(user.getUID(), "Meeting has ended", Calendar.getInstance().getTime().toString(), user.getFirstName());

                                    reference.push().setValue(googleMeetLink).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            System.out.println("Stop previous meeting successful");
                                            linkIsSent = false;
                                            endButton.setVisibility(View.INVISIBLE);
                                        } else {
                                            System.out.println("Fail to stop previous meeting");

                                        }
                                    });
                                }
                            });
                        }
                    }
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

    @Override
    public void onDestroy() {
        System.out.println("OnDestroy is called LiveMeetingFragment");
        super.onDestroy();

        if (userStartAMeeting) {
            GoogleMeetLink googleMeetLink = new GoogleMeetLink(user.getUID(), "Meeting has ended", Calendar.getInstance().getTime().toString(), user.getFirstName());

            reference.push().setValue(googleMeetLink).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Stop previous meeting successful");
                    linkIsSent = false;
                } else {
                    System.out.println("Fail to stop previous meeting");

                }
            });
        }

    }
}