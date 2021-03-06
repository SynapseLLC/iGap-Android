/*
* This is the source code of iGap for Android
* It is licensed under GNU AGPL v3.0
* You should have received a copy of the license in this archive (see LICENSE).
* Copyright © 2017 , iGap - www.iGap.net
* iGap Messenger | Free, Fast and Secure instant messaging application
* The idea of the RooyeKhat Media Company - www.RooyeKhat.co
* All rights reserved.
*/

package com.iGap.response;

import com.iGap.G;
import com.iGap.proto.ProtoChannelRevokeLink;
import com.iGap.proto.ProtoError;

public class ChannelRevokeLinkResponse extends MessageHandler {

    public int actionId;
    public Object message;
    public String identity;

    public ChannelRevokeLinkResponse(int actionId, Object protoClass, String identity) {
        super(actionId, protoClass, identity);

        this.message = protoClass;
        this.actionId = actionId;
        this.identity = identity;
    }

    @Override
    public void handler() {
        super.handler();

        ProtoChannelRevokeLink.ChannelRevokeLinkResponse.Builder builder = (ProtoChannelRevokeLink.ChannelRevokeLinkResponse.Builder) message;
        if (G.onChannelRevokeLink != null) {
            G.onChannelRevokeLink.onChannelRevokeLink(builder.getRoomId(), builder.getInviteLink(), builder.getInviteToken());
        }
    }

    @Override
    public void timeOut() {
        super.timeOut();
    }

    @Override
    public void error() {
        super.error();
        ProtoError.ErrorResponse.Builder errorResponse = (ProtoError.ErrorResponse.Builder) message;
        int majorCode = errorResponse.getMajorCode();
        int minorCode = errorResponse.getMinorCode();
        if (G.onChannelRevokeLink != null) {
            G.onChannelRevokeLink.onError(majorCode, minorCode);
        }
    }
}


