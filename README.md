# 实验五 RecyclerView 翻牌游戏

基于实验四在 `Dev` 分支开发。应用保留个人信息页和最高分，首页右上角菜单可打开“关于”对话框。

- 竖屏为 4 列 × 6 行，共 24 张牌。横屏为 10 列 × 3 行，共 30 张牌。旋转屏幕时保留各自的牌局和分数。
- 翻开一张牌扣 1 分。三张同点数奖励 16 分，三张仅同花色奖励 4 分，不匹配另扣 2 分。失败的三张牌保留至下一次点击，匹配成功的牌会锁定。
- 重置只重新开始当前方向的牌局。退出游戏时将本次进入游戏以来的最高分返回给首页。
- 发牌时每个入选点数发三张。按照点数匹配可以清空牌阵，混用花色匹配可能使剩余牌无法继续配对，此时可以重置。

用 Android Studio 打开工程根目录并同步 Gradle，使用 Android SDK 37、Android Studio 配套 JDK 和 API 34 或以上的设备运行。实验五使用独立应用 ID，可与实验四同时安装。

```powershell
./gradlew.bat assembleDebug testDebugUnitTest assembleDebugAndroidTest
```

界面测试可在 Android Studio 中运行 `MainActivityUITest`，也可安装应用与测试 APK 后执行：

```text
adb shell am instrument -w cn.edu.sicnu.cs.txp.android5.test/androidx.test.runner.AndroidJUnitRunner
```
