package com.szh.distributor.slice;

import com.szh.distributor.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.render.opengl.Utils;
import ohos.agp.utils.TextAlignment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice {
    static final HiLogLabel ability = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MainAbilitySlice");
    private TabList mtabList;

    // PagerSlider对象
    private PageSlider mPagerSlider;
    // 需要PagerSlider对象管理的用户界面列表
    private ArrayList<Component> mPageview;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        // 获取PagerSlider对象
        mPagerSlider = (PageSlider) findComponentById(ResourceTable.Id_pager_slider);

        // 创建PagerSlider所需要承载界面的列表
        mPageview = new ArrayList<Component>();
        mPageview.add(generateTextComponent("第1个界面"));
        mPageview.add(generateTextComponent("第2个界面"));
        mPageview.add(generateTextComponent("第3个界面"));
        // 实例化PageSliderProvider，为PagerSlider提供界面
        mPagerSlider.setProvider(new PageSliderProvider() {
            @Override
            public int getCount() {
                // 界面数量
                return mPageview.size();
            }

            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int i) {
                HiLog.info(ability,String.format("createPageInContainer%d",i));
                // 添加用户界面
                componentContainer.addComponent(mPageview.get(i));
                return mPageview.get(i);
            }

            @Override
            public void destroyPageFromContainer(ComponentContainer componentContainer, int i, Object o) {
                HiLog.info(ability,String.format("destroyPageFromContainer%d",i));
                // 添加用户界面
                componentContainer.removeComponent(mPageview.get(i));
            }

            @Override
            public boolean isPageMatchToObject(Component component, Object o) {
                // 判断createPageInContainer创建的界面与当前界面是否相同
                return component == o;
            }
        });
        //先进入设备界面
        mPagerSlider.setCurrentPage(1);

        mPagerSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {

            }

            @Override
            public void onPageSlideStateChanged(int i) {

            }

            @Override
            public void onPageChosen(int i) {
                //同步tablist
                mtabList.selectTabAt(i);
            }
        });

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

        mtabList.addTabSelectedListener(new TabList.TabSelectedListener() {
            @Override
            public void onSelected(TabList.Tab tab) {
                HiLog.info(ability,String.format("onSlected%d",tab.getTag()));
                mPagerSlider.setCurrentPage((int)tab.getTag());
            }

            @Override
            public void onUnselected(TabList.Tab tab) {

            }

            @Override
            public void onReselected(TabList.Tab tab) {

            }
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


    private Text generateTextComponent(String content) {
        Text text = new Text(this);
        text.setLayoutConfig(new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setText(content);
        text.setTextSize(80);
        return text;
    }
}

