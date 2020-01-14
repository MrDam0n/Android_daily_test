###添加Quick setting进入单手模式
+ 在JoySystemUI代码的com.android.systemui.qs.tiles包下，模仿其他例子创建一个extends QSTileImpl<QSTile.BooleanState>的类
```
package com.android.systemui.qs.tiles;

import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.android.systemui.R;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.tileimpl.QSTileImpl;

import javax.inject.Inject;

public class OnehandmodeTile extends QSTileImpl<QSTile.BooleanState> {
    private final Icon mIcon = ResourceIcon.get(R.drawable.ic_qs_onehandedmode);
    private KeyguardManager mKeyguardManager = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
    private ContentResolver mCr;
    @Inject
    protected OnehandmodeTile(QSHost host) {
        super(host);
        mCr = mContext.getContentResolver();
        ContentObserver mOneHandEnableObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                refreshState();
            }
        };
        mCr.registerContentObserver(Settings.System.getUriFor("one_handed_enabled"), false, mOneHandEnableObserver);
    }

    @Override
    public BooleanState newTileState() {
        return new BooleanState();
    }

    @Override
    protected void handleClick() {
        boolean isKeyguardLocked = mKeyguardManager.isKeyguardLocked();
        if(isKeyguardLocked){
              Log.d(TAG,"isKeyguardLocked:"+isKeyguardLocked);
            return;
        }
        final boolean activated = !mState.value;
        Log.d(TAG, "activated:" + activated);
        Settings.System.putInt(mContext.getContentResolver(), "quick_settings_onehand", activated ? 1 : 0);
        refreshState(activated);
        mState.value = !mState.value;
        //Begin add by zhongtao.lin for auto close status bar on 2020/01/03
        mHost.collapsePanels();
        //End add by zhongtao.lin for auto close status bar on 2020/01/03
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        boolean isInOneHandMode = Settings.System.getInt(mContext.getContentResolver(), "quick_settings_onehand", 0) == 1;
        Log.d(TAG, "quick_settings_onehand:" + isInOneHandMode);
        state.value = isInOneHandMode;
        state.label = getTileLabel();
        state.icon = mIcon;
        state.state = isInOneHandMode ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE;
    }

    @Override
    public int getMetricsCategory() {
        return 0;
    }

    @Override
    public Intent getLongClickIntent() {
        return null;
    }

    @Override
    protected void handleSetListening(boolean listening) {

    }

    @Override
    public CharSequence getTileLabel() {
        return mContext.getString(R.string.quick_settings_onehandmode_label);
    }

    //Begin added by zhongtao.lin for XR8743827 on 2020/01/02
    @Override
    protected void handleLongClick() {
        mHost.collapsePanels();
        ComponentName setting= new ComponentName("com.tct", "com.tct.onehandmode.OneHandModeActivity");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        //Calling startActivity() from outside of an Activity context requires the FLAG_ACTIVITY_NEW_TASK flag
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(setting);
        mContext.startActivity(intent);
    }
    //End added by zhongtao.lin for XR8743827 on 2020/01/02
}
```
+ 在com.android.systemui.qs.tileimpl包的QSFactoryImpl中，仿照其他例子，添加private final Provider<OnehandmodeTile> mOnehandmodeTileProvider及初始化等
+ 在com.android.systemui.util的Utils中QS_DEFAULT_TILES添加“onehandmode”
+ res文件的string.xml中添加tile显示的字符串<string name="quick_settings_onehandmode_label">One-handed mode</string>
