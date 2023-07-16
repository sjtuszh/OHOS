package com.szh.p3D;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.LocalRemoteObject;
import ohos.aafwk.content.Intent;
import ohos.event.notification.NotificationRequest;
import ohos.rpc.*;

public class UsrServiceAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        Utils.showToast(getContext(), "已启动UsrService");
        Utils.log("UsrService onStart");
        super.onStart(intent);
        //普通文本通知内容
        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent()
                .setTitle("UsrService")
                .setText("该Service常驻后台");
        //创建通知对象
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        NotificationRequest request = new NotificationRequest(1001)
                .setContent(notificationContent);
        keepBackgroundRunning(1001,request);
    }

    @Override
    public void onBackground() {

        super.onBackground();
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.log("UsrServiceAbility::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
        Utils.showToast(getContext(),"UsrService已接入");
    }


    @Override
    public IRemoteObject onConnect(Intent intent) {
        super.onConnect(intent);

        Utils.log(String.format("Usr连接"+intent.getStringParam("distributorINFO")));
        Utils.showToast(getContext(),"onConnect");
        UsrRemote remote_plus = new UsrRemote();

        return remote_plus;

    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    public class UsrRemote extends RemoteObject{
        private static final String DESCRIPTOR = "com.szh.DESCRIPTOR";
        public UsrRemote() {
            super(DESCRIPTOR);
        }

        public void UsrRemote(){
            Utils.log("myremote创建");
        }
    }


}