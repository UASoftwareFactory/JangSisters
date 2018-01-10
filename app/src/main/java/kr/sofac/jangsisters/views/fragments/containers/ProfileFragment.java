package kr.sofac.jangsisters.views.fragments.containers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.sofac.jangsisters.R;
import kr.sofac.jangsisters.activities.LoginActivity;
import kr.sofac.jangsisters.activities.SettingsActivity;
import kr.sofac.jangsisters.config.EnumPreference;
import kr.sofac.jangsisters.models.TabManager;
import kr.sofac.jangsisters.models.User;
import kr.sofac.jangsisters.utils.AppPreference;
import kr.sofac.jangsisters.utils.UserWrapper;
import kr.sofac.jangsisters.views.customview.ButtonLight;
import kr.sofac.jangsisters.views.fragments.BaseFragment;
import kr.sofac.jangsisters.views.fragments.viewElements.FollowersFragment;
import kr.sofac.jangsisters.views.fragments.viewElements.GridViewPostFragment;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.user_image) ImageView userImage;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.user_balance) TextView userBalance;
    @BindView(R.id.message) ButtonLight message;
    @BindView(R.id.follow) ButtonLight follow;
    @BindView(R.id.balance) ButtonLight balance;

    private FragmentManager fragmentManager;
    private int userID;
    private boolean myProfile;

    private GridViewPostFragment posts;
    private FollowersFragment followers;
    private FollowersFragment following;
    private GridViewPostFragment bookmarks;
    private AppPreference appPreference;
    private User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        appPreference = new AppPreference(getActivity());
        userID = getArguments().getInt(EnumPreference.USER_ID.toString());
        myProfile = getArguments().getBoolean(EnumPreference.MY_PROFILE.toString());
        if(myProfile){
            follow.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
            user = appPreference.getUser();
        }
        else{
            balance.setVisibility(View.GONE);
            user = UserWrapper.getUserByID(userID);
        }
        Glide.with(this).load(user.getAvatar())
                .apply(new RequestOptions().placeholder(R.drawable.boy))
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);
        username.setText(user.getName());

        initFragments();
        initTabLayout();
        return view;
    }

    private void initFragments() {
        posts = new GridViewPostFragment();
        Bundle bundle = new Bundle();
        Bundle bundle1 = new Bundle();
        bundle.putBoolean(EnumPreference.FOLLOWERS.toString(), true);
        bundle1.putBoolean(EnumPreference.FOLLOWERS.toString(), false);
        followers = new FollowersFragment();
        following = new FollowersFragment();
        followers.setArguments(bundle);
        following.setArguments(bundle1);
        bookmarks = new GridViewPostFragment();
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame, posts).commit();
    }

    private void initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("My posts"), true);
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        tabLayout.addTab(tabLayout.newTab().setText("Followers"));
        if (myProfile) {
            tabLayout.addTab(tabLayout.newTab().setText("Bookmarks"));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            setCurrentFragment(posts);
                            break;
                        case 1:
                            setCurrentFragment(following);
                            break;
                        case 2:
                            setCurrentFragment(followers);
                            break;
                        case 3:
                            setCurrentFragment(bookmarks);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            setCurrentFragment(posts);
                            break;
                        case 1:
                            setCurrentFragment(followers);
                            break;
                        case 2:
                            setCurrentFragment(following);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

    }

    private void setCurrentFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(TabManager.getTabManager().getMenuByPosition(4), menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                getActivity().overridePendingTransition(R.anim.forward_start, R.anim.forward_finish);
                break;
            case R.id.logout:
                appPreference.clearUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finishAffinity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
