##  保持屏幕常亮

1. 设置屏幕休眠时间15s，单手模式中点击settings和gravity按钮，屏幕会根据休眠时间进入休眠，没有刷新用户活动状态
2. 解决方案：
    + WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
```
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
```
    + powermanager weakup_lock
    不推荐

    + view.setKeepScreenOn(true)
    view需是可见的。单手两个button是动态生成的，使用其他view设置
    + powerManager userActivity
    ```
    power.userActivity(SystemClock.uptimeMillis(), PowerManager.USER_ACTIVITY_EVENT_OTHER, 0);
    ```
    + 事件传递
    在action_down、action_move、action_up中，不要return true
    


