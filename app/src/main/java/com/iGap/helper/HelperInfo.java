/*
 * This is the source code of iGap for Android
 * It is licensed under GNU AGPL v3.0
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright © 2017 , iGap - www.iGap.net
 * iGap Messenger | Free, Fast and Secure instant messaging application
 * The idea of the RooyeKhat Media Company - www.RooyeKhat.co
 * All rights reserved.
*/


package com.iGap.helper;

import com.iGap.realm.RealmRegisteredInfo;
import com.iGap.realm.RealmRegisteredInfoFields;
import com.iGap.realm.RealmRoom;
import com.iGap.realm.RealmRoomFields;
import com.iGap.request.RequestClientGetRoom;
import com.iGap.request.RequestUserInfo;
import io.realm.Realm;

public class HelperInfo {

    /**
     * compare user cacheId , if was equal don't do anything
     * otherwise send request for get user info
     *
     * @param userId userId for get old cacheId from RealmRegisteredInfo
     * @param cacheId new cacheId
     * @return return true if need update otherwise return false
     */

    public static boolean needUpdateUser(long userId, String cacheId) {

        Realm realm = Realm.getDefaultInstance();
        RealmRegisteredInfo realmRegisteredInfo = realm.where(RealmRegisteredInfo.class).equalTo(RealmRegisteredInfoFields.ID, userId).findFirst();

        if (realmRegisteredInfo != null && cacheId != null && realmRegisteredInfo.getCacheId().equals(cacheId)) {
            realm.close();
            return false;
        }
        new RequestUserInfo().userInfo(userId);

        realm.close();
        return true;
    }

    /**
     * if room isn't exist get info from server
     */
    public static boolean needUpdateRoomInfo(long roomId) {
        Realm realm = Realm.getDefaultInstance();
        RealmRoom realmRoom = realm.where(RealmRoom.class).equalTo(RealmRoomFields.ID, roomId).findFirst();
        if (realmRoom != null) {
            return false;
        }
        new RequestClientGetRoom().clientGetRoom(roomId, RequestClientGetRoom.CreateRoomMode.justInfo);

        realm.close();
        return true;
    }

}
