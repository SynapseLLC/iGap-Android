/*
* This is the source code of iGap for Android
* It is licensed under GNU AGPL v3.0
* You should have received a copy of the license in this archive (see LICENSE).
* Copyright © 2017 , iGap - www.iGap.net
* iGap Messenger | Free, Fast and Secure instant messaging application
* The idea of the RooyeKhat Media Company - www.RooyeKhat.co
* All rights reserved.
*/


package com.iGap.module;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.iGap.G;
import com.iGap.R;
import com.iGap.fragments.FragmentMap;
import com.iGap.interfaces.IResendMessage;
import com.iGap.proto.ProtoGlobal;
import com.iGap.proto.ProtoUserUpdateStatus;
import com.iGap.realm.RealmAttachment;
import com.iGap.realm.RealmRoomMessage;
import com.iGap.realm.RealmRoomMessageFields;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import java.util.List;

import static com.iGap.G.context;

public final class AppUtils {
    private AppUtils() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation.");
    }

    public static String[] exts = {".jpg", ".jpeg", ".gif", ".png", ".tif"};

    public static String suitableThumbFileName(String name) {
        boolean isImage = false;
        for (String ext : exts) {
            if (name != null && name.endsWith(ext)) {
                isImage = true;
                break;
            }
        }

        if (isImage) {
            return name;
        } else {
            return name.replaceFirst("([\\w\\W]+)(\\.(\\w+))$", "$1.jpg");
        }
    }

    /**
     * change enum to string for simple showing in toolbar when get status
     *
     * @param status UserUpdateStatus
     */

    public static String getStatsForUser(String status) {

        String userStatus = "";
        if ((status == null) || (status.equals(ProtoUserUpdateStatus.UserUpdateStatus.Status.OFFLINE.toString()))) {
            userStatus = context.getResources().getString(R.string.last_seen_recently);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.LONG_TIME_AGO.toString())) {
            userStatus = context.getResources().getString(R.string.long_time_ago);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.LAST_MONTH.toString())) {
            userStatus = context.getResources().getString(R.string.last_month);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.LAST_WEEK.toString())) {
            userStatus = context.getResources().getString(R.string.last_week);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.RECENTLY.toString())) {
            userStatus = context.getResources().getString(R.string.recently);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.SUPPORT.toString())) {
            userStatus = context.getResources().getString(R.string.support);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.SERVICE_NOTIFICATIONS.toString())) {
            userStatus = context.getResources().getString(R.string.service_notification);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.ONLINE.toString())) {
            userStatus = context.getResources().getString(R.string.online);
        } else if (status.equals(ProtoGlobal.RegisteredUser.Status.EXACTLY.toString())) {
            userStatus = ProtoGlobal.RegisteredUser.Status.EXACTLY.toString();
        }
        return userStatus;
    }

    public static void rightFileThumbnailIcon(final ImageView view, ProtoGlobal.RoomMessageType messageType, @Nullable final RealmRoomMessage message) {

        RealmAttachment attachment = null;

        if (message != null) attachment = message.getAttachment();


        if (messageType != null) {
            switch (messageType) {
                case VOICE:
                    setImageDrawable(view, R.drawable.microphone_icon);
                    break;
                case AUDIO:
                case AUDIO_TEXT:
                    setImageDrawable(view, R.drawable.green_music_note);
                case FILE:
                case FILE_TEXT:

                    if (attachment != null) {
                        if (attachment.getName().toLowerCase().endsWith(".pdf")) {
                            setImageDrawable(view, R.drawable.pdf_icon);
                        } else if (attachment.getName().toLowerCase().endsWith(".txt")) {
                            setImageDrawable(view, R.drawable.txt_icon);
                        } else if (attachment.getName().toLowerCase().endsWith(".exe")) {
                            setImageDrawable(view, R.drawable.exe_icon);
                        } else if (attachment.getName().toLowerCase().endsWith(".docs")) {
                            setImageDrawable(view, R.drawable.docx_icon);
                        } else {
                            setImageDrawable(view, R.drawable.file_icon);
                        }
                    } else {
                        setImageDrawable(view, R.drawable.file_icon);
                    }

                    break;
                case LOCATION:
                    getAndSetPositionPicture(message, view);

                    break;
                default:
                    if (attachment != null) {
                        if (attachment.isFileExistsOnLocal()) {
                            G.imageLoader.displayImage(AndroidUtils.suitablePath(attachment.getLocalFilePath()), view);
                        } else if (attachment.isThumbnailExistsOnLocal()) {
                            G.imageLoader.displayImage(AndroidUtils.suitablePath(attachment.getLocalThumbnailPath()), view);
                        } else {
                            view.setVisibility(View.GONE);
                        }
                    } else {
                        view.setVisibility(View.GONE);
                    }
                    break;
            }
        }

    }

    private static void getAndSetPositionPicture(final RealmRoomMessage message, final ImageView view) {
        if (message.getLocation().getImagePath() != null) {
            G.imageLoader.displayImage(AndroidUtils.suitablePath(message.getLocation().getImagePath()), view);
        } else {

            FragmentMap.loadImageFromPosition(message.getLocation().getLocationLat(), message.getLocation().getLocationLong(), new FragmentMap.OnGetPicture() {
                @Override public void getBitmap(Bitmap bitmap) {

                    view.setImageBitmap(bitmap);

                    final String savedPath = FragmentMap.saveBitmapToFile(bitmap);

                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override public void execute(Realm realm) {
                            if (message.getLocation() != null) {
                                message.getLocation().setImagePath(savedPath);
                            }
                        }
                    });
                    realm.close();
                }
            });
        }
    }

    /**
     * update message status automatically
     *
     * @param view TextView message status
     */
    public static void rightMessageStatus(ImageView view, ProtoGlobal.RoomMessageStatus status, boolean isSenderMe) {

        if (view == null) {
            return;
        }
        if (!isSenderMe) {
            view.setVisibility(View.GONE);
            return;
        } else {
            view.setVisibility(View.VISIBLE);
        }
        switch (status) {
            case DELIVERED:

                setImageDrawable(view, R.drawable.ic_double_check);

                //DrawableCompat.setTint(view.getDrawable(), Color.BLACK);
                view.setColorFilter(Color.BLACK);
                break;
            case FAILED:
                setImageDrawable(view, R.drawable.ic_error);

                //                DrawableCompat.setTint(view.getDrawable().mutate(), Color.RED);
                view.setColorFilter(view.getContext().getResources().getColor(R.color.red));
                break;
            case SEEN:

                setImageDrawable(view, R.drawable.ic_double_check);
                view.setColorFilter(view.getContext().getResources().getColor(R.color.iGapColor));
                //final Drawable originalDrawable = view.getDrawable();
                //final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
                //DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.iGapColor)));
                break;
            case SENDING:
                //setImageDrawable(view, R.drawable.ic_clock);
                //                DrawableCompat.setTint(view.getDrawable().mutate(), Color.BLACK);
                view.setColorFilter(view.getContext().getResources().getColor(R.color.black_register));
                break;
            case SENT:
                setImageDrawable(view, R.drawable.ic_check);
                view.setColorFilter(view.getContext().getResources().getColor(R.color.black_register));
                //                DrawableCompat.setTint(view.getDrawable().mutate(), Color.BLACK);
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * update message status automatically
     *
     * @param view TextView message status
     */
    public static void rightMessageStatus(ImageView view, ProtoGlobal.RoomMessageStatus status, ProtoGlobal.RoomMessageType messageType, boolean isSenderMe) {
        if (view == null) {
            return;
        }
        if (!isSenderMe) {
            view.setVisibility(View.GONE);
            return;
        } else {
            view.setVisibility(View.VISIBLE);
        }
        switch (status) {
            case DELIVERED:
                setImageDrawable(view, R.drawable.ic_double_check);
                //DrawableCompat.setTint(view.getDrawable().mutate(), Color.BLACK);
                break;
            case FAILED:
                setImageDrawable(view, R.drawable.ic_error);
                if (messageType == ProtoGlobal.RoomMessageType.IMAGE || messageType == ProtoGlobal.RoomMessageType.VIDEO || messageType == ProtoGlobal.RoomMessageType.GIF) {
                    DrawableCompat.setTint(view.getDrawable().mutate(), Color.WHITE);
                } else {
                    DrawableCompat.setTint(view.getDrawable().mutate(), Color.RED);
                }
                break;
            case SEEN:
                setImageDrawable(view, R.drawable.ic_double_check);
                final Drawable originalDrawable = view.getDrawable();
                final Drawable wrappedDrawable = DrawableCompat.wrap(originalDrawable);
                DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(view.getContext().getResources().getColor(R.color.iGapColor)));
                break;
            case SENDING:
                setImageDrawable(view, R.drawable.ic_clock);
                if (messageType == ProtoGlobal.RoomMessageType.IMAGE || messageType == ProtoGlobal.RoomMessageType.VIDEO || messageType == ProtoGlobal.RoomMessageType.GIF) {
                    DrawableCompat.setTint(view.getDrawable().mutate(), Color.WHITE);
                } else {
                    DrawableCompat.setTint(view.getDrawable().mutate(), Color.BLACK);
                }
                break;
            case SENT:
                setImageDrawable(view, R.drawable.ic_check);
                if (messageType == ProtoGlobal.RoomMessageType.IMAGE || messageType == ProtoGlobal.RoomMessageType.VIDEO || messageType == ProtoGlobal.RoomMessageType.GIF) {
                    DrawableCompat.setTint(view.getDrawable().mutate(), Color.WHITE);
                } else {
                    DrawableCompat.setTint(view.getDrawable().mutate(), Color.BLACK);
                }
                break;
            default:
                view.setVisibility(View.GONE);
                break;
        }
    }

    private static void setImageDrawable(ImageView view, int res) {
        view.setImageDrawable(io.meness.github.messageprogress.AndroidUtils.getDrawable(G.currentActivity, res));

        // view.setImageResource(res);
    }


    public static long findLastMessageId(long roomId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<RealmRoomMessage> roomMessages = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, roomId).findAllSorted(RealmRoomMessageFields.MESSAGE_ID, Sort.ASCENDING);
        if (!roomMessages.isEmpty()) {
            return roomMessages.first().getMessageId();
        }
        realm.close();
        return 0;
    }

    public static String rightLastMessage(long roomId, Resources resources, ProtoGlobal.Room.Type roomType, RealmRoomMessage message, RealmAttachment attachment) {
        String messageText;
        if (message == null) {
            return null;
        }

        if (message.isDeleted()) {
            return resources.getString(R.string.deleted_message);
            //return computeLastMessage(roomId, resources, roomType, attachment);
        } else if (!TextUtils.isEmpty(message.getMessage())) {
            return message.getMessage();
        } else if (message.getForwardMessage() != null && !TextUtils.isEmpty(message.getForwardMessage().getMessage())) {
            return message.getForwardMessage().getMessage();
        } else if (message.getReplyTo() != null && !TextUtils.isEmpty(message.getReplyTo().getMessage())) {
            return message.getReplyTo().getMessage();
        } else {
            switch (message.getForwardMessage() == null ? message.getMessageType() : message.getForwardMessage().getMessageType()) {
                case AUDIO_TEXT:
                case AUDIO:
                    if (attachment == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.last_msg_format_chat, attachment.getName());
                    break;
                case CONTACT:
                    messageText = "contact"; // need to fill messageText with a String because in return check null
                    break;
                case FILE_TEXT:
                case FILE:
                    if (attachment == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.last_msg_format_chat, attachment.getName());
                    break;
                case GIF_TEXT:
                case GIF:
                    if (attachment == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.last_msg_format_chat, attachment.getName());
                    break;
                case IMAGE_TEXT:
                case IMAGE:
                    if (attachment == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.last_msg_format_chat, attachment.getName());
                    break;
                case LOCATION:
                    messageText = resources.getString(R.string.last_msg_format_chat, resources.getString(R.string.location_message));
                    break;
                case LOG:
                    messageText = resources.getString(R.string.last_msg_format_chat, message.getLogMessage());
                    // resources.getString(R.string.log_message));
                    break;
                case VIDEO_TEXT:
                case VIDEO:
                    if (attachment == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.last_msg_format_chat, attachment.getName());
                    break;
                case VOICE:
                    if (attachment == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.last_msg_format_chat, attachment.getName());
                    break;
                default:
                    messageText = null;
                    break;
            }
        }

        return messageText;
    }

    /**
     * fetch type of message for show in reply view
     *
     * @param realmRoomMessage for detect message type
     * @return final message text
     */
    public static String replyTextMessage(RealmRoomMessage realmRoomMessage, Resources resources) {
        RealmRoomMessage message = realmRoomMessage.getForwardMessage() == null ? realmRoomMessage : realmRoomMessage.getForwardMessage();
        String messageText = "";
        if (message != null) {
            switch (message.getMessageType()) {
                case TEXT:
                    if (message.getMessage() != null) {
                        messageText = message.getMessage();
                    }
                    break;
                case AUDIO_TEXT:
                case AUDIO:
                    if (message.getAttachment() == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.audio_message);
                    break;
                case CONTACT:
                    messageText = message.getRoomMessageContact().getFirstName() + "\n" + message.getRoomMessageContact().getLastPhoneNumber();
                    break;
                case FILE_TEXT:
                case FILE:
                    if (message.getAttachment() == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.file_message);
                    break;
                case GIF_TEXT:
                case GIF:
                    if (message.getAttachment() == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.gif_message);
                    break;
                case IMAGE_TEXT:
                case IMAGE:
                    if (message.getAttachment() == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.image_message);
                    break;
                case LOCATION:
                    messageText = resources.getString(R.string.location_message);
                    break;
                case VIDEO_TEXT:
                case VIDEO:
                    if (message.getAttachment() == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.video_message);
                    break;
                case VOICE:
                    if (message.getAttachment() == null) {
                        return null;
                    }
                    messageText = resources.getString(R.string.voice_message);
                    break;
                default:
                    messageText = "";
                    break;
            }
        }
        return messageText;
    }


    private static String computeLastMessage(final long roomId, Resources resources, ProtoGlobal.Room.Type roomType, RealmAttachment attachment) {
        Realm realm = Realm.getDefaultInstance();
        String lastMessage = "";
        RealmResults<RealmRoomMessage> realmList = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, roomId).findAllSorted(RealmRoomMessageFields.MESSAGE_ID, Sort.DESCENDING);
        for (RealmRoomMessage realmRoomMessage : realmList) {
            if (realmRoomMessage != null && !realmRoomMessage.isDeleted()) {
                lastMessage = AppUtils.rightLastMessage(roomId, resources, roomType, realmRoomMessage, attachment);
                break;
            }
        }
        realm.close();
        return lastMessage;
    }

    public static long computeLastMessageTime(final long roomId) {
        Realm realm = Realm.getDefaultInstance();
        long lastMessageTime = 0;
        RealmResults<RealmRoomMessage> realmList = realm.where(RealmRoomMessage.class).equalTo(RealmRoomMessageFields.ROOM_ID, roomId).findAllSorted(RealmRoomMessageFields.MESSAGE_ID, Sort.DESCENDING);
        for (RealmRoomMessage realmRoomMessage : realmList) {
            if (realmRoomMessage != null && !realmRoomMessage.isDeleted()) {
                lastMessageTime = realmRoomMessage.getUpdateOrCreateTime();
                break;
            }
        }
        realm.close();
        return lastMessageTime;
    }

    public static MaterialDialog.Builder buildResendDialog(Context context, int failedMessagesCount, final IResendMessage listener) {
        List<String> items = new ArrayList<>();
        List<Integer> itemsId = new ArrayList<>();
        items.add(context.getString(R.string.resend_chat_message));
        itemsId.add(0);
        if (failedMessagesCount > 1) {
            items.add(String.format(context.getString(R.string.resend_all_messages), failedMessagesCount));
            itemsId.add(1);
        }
        items.add(context.getString(R.string.delete_item_dialog));
        itemsId.add(2);

        int[] newIds = new int[itemsId.size()];
        for (Integer integer : itemsId) {
            newIds[itemsId.indexOf(integer)] = integer;
        }

        return new MaterialDialog.Builder(context).title("Resend Messages").negativeText(context.getString(R.string.cancel)).items(items).itemsIds(newIds).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                switch (itemView.getId()) {
                    case 0:
                        listener.resendMessage();
                        break;
                    case 1:
                        listener.resendAllMessages();
                        break;
                    case 2:
                        listener.deleteMessage();
                        break;
                }
            }
        });
    }

    public static String humanReadableDuration(double d) {
        String output = Double.toString(d);

        if (output.contains(".")) {
            String[] split = output.split("\\.");
            if (split[1].length() > 2) {
                output = split[0] + "." + split[1].charAt(0) + split[1].charAt(1);
            }
        }

        return output;
    }
}
