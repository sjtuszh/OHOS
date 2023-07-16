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
import ohos.agp.components.IndexBar;
import ohos.bundle.ElementName;
import ohos.distributedschedule.interwork.DeviceManager;
import ohos.distributedschedule.interwork.IInitCallback;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;

public class AccessAbilitySlice extends AbilitySlice {
    private static final int DOMAIN_ID = 0x00101;
    private static final HiLogLabel accessABILITY_SLICE = new HiLogLabel(3, DOMAIN_ID, "MainAbilitySlice");
    Button login_btn;
    Button back_btn;


    private Button btnShowDeviceList;
    private Button btnStartRemote;
    private Button btnStopRemote;
    private Button btnConnectRemotePA;
    private Button btnControlRemotePA;
    private Button btnDisconnectRemotePA;


    // 当前应用包名
    private String BUNDLE_NAME = "com.szh.distributor";
    // 流转应用包名
    private String REMOTE_BUNDLE_NAME = "com.szh.p3D";
    // 流转FA名称
    private String REMOTE_FA_NAME = "com.szh.p3D.MainAbility";
    private String REMOTE_PA_NAME = "com.szh.p3D.UsrServiceAbility";
    // 流转PA名称
//    private String REMOTE_PA_NAME = "XXX.XXX.XXX.XXXAbility";
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
            HiLog.info(accessABILITY_SLICE, "device id success: " + deviceId);
        }

        @Override
        public void onInitFailure(String deviceId, int errorCode) {
            HiLog.info(accessABILITY_SLICE, "device id failed: " + deviceId + "errorCode: " + errorCode);
        }
    };
    // 设置流转任务管理服务设备状态变更的回调
    private IContinuationDeviceCallback callback = new IContinuationDeviceCallback() {
        @Override
        public void onDeviceConnectDone(String s, String s1) {}

        @Override
        public void onConnected(ContinuationDeviceInfo deviceInfo) {
            // 在用户选择设备后设置设备ID
            selectDeviceId = deviceInfo.getDeviceId();
            try {
                // 初始化分布式环境
                DeviceManager.initDistributedEnvironment(selectDeviceId, iInitCallback);
            } catch (RemoteException e) {
                HiLog.info(accessABILITY_SLICE, "initDistributedEnvironment failed");
            }
            //更新选择设备后的流转状态
            continuationRegisterManager.updateConnectStatus(abilityToken, selectDeviceId, DeviceConnectState.CONNECTED.getState(), null);
        }

        @Override
        public void onDeviceDisconnectDone(String s) {}

        @Override
        public void onDisconnected(String deviceId) {}
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
        //流转注册管理器
        continuationRegisterManager = getContinuationRegisterManager();

        mRegister();

        btnShowDeviceList = (Button) findComponentById(ResourceTable.Id_device_btn);
        btnShowDeviceList.setClickedListener(mShowDeviceListListener);

        btnStartRemote = (Button) findComponentById(ResourceTable.Id_start_btn);
        btnStartRemote.setClickedListener(mStartRemoteListener);
        HiLog.info(accessABILITY_SLICE,"按钮创建完成");



        back_btn = findComponentById(ResourceTable.Id_back_btn);
        back_btn.setClickedListener((Component component)->{
            terminate();
        });
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
    }


    private void mRegister(){
        HiLog.info(accessABILITY_SLICE, "register call.");
            //增加过滤条件
        ExtraParams params = new ExtraParams();
        String[] devTypes = new String[]{ExtraParams.DEVICETYPE_SMART_PAD, ExtraParams.DEVICETYPE_SMART_PHONE};
        params.setDevType(devTypes);

//      String jsonParams = "{'filter':{'commonFilter':{'system':{'harmonyVersion':'3.0.0'},'groupType':'1|256','curComType': 0x00030004,'faFilter':'{\"localVersionCode\":1,\"localMinCompatibleVersionCode\":2,\"targetBundleName\": \"com.szh.distributor\"}'}},'transferScene':0,'remoteAuthenticationDescription': '拉起HiVision扫描弹框描述','remoteAuthenticationPicture':''}";
//      arams.setJsonParams(jsonParams);
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

//            String jsonParams = "{'filter':{'commonFilter':{'system':{'harmonyVersion':'3.0.0'},'groupType':'1|256','curComType': 0x00030004,'faFilter':'{\"localVersionCode\":1,\"localMinCompatibleVersionCode\":2,\"targetBundleName\": \"com.szh.distributor\"}'}},'transferScene':0,'remoteAuthenticationDescription': '拉起HiVision扫描弹框描述','remoteAuthenticationPicture':''}";
//            params.setJsonParams(jsonParams);
            // 显示选择设备列表
            continuationRegisterManager.showDeviceList(abilityToken, params, null);
        }
    };

    // 启动远程FA/PA
    private Component.ClickedListener mStartRemoteListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
            if (selectDeviceId != null) {
                // 通过showDeviceList获取指定目标设备deviceId
                // 指定待启动FA/PA的bundleName和abilityName
                // 设置分布式标记，表明当前涉及分布式能力
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId(selectDeviceId)
                        .withBundleName(REMOTE_BUNDLE_NAME)
                        .withAbilityName(REMOTE_PA_NAME)
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
                Intent startIntent = new Intent();
                startIntent.setOperation(operation);
                // 通过AbilitySlice包含的startAbility接口实现跨设备启动FA/PA
                startAbility(startIntent);
            } else {
                btnStartRemote.setText("StartRemote selectDeviceId is null");
            }
        }
    };

    // 关闭远程PA/FA
    private Component.ClickedListener mStopRemoteListener = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
            if (selectDeviceId != null) {
                // 通过showDeviceList获取指定目标设备deviceId
                // 指定待关闭PA的bundleName和abilityName
                // 设置分布式标记，表明当前涉及分布式能力
                Operation operation = new Intent.OperationBuilder()
                        .withDeviceId(selectDeviceId)
                        .withBundleName(REMOTE_BUNDLE_NAME)
                        .withAbilityName(REMOTE_FA_NAME)
                        .withFlags(Intent.FLAG_ABILITYSLICE_MULTI_DEVICE)
                        .build();
                Intent stopIntent = new Intent();
                stopIntent.setOperation(operation);
                // 通过AbilitySlice包含的stopAbility接口实现跨设备关闭PA
                stopAbility(stopIntent);
            } else {
                btnStopRemote.setText("StopRemote selectDeviceId is null");
            }
        }
    };



}
