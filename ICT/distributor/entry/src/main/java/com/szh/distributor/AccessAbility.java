package com.szh.distributor;

import com.szh.distributor.slice.AccessAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

/*这个界面是体感软件打开后，向TV请求数据 */
public class AccessAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AccessAbilitySlice.class.getName());


    }
}
