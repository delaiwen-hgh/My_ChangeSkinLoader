SkinBaseApplication ��Ҫ���г�ʼ��: 
1.��Assers �ļ�����Ƥ�������浽����
2.�õ�ѡ�е�����(ֵΪ�յĻ�ʹ��Ĭ������ TypefaceUtils.CURRENT_TYPEFACE = TypefaceUtils.getTypeface(context); ) 
3.����ѡ�е�Ƥ���� isDefaultSkin = true; //��һ�����У�δѡ��Ƥ����������notifySkinUpdate()�Ĳ�����ʹ������AppĬ��Ƥ�����ԣ�


�̳�SkinBaseApplication ��Application������
 SkinConfig.setCanChangeStatusColor(true);
 SkinConfig.setCanChangeFont(true);

 SkinConfig.addSupportAttr("tabLayoutIndicator", new TabLayoutIndicatorAttr());
 AttrFactory��û�е����ԣ���ҪҪ�ȵ���addSupportAttr������ʵ��һ���̳�SkinAttr���࣬��Ȼ����dynamicAddView��������Ҳ�޷����ɶ�Ӧ��SkinAttr(apply����Ƥ��ת��)
 ��� AttrFactory �����ԣ����������Ƕ�̬�������������View (ToolBar��TabLayout��)û�м��뵽��ʼ���ļ����У�������Ҫ����
 dynamicAddView(toolbar, "background",  R.color.colorPrimaryDark);
  dynamicAddView(tablayout, "tabLayoutIndicator", R.color.colorPrimaryDark); ������ӵ������С�
 �ܽ�: ������ӵ� mSkinItemMap �����в��ܽ��л���

SkinBaseActivity��onCreate�����е� SkinInflaterFactory ���Acitivity�������б����Ҫ������View�����ռ����洢�ڼ����У�ͬʱҲ�ռ�TextView���������л�(SkinConfig.setCanChangeFont(true);�ſ���)


���岻�ñ��浽���أ�ֱ�ӵ��� Typeface.createFromAsset ��assets �м��أ������Ҫ�����л����壬Ҳ���Խ��������ص����ص��� Typeface.createFromFile���м���


��Դ����minSdkVersion һ��Ҫ�� ����Apkһ�£���Ȼ�޷������Դ���İ���
һ��Viewֻ������һ��Ƥ�����ԣ��������û�ʹǰ�����õ�ʵЧ��