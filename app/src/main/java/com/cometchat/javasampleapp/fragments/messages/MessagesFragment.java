package com.cometchat.javasampleapp.fragments.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.messages.CometChatMessages;
import com.cometchat.javasampleapp.R;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.models.User;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.constants.StringConstants;
import com.cometchat.javasampleapp.AppUtils;
import android.util.Log;


public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        CometChatMessages messagesView = view.findViewById(R.id.messages);

        if (getActivity() != null && getActivity().getIntent() != null) {
            String uid = getActivity().getIntent().getStringExtra(StringConstants.UID);
            if (uid != null) {
                // Determine if it's a user or a group (simplified logic for sample)
                CometChat.getUser(uid, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        messagesView.setUser(user);
                    }

                    @Override
                    public void onError(CometChatException e) {
                        CometChat.getGroup(uid, new CometChat.CallbackListener<Group>() {
                            @Override
                            public void onSuccess(Group group) {
                                messagesView.setGroup(group);
                            }

                            @Override
                            public void onError(CometChatException e) {
                                Log.e("MessagesFragment", "Error fetching user/group: " + e.getMessage());
                            }
                        });
                    }
                });
            } else {
                messagesView.setUser(AppUtils.getDefaultUser());
            }
        } else {
            messagesView.setUser(AppUtils.getDefaultUser());
        }

        return view;
    }
}