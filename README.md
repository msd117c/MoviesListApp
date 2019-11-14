# Task 6: Shrink

The issue is that movie id field is not annotated and after obfuscation it's reference is lost. We can solve it by annotating it with @SerializedName. After that all app is working again. Also to keep progressive loading of items we have to annotate totalResults in SearchResponse model.

If we build the apk and analyze it we can find a resource called "poster.jpg" which takes around 38% of size of apk. As it is not used in app we can remove it, reducing apk size.

To keep app working with coroutines and Room addon (Task-5) we have to modify a little bit the proguard by adding some rules to ignore coroutines warnings.
