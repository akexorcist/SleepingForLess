# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/ADT/sdk/tools/proguard/proguard-android.txt
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

# Realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class *
-dontwarn javax.**
-dontwarn io.realm.**

# Parceler
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }
-keep class org.parceler.Parceler$$Parcels

# Bottom Sheet Layout
-keep class com.bowyer.app.fabtransitionlayout.BottomSheetLayout
-keep class com.bowyer.app.fabtransitionlayout.FooterLayout
-dontwarn com.bowyer.app.fabtransitionlayout.**

# DeepLinkDispatch
-keep class com.airbnb.deeplinkdispatch.** { *; }
-keepclasseswithmembers class * {
     @com.airbnb.deeplinkdispatch.DeepLink <methods>;
}
-keepnames class com.akexorcist.sleepingforless.config.MyGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule

# Retrotit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Otto
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# Calligraphy
-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Google Analytics
-keep class com.google.analytics.** { *; }

# Google Cloud Messaging
-keep class com.google.android.gms.gcm.** { *; }

# Google Play Services
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# GSON
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Parceler
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep class org.parceler.Parceler$$Parcels

# App Compat v7
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# Card View v7
-keep class android.support.v7.widget.RoundRectDrawable { *; }

# Support Design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# Dilating Dots Progress Bar
-dontwarn com.zl.reik.dilatingdotsprogressbar.**
-keep class com.zl.reik.dilatingdotsprogressbar.** { *; }
-keep interface com.zl.reik.dilatingdotsprogressbar.** { *; }

# Floating Search View
-dontwarn com.mypopsy.widget.**
-keep class com.mypopsy.widget.** { *; }
-keep interface com.mypopsy.widget.** { *; }

# Material Styled Dialogs
-dontwarn com.afollestad.materialdialogs.**
-keep class com.afollestad.materialdialogs.** { *; }
-keep interface com.afollestad.materialdialogs.** { *; }

# View Reveal Animator
-dontwarn it.sephiroth.android.library.viewrevealanimator.**
-keep class it.sephiroth.android.library.viewrevealanimator.** { *; }
-keep interface it.sephiroth.android.library.viewrevealanimator.** { *; }

# Material Preference
-dontwarn com.jenzz.materialpreference.**
-keep class com.jenzz.materialpreference.** { *; }
-keep interface com.jenzz.materialpreference.** { *; }

# Material Styled Dialogs
-dontwarn com.github.javiersantos.**
-keep class com.github.javiersantos.** { *; }
-keep interface com.github.javiersantos.** { *; }

# Material Ripple
-dontwarn com.balysv.materialripple.**
-keep class com.balysv.materialripple.** { *; }
-keep interface com.balysv.materialripple.** { *; }

# Subsampling Scale Image View
-dontwarn com.davemorrissey.labs.subscaleview.**
-keep class com.davemorrissey.labs.subscaleview.** { *; }
-keep interface com.davemorrissey.labs.subscaleview.** { *; }

# Assent
-dontwarn com.afollestad.assent.**
-keep class com.afollestad.assent.** { *; }
-keep interface com.afollestad.assent.** { *; }
