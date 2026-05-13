package com.cometchat.javasampleapp.fragments.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.messages.CometChatMessages;
import com.cometchat.chatuikit.messagelist.MessageListConfiguration;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.MessageBubbleStyle;
import com.cometchat.javasampleapp.R;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.models.User;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.constants.StringConstants;
import com.cometchat.javasampleapp.AppUtils;
import android.util.Log;
import androidx.core.content.ContextCompat;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.MessagesRequest;

import android.content.Context;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.CometChatMessageTemplate;
import com.cometchat.chatuikit.shared.viewholders.MessagesViewHolderListener;
import com.cometchat.chatuikit.shared.views.CometChatMessageBubble.CometChatMessageBubble;
import androidx.recyclerview.widget.RecyclerView;
import com.cometchat.chat.models.BaseMessage;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.CometChatReceipt;
import java.util.List;
import android.widget.ImageView;
import android.graphics.PorterDuff;

public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        CometChatMessages messagesView = view.findViewById(R.id.messages_view);

        MessageListConfiguration messageListConfiguration = new MessageListConfiguration();

        // Create WhatsApp styles
        MessageBubbleStyle incomingStyle = new MessageBubbleStyle();
        incomingStyle.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.whatsapp_incoming_bubble));
        incomingStyle.setCornerRadius(Utils.convertDpToPx(requireContext(), 12));

        MessageBubbleStyle outgoingStyle = new MessageBubbleStyle();
        outgoingStyle.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.whatsapp_outgoing_bubble));
        outgoingStyle.setCornerRadius(Utils.convertDpToPx(requireContext(), 12));

        // Differentiate bubbles by modifying templates
        // TODO: Actually write the differentiation bubbles @Pranav

        // Configure Message Receipts to match WhatsApp (Blue ticks for read)
        // Note: Assuming the SDK uses default icons, we can also set custom ones if available.
        // If we had custom tick drawables, we would use:
        // messageListConfiguration.setReadIcon(R.drawable.ic_double_tick_blue);
        // For now, we ensure receipts are enabled and visible.

        Bundle bundle = getArguments();
        if (bundle != null) {
            String uid = bundle.getString(StringConstants.UID);
            String type = bundle.getString(StringConstants.TYPE);
            String name = bundle.getString(StringConstants.NAME);
            String avatar = bundle.getString(StringConstants.AVATAR);

            if (uid != null && type != null) {
                Log.d("MessagesFragment", "Configuring chat for: " + uid + " type: " + type);
                
                // Set explicit request builder to ensure data fetching is triggered
                MessagesRequest.MessagesRequestBuilder messagesRequestBuilder = new MessagesRequest.MessagesRequestBuilder();
                if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                    messagesRequestBuilder.setUID(uid);
                } else {
                    messagesRequestBuilder.setGUID(uid);
                }
                messageListConfiguration.setMessagesRequestBuilder(messagesRequestBuilder.setLimit(30));
                
                // Set configuration BEFORE setting user/group
                messagesView.setMessageListConfiguration(messageListConfiguration);

                if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                    User user = new User();
                    user.setUid(uid);
                    user.setName(name);
                    user.setAvatar(avatar);
                    messagesView.setUser(user);

                    // Fetch full user details in background to update header
                    CometChat.getUser(uid, new CometChat.CallbackListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            if (isAdded()) {
                                Log.d("MessagesFragment", "User fetched successfully: " + user.getName());
                                messagesView.setUser(user);
                            }
                        }

                        @Override
                        public void onError(CometChatException e) {
                            Log.e("MessagesFragment", "Error fetching user: " + e.getMessage());
                        }
                    });
                } else {
                    Group group = new Group();
                    group.setGuid(uid);
                    group.setName(name);
                    messagesView.setGroup(group);

                    // Fetch full group details in background to update header
                    CometChat.getGroup(uid, new CometChat.CallbackListener<Group>() {
                        @Override
                        public void onSuccess(Group group) {
                            if (isAdded()) {
                                Log.d("MessagesFragment", "Group fetched successfully: " + group.getName());
                                messagesView.setGroup(group);
                            }
                        }

                        @Override
                        public void onError(CometChatException e) {
                            Log.e("MessagesFragment", "Error fetching group: " + e.getMessage());
                        }
                    });
                }
            } else {
                Log.w("MessagesFragment", "UID or TYPE is null in bundle");
            }
        } else {
            Log.w("MessagesFragment", "Arguments bundle is null");
        }

        return view;
    }
}
