    


Android 主题换肤库（支持插件+应用内换肤）
=========================




--------------------

Demo 演示：
![enter image description here](https://github.com/delaiwen-hgh/My_ChangeSkinLoader/blob/master/GIF.gif)

---

功能
--
 1. 插件换肤
 2. 应用内换肤
 3. 更换字体
 4. 加载网络皮肤文件和字体
 
 

---

 一. 换肤
--

 5. 依赖库 skinlibrary
 6. 让你的 Application 继承于 SkinBaseApplication
 7. 让你的 Activity 继承于 SkinBaseActivity，如果使用了 Fragment 则继承于 SkinBaseFragment
 8. 在需要换肤的根布局上添加 xmlns:skin="http://schemas.android.com/android/skin" ，然后在需要换肤的View上加上 skin:enable="true" 
 9. 新建一个项目模块（只包含有资源文件的Apk,为防止用户误点击可将apk后缀改成skin等），其中包含的资源文件的 name 一定要和原项目中有换肤需求的 View 所使用的资源name一致。
 10. 上一步生成的资源文件( ×××.apk )，改名为 ×××.skin，放入 assets 中的 skin 目录下（ skin 目录是自己新建的）
 11. 调用换肤：
   

 在 assets/skin 文件夹中的皮肤

    ```java
SkinManager.getInstance().loadSkin("Your skin file name in assets(eg:theme.skin)",
                                new ILoaderListener() {
                                    @Override
                                    public void onStart() {
                                        Toast.makeText(getApplicationContext(), "正在切换中", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onFailed() {
                                        Toast.makeText(getApplicationContext(), "切换失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );
   
   ```
 
 皮肤来源于网络
 ```java
SkinManager.getInstance().loadSkinFromUrl(skinUrl, new ILoaderListener() {
                    @Override
                    public void onStart() {
                        Log.i("ILoaderListener", "正在切换中");
                        dialog.setContent("正在从网络下载皮肤文件");
                        dialog.show();
                    }

                    @Override
                    public void onSuccess() {
                        Log.i("ILoaderListener", "切换成功");
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed(String errMsg) {
                        Log.i("ILoaderListener", "切换失败:" + errMsg);
                        dialog.setContent("换肤失败:" + errMsg);
                    }

                    @Override
                    public void onProgress(int progress) {
                        Log.i("ILoaderListener", "皮肤文件下载中:" + progress);
                        dialog.setProgress(progress);
                    }
                });
    ```


 

  应用内换肤:
        在需要更换皮肤的资源名称后面添加后缀（eg:_red）
   ```java      
  <color name="colorPrimary_red">#5e03c6</color>
    <color name="colorPrimaryDark_red">#991226</color>
    <color name="colorAccent_red">#cc8411</color>

    <color name="item_bg_red">#103993</color>
    <color name="item_tv_content_color_red">#d4b416</color>
    <color name="item_tv_date_color_red">#E5E5E5</color>
   ```
   
 调用此方法即可
  
  ```java
    SkinManager.getInstance().changeSkin("red");
  ```
  可使用应用内换肤功能轻松实现夜间模式的切换，把对应的夜间资源名称改下后缀然后调用即可。

 二. 换肤属性的扩展
--
 本开源库默认支持 textColor , background 和 src 的换肤。如果你还需要对其他属性进行换肤，那么就需要去自定义了。
那么如何自定义呢？看下面这个例子：
TabLayout大家应该都用过吧。它下面会有一个指示器，当我们换肤的时候也希望这个指示器的颜色也跟着更改。

 - 新建一个 TabLayoutIndicatorAttr 继承于 SkinAttr，然后重写 apply 方法。apply 方法在换肤的时候就会被调用
 - 代码的详细实现
  ```java 
public class TabLayoutIndicatorAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TabLayout) {
            TabLayout tl = (TabLayout) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinResourcesUtils.getColor(attrValueRefId);
                tl.setSelectedTabIndicatorColor(color);
            }
        }
    }
}

 ```
 注：attrValueRefId：就是资源 id。SkinResourcesUtils 是用来获取皮肤包里的资源。

 - 当上面的工作完成之后，就到我们自己的 Application 的 onCreate 方法中加入
```java
SkinConfig.addSupportAttr("tabLayoutIndicator", new TabLayoutIndicatorAttr());
```

 - 最后我们就可以正常使用了
 ```java
 dynamicAddView(mTabLayout, "tabLayoutIndicator", R.color.colorPrimaryDark);
 
 ```
   
 三. 关于字体切换
----
 所有的字体都放到 assets/fonts 文件夹下
 调用方法: 
```java
SkinManager.getInstance().loadFont("xx.ttf")
```
字体来源于网络
```java
 SkinManager.getInstance().loadFontFromUrl(FONTURL, mFontLoaderListener);
                   
```
注：字体切换功能默认不开启，需要字体切换功能请在你的Application中加入SkinConfig.setCanChangeFont(true);

 四. 其他一些重要的api
----

  

 - SkinManager.getInstance().restoreDefaultTheme(): 重置默认皮肤
 - dynamicAddView：当动态创建的View也需要换肤的时候,就可以调用dynamicAddView
 - dynamicAddFontView：当动态创建的View也需要更换字体的时候需要调用

五.注意事项 
----

 - 换肤默认只支持 android 的常用控件，对于支持库的控件和自定义控件的换肤需要动态添加（例如： dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimaryDark);），在布局文件中使用skin:enable="true"是无效的。
 - 默认不支持状态栏颜色的更改，如果需要换肤的同时也要更改状态栏颜色，请到您的Application文件中加入SkinConfig.setCanChangeStatusColor(true);状态栏的颜色值来源于colorPrimaryDark
 - 本开源库使用的 Activity 是 AppCompatActivity，使用的 Fragment 是 android.support.v4.app.Fragment
 - 有换肤需求View所使用的资源一定要是引用值，例如：@color/red，而不是#ff0000
 - 具体的使用请到示例项目中查看



---
致谢
-
本项目是基于ThemeSkinning这个开源库改进而来(改进:增加异步线程进行优化，扩展应用内换肤等)，再次对原作者表示感谢 [ThemeSkinning](https://github.com/burgessjp/ThemeSkinning)

关于此库的关键源码解析[Android主题换肤 无缝切换](http://www.jianshu.com/p/af7c0585dd5b)