package com.cometchat.javasampleapp.fragments.groups;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.javasampleapp.R;

import com.cometchat.chatuikit.creategroup.CometChatCreateGroup;
import com.cometchat.chatuikit.shared.resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.chat.models.Group;

public class CreateGroupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        CometChatCreateGroup createGroup = view.findViewById(R.id.create_group);

        createGroup.setOnCreateGroup(new CometChatCreateGroup.OnCreateGroup() {
            @Override
            public void onCreateGroup(Context context, Group group) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        return view;
    }
}