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
                if (conversation.getConversationType().equals(com.cometchat.chat.constants.CometChatConstants.CONVERSATION_TYPE_USER)) {
                    intent.putExtra(StringConstants.UID, ((User) conversation.getConversationWith()).getUid());
                    intent.putExtra(StringConstants.NAME, ((User) conversation.getConversationWith()).getName());
                    intent.putExtra(StringConstants.AVATAR, ((User) conversation.getConversationWith()).getAvatar());
                } else {
                    intent.putExtra(StringConstants.UID, ((Group) conversation.getConversationWith()).getGuid());
                    intent.putExtra(StringConstants.NAME, ((Group) conversation.getConversationWith()).getName());
                }
                startActivity(intent);
            }
        });
        return view;
    }
}