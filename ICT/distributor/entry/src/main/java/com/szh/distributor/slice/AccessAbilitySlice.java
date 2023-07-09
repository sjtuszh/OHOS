package com.szh.distributor.slice;

import com.szh.distributor.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class AccessAbilitySlice extends AbilitySlice {
    Button login_btn;
    Button back_btn;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_access);
        login_btn = (Button) findComponentById(ResourceTable.Id_login_room_btn);
        back_btn = (Button) findComponentById(ResourceTable.Id_back_btn);

        login_btn.setClickedListener((Component component) -> {

        });

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
}
