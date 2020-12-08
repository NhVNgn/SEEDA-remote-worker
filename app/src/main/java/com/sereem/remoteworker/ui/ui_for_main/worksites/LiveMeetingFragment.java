package com.sereem.remoteworker.ui.ui_for_main.worksites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.FragmentLiveMeetingBinding;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;
import com.sereem.remoteworker.ui.CustomSnackbar;
import com.sereem.remoteworker.ui.ErrorDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * LiveMeetingFragment class, used for creating new google meeting and showing current meeting.
 */
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

        createMeetingButton.setOnClickListener(view -> {
            String url = "http://meet.google.com/new";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        linkButton.setOnClickListener(view -> {
            if (urlGoogleMeet == null || urlGoogleMeet.equals("No meeting available")
                    || urlGoogleMeet.contains("Meeting has ended"))
                CustomSnackbar.create(getView()).setText("There is not meeting link now").show();
            else{
                String url = urlGoogleMeet;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
            GoogleMeetLink googleMeetLink = new GoogleMeetLink(user.getUID(), urlGoogleMeet,
                    Calendar.getInstance().getTime().toString(),
                    user.getFirstName(), WorkSite.getChosenWorksite().getSiteID());
            userStartAMeeting = true;
            if (!linkIsSent)
                sendLink(googleMeetLink);


        }
    }

    private void sendLink(GoogleMeetLink googleMeetLink) {
        reference.setValue(googleMeetLink).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                linkIsSent = true;
                CustomSnackbar.create(getView()).setText("Sent to other user").show();
            }else {
                ErrorDialog.show(getContext());
                
            }
        });
    }

    private void receiveGoogleMeetLink(){
        linkList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linkList.clear();
                GoogleMeetLink googleMeetLink = snapshot.getValue(GoogleMeetLink.class);

                if (googleMeetLink != null)
                {
                    System.out.println("LinkList is not empty");
                    urlGoogleMeet = googleMeetLink.getLink();
                    hostName = googleMeetLink.getHost();
                    System.out.println(hostName);
                    hostTextView.setText(hostName);
                    linkContainerTextView.setText(urlGoogleMeet);
                    System.out.println("HOST NAME IS: " + hostName);

                    if (hostName != null && !urlGoogleMeet.equals("Meeting has ended")){
                        System.out.println("HOST NAME IS NOT NULL");
                        if (googleMeetLink.getUserId().equals(user.getUID())){
                            endButton.setVisibility(View.VISIBLE);
                            endButton.setOnClickListener(view -> {
                                GoogleMeetLink googleMeetLink1 = new GoogleMeetLink(user.getUID(),
                                        "Meeting has ended",
                                        Calendar.getInstance().getTime().toString(),
                                        user.getFirstName(),
                                        WorkSite.getChosenWorksite().getSiteID());

                                reference.setValue(googleMeetLink1).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        System.out.println("Stop previous meeting successful");
                                        linkIsSent = false;
                                        endButton.setVisibility(View.INVISIBLE);
                                    } else {
                                        System.out.println("Fail to stop previous meeting");

                                    }
                                });
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(error.getCode() != DatabaseError.PERMISSION_DENIED && getContext() != null)
                    ErrorDialog.show(getContext());
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

        if (Intent.ACTION_SEND.equals(action) && type != null && !checkIfIntentIsRepeated(intent)) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            }
        }

        getActivity().getIntent().setAction("");
        if(!linkIsSent) {
            endButton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean checkIfIntentIsRepeated(Intent intent){
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String[] arr = sharedText.split("\n");
        System.out.println("onResume: urlGoogleMeetIs " +  urlGoogleMeet + "intent url is" + arr[1].trim());
        return arr[1].trim().equals(urlGoogleMeet);
    }
    @Override
    public void onDestroy() {
        System.out.println("OnDestroy is called LiveMeetingFragment");
        super.onDestroy();

        if (userStartAMeeting) {
            GoogleMeetLink googleMeetLink = new GoogleMeetLink(user.getUID(), "Meeting has ended", Calendar.getInstance().getTime().toString(), user.getFirstName(), WorkSite.getChosenWorksite().getSiteID());

            reference.setValue(googleMeetLink).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Stop previous meeting successful");
                    linkIsSent = false;
                } else {
                    System.out.println("Fail to stop previous meeting");

                }
            });
        }
        if(!linkIsSent) {
            endButton.setVisibility(View.INVISIBLE);
        } else {
            endButton.setVisibility(View.VISIBLE);
        }

    }

}