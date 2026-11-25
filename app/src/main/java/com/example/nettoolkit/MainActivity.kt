Run gradle assembleDebug --no-daemon
To honour the JVM settings for this build a single-use Daemon process will be forked. For more on this, please refer to https://docs.gradle.org/8.10.2/userguide/gradle_daemon.html#sec:disabling_the_daemon in the Gradle documentation.
Daemon will be stopped at the end of the build 
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:mergeDebugNativeDebugMetadata NO-SOURCE
> Task :app:checkKotlinGradlePluginConfigurationErrors SKIPPED
> Task :app:generateDebugResValues
> Task :app:checkDebugAarMetadata
> Task :app:mapDebugSourceSetPaths
> Task :app:generateDebugResources
> Task :app:packageDebugResources
> Task :app:mergeDebugResources
> Task :app:createDebugCompatibleScreenManifests
> Task :app:extractDeepLinksDebug
> Task :app:parseDebugLocalResources
> Task :app:processDebugMainManifest
> Task :app:processDebugManifest
> Task :app:processDebugManifestForPackage
> Task :app:javaPreCompileDebug
> Task :app:mergeDebugShaders
> Task :app:compileDebugShaders NO-SOURCE
> Task :app:generateDebugAssets UP-TO-DATE
> Task :app:mergeDebugAssets
> Task :app:compressDebugAssets
> Task :app:desugarDebugFileDependencies
> Task :app:mergeDebugJniLibFolders
> Task :app:checkDebugDuplicateClasses
> Task :app:mergeDebugNativeLibs
> Task :app:processDebugResources
> Task :app:mergeLibDexDebug

> Task :app:stripDebugDebugSymbols
Unable to strip the following libraries, packaging them as they are: libandroidx.graphics.path.so.

> Task :app:validateSigningDebug
> Task :app:writeDebugAppMetadata
> Task :app:writeDebugSigningConfigVersions
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:144:35 Unresolved reference 'TracertScreen'.

e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:145:33 Unresolved reference 'WhoisScreen'.
> Task :app:compileDebugKotlin FAILED
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:146:37 Unresolved reference 'CmdToolsScreen'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:165:31 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:165:59 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:165:61 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:166:34 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:166:62 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:166:64 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:167:29 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:167:57 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:167:59 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:207:68 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:208:45 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:208:73 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:209:29 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:253:9 Unresolved reference 'TerminalBox'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:263:22 Property delegate must have a 'getValue(Nothing?, KMutableProperty0<*>)' method. None of the following functions is applicable:
fun FloatState.getValue(thisObj: Any?, property: KProperty<*>): Float
fun DoubleState.getValue(thisObj: Any?, property: KProperty<*>): Double
fun IntState.getValue(thisObj: Any?, property: KProperty<*>): Int
fun LongState.getValue(thisObj: Any?, property: KProperty<*>): Long
fun <T> State<T>.getValue(thisObj: Any?, property: KProperty<*>): T
fun <K, V> Map<K, V>.getValue(key: K): V
fun <V, V1 : V> Map<in String, V>.getValue(thisRef: Any?, property: KProperty<*>): V1
fun <V, V1 : V> MutableMap<in String, out V>.getValue(thisRef: Any?, property: KProperty<*>): V1
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:263:22 Property delegate must have a 'setValue(Nothing?, KMutableProperty0<*>, ERROR CLASS: Ambiguity: getValue, [androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, kotlin/collections/getValue, kotlin/collections/getValue, kotlin/collections/getValue])' method. None of the following functions is applicable:
fun MutableFloatState.setValue(thisObj: Any?, property: KProperty<*>, value: Float): Unit
fun MutableDoubleState.setValue(thisObj: Any?, property: KProperty<*>, value: Double): Unit
fun MutableIntState.setValue(thisObj: Any?, property: KProperty<*>, value: Int): Unit
fun MutableLongState.setValue(thisObj: Any?, property: KProperty<*>, value: Long): Unit
fun <T> MutableState<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T): Unit
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:263:79 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:263:79 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:263:79 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:264:1 Argument type mismatch: actual type is 'kotlin.String', but 'androidx.compose.runtime.SnapshotMutationPolicy<T>' was expected.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:264:5 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:264:5 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:265:5 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:265:9 Unresolved reference 'pingJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:265:17 Unresolved reference 'by'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:266:21 Unresolved reference 'pingJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:268:5 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:271:29 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:301:21 Unresolved reference 'outputLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:301:62 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:301:62 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:302:2 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:302:2 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:303:21 Unresolved reference 'pingJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:304:25 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:304:71 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:305:62 Unresolved reference 'outputLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:305:81 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:305:81 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:306:4 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:306:4 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:309:18 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:310:17 Unresolved reference 'enabled'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:310:64 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:311:17 Unresolved reference 'modifier'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:312:13 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:313:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:317:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:319:25 Unresolved reference 'pingJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:320:25 Unresolved reference 'outputLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:320:39 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:320:39 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:321:1 Unsupported [Collection literals outside of annotations].
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:321:2 Unresolved reference '사용자에'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:321:10 Unresolved reference '중지됨'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:322:2 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:322:2 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:331:9 Unresolved reference 'TerminalBox'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:331:21 Unresolved reference 'title'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:331:21 Only expressions are allowed in this context.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:331:47 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:331:59 Unresolved reference 'outputLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:342:20 Property delegate must have a 'getValue(Nothing?, KMutableProperty0<*>)' method. None of the following functions is applicable:
fun FloatState.getValue(thisObj: Any?, property: KProperty<*>): Float
fun DoubleState.getValue(thisObj: Any?, property: KProperty<*>): Double
fun IntState.getValue(thisObj: Any?, property: KProperty<*>): Int
fun LongState.getValue(thisObj: Any?, property: KProperty<*>): Long
fun <T> State<T>.getValue(thisObj: Any?, property: KProperty<*>): T
fun <K, V> Map<K, V>.getValue(key: K): V
fun <V, V1 : V> Map<in String, V>.getValue(thisRef: Any?, property: KProperty<*>): V1
fun <V, V1 : V> MutableMap<in String, out V>.getValue(thisRef: Any?, property: KProperty<*>): V1
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:342:20 Property delegate must have a 'setValue(Nothing?, KMutableProperty0<*>, ERROR CLASS: Ambiguity: getValue, [androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, kotlin/collections/getValue, kotlin/collections/getValue, kotlin/collections/getValue])' method. None of the following functions is applicable:
fun MutableFloatState.setValue(thisObj: Any?, property: KProperty<*>, value: Float): Unit
fun MutableDoubleState.setValue(thisObj: Any?, property: KProperty<*>, value: Double): Unit
fun MutableIntState.setValue(thisObj: Any?, property: KProperty<*>, value: Int): Unit
fun MutableLongState.setValue(thisObj: Any?, property: KProperty<*>, value: Long): Unit
fun <T> MutableState<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T): Unit
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:342:75 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:342:75 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:342:75 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:343:1 Argument type mismatch: actual type is 'kotlin.String', but 'androidx.compose.runtime.SnapshotMutationPolicy<T>' was expected.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:343:5 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:343:5 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:344:5 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:344:9 Unresolved reference 'traceJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:344:18 Unresolved reference 'by'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:345:21 Unresolved reference 'traceJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:347:5 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:350:29 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:364:13 No value passed for parameter 'content'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:368:21 Unresolved reference 'textLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:368:59 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:368:59 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:369:2 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:369:2 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:370:21 Unresolved reference 'traceJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:371:41 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:375:25 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:375:81 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:375:90 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:377:33 Unresolved reference 'textLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:377:53 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:377:53 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:378:2 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:378:2 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:382:57 Unresolved reference 'textLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:382:69 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:382:69 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:383:1 Unresolved reference '추적'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:383:6 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:384:4 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:384:4 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:386:18 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:387:17 Unresolved reference 'enabled'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:388:13 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:389:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:392:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:392:40 Unresolved reference 'traceJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:398:9 Unresolved reference 'SingleChoiceSegmentedButtonRow'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:398:39 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:398:40 Unresolved reference 'modifier'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:398:40 Only expressions are allowed in this context.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:398:40 Suspend function 'suspend fun <T> CoroutineDispatcher.invoke(noinline block: suspend CoroutineScope.() -> T): T' should be called only from a coroutine or another suspend function.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:398:76 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:399:13 None of the following candidates is applicable:
fun MultiChoiceSegmentedButtonRowScope.SegmentedButton(checked: Boolean, onCheckedChange: (Boolean) -> Unit, shape: Shape, modifier: Modifier = ..., enabled: Boolean = ..., colors: SegmentedButtonColors = ..., border: BorderStroke = ..., interactionSource: MutableInteractionSource? = ..., icon: @Composable() ComposableFunction0<Unit> = ..., label: @Composable() ComposableFunction0<Unit>): Unit
fun SingleChoiceSegmentedButtonRowScope.SegmentedButton(selected: Boolean, onClick: () -> Unit, shape: Shape, modifier: Modifier = ..., enabled: Boolean = ..., colors: SegmentedButtonColors = ..., border: BorderStroke = ..., interactionSource: MutableInteractionSource? = ..., icon: @Composable() ComposableFunction0<Unit> = ..., label: @Composable() ComposableFunction0<Unit>): Unit
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:402:49 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:403:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:404:13 None of the following candidates is applicable:
fun MultiChoiceSegmentedButtonRowScope.SegmentedButton(checked: Boolean, onCheckedChange: (Boolean) -> Unit, shape: Shape, modifier: Modifier = ..., enabled: Boolean = ..., colors: SegmentedButtonColors = ..., border: BorderStroke = ..., interactionSource: MutableInteractionSource? = ..., icon: @Composable() ComposableFunction0<Unit> = ..., label: @Composable() ComposableFunction0<Unit>): Unit
fun SingleChoiceSegmentedButtonRowScope.SegmentedButton(selected: Boolean, onClick: () -> Unit, shape: Shape, modifier: Modifier = ..., enabled: Boolean = ..., colors: SegmentedButtonColors = ..., border: BorderStroke = ..., interactionSource: MutableInteractionSource? = ..., icon: @Composable() ComposableFunction0<Unit> = ..., label: @Composable() ComposableFunction0<Unit>): Unit
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:407:49 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:408:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:409:10 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:412:13 Unresolved reference 'TerminalBox'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:412:73 Unresolved reference 'textLog'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:414:13 Unresolved reference 'VisualTracerouteMap'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:415:10 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:419:1 Modifier 'private' is not applicable to 'local class'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:687:29 Suspend function 'suspend fun <T> CoroutineDispatcher.invoke(noinline block: suspend CoroutineScope.() -> T): T' should be called only from a coroutine or another suspend function.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:687:29 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:687:52 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:687:52 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:1 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:7 Syntax error: Expecting an expression.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:7 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:8 Too many arguments for 'suspend fun <T> CoroutineDispatcher.invoke(noinline block: suspend CoroutineScope.() -> T): T'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:19 Unresolved reference 'ms'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:21 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:688:21 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:727:68 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:737:9 Unresolved reference 'TerminalBox'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:748:22 Property delegate must have a 'getValue(Nothing?, KMutableProperty0<*>)' method. None of the following functions is applicable:
fun FloatState.getValue(thisObj: Any?, property: KProperty<*>): Float
fun DoubleState.getValue(thisObj: Any?, property: KProperty<*>): Double
fun IntState.getValue(thisObj: Any?, property: KProperty<*>): Int
fun LongState.getValue(thisObj: Any?, property: KProperty<*>): Long
fun <T> State<T>.getValue(thisObj: Any?, property: KProperty<*>): T
fun <K, V> Map<K, V>.getValue(key: K): V
fun <V, V1 : V> Map<in String, V>.getValue(thisRef: Any?, property: KProperty<*>): V1
fun <V, V1 : V> MutableMap<in String, out V>.getValue(thisRef: Any?, property: KProperty<*>): V1
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:748:22 Property delegate must have a 'setValue(Nothing?, KMutableProperty0<*>, ERROR CLASS: Ambiguity: getValue, [androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, androidx/compose/runtime/getValue, kotlin/collections/getValue, kotlin/collections/getValue, kotlin/collections/getValue])' method. None of the following functions is applicable:
fun MutableFloatState.setValue(thisObj: Any?, property: KProperty<*>, value: Float): Unit
fun MutableDoubleState.setValue(thisObj: Any?, property: KProperty<*>, value: Double): Unit
fun MutableIntState.setValue(thisObj: Any?, property: KProperty<*>, value: Int): Unit
fun MutableLongState.setValue(thisObj: Any?, property: KProperty<*>, value: Long): Unit
fun <T> MutableState<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T): Unit
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:748:77 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:748:77 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:748:77 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:749:1 Argument type mismatch: actual type is 'kotlin.String', but 'androidx.compose.runtime.SnapshotMutationPolicy<T>' was expected.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:749:5 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:749:5 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:750:5 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:750:9 Unresolved reference 'cmdJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:750:16 Unresolved reference 'by'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:751:19 Unresolved reference 'cmdJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:763:5 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:766:29 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:780:38 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:781:57 Assignment type mismatch: actual type is 'kotlin.Any', but 'kotlin.String' was expected.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:816:21 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:816:56 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:816:56 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:817:2 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:817:2 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:818:21 Unresolved reference 'cmdJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:821:43 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:822:65 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:825:33 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:825:105 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:826:70 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:826:89 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:826:89 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:827:4 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:827:4 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:830:42 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:831:43 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:832:33 Suspension functions can only be called within coroutine body.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:832:65 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:834:40 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:835:43 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:836:33 Suspension functions can only be called within coroutine body.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:836:65 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:838:40 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:839:43 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:840:33 Suspension functions can only be called within coroutine body.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:840:65 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:842:43 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:843:43 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:844:33 Suspension functions can only be called within coroutine body.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:844:65 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:846:29 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:846:34 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:847:43 Unresolved reference 'NetworkHelper'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:848:33 Suspension functions can only be called within coroutine body.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:848:65 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:852:18 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:853:17 Unresolved reference 'enabled'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:853:35 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:854:17 Unresolved reference 'modifier'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:855:13 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:856:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:860:17 @Composable invocations can only happen from the context of a @Composable function
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:862:25 Unresolved reference 'cmdJob'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:863:25 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:863:39 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:863:39 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:864:1 Unsupported [Collection literals outside of annotations].
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:864:2 Unresolved reference '사용자에'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:864:10 Unresolved reference '중지됨'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:865:2 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:865:2 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:873:9 Unresolved reference 'TerminalBox'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:873:21 Unresolved reference 'title'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:873:21 Only expressions are allowed in this context.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:873:55 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:873:67 Unresolved reference 'cmdOutput'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:900:1 Named object 'NetworkHelper' cannot be local. Try to use an anonymous object instead.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:969:13 None of the following candidates is applicable:
fun Appendable.appendLine(): Appendable
fun Appendable.appendLine(value: Char): Appendable
fun Appendable.appendLine(value: CharSequence?): Appendable
fun StringBuilder.appendLine(value: StringBuffer?): StringBuilder
fun StringBuilder.appendLine(value: Byte): StringBuilder
fun StringBuilder.appendLine(value: Double): StringBuilder
fun StringBuilder.appendLine(value: Float): StringBuilder
fun StringBuilder.appendLine(value: Int): StringBuilder
fun StringBuilder.appendLine(value: Long): StringBuilder
fun StringBuilder.appendLine(value: Short): StringBuilder
fun StringBuilder.appendLine(value: StringBuilder?): StringBuilder
fun StringBuilder.appendLine(): StringBuilder
fun StringBuilder.appendLine(value: Any?): StringBuilder
fun StringBuilder.appendLine(value: Boolean): StringBuilder
fun StringBuilder.appendLine(value: Char): StringBuilder
fun StringBuilder.appendLine(value: CharArray): StringBuilder
fun StringBuilder.appendLine(value: CharSequence?): StringBuilder
fun StringBuilder.appendLine(value: String?): StringBuilder
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:969:24 Identity equality for arguments of types 'kotlin.Boolean' and 'kotlin.String' is prohibited.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:969:25 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:969:25 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:970:5 Unresolved reference '추가'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:970:13 Unresolved reference '인터페이스'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:970:24 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:970:24 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:970:24 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:988:14 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1383:13 None of the following candidates is applicable:
fun Appendable.appendLine(): Appendable
fun Appendable.appendLine(value: Char): Appendable
fun Appendable.appendLine(value: CharSequence?): Appendable
fun StringBuilder.appendLine(value: StringBuffer?): StringBuilder
fun StringBuilder.appendLine(value: Byte): StringBuilder
fun StringBuilder.appendLine(value: Double): StringBuilder
fun StringBuilder.appendLine(value: Float): StringBuilder
fun StringBuilder.appendLine(value: Int): StringBuilder
fun StringBuilder.appendLine(value: Long): StringBuilder
fun StringBuilder.appendLine(value: Short): StringBuilder
fun StringBuilder.appendLine(value: StringBuilder?): StringBuilder
fun StringBuilder.appendLine(): StringBuilder
fun StringBuilder.appendLine(value: Any?): StringBuilder
fun StringBuilder.appendLine(value: Boolean): StringBuilder
fun StringBuilder.appendLine(value: Char): StringBuilder
fun StringBuilder.appendLine(value: CharArray): StringBuilder
fun StringBuilder.appendLine(value: CharSequence?): StringBuilder
fun StringBuilder.appendLine(value: String?): StringBuilder
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1383:40 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1383:40 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1383:40 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1384:3 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1384:3 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1385:13 Unresolved reference 'addresses'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1385:22 Syntax error: Expecting an element.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1385:33 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1386:45 Unresolved reference 'hostAddress'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1387:14 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1388:13 'if' must have both main and 'else' branches when used as an expression.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1389:17 None of the following candidates is applicable:
fun Appendable.appendLine(): Appendable
fun Appendable.appendLine(value: Char): Appendable
fun Appendable.appendLine(value: CharSequence?): Appendable
fun StringBuilder.appendLine(value: StringBuffer?): StringBuilder
fun StringBuilder.appendLine(value: Byte): StringBuilder
fun StringBuilder.appendLine(value: Double): StringBuilder
fun StringBuilder.appendLine(value: Float): StringBuilder
fun StringBuilder.appendLine(value: Int): StringBuilder
fun StringBuilder.appendLine(value: Long): StringBuilder
fun StringBuilder.appendLine(value: Short): StringBuilder
fun StringBuilder.appendLine(value: StringBuilder?): StringBuilder
fun StringBuilder.appendLine(): StringBuilder
fun StringBuilder.appendLine(value: Any?): StringBuilder
fun StringBuilder.appendLine(value: Boolean): StringBuilder
fun StringBuilder.appendLine(value: Char): StringBuilder
fun StringBuilder.appendLine(value: CharArray): StringBuilder
fun StringBuilder.appendLine(value: CharSequence?): StringBuilder
fun StringBuilder.appendLine(value: String?): StringBuilder
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1389:28 Identity equality for arguments of types 'kotlin.Boolean' and 'kotlin.String' is prohibited.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1389:29 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1389:29 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1390:5 Unresolved reference '상세'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1390:12 Unresolved reference '레코드'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1390:21 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1390:21 Syntax error: Expecting ','.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1390:21 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1391:17 Only expressions are allowed here.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1394:18 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1395:14 Syntax error: Expecting ')'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1462:73 Syntax error: Expecting '"'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1462:73 Syntax error: Incorrect template entry: .
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1463:1 Unresolved reference '현재'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1463:7 Unresolved reference 'IP'.
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1463:9 Syntax error: Unexpected tokens (use ';' to separate expressions on the same line).
e: file:///home/runner/work/nettoolkit-android/nettoolkit-android/app/src/main/java/com/example/nettoolkit/MainActivity.kt:1495:2 Syntax error: Expecting '}'.


FAILURE: Build failed with an exception.
> Task :app:mergeExtDexDebug

gradle/actions: Writing build results to /home/runner/work/_temp/.gradle-actions/build-results/__run_2-1795593298498.json
* What went wrong:
28 actionable tasks: 28 executed
Execution failed for task ':app:compileDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
   > Compilation error. See log for more details

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights.
> Get more help at https://help.gradle.org.

BUILD FAILED in 19s
Error: Process completed with exit code 1.
