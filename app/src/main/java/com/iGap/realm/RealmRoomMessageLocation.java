/*
* This is the source code of iGap for Android
* It is licensed under GNU AGPL v3.0
* You should have received a copy of the license in this archive (see LICENSE).
* Copyright © 2017 , iGap - www.iGap.net
* iGap Messenger | Free, Fast and Secure instant messaging application
* The idea of the RooyeKhat Media Company - www.RooyeKhat.co
* All rights reserved.
*/


package com.iGap.realm;

import com.iGap.module.SUID;
import com.iGap.proto.ProtoGlobal;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmRoomMessageLocationRealmProxy;
import io.realm.annotations.PrimaryKey;
import org.parceler.Parcel;

@Parcel(implementations = {RealmRoomMessageLocationRealmProxy.class},
        value = Parcel.Serialization.BEAN,
        analyze = {RealmRoomMessageLocation.class})
public class RealmRoomMessageLocation extends RealmObject {
    private double locationLat;
    private double locationLong;
    private String imagePath;
    @PrimaryKey
    private long id;

    public static RealmRoomMessageLocation build(final ProtoGlobal.RoomMessageLocation input, Long id) {
        Realm realm = Realm.getDefaultInstance();

        RealmRoomMessageLocation messageLocation = null;

        if (id != null) {
            messageLocation = realm.where(RealmRoomMessageLocation.class).equalTo(RealmRoomMessageLocationFields.ID, id).findFirst();
        }

        if (messageLocation == null) {
            messageLocation = realm.createObject(RealmRoomMessageLocation.class, SUID.id().get());
        }


        messageLocation.setLocationLat(input.getLat());
        messageLocation.setLocationLong(input.getLon());

        realm.close();

        return messageLocation;
    }

    @Override
    public String toString() {
        return Double.toString(getLocationLat()) + "," + Double.toString(getLocationLong());
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
