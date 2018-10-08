# CustomRecyclerView
## 使用方法
在build.gradle :project添加
>allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
在build.gradle :app添加
>dependencies {
	        implementation 'com.github.Huazhiling:CustomRecyclerView:Tag'
	}

使用方法可参考demo
[MainActivity](https://github.com/Huazhiling/CustomRecyclerView/blob/master/app/src/main/java/com/dasu/customrecyclerview/MainActivity.java)

#在activity的xml布局中将RecyclerView替换成CustomRecyclerView即可
