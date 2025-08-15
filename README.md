# FusionPlatform

一个基于 Android 的视频内容应用平台，采用现代化的架构设计。

## 项目特性

- 🎥 视频内容浏览和播放
- 🔍 视频搜索和分类
- 👤 用户个人资料管理
- 📱 现代化的 Material Design UI
- 🏗️ Clean Architecture 架构
- 🔧 使用 Jetpack Compose 构建 UI

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构**: MVVM + Clean Architecture
- **依赖注入**: Hilt
- **网络请求**: Retrofit + OkHttp
- **本地数据库**: Room
- **异步处理**: Coroutines + Flow

## 项目结构

```
app/
├── src/main/java/com/eyepetizer/app/
│   ├── data/           # 数据层
│   ├── domain/         # 业务逻辑层
│   ├── presentation/   # 表现层
│   └── di/            # 依赖注入
```

## 开始使用

1. 克隆项目
```bash
git clone git@github.com:qicun/FusionPlatform.git
```

2. 在 Android Studio 中打开项目

3. 同步 Gradle 依赖

4. 运行应用

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。