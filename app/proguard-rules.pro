# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/pc3/Android/Sdk/tools/proguard/proguard-android.txt
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
-keepattributes SourceFile,LineNumberTable,*Annotation*
-keepattributes Signature
-keepattributes Exceptions

-keep class org.apache.http.**
-keep class android.net.http.AndroidHttpClient
#-keep class com.google.android.gms.**
#-keep class com.android.volley.toolbox.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
#-dontwarn com.google.android.gms.**
#-dontwarn com.android.volley.toolbox.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.gjurma.gjurma.Models.** { *; }

##---------------End: proguard configuration for Gson  ----------


# Retain generated class which implement ViewBinder.
-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinder.
#-keep class butterknife.*
#-dontwarn butterknife.internal.**
#-keep public class * implements butterknife.internal.ViewBinder { public <init>(); }
#-keepclasseswithmembernames class * { @butterknife.* <methods>; }
#-keepclasseswithmembernames class * { @butterknife.* <fields>; }


-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}



-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

