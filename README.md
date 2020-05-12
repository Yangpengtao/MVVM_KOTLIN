# 抠抠快送项目
开发语言：kotlin<br/>
包名：com.oomall.kouclodelivery<br/>
架构：Model-View-ViewModel

## 项目介绍

#### 包名
- camera camera1集成
- camera2 camera2集成
- constant 常量包
    - 包含接口名
    - 存放SharedPreference的K
    - 通用常量
- data实体类
- network 网络请求配置
- proxy 代理类，ViewModel层可直接调用。不与内部直接交互
    - 网络代理
    - 图片代理
    - SharedPreference代理
    - 线程代理
- tools 工具类
    - 图片配置
    - 高德地图配置
    - Shared Preference配置
    - 线程配置
- ui 页面包
    - adapter 适配器
        - base 基础类
    - base activity基础类
    - dialog 通用弹窗
    - view 页面
    - view_model 相关页面逻辑层
    - widget 自定义控件
- utils 工具类层，通用的
- KoucloApplication

### 第三方框架
- 图片框架：glide
- 网络框架：okhttp3
- json解析：gson
- 轮播图:banner
- 地图：高德地图

### 开发说明
1，相应的view对应相应的viewModel<br/>
2，view层只写UI的操作（比如接受数据的回调，成功UI该如何显示，失败该如何显示，数据回调绑定数据等），数据逻辑都要写在viewModel。这样既能宝成UI层不会堆积大量代码，在viewModel层写便于复用，结构清晰<br/>
3，主要使用工具图片加载，网络请求，地图，线程都通过代理类实现，方便以后的更改，包名proxy。比如网络框架，现在使用的是OKHTTP，如果我们要换成retrofit，我们只需配置好retrofit后，在application文件将代码 HelperHttp.init(OKHttpProcessor)换成HelperHttp.init(RetrofitProcessor）即可，不需要更改大量用到的代码<br/>
4，ui包下的base包中含有相应的功能基础类，什么类型的activity要继承什么样的类，便于快速开发，以及统一管理，比如BaseMapActivity是关于地图activity的基类，继承该类避免好多重复的编写，以及重写好多没用的方法，造成代码繁多<br/>
5，该项目开启了代码混淆，需要不被混淆的代码，或者加入第三方库时，务必在proguard-rules.pro.xml里添加相应的混淆代码。<br/>
6，知识不断提升，代码不断优化。"# MVVM-KOTLIN" 
