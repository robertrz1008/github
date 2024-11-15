package py.edu.facitec.githubsearch;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface UserService {
    @GET("/users/{login}")
    void getUserByLogin(@Path("login") String login, Callback<User> callback);

    @GET("/users/{login}/followers")
    void getUserFollower(@Path("login") String login, Callback<List<User>> callback);

    @GET("/users/{login}/following")
    void getUserFollowing(@Path("login") String login, Callback<User> callback);
}
