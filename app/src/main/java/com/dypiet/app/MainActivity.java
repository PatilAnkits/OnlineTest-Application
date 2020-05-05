package com.dypiet.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import com.dypiet.app.viewholder.BranchViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button Comp;
    RecyclerView mRecycler;
    LinearLayoutManager mManager;
    FirebaseRecyclerAdapter<String, BranchViewHolder> mAdapter;
    LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
        mRecycler = findViewById (R.id.recycler_view);
        animationView = findViewById (R.id.animation_view);
        mManager = new LinearLayoutManager (this);
        mManager.setStackFromEnd (true);
        mManager.setReverseLayout (true);
        mRecycler.setHasFixedSize (true);
        mRecycler.setLayoutManager (mManager);

        final SwipeRefreshLayout mSwipeRefreshLayout = findViewById (R.id.swipeRefreshlayout);

        mSwipeRefreshLayout.setRefreshing (true);
        Query postsQuery = getQuery ();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(postsQuery, String.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<String, BranchViewHolder>(options) {

            @Override
            public BranchViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new BranchViewHolder (inflater.inflate(R.layout.item_branch, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final BranchViewHolder viewHolder, int position, final String model) {
                final DatabaseReference postRef = getRef(position);
                postRef.addListenerForSingleValueEvent (new ValueEventListener ( ) {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mSwipeRefreshLayout.setRefreshing (false);
                        mSwipeRefreshLayout.setEnabled (false);
                        animationView.cancelAnimation ();
                        animationView.setVisibility (View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mSwipeRefreshLayout.setRefreshing (false);
                        animationView.setVisibility (View.GONE);
                    }
                });
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.setText (model);
                viewHolder.setOnClick (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View v) {

                        final Dialog dialog = new Dialog(MainActivity.this);
                        dialog.setContentView (R.layout.year_dialog);
                        dialog.setCancelable (true);
                        Button buttonFE = dialog.findViewById (R.id.dialog_btn_fyear);
                        Button buttonSE = dialog.findViewById (R.id.dialog_btn_syear);

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));
                        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = width;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        buttonFE.setWidth (width);
                        buttonFE.setOnClickListener (new View.OnClickListener ( ) {
                            @Override
                            public void onClick(View v) {
                                dialog.hide ();
                                Snackbar.make (findViewById (android.R.id.content),"First year under maintenance !!!",Snackbar.LENGTH_LONG).show ();

                            }
                        });

                        buttonSE.setOnClickListener (new View.OnClickListener ( ) {
                            @Override
                            public void onClick(View v) {
                                dialog.hide ();
                                Intent intent =  new Intent (MainActivity.this,BranchViewActivity.class);
                                intent.putExtra ("Branch",postKey);
                                intent.putExtra ("Year","SE");
                                startActivity (intent);
                            }
                        });
                        dialog.show ();
                    }
                });
                // Bind Post to ViewHolder, setting OnClickListener for the star button

            }
        };
        mRecycler.setAdapter(mAdapter);

        if(!isConnected ()){
            mSwipeRefreshLayout.setRefreshing (false);
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning)
                    .setTitle("No Internet !")
                    .setCancelable (false)
                    .setMessage("Please turn on data or Wi-Fi to access this app")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish ();
                        }

                    })
                    .show();
        }
    }

    private Query getQuery() {
        return FirebaseDatabase.getInstance ().getReference ().child ("Depts");
    }

    @Override
    protected void onStart() {
        super.onStart ( );
        if(mAdapter!=null){
            mAdapter.startListening ();
        }
    }

    @Override
    protected void onStop() {
        super.onStop ( );
        if(mAdapter!=null){
            mAdapter.stopListening ();
        }
    }

    public boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

