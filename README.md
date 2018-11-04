# CustomRecyclerView
用于减少ScrollView嵌套RecyclerView， 可将View当成头部添加到Rv中
### 添加Header的样子
![](https://upload-images.jianshu.io/upload_images/3258838-f26d003341faedb0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
### 添加Footer的样子
![](https://upload-images.jianshu.io/upload_images/3258838-788295b0a5c0cfa5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

目前已经增加了下拉刷新的逻辑， 上拉加载还在完善中

## 使用方法
在build.gradle :project添加
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
在build.gradle :app添加
```
dependencies {
	implementation 'com.github.Huazhiling:CustomRecyclerView:1.1.07'
}
```
使用方法可参考demo
[MainActivity](https://github.com/Huazhiling/CustomRecyclerView/blob/master/app/src/main/java/com/dasu/customrecyclerview/MainActivity.java)

# 在activity的xml布局中将RecyclerView替换成ScrollWrapRecycler即可
# 后续会添加更多的功能 比如刷新加载（`已完成`），滑动删除等等， 想到的有时间一般都会加上去。。
