package edu.ewubd.bloodplus;


import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase proDB;
    DatabaseReference proDbRef;
    private Button logOut, userDetail;
    List<Post> postList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView proRecyclerView;
    PostAdapter postAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }


    ImageView proUserImage;
    TextView proUserName, proUserEmail, proUserCountry;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        proRecyclerView = view.findViewById(R.id.profileRecycle);
        proRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        proRecyclerView.setHasFixedSize(true);

        proDB = FirebaseDatabase.getInstance();
        proDbRef = proDB.getReference();

        proUserImage = view.findViewById(R.id.proUserImage);
        proUserName= view.findViewById(R.id.proUserName);
        proUserEmail = view.findViewById(R.id.proUserEmail);
        logOut = view.findViewById(R.id.logOut);
        userDetail = view.findViewById(R.id.userDetail);


        //set user name and email
        proUserEmail.setText(currentUser.getEmail());
        proUserName.setText(currentUser.getDisplayName());

        //now we will user Glide to load user image
        //Glide.with(this).load(currentUser.getPhotoUrl()).into(proUserImage);
        Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(proUserImage);

        userDetail.setOnClickListener(v -> funUserDetail());

        logOut.setOnClickListener(v -> funLogOut());

        proRecyclerView.setOnLongClickListener(v -> funOption());
        return view;
    }

    private boolean funOption() {
        Toast.makeText(getActivity(),"setOnLongClickListener",Toast.LENGTH_SHORT).show();
        return true;
    }

    private void funUserDetail() {
        startActivity(new Intent(getActivity(),UserDetail.class));
    }

    private void funLogOut() {
       auth.signOut();
        Toast.makeText(getActivity(),"Logged Out!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(),LoginActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        //query by userID on Posts
        Query query = proDbRef.child("Posts").orderByChild("userID").equalTo(currentUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList =  new ArrayList<>();


                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot posts : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Post post = posts.getValue(Post.class);
                        postList.add(post);
                    }
                }
                //showing only user Posts on this profile
                postAdapter = new PostAdapter(getActivity(),postList);
                proRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}