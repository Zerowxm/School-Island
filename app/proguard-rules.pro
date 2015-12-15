# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\zerol\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法


#-libraryjars libs/easemobchat_2.2.2.jar
#-libraryjars libs/jpush-sdk-release1.8.0.jar
#-libraryjars libs/locSDK_5.3.jar
#-libraryjars libs/MobLogCollector.jar
#-libraryjars libs/MobTools.jar
#-libraryjars libs/ShareSDK-Core-2.6.2.jar
#-libraryjars libs/ShareSDK-Email-2.6.2.jar
#-libraryjars libs/ShareSDK-QQ-2.6.2.jar
#-libraryjars libs/ShareSDK-QZone-2.6.2.jar
#-libraryjars libs/ShareSDK-ShortMessage-2.6.2.jar
#-libraryjars libs/ShareSDK-SinaWeibo-2.6.2.jar
#-libraryjars libs/ShareSDK-Tencentweibo-2.6.2.jar
#-libraryjars libs/ShareSDK-Wechat-2.6.2.jar
#-libraryjars libs/ShareSDK-Wechat-Core-2.6.2.jar
#-libraryjars libs/ShareSDK-Wechat-Moments-2.6.2.jar

-keep class android.support.v7.** {*;}
-keep interface android.support.v7.** {*;}
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-dontwarn  com.easemob.**

-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆

#-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
#    native <methods>;
#}
#-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
#    public void *(android.view.View);
#}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}


-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}
# Keep Picasso
-keep class com.squareup.picasso.** { *; }
-keepclasseswithmembers class * {
    @com.squareup.picasso.** *;
}
-keepclassmembers class * {
    @com.squareup.picasso.** *;
}

# Keep these for GSON and Jackson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

####################jpush##################
-keep class cn.jpush.** { *; }
-keep public class com.umeng.fb.ui.ThreadView { } #双向反馈功能代码不混淆
-dontwarn cn.jpush.**
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

#-keep class com.jakewharton:butterknife.** { *; }
## butter knife proguard rules

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


-dontwarn android.support.v4.**
-keep public class com.mob.tools.* { public *; }
-dontwarn com.mob.tools.**

-keepnames class org.apache.** {*;}
-keep public class org.apache.** {*;}
-dontwarn org.apache.**
-dontwarn org.apache.commons.logging.LogFactory
-dontwarn org.apache.http.annotation.ThreadSafe
-dontwarn org.apache.http.annotation.Immutable
-dontwarn org.apache.http.annotation.NotThreadSafe
## ----------------------------------
##      sharesdk
## ----------------------------------
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*

-keep class javax.** { * ;}
-dontwarn com.google.**
-keep class com.google.** { * ;}

-dontwarn javax.naming.**
###
##-keep class de.hdodenhof:circleimageview.**{*;}
##-dontwarn de.hdodenhof:circleimageview.**

#如果引用了v4或者v7包
-dontwarn android.support.**

##gson
##-libraryjars libs/gson-2.2.2.jar
#-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
#
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

##保持 Serializable 不被混淆并且enum 类也不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    !static !transient <fields>;
#    !private <fields>;
#    !private <methods>;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#
##apk 包内所有 class 的内部结构
#-dump class_files.txt
##未混淆的类和成员
#-printseeds seeds.txt
##列出从 apk 中删除的代码
#-printusage unused.txt
##混淆前后的映射
#-printmapping mapping.txt
