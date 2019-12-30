package com.example.messagepass;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.messagepass.Fragments.ChatsFragment;
import com.example.messagepass.Fragments.UserFragment;
import com.example.messagepass.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CircularImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);

        Toolbar toolbar = findViewById( R.id.toolbar);
        setSupportActionBar(toolbar);
         getSupportActionBar() .setTitle("");


        profile_image = findViewById( R.id.profile_image);
        username = findViewById( R.id.username);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
       // if(firebaseUser!=null) {
            reference = FirebaseDatabase.getInstance().getReference("Users").child( firebaseUser.getUid() );
            profile_image = findViewById( R.id.profile_image);
            username = findViewById( R.id.username);
            reference.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue( User.class );
                    username.setText( user.getUsername());
                    if (user.getImageURL().equals( "default" )) {
                        profile_image.setImageResource( R.mipmap.ic_launcher );
                    } else {
                        Glide.with( MainActivity.this ).load( user.getImageURL() ).into( profile_image );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
            TabLayout tabLayout = findViewById( R.id.tab_layout );
            ViewPager viewPager = findViewById( R.id.View_pager );
            ViewPageAdapter viewPageAdapter = new ViewPageAdapter( getSupportFragmentManager() );

            viewPageAdapter.addFragment( new ChatsFragment(),"Chat" );
            viewPageAdapter.addFragment( new UserFragment(),"Users" );

            viewPager.setAdapter( viewPageAdapter );
            tabLayout.setupWithViewPager( viewPager );
      // }else{
        //    Intent startIntent=(new Intent( MainActivity.this,StartActivity.class));
          //  startActivity( startIntent );
            //finish();
    //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
            FirebaseAuth.getInstance().signOut();
            startActivity( new Intent( MainActivity.this, StartActivity.class ) );
            finish();
            return true;
        }
        return  false;
    }
    class ViewPageAdapter extends FragmentPagerAdapter{
        private ArrayList<Fragment>fragments;
        private ArrayList<String>titles;

        ViewPageAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles= new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public  void addFragment(Fragment fragment,String title){
            fragments.add( fragment );
            titles.add(title);

        }
        //

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
