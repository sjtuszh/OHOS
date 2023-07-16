package com.szh.p3D.slice;

import com.szh.p3D.Utils;
import com.szh.p3D.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.IAbilityConnection;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.components.*;
import ohos.bundle.ElementName;
import ohos.rpc.IRemoteObject;

public class MainAbilitySlice extends AbilitySlice {
    Button starUsr_btn;
    Button connectUsr_btn;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        TableLayout parentLayout = (TableLayout)  findComponentById(ResourceTable.Id_tableLayout_players);


        for (int i=0;i<4;i++) {
            Component childLayout = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_player, null, false);

            parentLayout.addComponent(childLayout);
        }

        starUsr_btn = (Button) findComponentById(ResourceTable.Id_startUsr_btn);
        starUsr_btn.setClickedListener(mStartUsrServer);

        connectUsr_btn = findComponentById(ResourceTable.Id_connectUsr_btn);
        connectUsr_btn.setClickedListener(mconnectUsrServer);

    }



    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
    private Component.ClickedListener mStartUsrServer = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
            Intent intentStartUsr = new Intent();
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.szh.p3D")
                    .withAbilityName("com.szh.p3D.UsrServiceAbility")
                    .build();
            intentStartUsr.setOperation(operation);
            startAbility(intentStartUsr);



        }
    };
    private Component.ClickedListener mconnectUsrServer = new Component.ClickedListener() {
        @Override
        public void onClick(Component arg0) {
            Intent intentconnectUsr = new Intent();
            intentconnectUsr.setParam("distributorINFO","这是distributor传过来的intent");
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.szh.p3D")
                    .withAbilityName("com.szh.p3D.UsrServiceAbility")
                    .build();
            intentconnectUsr.setOperation(operation);
            connectAbility(intentconnectUsr,mConn);


        }
    };

    private IAbilityConnection mConn = new IAbilityConnection() {
        @Override
        public void onAbilityConnectDone(ElementName elementName, IRemoteObject iRemoteObject, int i) {
            Utils.log("连接完成");

        }

        @Override
        public void onAbilityDisconnectDone(ElementName elementName, int i) {

        }
    };


}
