package com.cometchat.javasampleapp.fragments.messages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.core.MessagesRequest;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.messages.CometChatMessages;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.MessageBubbleStyle;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.constants.StringConstants;

import java.util.Arrays;

public class MessagesFragment extends Fragment {

    private static final String TAG = "MessagesFragment";

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        CometChatMessages messagesView = view.findViewById(R.id.messages_view);

        // Prevent NullPointerException
        if (messagesView == null) {
            Log.e(TAG, "messages_view not found in fragment_messages.xml");
            return view;
        }

        // Context safety check
        if (getContext() == null) {
            Log.e(TAG, "Context is null");
            return view;
        }

        try {

            MessageListConfiguration messageListConfiguration =
                    new MessageListConfiguration();

            // Incoming bubble style
            MessageBubbleStyle incomingStyle = new MessageBubbleStyle();
            incomingStyle.setBackgroundColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.whatsapp_incoming_bubble
                    )
            );
            incomingStyle.setCornerRadius(
                    Utils.convertDpToPx(requireContext(), 12)
            );

            // Outgoing bubble style
            MessageBubbleStyle outgoingStyle = new MessageBubbleStyle();
            outgoingStyle.setBackgroundColor(
                    ContextCompat.getColor(
                            requireContext(),
                            R.color.whatsapp_outgoing_bubble
                    )
            );
            outgoingStyle.setCornerRadius(
                    Utils.convertDpToPx(requireContext(), 12)
            );

            Bundle bundle = getArguments();

            if (bundle == null) {
                Log.e(TAG, "Arguments bundle is null");
                return view;
            }

            String uid = bundle.getString(StringConstants.UID, "");
            String type = bundle.getString(StringConstants.TYPE, "");
            String name = bundle.getString(StringConstants.NAME, "");
            String avatar = bundle.getString(StringConstants.AVATAR, "");
            String groupType = bundle.getString(StringConstants.GROUP_TYPE);
            if (groupType == null || groupType.isEmpty()) {
                groupType = CometChatConstants.GROUP_TYPE_PUBLIC;
            }

            if (uid.isEmpty() || type.isEmpty()) {
                Log.e(TAG, "UID or TYPE is empty");
                return view;
            }

            Log.d(TAG, "Opening chat: " + uid + " type: " + type);

            MessagesRequest.MessagesRequestBuilder builder = new MessagesRequest.MessagesRequestBuilder();

            // Explicitly set categories and types to ensure all content is loaded
            builder.setCategories(Arrays.asList(
                    CometChatConstants.CATEGORY_MESSAGE,
                    CometChatConstants.CATEGORY_CUSTOM,
                    CometChatConstants.CATEGORY_ACTION
            ));

            builder.setTypes(Arrays.asList(
                    CometChatConstants.MESSAGE_TYPE_TEXT,
                    CometChatConstants.MESSAGE_TYPE_IMAGE,
                    CometChatConstants.MESSAGE_TYPE_VIDEO,
                    CometChatConstants.MESSAGE_TYPE_AUDIO,
                    CometChatConstants.MESSAGE_TYPE_FILE,
                    CometChatConstants.MESSAGE_TYPE_CUSTOM
            ));

            builder.setLimit(30);
            builder.hideReplies(true);

            messageListConfiguration.setMessagesRequestBuilder(builder);

            // IMPORTANT: Set configuration before setting the user/group
            messagesView.setMessageListConfiguration(messageListConfiguration);

            // USER CHAT
            if (CometChatConstants.RECEIVER_TYPE_USER.equals(type)) {

                User user = new User();
                user.setUid(uid);
                user.setName(name);

                if (avatar != null) {
                    user.setAvatar(avatar);
                }

                messagesView.setUser(user);

                // Fetch full user
                CometChat.getUser(uid, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {

                        if (!isAdded() || getActivity() == null) {
                            return;
                        }

                        Log.d(TAG, "User fetched: " + user.getName());

                        try {
                            messagesView.setUser(user);
                        } catch (Exception e) {
                            Log.e(TAG, "setUser crash: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.e(TAG, "Error fetching user: " + e.getMessage());
                    }
                });

            } else {

                // Initialize with basic group info immediately so messages can start loading
                Group group = new Group();
                group.setGuid(uid);
                group.setName(name != null && !name.isEmpty() ? name : "Group");
                group.setIcon(avatar);
                group.setGroupType(groupType);
                messagesView.setGroup(group);

                // Fetch full group details from SDK to update the header and check membership
                CometChat.getGroup(uid, new CometChat.CallbackListener<Group>() {
                    @Override
                    public void onSuccess(Group group) {

                        if (!isAdded() || getActivity() == null) {
                            return;
                        }

                        Log.d(TAG, "Group fetched: " + group.getName() + " Joined: " + group.isJoined());

                        try {
                            if (!group.isJoined() && group.getGroupType().equalsIgnoreCase(CometChatConstants.GROUP_TYPE_PUBLIC)) {
                                CometChat.joinGroup(group.getGuid(), group.getGroupType(), "", new CometChat.CallbackListener<Group>() {
                                    @Override
                                    public void onSuccess(Group joinedGroup) {
                                        messagesView.setGroup(joinedGroup);
                                    }

                                    @Override
                                    public void onError(CometChatException e) {
                                        messagesView.setGroup(group);
                                    }
                                });
                            } else {
                                messagesView.setGroup(group);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "setGroup crash: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.e(TAG, "Error fetching group: " + e.getMessage());
                    }
                });
            }

        } catch (Exception e) {
            Log.e(TAG, "Fatal error: ", e);
        }

        return view;
    }
}