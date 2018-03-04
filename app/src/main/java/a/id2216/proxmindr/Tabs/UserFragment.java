package a.id2216.proxmindr.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


import a.id2216.proxmindr.LoginActivity;
import a.id2216.proxmindr.R;
import a.id2216.proxmindr.ReminderPackage.ReminderStorage;


public class UserFragment extends Fragment {
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_tab, container, false);
        Button logout = v.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                ReminderStorage.destroy();
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
        });
        return v;
    }
}
