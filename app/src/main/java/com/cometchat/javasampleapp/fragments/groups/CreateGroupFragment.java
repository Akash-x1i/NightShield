package com.cometchat.javasampleapp.fragments.groups;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.util.Log;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.javasampleapp.R;

import com.cometchat.chatuikit.creategroup.CometChatCreateGroup;
import com.cometchat.chatuikit.shared.Interfaces.OnError;

public class CreateGroupFragment extends Fragment {

    private static final String TAG = "CreateGroupFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        CometChatCreateGroup createGroup = view.findViewById(R.id.create_group);

        createGroup.setOnCreateGroup(new CometChatCreateGroup.OnCreateGroup() {
            @Override
            public void onCreateGroup(Context context, Group group) {
                Log.d(TAG, "onCreateGroup triggered for: " + group.getName());
                
                // Ensure GUID is set (SDK requires it)
                if (group.getGuid() == null || group.getGuid().isEmpty()) {
                    String generatedGuid = group.getName().toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "_" + System.currentTimeMillis();
                    group.setGuid(generatedGuid);
                }

                Toast.makeText(context, "Creating group...", Toast.LENGTH_SHORT).show();

                // Call SDK to create the group
                CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
                    @Override
                    public void onSuccess(Group group) {
                        Log.d(TAG, "Group created successfully: " + group.getName());
                        if (isAdded() && getActivity() != null) {
                            Toast.makeText(getContext(), "Group created successfully", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.e(TAG, "Group creation failed: " + e.getMessage());
                        if (isAdded() && getContext() != null) {
                            Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        createGroup.setOnError(new OnError() {
            @Override
            public void onError(Context context, CometChatException e) {
                Log.e(TAG, "UI Error: " + e.getMessage());
            }
        });

        return view;
    }
}