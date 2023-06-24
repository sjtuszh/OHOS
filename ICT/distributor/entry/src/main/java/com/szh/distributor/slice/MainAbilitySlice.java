package com.szh.distributor.slice;

import com.szh.distributor.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice {
    static final HiLogLabel ability = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MainAbilitySlice");
    private TabList mtabList;


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);


        //初始化TabList
        mtabList = (TabList) findComponentById(ResourceTable.Id_tab_list);
        HiLog.info(ability,"初始化TabList");

        mtabList.setFixedMode(true);
        String[] tabListTags = {"设备", "应用", "个人中心"};
        for (int ntag=0;ntag<=2;ntag++) {
            TabList.Tab tab = mtabList.new Tab(this);
            tab.setText(tabListTags[ntag]);
            tab.setPadding(12, 0, 12, 0);
            tab.setTag(ntag);
            mtabList.addTab(tab);
        }
        mtabList.selectTabAt(1);


    }

    @Override
    public void onActive() {
        super.onActive();

    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }


}

