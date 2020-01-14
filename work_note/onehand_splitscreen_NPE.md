### 单手设置页面进入分屏点击button会重启
- Preference#onBindView()
分屏时RadioGroupPreference页面上暂时不会显示出来，需要往下滚动一下看到RadioGroup部分才会执行onBindView方法。而mScaleGroup是在onBinderView方法中初始化的，所以报了空指针异常。
```
package com.tct.onehandmode;
......
public class RadioGroupPreference extends Preference{
    private static final String TAG = "RadioGroupPreference";

    private RadioGroup mScaleGroup;
    .......

    @Override
    protected void onBindView(View v) {
        super.onBindView(v);
        mScaleGroup = (RadioGroup) v.findViewById(R.id.one_hand_scale_group);
        mScaleGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
        updateView();
    }

    public void updateView() {
        int scaleIndex = Settings.System.getInt(mCr, ONE_HANDED_SCALE, 0);
        if (mScaleGroup != null) {
            RadioButton scaleChild = (RadioButton)mScaleGroup.getChildAt(2*scaleIndex); /*new scale radio group has subline view, so the scaleindex need to change*/
            if (scaleChild != null) scaleChild.setChecked(true);
        }
    }
......
}

```

----------
#### 解决方法
- 增加判空
```
    public void updateView() {
        Log.d(TAG, "updateView");
        int scaleIndex = Settings.System.getInt(mCr, ONE_HANDED_SCALE, 0);
        if (mScaleGroup != null) {
            RadioButton scaleChild = (RadioButton)mScaleGroup.getChildAt(2*scaleIndex); /*new scale radio group has subline view, so the scaleindex need to change*/
            if (scaleChild != null) scaleChild.setChecked(true);
        } else {
            Log.d(TAG, "mScaleGroup null");
        }
    }
```
- onBindView方法内更新UI，updateView方法更新数据
将需要更新的数据作为类变量，在onBindView初始化view时顺带更新ui（如设置radio button checked），取类变量的值设置上去。
在外部可调用的方法中为该类变量设置新值，最后调用notifyChanged()方法(notifyChanged会调用onBindView)通知ui更新。
核心想法是：将ui操作放在一起
```
......
    private TextView firstName；
    private String text;
......
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        firstName = (TextView)view.findViewById(R.id.firstname);
        firstName.setText(text);
    ｝
......
    public void setText(String text){ 
        this.text = text;
        notifyChanged();
    }
......
```
- 重写、调用notifyChanged()方法
view不可见时，此方法调用了也不会更新ui，即不会调用onBindView方法；view可见时，会调用onBindView方法更新界面。正是我们期望的结果。

RadioGroupPreference.java
```
......
    //重写方法将权限拓展为public，外部调用
    @Override
    public void notifyChanged() {
        Log.d(TAG, "notifyChanged");
        super.notifyChanged();
    }
......
```
OneHandModeActivity.java
```  
mRadioGroupPreference.notifyChanged();    
```