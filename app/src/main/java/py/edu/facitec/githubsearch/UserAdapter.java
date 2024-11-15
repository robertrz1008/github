package py.edu.facitec.githubsearch;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(@NonNull Context context, List<User> users) {
        super(context, R.layout.item_user, R.id.tvItemUserLogin, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        TextView urlTextView = v.findViewById(R.id.tvItemUserUrl);
        TextView loginTextView = v.findViewById(R.id.tvItemUserLogin);
        ImageView avatarImageView = v.findViewById(R.id.imageViewUserAvatar);

        User u = getItem(position);

        Picasso.get().load(u.getAvatarUrl()).into(avatarImageView);
        urlTextView.setText(u.getHtmlUrl());
        loginTextView.setText(u.getLogin());

        return v;
    }
}
