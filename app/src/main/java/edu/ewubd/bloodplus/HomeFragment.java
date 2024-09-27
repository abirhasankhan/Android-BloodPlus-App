package edu.ewubd.bloodplus;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser currentUser;
    FirebaseDatabase fDatabase;
    DatabaseReference databaseReference;
    Dialog popAddPost;
    ImageView cpUserImage;
    EditText cpTitle, cpDate, cpPhone, cpCountry, cpCity, cpDes;
    FloatingActionButton btnPsot;
    Button cpBtnPost, cpBtnClose;
    ProgressBar cpBar;
    TableLayout TL01;
    List<Post> postList;
    String key= "";
    UserDetail userDetail;


    private final String[] items = {"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"};
    private String item;

    AutoCompleteTextView autoCompleteTxt;

    ArrayAdapter<String> adapterItems;

    ActivityResultLauncher<Intent> launchSomeActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView postRecyclerView;
    PostAdapter postAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


        //int pop
        intPopup();
        loadData();



    }


    private void intPopup() {

        popAddPost = new Dialog(getContext());

        popAddPost.setContentView(R.layout.popup_add_psot);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes()
                .gravity = Gravity.TOP;

        //set blood type
        autoCompleteTxt = popAddPost.findViewById(R.id.auto_complete_text);

        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.list_item, items);

        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext(), "Blood Group: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        cpUserImage = popAddPost.findViewById(R.id.cpUserImage);
        cpTitle = popAddPost.findViewById(R.id.cpTitle);
        cpDate = popAddPost.findViewById(R.id.cpDate);
        cpPhone = popAddPost.findViewById(R.id.cpPhone);
        cpCountry = popAddPost.findViewById(R.id.cpCountry);
        cpCity = popAddPost.findViewById(R.id.cpCity);
        cpDes = popAddPost.findViewById(R.id.cpDes);
        cpBtnPost = popAddPost.findViewById(R.id.cpBtnPost);
        cpBar = popAddPost.findViewById(R.id.cpProgressBar);
        TL01 = popAddPost.findViewById(R.id.TL01);
        cpBtnClose = popAddPost.findViewById(R.id.cpBtnClose);

        //load current user profile photo
        Glide.with(this).load(currentUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(cpUserImage);

        //add Post
        cpBtnPost.setOnClickListener(v -> funPost());

        cpBtnClose.setOnClickListener(v -> funClose());


    }

    private void funClose() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?");

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Close", (DialogInterface.OnClickListener) (dialog, which) -> {

            cpBar.setVisibility(View.INVISIBLE);
            popAddPost.dismiss();

        });

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("Save as draft", (DialogInterface.OnClickListener) (dialog, which) -> {




            cpPhone = popAddPost.findViewById(R.id.cpPhone);
            cpCountry = popAddPost.findViewById(R.id.cpCountry);
            cpCity = popAddPost.findViewById(R.id.cpCity);
            cpDes = popAddPost.findViewById(R.id.cpDes);
            cpBtnPost = popAddPost.findViewById(R.id.cpBtnPost);
            cpBar = popAddPost.findViewById(R.id.cpProgressBar);
            TL01 = popAddPost.findViewById(R.id.TL01);
            cpBtnClose = popAddPost.findViewById(R.id.cpBtnClose);

            String title = cpTitle.getText().toString().trim();
            String bloodType = item;
            String date  = cpDate.getText().toString().trim();
            String phone = cpPhone.getText().toString().trim();
            String country = cpCountry.getText().toString().trim();
            String city = cpCity.getText().toString().trim();
            String desc = cpDes.getText().toString().trim();


            if (key.length() == 0) {
                key = title + System.currentTimeMillis();
            }

            String values = title +"-::-"+ date +"-::-"+ phone +"-::-"+ country +"-::-"+ city +"-::-"+ desc +"-::-"+ bloodType;

            KeyValueDB kvdb = new KeyValueDB(getActivity());
            kvdb.insertKeyValue(key,values);

            System.out.println("Key is: "+key);
            System.out.println(values);
            showMessage("Save as draft");
            cpBar.setVisibility(View.INVISIBLE);
            popAddPost.dismiss();

            //dialog.cancel();
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();


    }
    private void loadData(){
        postList = new ArrayList<>();
        KeyValueDB db = new KeyValueDB(getContext());
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        if (rows.getCount() == 0) {
            return;
        }
        //events = new Event[rows.getCount()];
        while (rows.moveToNext()) {
            String key = rows.getString(0);
            String eventData = rows.getString(1);
            String[] fieldValues = eventData.split("-::-");
            if (fieldValues.length >= 1) {
                String title = fieldValues[0];
                String date = fieldValues[1];
                String phone = fieldValues[2];
                String country = fieldValues[3];
                String city = fieldValues[4];
                String desc = fieldValues[5];
                String bloodType = fieldValues[6];

                Post p = new Post(title, currentUser.getDisplayName(), date, phone, country, city, desc, bloodType, currentUser.getUid(), currentUser.getPhotoUrl().toString());
                postList.add(p);
            }
        }
        db.close();
        PostAdapter adapter = new PostAdapter(getContext(), postList);
        //lvEvents.setAdapter(adapter);
        /*
        CustomEventAdapter adapter = new CustomEventAdapter(this, events);
        lvEvents.setAdapter(adapter);

        // handle the click on an event-list item
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                // String item = (String) parent.getItemAtPosition(position);
                System.out.println(position);
                Intent i = new Intent(UpcomingEventActivity.this, EventInformation.class);
                i.putExtra("EventKey", events.get(position).key);
                startActivity(i);

            }
        });
        // handle the long-click on an event-list item
        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //String message = "Do you want to delete event - "+events[position].name +" ?";
                String message = "Do you want to delete event - "+events.get(position).name +" ?";
                System.out.println(message);
                showDialog(message, "Delete Event", events.get(position).key);
                return true;
            }
        });*/

    }


    private void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }

    private void funPost() {

        //checking input
        if (!cpTitle.getText().toString().isEmpty() && !cpDate.getText().toString().isEmpty()
                && !cpPhone.getText().toString().isEmpty() && !cpCountry.getText().toString().isEmpty()
                && !cpCity.getText().toString().isEmpty()  && !item.isEmpty()){

            //create Post object
            Post post = new Post(cpTitle.getText().toString(), currentUser.getDisplayName(), cpDate.getText().toString(), cpPhone.getText().toString(),
                    cpCountry.getText().toString(), cpCity.getText().toString(), cpDes.getText().toString(), item,
                    currentUser.getUid(),  currentUser.getPhotoUrl().toString());

            //add post
            addPost(post);
            TL01.setAlpha((float) 0.4);
            cpBar.setVisibility(View.VISIBLE);


        } else {
            showMessage("Please verify all input");
            cpBar.setVisibility(View.INVISIBLE);

        }


    }

    private void addPost(Post post) {

        FirebaseDatabase pDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = pDatabase.getReference("Posts").push();

        //get post unique id nad update post key
        String postKey = myref.getKey();
        post.setPostKey(postKey);

        //add post to firebase database
        myref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                showMessage("Post uploaded successfully");
                cpBar.setVisibility(View.INVISIBLE);
                popAddPost.dismiss();

            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        postRecyclerView = view.findViewById(R.id.pRecycle);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);

        fDatabase = FirebaseDatabase.getInstance();
        databaseReference = fDatabase.getReference("Posts");


        btnPsot = view.findViewById(R.id.btnPsot);
        btnPsot.setOnClickListener(v -> {
            popAddPost.show();
        });

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        //get list Posts from database

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);

                }
                postAdapter = new PostAdapter(getActivity(), postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




/*
        Query query = databaseReference.orderByChild("userID").equalTo(currentUser.getUid());
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
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }/*
    postRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //String message = "Do you want to delete event - "+events[position].name +" ?";
            String message = "Do you want to delete event - "+events.get(position).name +" ?";
            System.out.println(message);
            showDialog(message, "Delete Event", events.get(position).key);
            return true;
        }
    });*/



}
