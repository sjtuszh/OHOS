package com.szh.distributor;

import com.szh.distributor.slice.AccessAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.IAbilityContinuation;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/*这个界面是体感软件打开后，向TV请求数据 */
public class AccessAbility extends Ability implements IAbilityContinuation {
    private static final int DOMAIN_ID = 0x00301;
    private static final HiLogLabel ABILITY_CONTI = new HiLogLabel(3, DOMAIN_ID, "MainAbility");
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AccessAbilitySlice.class.getName());
        HiLog.info(ABILITY_CONTI,"AccessAbilityStarted");


    }
    @Override
    public boolean onStartContinuation() {
        HiLog.info(ABILITY_CONTI, "onStartContinuation called");
        return true;
    }

    @Override
    public boolean onSaveData(IntentParams saveData) {
        HiLog.info(ABILITY_CONTI, "onSaveData called");
        return true;
    }

    @Override
    public boolean onRestoreData(IntentParams restoreData) {
        HiLog.info(ABILITY_CONTI, "onRestoreData called");
        return true;
    }

    @Override
    public void onCompleteContinuation(int result) {
        HiLog.info(ABILITY_CONTI, "onCompleteContinuation called");
    }
}
