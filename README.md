# Android4 翻牌游戏

Android 移动应用开发实验四，Kotlin + XML 布局，采用 MVC 结构。

## 功能

- 主界面：显示唐熙鹏 2024080538 的个人资料与游戏最高分。
- 个人信息：修改姓名、学号、电话、邮箱、性别与爱好，保存后返回主界面；信息仅保存在内存。
- 翻牌游戏：4×4 牌阵、匹配计分、重新开始与退出。

## 在 Android Studio 中运行

1. 克隆仓库，用 Android Studio 打开项目根目录。
2. 安装 Android SDK 37，使用 Android Studio 配套 JDK，等待 Gradle 同步。
3. 选择 API 34 或以上的模拟器或设备，运行 app。

本机 SDK 路径不提交，Android Studio 会生成 local.properties。

## 测试

```sh
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

Windows 可使用 gradlew.bat。UI 测试需要已连接的模拟器或设备及对应测试依赖。

本次验证：原项目构建成功，9 项模型单元测试通过；测试 APK 经 AndroidJUnitRunner 直接执行，3 项 Espresso UI 测试通过。Gradle 离线 connectedDebugAndroidTest 因 UTP 依赖未缓存未完成。

## 计分规则

翻开一张未选中的牌扣 1 分；同点数匹配基础分 4，同花色基础分 1，成功奖励为基础分 ×4；不匹配另扣 2 分。匹配牌不可再次选择。

## 当前限制

个人信息和游戏进度不持久化，页面重建后可能重置；退出游戏仅回传当前局分数，重置前高分可能丢失。随机发牌不保证所有牌均能配对。部分牌面符号和连接符仍可进一步迁移为字符串资源。

