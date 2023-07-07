package com.szh.p3D.slice;

import com.szh.p3D.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        TableLayout parentLayout = (TableLayout)  findComponentById(ResourceTable.Id_tableLayout_players);


        for (int i=0;i<4;i++) {
            Component childLayout = (DirectionalLayout) LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_layout_player, null, false);

            parentLayout.addComponent(childLayout);
        }




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
