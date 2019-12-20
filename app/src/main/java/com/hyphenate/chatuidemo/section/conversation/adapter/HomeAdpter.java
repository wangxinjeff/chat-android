package com.hyphenate.chatuidemo.section.conversation.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.DateUtils;

import java.util.Date;

public class HomeAdpter extends EaseBaseRecyclerViewAdapter<EMConversation> {
    private EaseImageView avatar;
    private TextView mUnreadMsgNumber;
    private TextView name;
    private TextView time;
    private ImageView mMsgState;
    private TextView mentioned;
    private TextView message;

    @Override
    public int getItemLayoutId() {
        return R.layout.ease_row_chat_history;
    }

    @Override
    public void initView(View itemView) {
        avatar = itemView.findViewById(R.id.avatar);
        mUnreadMsgNumber = itemView.findViewById(R.id.unread_msg_number);
        name = itemView.findViewById(R.id.name);
        time = itemView.findViewById(R.id.time);
        mMsgState = itemView.findViewById(R.id.msg_state);
        mentioned = itemView.findViewById(R.id.mentioned);
        message = itemView.findViewById(R.id.message);
    }

    @Override
    public void setData(EMConversation item, int position) {
        String username = item.conversationId();
        mentioned.setVisibility(View.GONE);
        if(item.getType() == EMConversation.EMConversationType.GroupChat) {
            // TODO: 2019/12/20 0020 添加关于是否需要消息提醒的逻辑 这块逻辑需要理一理
            avatar.setImageResource(R.drawable.ease_group_icon);
            EMGroup group = DemoHelper.getInstance().getGroupManager().getGroup(username);
            name.setText(group != null ? group.getGroupName() : username);
        }else if(item.getType() == EMConversation.EMConversationType.ChatRoom) {
            avatar.setImageResource(R.drawable.ease_group_icon);
            EMChatRoom chatRoom = DemoHelper.getInstance().getChatroomManager().getChatRoom(username);
            name.setText(chatRoom != null && !TextUtils.isEmpty(chatRoom.getName()) ? chatRoom.getName() : username);
        }else {
            avatar.setImageResource(R.drawable.ease_default_avatar);
            name.setText(username);
        }

        if(item.getUnreadMsgCount() > 0) {
            mUnreadMsgNumber.setText(String.valueOf(item.getUnreadMsgCount()));
            mUnreadMsgNumber.setVisibility(View.VISIBLE);
        }else {
            mUnreadMsgNumber.setVisibility(View.GONE);
        }

        if(item.getAllMsgCount() != 0) {
            EMMessage lastMessage = item.getLastMessage();
            message.setText(EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(lastMessage, mContext)));
            time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                mMsgState.setVisibility(View.VISIBLE);
            } else {
                mMsgState.setVisibility(View.GONE);
            }
        }

    }
}
