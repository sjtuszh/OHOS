package com.szh.distributor.slice;

import com.szh.distributor.ResourceTable;
import com.szh.distributor.components.Utils;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.ability.continuation.*;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.bundle.ElementName;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.distributedschedule.interwork.IInitCallback;
import ohos.hiviewdfx.Debug;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.*;

public class AccessAbilitySlice extends AbilitySlice {
    Button device_btn;
    Button start_btn;
    Button connect_btn;
    Button back_btn;


    // 当前应用包名
    private String BUNDLE_NAME = "com.szh.distributor";
    // 流转应用包名
    private String REMOTE_BUNDLE_NAME = "com.szh.p3D";
    // 流转FA名称
    private String REMOTE_FA_NAME = "com.szh.p3D.MainAbility";
    // 流转PA名称
    private String REMOTE_PA_NAME = "com.szh.p3D.UsrServiceAbility";
    // 注册流转任务管理服务后返回的Ability token
    private int abilityToken;
    // 用户在设备列表中选择设备后返回的设备ID
    private String selectDeviceId;
    // 用户是否已发起可拉回流转流程
    private boolean isReversibly = false;
    // 获取流转任务管理服务管理类
    private IContinuationRegisterManager continuationRegisterManager;
    // 设置初始化分布式环境的回调
    private IInitCallback iInitCallback = new IInitCallback() {
        @Override
        public void onInitSuccess(String deviceId) {
            Utils.log( "device id success: " + deviceId);
        }

        @Override
        public void onInitFailure(String deviceId, int errorCode) {
            Utils.log("device id failed: " + deviceId + "errorCode: " + errorCode);
        }
    };

    // 设置流转任务管理服务设备状态变更的回调
    private IContinuationDeviceCallback callback = new IContinuationDeviceCallback() {
        @Override
        public void onDeviceConnectDone(String s, String s1) {
        }

        @Override
        public void onDeviceDisconnectDone(String s) {
        }

        @Override
        public void onConnected(ContinuationDeviceInfo deviceInfo) {
            // 在用户选择设备后设置设备ID
            selectDeviceId = deviceInfo.getDeviceId();
            try {
                // 初始化分布式环境
                DeviceManager.initDistributedEnvironment(selectDeviceId, iInitCallback);
            } catch (RemoteException e) {
                Utils.log("initDistributedEnvironment failed");
            }
            //更新选择设备后的流转状态
            continuationRegisterManager.updateConnectStatus(abilityToken, selectDeviceId, DeviceConnectState.CONNECTED.getState(), null);
        }
    };

    // 设置注册流转任务管理服务回调
    private RequestCallback requestCallback = new RequestCallback() {
        @Override
        public void onResult(int result) {
            abilityToken = result;
        }
    };


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_access);
        Utils.log("onStart the AccessAbilitySlice");
        continuationRegisterManager = getContinuationRegisterManager();

        start_btn = (Button) findComponentById(ResourceTable.Id_start_btn);
        start_btn.setClickedListener(mStartUsrServer);

        mRegisterListener();



        device_btn = (Button) findComponentById(ResourceTable.Id_device_btn);
        device_btn.setClickedListener(mShowDeviceListListener);
        //连接TV的UsrService
        connect_btn = (Button) findComponentById(ResourceTable.Id_connect_btn);
        connect_btn.setClickedListener(mConnectRemotePAListener);



        back_btn = (Button) findComponentById(ResourceTable.Id_back_btn);

    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 解注册流转任务管理服务
        continuationRegisterManager.unregister(abilityToken, null);
        // 断开流转任务管理服务连接
        continuationRegisterManager.disconnect();
        super.onStop();
    }

    // 注册流转任务管理服务
    private void mRegisterListener() {
        Utils.showToast(getContext(),"已注册");
        //增加过滤条件
        ExtraParams params = new ExtraParams();
        String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE};
        params.setDevType(devTypes);
//        String jsonParams = "{'filter':{'commonFilter':{'system':{'harmonyVersion':'2.0.0'},'groupType':'1|256','curComType': 0x00030004,'transferScene':0,'remoteAuthenticationDescription': '拉起HiVision扫描弹框描述','remoteAuthenticationPicture':''}";
//        params.setJsonParams(jsonParams);
        continuationRegisterManager.register(BUNDLE_NAME, params, callback, requestCallback);
    }

    // 显示设备列表，获取设备信息
    private Component.ClickedListener mShowDeviceListListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {

            // 设置过滤设备类型
            ExtraParams params = new ExtraParams();
            String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE};
            params.setDevType(devTypes);
//            String jsonParams = "{'filter':{'commonFilter':{'system':{'harmonyVersion':'2.0.0'},'groupType':'1|256','curComType': 0x00030004,'transferScene':0,'remoteAuthenticationDescription': '拉起HiVision扫描弹框描述','remoteAuthenticationPicture':''}";
//            params.setJsonParams(jsonParams);
            // 显示选择设备列表
            continuationRegisterManager.showDeviceList(abilityToken, params, null);
        }
    };

    // 用于管理连接关系
    private IAbilityConnection mConn = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName element, IRemoteObject remote, int resultCode) {
            Utils.log("connect完成");

//            mProxy = new MyRemoteProxy(remote);
//            connect_btn.setText("connectRemoteAbility done");
        }

        @Override
        public void onAbilityDisconnectDone(ElementName element, int resultCode) {

        }
    };


    //启动远程  PA
    private Component.ClickedListener mStartUsrServer = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
            Intent intentStartUsr = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId(selectDeviceId)
                    .withBundleName(REMOTE_BUNDLE_NAME)
                    .withAbilityName(REMOTE_FA_NAME)
                    .build();
            intentStartUsr.setOperation(operation);
            startAbility(intentStartUsr);
            Utils.log("已启动远程ABILITY");
        }
    };
    // 连接远程PA
    private Component.ClickedListener mConnectRemotePAListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
            Utils.log("请求连接UsrService");
            if (selectDeviceId != null) {
                // 指定待连接PA的bundleName和abilityName
                // 设置分布式标记，表明当前涉及分布式能力
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId(selectDeviceId)
                        .withBundleName(REMOTE_BUNDLE_NAME)
                        .withAbilityName(REMOTE_PA_NAME)
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
                Intent connectPAIntent = new Intent();
                connectPAIntent.setParam("distributorINFO","这是手机distributor传过来的intent");
                connectPAIntent.setOperation(operation);
                // 通过AbilitySlice包含的connectAbility接口实现跨设备连接PA
                connectAbility(connectPAIntent, mConn);
                Utils.showToast(getContext(),"已连接TVservice");
            }
        }
    };

    // 控制已连接PA执行加法
    private Component.ClickedListener mControlPAListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
//            if (mProxy != null) {
//                int ret = -1;
//                try {
//                    ret = mProxy.plus(10, 20);
//                } catch (RemoteException e) {
//
//                }
//                start_btn.setText("ControlRemotePA result = " + ret);
//            }
        }
    };


}
