# 实验六 多 Fragment 应用

在实验五的基础上，使用一个 Activity 和底部标签切换三个 Fragment 实例。个人信息页可编辑资料，点击「保存」后写入文件。两张配对和三张配对各自保存牌序、翻开和匹配状态、得分及最高分。

- 两种玩法均沿用实验五的计分规则。每翻开一张牌扣 1 分，同点数匹配奖励 16 分，同花色匹配奖励 4 分，匹配失败另扣 2 分。
- 竖屏每种模式均有 24 张牌。横屏两张配对有 24 张牌，三张配对有 30 张牌。横竖屏牌局分别保存，重置仅影响当前牌局。
- 资料和牌局使用 `Serializable`、`ObjectInputStream` 和 `ObjectOutputStream` 存放在应用私有文件目录中。

用 Android Studio 打开本目录并同步 Gradle，选择 API 34 或以上的模拟器或设备，运行 `app`。项目使用 Android SDK 37。实验六使用独立应用 ID，可与实验五同时安装。

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest connectedDebugAndroidTest
```
