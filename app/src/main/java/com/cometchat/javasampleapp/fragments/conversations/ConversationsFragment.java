package com.cometchat.javasampleapp.fragments.conversations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.javasampleapp.R;
import com.cometchat.chatuikit.conversations.CometChatConversations;
import com.cometchat.chat.models.Conversation;
import com.cometchat.javasampleapp.activity.ComponentLaunchActivity;
import android.content.Intent;
import android.widget.Toast;

import com.cometchat.javasampleapp.constants.StringConstants;
import com.cometchat.chat.models.User;
import com.cometchat.chat.models.Group;


import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;


public class ConversationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);
        CometChatConversations conversations = view.findViewById(R.id.conversationWithMessages);
        conversations.setItemClickListener(new OnItemClickListener<Conversation>() {
            @Override
            public void OnItemClick(Conversation conversation, int index) {
                Intent intent = new Intent(getContext(), ComponentLaunchActivity.class);
                intent.putExtra("component", R.id.messages);

                try {
                    String converString = conversation.getConversationType();
                    if (converString.equals(com.cometchat.chat.constants.CometChatConstants.CONVERSATION_TYPE_USER)) {
                        intent.putExtra(StringConstants.UID, ((User) conversation.getConversationWith()).getUid());
                        intent.putExtra(StringConstants.NAME, ((User) conversation.getConversationWith()).getName());
                        intent.putExtra(StringConstants.AVATAR, ((User) conversation.getConversationWith()).getAvatar());
                        intent.putExtra(StringConstants.TYPE, com.cometchat.chat.constants.CometChatConstants.RECEIVER_TYPE_USER);
                    } else {
                        intent.putExtra(StringConstants.UID, ((Group) conversation.getConversationWith()).getGuid());
                        intent.putExtra(StringConstants.NAME, ((Group) conversation.getConversationWith()).getName());
                        intent.putExtra(StringConstants.AVATAR, ((Group) conversation.getConversationWith()).getIcon());
                        intent.putExtra(StringConstants.GROUP_TYPE, ((Group) conversation.getConversationWith()).getGroupType());
                        intent.putExtra(StringConstants.TYPE, com.cometchat.chat.constants.CometChatConstants.RECEIVER_TYPE_GROUP);
                    }
                } catch (NullPointerException e) {

                } finally {
                    startActivity(intent);

                }
            }
        });
        return view;
    }
}