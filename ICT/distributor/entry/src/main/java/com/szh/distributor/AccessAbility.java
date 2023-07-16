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
    private static final HiLogLabel ACCESS_ABILITY = new HiLogLabel(HiLog.LOG_APP,0x00102,"AccessAbility");
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(AccessAbilitySlice.class.getName());


    }

    @Override
    public boolean onStartContinuation() {
        return false;
    }

    @Override
    public boolean onSaveData(IntentParams intentParams) {
        return false;
    }

    @Override
    public boolean onRestoreData(IntentParams intentParams) {
        return false;
    }

    @Override
    public void onCompleteContinuation(int i) {

    }

    @Override
    public void onRemoteTerminated() {
        IAbilityContinuation.super.onRemoteTerminated();
    }

    @Override
    public void onFailedContinuation(int errorCode) {
        IAbilityContinuation.super.onFailedContinuation(errorCode);
    }
}
