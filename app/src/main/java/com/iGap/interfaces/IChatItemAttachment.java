/*
* This is the source code of iGap for Android
* It is licensed under GNU AGPL v3.0
* You should have received a copy of the license in this archive (see LICENSE).
* Copyright © 2017 , iGap - www.iGap.net
* iGap Messenger | Free, Fast and Secure instant messaging application
* The idea of the RooyeKhat Media Company - www.RooyeKhat.co
* All rights reserved.
*/

package com.iGap.interfaces;

import android.support.v7.widget.RecyclerView;
import com.iGap.module.enums.LocalFileType;

public interface IChatItemAttachment<VH extends RecyclerView.ViewHolder> {
    void onLoadThumbnailFromLocal(VH holder, String localPath, LocalFileType fileType);

    void onPlayPauseGIF(VH holder, String localPath);

    /**
     * add this prt for video player
     */
    //void onPlayPauseVideo(VH holder, String localPath, int isHide, double time);
}
