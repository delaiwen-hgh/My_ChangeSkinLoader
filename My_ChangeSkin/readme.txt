SkinBaseApplication 主要进行初始化: 
1.将Assers 文件夹下皮肤包保存到本地
2.得到选中的字体(值为空的话使用默认字体 TypefaceUtils.CURRENT_TYPEFACE = TypefaceUtils.getTypeface(context); ) 
3.加载选中的皮肤（ isDefaultSkin = true; //第一次运行，未选择皮肤，不进行notifySkinUpdate()的操作，使用宿主App默认皮肤属性）


继承SkinBaseApplication 的Application可设置
 SkinConfig.setCanChangeStatusColor(true);
 SkinConfig.setCanChangeFont(true);

 SkinConfig.addSupportAttr("tabLayoutIndicator", new TabLayoutIndicatorAttr());
 AttrFactory中没有的属性，需要要先调用addSupportAttr方法和实现一个继承SkinAttr的类，不然就算dynamicAddView（）方法也无法生成对应的SkinAttr(apply进行皮肤转换)
 如果 AttrFactory 有属性，但是由于是动态创建或者特殊的View (ToolBar，TabLayout等)没有加入到初始化的集合中，所以需要调用
 dynamicAddView(toolbar, "background",  R.color.colorPrimaryDark);
  dynamicAddView(tablayout, "tabLayoutIndicator", R.color.colorPrimaryDark); 自行添加到集合中。
 总结: 必须添加到 mSkinItemMap 集合中才能进行换肤

SkinBaseActivity的onCreate方法中的 SkinInflaterFactory 类对Acitivity中所有有标记需要换肤的View进行收集并存储在集合中，同时也收集TextView进行字体切换(SkinConfig.setCanChangeFont(true);才可以)


字体不用保存到本地，直接调用 Typeface.createFromAsset 从assets 中加载，如果需要在线切换字体，也可以将字体下载到本地调用 Typeface.createFromFile进行加载


资源包的minSdkVersion 一定要和 宿主Apk一致，不然无法获得资源包的包名
一个View只能设置一个皮肤属性，后面设置会使前面设置的实效。