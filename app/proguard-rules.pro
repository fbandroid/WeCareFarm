# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# Platform calls Class.forName on types which do not exist on Android to determine platform.


-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# ItextG
-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**

-keep class com.itextpdf.** { *; }

-keep class javax.xml.crypto.dsig.** { *; }
-dontwarn javax.xml.crypto.dsig.**

-keep class org.apache.jcp.xml.dsig.internal.dom.** { *; }
-dontwarn org.apache.jcp.xml.dsig.internal.dom.**

-keep class javax.xml.crypto.dom.** { *; }
-dontwarn javax.xml.crypto.dom.**

-keep class org.apache.xml.security.utils.** { *; }
-dontwarn org.apache.xml.security.utils.**

-keep class javax.xml.crypto.XMLStructure
-dontwarn javax.xml.crypto.XMLStructure

# Okhttp3
-dontwarn okio.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.* { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class com.codefuelindia.wecarefarm.model.** { *; }

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}