# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\android\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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


########## My Tricks
-keep class io.github.meness.**, io.meness.github.** , com.iGap.proto.** , com.neovisionaries.ws.client.** { *; }
-keep class com.iGap.helper.HelperFillLookUpClass { *; }
-keep public class * extends com.iGap.response.MessageHandler
-keepclasseswithmembers class * {
   public <init>(int, java.lang.Object, java.lang.String);
}

-keep class * extends com.iGap.response.MessageHandler {
    *;
}
###

###Crashlytics
-keep class com.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable
###

###For Compress Module
-dontwarn com.googlecode.mp4parser.**
###
###Trim
-keep class com.coremedia.** { *; }
-keep class com.mp4parser.** { *; }
-keep class com.googlecode.** { *; }
###


-keepattributes Signature,InnerClasses
-keepclasseswithmembers class io.netty.** {
    *;
}
-keepnames class io.netty.** {
    *;
}

-keepnames class com.squareup.** {
    *;
}

# protobuf
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

#
-keep class io.netty.** { *; }
-dontwarn io.netty.**

-keep class org.jboss.** { *; }
-keep enum org.jboss.** { *; }

-keep class sun.nio.sctp.AbstractNotificationHandler { *; }

# for fastadapter
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# Parceler library
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }

-keep class org.parceler.Parceler$$Parcels

#parcel
-keepclassmembers class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements android.os.Parcelable {
 public <fields>;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#
-keep class org.apache.http.**
-keep class android.net.http.**
-dontwarn com.google.android.gms.**

# Realm library
-keepnames public class * extends io.realm.RealmObject
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.** { *; }
-dontwarn io.realm.**

-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

-dontwarn javax.servlet.**
-dontwarn org.joda.time.**
-dontwarn org.w3c.dom.**

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontwarn android.support.**
-verbose

-dontwarn java.nio.file.*
-dontwarn com.squareup.javapoet.*

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
