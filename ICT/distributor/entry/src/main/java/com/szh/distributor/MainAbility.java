package com.szh.distributor;

import com.szh.distributor.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends Ability {
    private static final HiLogLabel MAIN = new HiLogLabel(HiLog.LOG_APP,0x001100,"MainAbility");
    @Override
    public void onStart(Intent intent) {
        HiLog.info(MAIN,"APPonStart");
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }
}
