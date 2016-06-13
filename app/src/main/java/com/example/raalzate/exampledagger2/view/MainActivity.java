package com.example.raalzate.exampledagger2.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.raalzate.exampledagger2.App;
import com.example.raalzate.exampledagger2.R;
import com.example.raalzate.exampledagger2.api.GitHubApiInterface;
import com.example.raalzate.exampledagger2.model.RepositoryPOJO;

import java.util.ArrayList;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Inject SharedPreferences mSharedPreferences;
    @Inject Retrofit mRetrofit;
    @Inject GitHubApiInterface mGitHubApiInterface;

    @BindView(R.id.list_repositories) RecyclerView mRepositories;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private RepositoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mAdapter = new RepositoryAdapter(getBaseContext());
        mRepositories.setLayoutManager(new LinearLayoutManager(this));
        mRepositories.setAdapter(mAdapter);

        App.getComponent(getBaseContext()).inject(MainActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        callWebService();
    }

    private void callWebService(){
        Call<ArrayList<RepositoryPOJO>> call = mGitHubApiInterface.getRepository("raalzate");

        call.enqueue(new Callback<ArrayList<RepositoryPOJO>>() {
            @Override
            public void onResponse(Call<ArrayList<RepositoryPOJO>> call,
                                   Response<ArrayList<RepositoryPOJO>> response) {
                if(response.isSuccessful()) {
                    mAdapter.setData(response.body());
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<RepositoryPOJO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}