## Android-LibFramework 项目说明

#### 项目描述
  一个简单的Android项目基础框架，提供基于注解的资源注入和事件绑定，UI管理，UI log打印等功能，达到快速开发的目的
  
#### 项目结构
   文件和路径 | 功能
   -----------|-----------
   src\ | java 源码文件
   res\ | resources 文件
   libc\ | 编解码所需的 c 源码文件
   libs | 依赖库
   ... | ...
  
#### 详细描述
  1) 整合了 xutils 的注解框架，通过注解进行UI，资源和事件绑定，达到 IoC 的目的
```java
      @ContentView(R.layout.activity_main)
      public class ActivityMain extends BaseFragmentActivity  {
      
          @ViewInject(R.id.btn)
          private Button btn;
	        
          @OnClick({R.id.btn1, R.id.btn2})
          public void onClick(View v) {
              // do something
          }
	        
          @OnCompoundButtonCheckedChange({R.id.cb1, R.id.cb2})
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              // do something
          }
          
          @OnRadioGroupCheckedChange(R.id.rdogrp_bottom_panel)
          public void onCheckedChanged(RadioGroup group, int checkedId) {
              // do something
          }
          
      }
```

  2) BaseActivity 提供指定工作流程，子类只需关注于业务逻辑实现  
```Java
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          ActivityManager.getInstance().addActivity(this);  // 加入界面管理器便于销毁等管理
          setLayoutView();         // 使用注解注入ContentView时，不用重写此方法
          ViewUtils.inject(this);  // 声明注解本类
          initData();              // 执行数据初始化
          initWidget();            // 执行控件初始化
          registerBroadcast();     // 注册广播
          log("---------onCreate");    
      }
```

  3) BaseFragment 按指定流程执行 inflate 和初始化工作
```Java
      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          super.onCreateView(inflater, container, savedInstanceState);
          View view = inflaterView(inflater, container, savedInstanceState);  // 实例化view
          ViewUtils.inject(this, view);  // 声明注解本类
          initWidget(view);              // 执行控件初始化
          log("---------onCreateView");
          return view;
      }
```

  4) BaseFragmentActivity 除了提供 BaseActivity 的功能外，还提供`addFragment`, `changeFragment`,`replaceFragment`方法，  
  简化 Fragment 操作

