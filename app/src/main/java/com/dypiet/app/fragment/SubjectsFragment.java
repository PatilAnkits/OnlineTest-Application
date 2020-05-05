package com.dypiet.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.dypiet.app.Quiz;
import com.dypiet.app.R;
import com.dypiet.app.viewholder.SubjectViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public abstract class SubjectsFragment extends Fragment {

    private static final String TAG = "SubjectFragment";
    private FirebaseRecyclerAdapter<String,SubjectViewHolder> mAdapter;
    // [START define_database_reference]

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    private DatabaseReference mDatabase;
    LottieAnimationView animationView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView (inflater, container, savedInstanceState);
        View rootView = inflater.inflate (R.layout.activity_subjects, container, false);

        mRecycler = rootView.findViewById (R.id.recycler_view);
        mRecycler.setHasFixedSize (true);
        mSwipeRefreshLayout = rootView.findViewById (R.id.swipeRefreshlayout);
        mDatabase = FirebaseDatabase.getInstance ().getReference ().child ("Quiz");
        animationView = rootView.findViewById (R.id.animation_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated (savedInstanceState);

        mManager = new LinearLayoutManager (getActivity ());
        mManager.setReverseLayout (true);
        mManager.setStackFromEnd (true);
        mRecycler.setLayoutManager (mManager);

        mSwipeRefreshLayout.setRefreshing (true);
        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery ();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<String>()
                .setQuery(postsQuery, String.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<String, SubjectViewHolder>(options) {

            @Override
            public SubjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new SubjectViewHolder (inflater.inflate(R.layout.item_subject, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(SubjectViewHolder viewHolder, int position, final String model) {
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
                        Intent intent = new Intent (getActivity (), Quiz.class);
                        intent.putExtra ("Branch",getBranch());
                        intent.putExtra ("Subject",postKey);
                        System.out.println("POSTKEY :::"+postKey);
                        startActivity (intent);
                    }
                });
                // Bind Post to ViewHolder, setting OnClickListener for the star button

            }
        };
        mRecycler.setAdapter(mAdapter);
    }

    protected abstract String getBranch();

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public abstract Query getQuery();
}
