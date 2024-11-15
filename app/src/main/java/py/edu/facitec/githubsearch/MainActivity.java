package py.edu.facitec.githubsearch;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements Callback<User>{

    UserService service;
    SearchView loginSearchView;
    ImageView avatarImageView;
    TextView nameTextView, urlTextView, locationTextView, errorTextView;
    ProgressBar progressBar, progressBarUsers;
    LinearLayout resultLayout, emptyLayout;
    ListView usersListView;
    Callback<List<User>> callback;
    User user;
    Button followersButton, followingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loginSearchView = findViewById(R.id.searchViewLogin);
        avatarImageView = findViewById(R.id.imageViewAvatar);
        nameTextView = findViewById(R.id.tvName);
        locationTextView = findViewById(R.id.tvLocation);
        urlTextView = findViewById(R.id.tvUrl);
        errorTextView = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressbar);
        progressBarUsers = findViewById(R.id.progressbarUsers);
        resultLayout = findViewById(R.id.layout_success);
        emptyLayout = findViewById(R.id.linearLayoutEmpty);
        usersListView = findViewById(R.id.listViewUsers);
        followersButton = findViewById(R.id.buttonFollowers);
        followingButton = findViewById(R.id.buttonFollowing);

        service = new RestAdapter
                .Builder()
                .setEndpoint(getString(R.string.end_point))
                .build()
                .create(UserService.class);

        /*service.getUserByLogin("mojombo", new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        Log.i("RESULT", user.toString());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("RESULT", error.getLocalizedMessage());
                    }
                });*/
        loginSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                service.getUserByLogin(s, MainActivity.this);
                progressBar.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        callback = new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                usersListView.setVisibility(View.VISIBLE);
                progressBarUsers.setVisibility(View.GONE);
                //ArrayAdapter<User> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, users);
                UserAdapter adapter = new UserAdapter(MainActivity.this, users);
                usersListView.setAdapter(adapter);
            }

            @Override
            public void failure(RetrofitError error) {
                usersListView.setVisibility(View.GONE);
                progressBarUsers.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public void success(User user, Response response) {
        nameTextView.setText(user.getName());
        urlTextView.setText(user.getHtmlUrl());
        locationTextView.setText(user.getLocation());
        Picasso.get().load(user.getAvatarUrl()).into(avatarImageView);
        resultLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        this.user = user;
        progressBarUsers.setVisibility(View.VISIBLE);
        usersListView.setVisibility(View.GONE);
        followersButton.setText(user.getFollower()+"followers");
        followingButton.setText(user.getFollowing()+"following");

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User u = (User) adapterView.getAdapter().getItem(i);
                loginSearchView.setQuery(u.getLogin(), true);
            }
        });

        //users Followers
        service.getUserFollower(user.getLogin(), callback);
    }

    @Override
    public void failure(RetrofitError error) {
        resultLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(error.getLocalizedMessage());
    }

    public void getFollowing(View view) {
        progressBarUsers.setVisibility(View.VISIBLE);
        usersListView.setVisibility(View.GONE);
        //users Followers
        service.getUserFollower(user.getLogin(), callback);
    }

    public void getFollower(View view) {
        progressBarUsers.setVisibility(View.VISIBLE);
        usersListView.setVisibility(View.GONE);
        //users Followers
        service.getUserFollower(user.getLogin(), callback);
    }
}