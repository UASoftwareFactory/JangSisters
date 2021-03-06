package kr.sofac.jangsisters.views.fragments.viewElements;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.sofac.jangsisters.R;
import kr.sofac.jangsisters.activities.UserActivity;
import kr.sofac.jangsisters.config.KeyTransferFlag;
import kr.sofac.jangsisters.config.KeyTransferObj;
import kr.sofac.jangsisters.models.User;
import kr.sofac.jangsisters.network.Connection;
import kr.sofac.jangsisters.utils.ProgressBar;
import kr.sofac.jangsisters.views.adapters.FollowersAdapter;
import kr.sofac.jangsisters.views.fragments.BaseFragment;

public class FollowersFragment extends BaseFragment{

    @BindView(R.id.recycler) RecyclerView recycler;
    private List<User> users;
    private ProgressBar progressBar;
    private int userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, null);
        ButterKnife.bind(this, view);
        userID = getArguments().getInt(KeyTransferObj.USER_ID.toString(), 0);
        progressBar = new ProgressBar(getActivity());
        progressBar.showView();
        boolean isFollowers = getArguments().getBoolean(KeyTransferFlag.IS_SHOWING_FOLLOWERS.toString());
        if(isFollowers)
            loadFollowers();
        else
            loadFollowing();
        return view;
    }

    private void loadFollowers() {
        new Connection<List<User>>().getFollowers(userID, (isSuccess, answerServerResponse) -> {
            if(isSuccess){
                users = answerServerResponse.getDataTransferObject();
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycler.setAdapter(new FollowersAdapter(users, position -> {
                    startActivity(new Intent(getActivity(), UserActivity.class)
                            .putExtra(KeyTransferObj.USER_ID.toString(), users.get(position).getId())
                            .putExtra(KeyTransferFlag.IS_MY_PROFILE.toString(), false));
                }));
            }
            else{
                //todo handle error
            }
            progressBar.dismissView();
        });
    }

    private void loadFollowing() {
        new Connection<List<User>>().getFollowing(userID, (isSuccess, answerServerResponse) -> {
            if(isSuccess){
                users = answerServerResponse.getDataTransferObject();
                recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycler.setAdapter(new FollowersAdapter(users, position -> {
                    startActivity(new Intent(getActivity(), UserActivity.class)
                            .putExtra(KeyTransferObj.USER_ID.toString(), users.get(position).getId())
                            .putExtra(KeyTransferFlag.IS_MY_PROFILE.toString(), false));
                }));
            }
            else{
                //todo handle error
            }
            progressBar.dismissView();
        });
    }
}
