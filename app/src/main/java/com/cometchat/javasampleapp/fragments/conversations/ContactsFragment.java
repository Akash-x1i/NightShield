package com.cometchat.javasampleapp.fragments.conversations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.javasampleapp.R;
import android.content.Intent;
import com.cometchat.chatuikit.contacts.CometChatContacts;
import com.cometchat.chat.models.User;
import com.cometchat.chat.models.Group;
import com.cometchat.javasampleapp.activity.ComponentLaunchActivity;
import com.cometchat.javasampleapp.constants.StringConstants;
import android.content.Context;

public class ContactsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        CometChatContacts contacts = view.findViewById(R.id.contacts);
        contacts.setOnItemClickListener(new CometChatContacts.OnItemSelection() {
            @Override
            public void onItemSelection(Context context, User user, Group group) {
                Intent intent = new Intent(getContext(), ComponentLaunchActivity.class);
                intent.putExtra("component", R.id.messages);
                if (user != null) {
                    intent.putExtra(StringConstants.UID, user.getUid());
                    intent.putExtra(StringConstants.NAME, user.getName());
                    intent.putExtra(StringConstants.AVATAR, user.getAvatar());
                    intent.putExtra(StringConstants.TYPE, com.cometchat.chat.constants.CometChatConstants.RECEIVER_TYPE_USER);
                } else if (group != null) {
                    intent.putExtra(StringConstants.UID, group.getGuid());
                    intent.putExtra(StringConstants.NAME, group.getName());
                    intent.putExtra(StringConstants.TYPE, com.cometchat.chat.constants.CometChatConstants.RECEIVER_TYPE_GROUP);
                }
                startActivity(intent);
            }
        });
        return view;
    }
}
