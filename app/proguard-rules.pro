-keepattributes Signature
-keepattributes SetJavaScriptEnabled
#endregion

-keepattributes JavascriptInterface

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}