package com.cometchat.javasampleapp.fragments.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.javasampleapp.R;


import android.content.Intent;
import com.cometchat.chatuikit.groups.CometChatGroups;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chat.models.Group;
import com.cometchat.javasampleapp.activity.ComponentLaunchActivity;
import com.cometchat.javasampleapp.constants.StringConstants;

public class GroupsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);
        CometChatGroups groups = view.findViewById(R.id.group);
        groups.setItemClickListener(new OnItemClickListener<Group>() {
            @Override
            public void OnItemClick(Group group, int i) {
                Intent intent = new Intent(getContext(), ComponentLaunchActivity.class);
                intent.putExtra("component", R.id.messages);
                intent.putExtra(StringConstants.UID, group.getGuid());
                intent.putExtra(StringConstants.NAME, group.getName());
                intent.putExtra(StringConstants.AVATAR, group.getIcon());
                intent.putExtra(StringConstants.GROUP_TYPE, group.getGroupType());
                intent.putExtra(StringConstants.TYPE, com.cometchat.chat.constants.CometChatConstants.RECEIVER_TYPE_GROUP);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.create_group_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ComponentLaunchActivity.class);
                intent.putExtra("component", R.id.create_group);
                startActivity(intent);
            }
        });
        return view;
    }
}
