package edu.ewubd.bloodplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetail extends AppCompatActivity {

    ImageView pdUserImg, commentUserImg;
    TextView pdTitle, pdDateAndName, pdBlood, pdPhone, pdDate, pdDes;
    EditText pdcomment;
    Button btnComment;
    String postKey;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase database;
    RecyclerView reCommentView;

    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String commentKey = "Comment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Objects.requireNonNull(getSupportActionBar()).hide();

        reCommentView = findViewById(R.id.pdRec);

        pdUserImg = findViewById(R.id.pdUserImage);
        commentUserImg = findViewById(R.id.pdCommentImage);

        pdTitle =  findViewById(R.id.pdTitle);
        pdDateAndName =  findViewById(R.id.pdNameAndDate);
        pdBlood =  findViewById(R.id.pdBlooadType);
        pdPhone =  findViewById(R.id.pdPhone);
        pdDate=  findViewById(R.id.pdDate);
        pdDes = findViewById(R.id.pdDes);

        pdcomment = findViewById(R.id.pdComment);

        btnComment = findViewById(R.id.btnComment);
        btnComment.setOnClickListener(v -> funComment());

        //bind all all into those view
        //get Post data


        //goPostPage.putExtra("name", mData.get(position).getUserName);

        String postUserName = getIntent().getExtras().getString("name");

        String postUserImg = getIntent().getExtras().getString("userImage");
        Glide.with(this).load(postUserImg).apply(RequestOptions.circleCropTransform()).into(pdUserImg);

        String postTitle = getIntent().getExtras().getString("title");
        pdTitle.setText(postTitle);

        String postBlood = getIntent().getExtras().getString("blood");
        pdBlood.setText(postBlood);

        String postDate = getIntent().getExtras().getString("date");
        pdDate.setText(postDate);

        String postPhone = getIntent().getExtras().getString("phone");
        pdPhone.setText(postPhone);

        String postDesc= getIntent().getExtras().getString("desc");
        pdDes.setText(postDesc);

/*      for country and city. no need for this version
        String postCountry= getIntent().getExtras().getString("country");
        String postCity= getIntent().getExtras().getString("city");
*/

        //set comment and user image;
        Glide.with(this).load(fUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(commentUserImg);

        //get post id
        postKey = getIntent().getExtras().getString("postKey");

        String date = timeStampToString(getIntent().getExtras().getLong("postDate"));
        pdDateAndName.setText(date +"  | by "+ postUserName);


        //ini RecyclerView for comment
        iniRvComment();

    }

    private void iniRvComment() {

        reCommentView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = database.getReference(commentKey).child(postKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                    Comment comment = dataSnapshot.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                reCommentView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void funComment() {
        btnComment.setVisibility(View.INVISIBLE);
        DatabaseReference commentReference = database.getReference(commentKey).child(postKey).push();
        String comment = pdcomment.getText().toString();
        String uid = fUser.getUid();
        String uname = fUser.getDisplayName();
        String uimg = fUser.getPhotoUrl().toString();

        Comment comm = new Comment(comment,uid,uname,uimg);
        commentReference.setValue(comm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showMessage("Your comment has been added");
                pdcomment.setText("");
                btnComment.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Fail to add your comment"+e.getMessage());
            }
        });
    }

    private void showMessage(String comment_added) {
        Toast.makeText(this,comment_added,Toast.LENGTH_LONG).show();
    }

    private String timeStampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();

        return date;

    }

}