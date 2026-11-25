package com.example.nettoolkit

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            enableEdgeToEdge()
        } catch (_: Throwable) {
        }
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                NetToolkitApp()
            }
        }
    }
}

data class TraceHop(
    val hop: Int,
    val ip: String,
    val rttMs: Long,
    val city: String = "",
    val country: String = "",
    val isp: String = "",
    val lat: Double? = null,
    val lon: Double? = null
)

enum class AppTab(val title: String) {
    IP_CONFIG("IP / NAT"),
    PING("Ping"),
    TRACERT("Tracert"),
    WHOIS("Whois"),
    CMD_TOOLS("CMD 도구")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetToolkitApp() {
    var selectedTab by remember { mutableStateOf(AppTab.IP_CONFIG) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NetToolkit Pro v1.3", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    AppTab.IP_CONFIG -> Icons.Default.Router
                                    AppTab.PING -> Icons.Default.NetworkPing
                                    AppTab.TRACERT -> Icons.Default.Public
                                    AppTab.WHOIS -> Icons.Default.TravelExplore
                                    AppTab.CMD_TOOLS -> Icons.Default.Terminal
                                },
                                contentDescription = tab.title
                            )
                        },
                        label = { Text(tab.title, fontSize = 11.sp) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            when (selectedTab) {
                AppTab.IP_CONFIG -> IpConfigScreen()
                AppTab.PING -> PingScreen()
                AppTab.TRACERT -> TracertScreen()
                AppTab.WHOIS -> WhoisScreen()
                AppTab.CMD_TOOLS -> CmdToolsScreen()
            }
        }
    }
}

@Composable
fun IpConfigScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var ipDetails by remember { mutableStateOf("네트워크 정보를 불러오는 중...") }
    var publicIpInfo by remember { mutableStateOf("공인 IP 확인 중...") }
    var previousIp by remember { mutableStateOf<String?>(null) }
    var statusBanner by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun refreshNetworkInfo(afterRenew: Boolean = false) {
        scope.launch {
            isLoading = true
            val localReport = withContext(Dispatchers.IO) { NetworkHelper.getIpConfigAll(context) }
            val currentLocalIp = withContext(Dispatchers.IO) { NetworkHelper.getPrimaryIp(context) }
            val natReport = withContext(Dispatchers.IO) { NetworkHelper.getNatPublicIp(currentLocalIp) }

            if (afterRenew && previousIp != null) {
                statusBanner = "갱신 확인 완료: 이전 IP [$previousIp] -> 현재 IP [$currentLocalIp]"
            }
            ipDetails = localReport
            publicIpInfo = natReport
            isLoading = false
        }
    }

    val connectivityPanelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        refreshNetworkInfo(afterRenew = true)
    }

    LaunchedEffect(Unit) { refreshNetworkInfo() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { refreshNetworkInfo() },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("새로고침")
            }

            FilledTonalButton(
                onClick = {
                    scope.launch {
                        previousIp = withContext(Dispatchers.IO) { NetworkHelper.getPrimaryIp(context) }
                        val renewedByRoot = withContext(Dispatchers.IO) {
                            NetworkHelper.tryReleaseRenew(context)
                        }
                        if (renewedByRoot) {
                            delay(2500)
                            refreshNetworkInfo(afterRenew = true)
                        } else {
                            statusBanner = "안드로이드 보안 정책에 따라 인터넷 설정 창을 열었습니다. Wi-Fi 또는 모바일 데이터를 껐다 켠 뒤 닫으면 새 IP를 확인합니다."
                            try {
                                val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                                connectivityPanelLauncher.launch(panelIntent)
                            } catch (_: Throwable) {
                                try {
                                    connectivityPanelLauncher.launch(Intent(Settings.ACTION_WIFI_SETTINGS))
                                } catch (_: Throwable) {
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Icon(Icons.Default.SyncAlt, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Release/Renew")
            }
        }

        statusBanner?.let { msg ->
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(msg, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("NAT / 공인 IP 상태", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(6.dp))
                SelectionContainer {
                    Text(publicIpInfo, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                }
            }
        }

        TerminalBox(title = "Windows [ipconfig /all] 스타일 상세 정보", content = ipDetails)
    }
}

@Composable
fun PingScreen() {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var targetHost by remember { mutableStateOf("8.8.8.8") }
    var pingCount by remember { mutableStateOf("5") }
    var outputLog by remember { mutableStateOf("호스트 또는 IP를 입력하고 Ping 실행을 누르세요.\n") }
    var pingJob by remember { mutableStateOf<Job?>(null) }
    val isRunning = pingJob?.isActive == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = targetHost,
                onValueChange = { targetHost = it.trim() },
                label = { Text("대상 IP / 도메인") },
                singleLine = true,
                modifier = Modifier.weight(2f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            OutlinedTextField(
                value = pingCount,
                onValueChange = { pingCount = it.filter { ch -> ch.isDigit() }.take(2) },
                label = { Text("횟수") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    focusManager.clearFocus()
                    val count = pingCount.toIntOrNull()?.coerceIn(1, 50) ?: 4
                    outputLog = "> ping -c $count $targetHost\n"
                    pingJob = scope.launch(Dispatchers.IO) {
                        NetworkHelper.streamPing(targetHost, count) { line ->
                            scope.launch(Dispatchers.Main) { outputLog += "$line\n" }
                        }
                    }
                },
                enabled = !isRunning && targetHost.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isRunning) "Ping 응답 대기중..." else "Ping 실행")
            }

            if (isRunning) {
                OutlinedButton(
                    onClick = {
                        pingJob?.cancel()
                        outputLog += "\n[사용자에 의해 중지됨]\n"
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("중지")
                }
            }
        }

        TerminalBox(title = "실시간 ICMP Ping 응답", content = outputLog)
    }
}

@Composable
fun TracertScreen() {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var targetHost by remember { mutableStateOf("google.com") }
    var viewMode by remember { mutableIntStateOf(1) }
    var hops by remember { mutableStateOf<List<TraceHop>>(emptyList()) }
    var textLog by remember { mutableStateOf("추적할 도메인이나 IP를 입력 후 추적을 누르세요.\n") }
    var traceJob by remember { mutableStateOf<Job?>(null) }
    val isRunning = traceJob?.isActive == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = targetHost,
                onValueChange = { targetHost = it.trim() },
                label = { Text("추적 대상 호스트 / IP") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )
            Button(
                onClick = {
                    focusManager.clearFocus()
                    hops = emptyList()
                    textLog = "> tracert $targetHost 시작...\n"
                    traceJob = scope.launch(Dispatchers.IO) {
                        val originHop = NetworkHelper.fetchGeoForHop(0, "", 0L)
                        if (originHop.lat != null) {
                            withContext(Dispatchers.Main) { hops = hops + originHop }
                        }
                        NetworkHelper.runTraceroute(targetHost, maxHops = 20) { hopItem, rawLine ->
                            scope.launch(Dispatchers.Main) {
                                textLog += "$rawLine\n"
                                hops = hops + hopItem
                            }
                        }
                        withContext(Dispatchers.Main) { textLog += "\n추적 완료.\n" }
                    }
                },
                enabled = !isRunning && targetHost.isNotBlank()
            ) {
                Text(if (isRunning) "추적중..." else "추적")
            }
            if (isRunning) {
                IconButton(onClick = { traceJob?.cancel() }) {
                    Icon(Icons.Default.StopCircle, contentDescription = "중지", tint = Color(0xFFFF6B6B))
                }
            }
        }

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = viewMode == 0,
                onClick = { viewMode = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) { Text("터미널 경로 (tracert)") }
            SegmentedButton(
                selected = viewMode == 1,
                onClick = { viewMode = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) { Text("지구본 시각화 (3D Globe)") }
        }

        if (viewMode == 0) {
            TerminalBox(title = "Traceroute 로그 (최대 20 Hops)", content = textLog)
        } else {
            VisualTracerouteMap(hops = hops, isRunning = isRunning)
        }
    }
}

private data class GlobeProjectedPoint(
    val x: Float,
    val y: Float,
    val visible: Boolean,
    val depth: Double
)

@Composable
fun VisualTracerouteMap(hops: List<TraceHop>, isRunning: Boolean) {
    // 서울(경도 127도)이 정면/우상단에 잘 보이도록 초기 경도 회전값 설정
    var rotLonDeg by remember { mutableFloatStateOf(-127f) }
    var rotLatDeg by remember { mutableFloatStateOf(-28f) }
    var autoCenterApplied by remember { mutableStateOf(false) }

    val geoHops = hops.filter { it.lat != null && it.lon != null && it.lat.isFinite() && it.lon.isFinite() }

    LaunchedEffect(geoHops.size) {
        if (!autoCenterApplied && geoHops.isNotEmpty()) {
            val first = geoHops.first()
            rotLonDeg = (-first.lon!!).toFloat()
            rotLatDeg = (first.lat!! * 0.5).toFloat().coerceIn(-45f, 45f)
            autoCenterApplied = true
        }
    }

    val labelPaint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = 26f
            isFakeBoldText = true
        }
    }

    // 대표 대륙 윤곽 좌표(위도, 경도)로 지구본 위에 대륙 실루엣 표현
    val continentPolygons = remember {
        listOf(
            // 한반도/동아시아 대륙
            listOf(35.0 to 126.0, 38.0 to 129.0, 42.0 to 135.0, 50.0 to 135.0, 52.0 to 120.0, 40.0 to 118.0, 34.0 to 122.0, 35.0 to 126.0),
            // 일본 열도
            listOf(31.5 to 131.0, 34.0 to 135.0, 36.0 to 140.5, 42.0 to 142.0, 44.0 to 145.0, 41.0 to 140.0, 34.0 to 132.0, 31.5 to 131.0),
            // 유라시아 본토
            listOf(22.0 to 78.0, 10.0 to 77.0, 25.0 to 62.0, 41.0 to 29.0, 55.0 to 10.0, 70.0 to 25.0, 72.0 to 80.0, 68.0 to 170.0, 55.0 to 135.0, 22.0 to 108.0, 22.0 to 78.0),
            // 북아메리카
            listOf(15.0 to -92.0, 30.0 to -97.0, 30.0 to -82.0, 45.0 to -67.0, 58.0 to -64.0, 70.0 to -125.0, 60.0 to -150.0, 48.0 to -125.0, 32.0 to -117.0, 15.0 to -92.0),
            // 남아메리카
            listOf(10.0 to -72.0, 5.0 to -50.0, -10.0 to -35.0, -25.0 to -48.0, -54.0 to -68.0, -35.0 to -72.0, -5.0 to -81.0, 10.0 to -72.0),
            // 아프리카
            listOf(35.0 to -6.0, 32.0 to 32.0, 12.0 to 51.0, -15.0 to 40.0, -34.0 to 20.0, -18.0 to 12.0, 5.0 to -8.0, 20.0 to -17.0, 35.0 to -6.0),
            // 오세아니아/호주
            listOf(-15.0 to 130.0, -18.0 to 146.0, -35.0 to 150.0, -38.0 to 140.0, -32.0 to 115.0, -20.0 to 114.0, -15.0 to 130.0)
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF050B14))
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        rotLonDeg += dragAmount.x * 0.45f
                        rotLatDeg = (rotLatDeg + dragAmount.y * 0.45f).coerceIn(-75f, 75f)
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                if (w <= 0f || h <= 0f) return@Canvas

                val center = Offset(w / 2f, h / 2f)
                val radius = minOf(w, h) * 0.42f
                if (radius <= 10f) return@Canvas

                val lambda0 = Math.toRadians(rotLonDeg.toDouble())
                val phi0 = Math.toRadians(rotLatDeg.toDouble())

                // 정사영(Orthographic) 3D 지구본 투영 함수
                fun projectGlobe(latDeg: Double, lonDeg: Double, altitudeFactor: Double = 1.0): GlobeProjectedPoint {
                    val phi = Math.toRadians(latDeg)
                    val lam = Math.toRadians(lonDeg) - lambda0
                    val cosPhi = cos(phi)
                    val sinPhi = sin(phi)
                    val cosLam = cos(lam)
                    val sinLam = sin(lam)
                    val cosPhi0 = cos(phi0)
                    val sinPhi0 = sin(phi0)

                    val x3d = cosPhi * sinLam
                    val y3d = sinPhi0 * cosPhi * cosLam - cosPhi0 * sinPhi
                    val z3d = cosPhi0 * cosPhi * cosLam + sinPhi0 * sinPhi

                    val r = radius * altitudeFactor
                    val sx = (center.x + x3d * r).toFloat()
                    val sy = (center.y - y3d * r).toFloat()
                    return GlobeProjectedPoint(sx, sy, z3d >= -0.05, z3d)
                }

                // 1) 대기권 후광 + 입체 셰이딩 지구본 구체 그리기
                drawContext.canvas.nativeCanvas.apply {
                    val haloPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        shader = RadialGradient(
                            center.x,
                            center.y,
                            radius * 1.18f,
                            intArrayOf(
                                android.graphics.Color.argb(90, 56, 189, 248),
                                android.graphics.Color.argb(25, 14, 116, 144),
                                android.graphics.Color.TRANSPARENT
                            ),
                            floatArrayOf(0.75f, 0.92f, 1.0f),
                            Shader.TileMode.CLAMP
                        )
                    }
                    drawCircle(center.x, center.y, radius * 1.18f, haloPaint)

                    val spherePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        shader = RadialGradient(
                            center.x - radius * 0.32f,
                            center.y - radius * 0.32f,
                            radius * 1.25f,
                            intArrayOf(
                                android.graphics.Color.rgb(29, 78, 137),
                                android.graphics.Color.rgb(12, 33, 66),
                                android.graphics.Color.rgb(5, 13, 28)
                            ),
                            floatArrayOf(0.0f, 0.65f, 1.0f),
                            Shader.TileMode.CLAMP
                        )
                    }
                    drawCircle(center.x, center.y, radius, spherePaint)
                }

                // 지구본 외곽선
                drawCircle(
                    color = Color(0xFF38BDF8).copy(alpha = 0.7f),
                    radius = radius,
                    center = center,
                    style = Stroke(width = 2f)
                )

                // 2) 위선(Latitudes) 및 경선(Longitudes) 격자선 그리기
                val gridColor = Color(0xFF38BDF8).copy(alpha = 0.18f)
                for (lat in -60..60 step 30) {
                    val path = Path()
                    var started = false
                    for (lon in -180..180 step 6) {
                        val pt = projectGlobe(lat.toDouble(), lon.toDouble())
                        if (pt.visible) {
                            if (!started) {
                                path.moveTo(pt.x, pt.y)
                                started = true
                            } else {
                                path.lineTo(pt.x, pt.y)
                            }
                        } else {
                            started = false
                        }
                    }
                    drawPath(path, color = gridColor, style = Stroke(width = 1f))
                }

                for (lon in -180..150 step 30) {
                    val path = Path()
                    var started = false
                    for (lat in -90..90 step 6) {
                        val pt = projectGlobe(lat.toDouble(), lon.toDouble())
                        if (pt.visible) {
                            if (!started) {
                                path.moveTo(pt.x, pt.y)
                                started = true
                            } else {
                                path.lineTo(pt.x, pt.y)
                            }
                        } else {
                            started = false
                        }
                    }
                    drawPath(path, color = gridColor, style = Stroke(width = 1f))
                }

                // 3) 지구본 위 대륙 윤곽 렌더링
                val landStrokeColor = Color(0xFF34D399).copy(alpha = 0.55f)
                val landFillColor = Color(0xFF10B981).copy(alpha = 0.12f)
                continentPolygons.forEach { poly ->
                    val polyPath = Path()
                    var started = false
                    poly.forEach { (lat, lon) ->
                        val pt = projectGlobe(lat, lon)
                        if (pt.visible) {
                            if (!started) {
                                polyPath.moveTo(pt.x, pt.y)
                                started = true
                            } else {
                                polyPath.lineTo(pt.x, pt.y)
                            }
                        } else {
                            started = false
                        }
                    }
                    drawPath(polyPath, color = landFillColor)
                    drawPath(polyPath, color = landStrokeColor, style = Stroke(width = 1.6f))
                }

                // 4) 홉(Hop) 간 구면 궤적(3D Arc) 연결
                for (i in 0 until geoHops.size - 1) {
                    val a = geoHops[i]
                    val b = geoHops[i + 1]
                    val arcPath = Path()
                    var started = false
                    val steps = 24
                    for (s in 0..steps) {
                        val t = s.toDouble() / steps.toDouble()
                        val interpLat = a.lat!! + (b.lat!! - a.lat!!) * t
                        val interpLon = a.lon!! + (b.lon!! - a.lon!!) * t
                        val arcLift = 1.0 + sin(t * Math.PI) * 0.12
                        val pt = projectGlobe(interpLat, interpLon, arcLift)
                        if (pt.visible) {
                            if (!started) {
                                arcPath.moveTo(pt.x, pt.y)
                                started = true
                            } else {
                                arcPath.lineTo(pt.x, pt.y)
                            }
                        } else {
                            started = false
                        }
                    }
                    drawPath(
                        path = arcPath,
                        color = Color(0xFF38BDF8),
                        style = Stroke(
                            width = 3f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                        )
                    )
                }

                // 5) 지구본 전면 홉 마커 및 라벨 렌더링
                geoHops.forEach { hop ->
                    val pt = projectGlobe(hop.lat!!, hop.lon!!, 1.02)
                    if (pt.visible) {
                        val nodeColor = when {
                            hop.hop == 0 -> Color(0xFF22C55E)
                            hop.rttMs < 50 -> Color(0xFF38BDF8)
                            hop.rttMs < 150 -> Color(0xFFFACC15)
                            else -> Color(0xFFF87171)
                        }
                        drawCircle(color = nodeColor.copy(alpha = 0.30f), radius = 15f, center = Offset(pt.x, pt.y))
                        drawCircle(color = nodeColor, radius = 6.5f, center = Offset(pt.x, pt.y))

                        val label = if (hop.hop == 0) "출발 (${hop.city.ifBlank { "내 위치" }})" else "#${hop.hop} ${hop.city.ifBlank { hop.country }}"
                        drawContext.canvas.nativeCanvas.drawText(label, pt.x + 10f, pt.y - 8f, labelPaint)
                    }
                }
            }

            Text(
                text = if (isRunning) "지구본 경로 추적 중... (한 손가락으로 지구본 회전)" else "한 손가락 드래그로 지구본 회전 | 홉: ${hops.size}",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
            )
        }

        if (hops.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                hops.forEach { hop ->
                    AssistChip(
                        onClick = {
                            if (hop.lat != null && hop.lon != null) {
                                rotLonDeg = (-hop.lon).toFloat()
                                rotLatDeg = (hop.lat * 0.5).toFloat().coerceIn(-75f, 75f)
                            }
                        },
                        label = {
                            val title = if (hop.hop == 0) "출발" else "Hop ${hop.hop}"
                            val loc = listOf(hop.city, hop.country).filter { it.isNotBlank() }.joinToString(", ").ifBlank { "사설/응답없음" }
                            Text("$title: ${hop.ip}\n$loc (${hop.rttMs}ms)", fontSize = 11.sp)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WhoisScreen() {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var queryInput by remember { mutableStateOf("1.1.1.1") }
    var resultText by remember { mutableStateOf("조회할 IP 주소 또는 도메인을 입력하세요 (비워두면 내 공인 IP를 조회합니다).") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = queryInput,
                onValueChange = { queryInput = it.trim() },
                label = { Text("대상 IP 또는 도메인 (공란=내 IP)") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )
            Button(
                onClick = {
                    focusManager.clearFocus()
                    loading = true
                    scope.launch {
                        resultText = withContext(Dispatchers.IO) { NetworkHelper.lookupWhoisAndGeo(queryInput) }
                        loading = false
                    }
                },
                enabled = !loading
            ) {
                Text(if (loading) "조회중..." else "Whois 조회")
            }
        }

        TerminalBox(title = "Whois / GeoIP / ISP / ASN 조회 결과", content = resultText)
    }
}

@Composable
fun CmdToolsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var selectedCmd by remember { mutableStateOf("nbtstat -a") }
    var targetArg by remember { mutableStateOf("192.168.0.1") }
    var cmdOutput by remember { mutableStateOf("실행할 네트워크 명령어를 선택하고 실행을 누르세요.\n") }
    var cmdJob by remember { mutableStateOf<Job?>(null) }
    val running = cmdJob?.isActive == true

    val commands = listOf(
        "nbtstat -a",
        "pathping",
        "netsh wlan",
        "nslookup",
        "portscan",
        "route print",
        "subnet scan"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            commands.forEach { cmd ->
                FilterChip(
                    selected = selectedCmd == cmd,
                    onClick = {
                        selectedCmd = cmd
                        if (cmd == "nbtstat -a" && (targetArg == "google.com" || targetArg == "naver.com")) {
                            val gw = NetworkHelper.getDefaultGatewayIp(context)
                            if (gw != null) targetArg = gw
                        } else if (cmd == "pathping" && (targetArg.startsWith("192.168.") || targetArg.startsWith("10."))) {
                            targetArg = "8.8.8.8"
                        }
                    },
                    label = { Text(cmd) }
                )
            }
        }

        val needsTarget = selectedCmd in listOf("nbtstat -a", "pathping", "nslookup", "portscan")
        if (needsTarget) {
            OutlinedTextField(
                value = targetArg,
                onValueChange = { targetArg = it.trim() },
                label = {
                    Text(
                        when (selectedCmd) {
                            "nbtstat -a" -> "대상 로컬 IP (예: 192.168.0.10 또는 PC/NAS IP)"
                            "pathping" -> "경로·손실 분석 대상 IP / 도메인 (예: 8.8.8.8)"
                            else -> "대상 도메인 또는 IP 입력"
                        }
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    focusManager.clearFocus()
                    cmdOutput = "> $selectedCmd 실행 중...\n"
                    cmdJob = scope.launch(Dispatchers.IO) {
                        when (selectedCmd) {
                            "nbtstat -a" -> {
                                val res = NetworkHelper.runNbtStat(targetArg)
                                withContext(Dispatchers.Main) { cmdOutput = res }
                            }
                            "pathping" -> {
                                NetworkHelper.runPathPing(targetArg, maxHops = 15, samplesPerHop = 4) { line ->
                                    scope.launch(Dispatchers.Main) { cmdOutput += "$line\n" }
                                }
                            }
                            "netsh wlan" -> {
                                val res = NetworkHelper.getNetshWlan(context)
                                withContext(Dispatchers.Main) { cmdOutput = res }
                            }
                            "nslookup" -> {
                                val res = NetworkHelper.runNsLookup(targetArg)
                                withContext(Dispatchers.Main) { cmdOutput = res }
                            }
                            "portscan" -> {
                                val res = NetworkHelper.runPortScan(targetArg)
                                withContext(Dispatchers.Main) { cmdOutput = res }
                            }
                            "route print" -> {
                                val res = NetworkHelper.getRouteTable(context)
                                withContext(Dispatchers.Main) { cmdOutput = res }
                            }
                            else -> {
                                val res = NetworkHelper.runLocalSubnetScan(context)
                                withContext(Dispatchers.Main) { cmdOutput = res }
                            }
                        }
                    }
                },
                enabled = !running,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (running) "명령 처리 중..." else "$selectedCmd 실행")
            }

            if (running) {
                OutlinedButton(
                    onClick = {
                        cmdJob?.cancel()
                        cmdOutput += "\n[사용자에 의해 중지됨]\n"
                    }
                ) {
                    Text("중지")
                }
            }
        }

        TerminalBox(title = "CMD 실행 결과 [$selectedCmd]", content = cmdOutput)
    }
}

@Composable
fun TerminalBox(title: String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, color = Color(0xFF58A6FF), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFF30363D))
            SelectionContainer {
                Box(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    Text(
                        text = content,
                        color = Color(0xFFE6EDF3),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

object NetworkHelper {

    private fun httpGet(urlStr: String): String {
        val conn = (URL(urlStr).openConnection() as HttpURLConnection).apply {
            connectTimeout = 5000
            readTimeout = 5000
            requestMethod = "GET"
            setRequestProperty("User-Agent", "NetToolkitPro/1.3")
        }
        return try {
            conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
    }

    fun getDefaultGatewayIp(context: Context): String? = try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val lp = cm.getLinkProperties(cm.activeNetwork)
        lp?.routes?.firstOrNull { try { it.isDefaultRoute && it.gateway is Inet4Address } catch (_: Throwable) { false } }
            ?.gateway?.hostAddress
    } catch (_: Throwable) {
        null
    }

    fun getPrimaryIp(context: Context): String = try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val linkProps = cm.getLinkProperties(cm.activeNetwork)
        val addrs = linkProps?.linkAddresses?.map { it.address }.orEmpty()
        val ipv4 = addrs.firstOrNull { it is Inet4Address && !it.isLoopbackAddress }?.hostAddress
        val ipv6 = addrs.firstOrNull { it is Inet6Address && !it.isLoopbackAddress && !it.isLinkLocalAddress }?.hostAddress
        ipv4 ?: ipv6 ?: "연결 없음"
    } catch (_: Throwable) {
        "확인 불가"
    }

    fun getIpConfigAll(context: Context): String = try {
        buildString {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNet = cm.activeNetwork
            val lp: LinkProperties? = cm.getLinkProperties(activeNet)

            appendLine("=== 활성 어댑터 (Active LinkProperties) ===")
            if (lp == null) {
                appendLine("현재 활성화된 인터넷 네트워크가 없습니다.")
            } else {
                appendLine("인터페이스 이름 : ${lp.interfaceName ?: "N/A"}")
                appendLine("도메인 이름     : ${lp.domains ?: "N/A"}")
                val dhcp = try {
                    if (Build.VERSION.SDK_INT >= 30) lp.dhcpServerAddress?.hostAddress ?: "N/A" else "N/A"
                } catch (_: Throwable) { "N/A" }
                appendLine("DHCP 서버       : $dhcp")
                appendLine("MTU 크기        : ${if (lp.mtu > 0) lp.mtu else "기본값(1500)"}")

                lp.linkAddresses.forEach { la ->
                    val type = if (la.address is Inet4Address) "IPv4 주소" else "IPv6 주소"
                    val maskInfo = if (la.address is Inet4Address) " (서브넷: ${prefixToMask(la.prefixLength)})" else "/${la.prefixLength}"
                    appendLine("$type      : ${la.address.hostAddress}$maskInfo")
                }

                val gateways = lp.routes
                    .filter { try { it.isDefaultRoute } catch (_: Throwable) { false } }
                    .mapNotNull { it.gateway?.hostAddress }
                    .distinct()
                appendLine("기본 게이트웨이 : ${gateways.joinToString(", ").ifBlank { "N/A" }}")
                appendLine("DNS 서버 목록   : ${lp.dnsServers.mapNotNull { it.hostAddress }.joinToString(", ").ifBlank { "N/A" }}")
                appendLine("Private DNS     : ${if (lp.isPrivateDnsActive) "활성 (${lp.privateDnsServerName ?: "자동"})" else "비활성"}")
            }

            appendLine("\n=== 추가 네트워크 인터페이스 ===")
            try {
                val interfaces = NetworkInterface.getNetworkInterfaces()?.toList().orEmpty()
                if (interfaces.isEmpty()) {
                    appendLine("표시할 추가 인터페이스가 없습니다.")
                } else {
                    interfaces.forEach { nif ->
                        val up = try { nif.isUp && !nif.isLoopback } catch (_: Throwable) { false }
                        if (up) {
                            appendLine("[${nif.name}] ${nif.displayName}")
                            nif.interfaceAddresses.forEach { ia ->
                                appendLine("  - IP: ${ia.address.hostAddress} /${ia.networkPrefixLength}")
                            }
                        }
                    }
                }
            } catch (_: Throwable) {
                appendLine("Android 11+ 보안 정책으로 로우레벨 인터페이스 열람이 제한되었습니다.")
            }
        }
    } catch (t: Throwable) {
        "네트워크 정보 조회 중 오류: ${t.localizedMessage}"
    }

    fun getNatPublicIp(localIp: String): String = try {
        val obj = JSONObject(httpGet("https://ipwho.is/"))
        val publicIp = obj.optString("ip", "확인 실패")
        val isp = obj.optJSONObject("connection")?.optString("isp") ?: "N/A"
        val country = obj.optString("country", "")
        val city = obj.optString("city", "")
        val isBehindNat = localIp != publicIp && localIp != "연결 없음"
        buildString {
            appendLine("내부 할당 IP : $localIp")
            appendLine("외부 공인 IP : $publicIp")
            appendLine("NAT 상태     : ${if (isBehindNat) "NAT 공유기 / 통신사 게이트웨이 경유 중" else "공인 IP 직접 연결"}")
            append("ISP / 위치   : $isp ($city, $country)")
        }
    } catch (t: Throwable) {
        "외부 NAT IP 조회 실패: ${t.localizedMessage}"
    }

    fun tryReleaseRenew(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetwork?.let { cm.reportNetworkConnectivity(it, false) }
        } catch (_: Throwable) {
        }

        return try {
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "svc wifi disable && sleep 1 && svc wifi enable"))
            proc.waitFor() == 0
        } catch (_: Throwable) {
            false
        }
    }

    fun runNbtStat(targetIpInput: String): String {
        val ip = targetIpInput.trim()
        if (ip.isBlank()) return "조회할 IP 주소를 입력하세요 (예: 192.168.0.10)."
        return try {
            val addr = InetAddress.getByName(ip)
            val packetData = byteArrayOf(
                0x13, 0x37, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x20,
                0x43, 0x4B,
                0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41,
                0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41,
                0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41,
                0x41, 0x41, 0x41, 0x41, 0x41, 0x41,
                0x00,
                0x00, 0x21,
                0x00, 0x01
            )

            DatagramSocket().use { socket ->
                socket.soTimeout = 2000
                val sendPkt = DatagramPacket(packetData, packetData.size, addr, 137)
                socket.send(sendPkt)

                val recvBuf = ByteArray(1024)
                val recvPkt = DatagramPacket(recvBuf, recvBuf.size)
                socket.receive(recvPkt)

                val len = recvPkt.length
                val payloadStart = 56
                if (len <= payloadStart) {
                    return "NetBIOS 응답을 수신했으나 이름 테이블 데이터가 비어 있습니다 ($ip)."
                }

                val numNames = recvBuf[payloadStart].toInt() and 0xFF
                var cursor = payloadStart + 1
                val nameEntries = mutableListOf<String>()

                for (i in 0 until numNames) {
                    if (cursor + 18 > len) break
                    val rawName = String(recvBuf, cursor, 15, Charsets.ISO_8859_1).trim()
                    val suffix = recvBuf[cursor + 15].toInt() and 0xFF
                    val flags = ((recvBuf[cursor + 16].toInt() and 0xFF) shl 8) or (recvBuf[cursor + 17].toInt() and 0xFF)
                    val isGroup = (flags and 0x8000) != 0
                    val roleDesc = when (suffix) {
                        0x00 -> if (isGroup) "Workgroup / Domain" else "Workstation Service"
                        0x03 -> "Messenger Service"
                        0x1B -> "Domain Master Browser"
                        0x1C -> "Domain Controllers"
                        0x1D -> "Master Browser"
                        0x1E -> "Browser Service Elections"
                        0x20 -> "File Server Service"
                        else -> "Code 0x" + String.format("%02X", suffix)
                    }
                    val typeLabel = if (isGroup) "<GROUP>" else "<UNIQUE>"
                    nameEntries.add(String.format("  %-16s %-8s %s", rawName, typeLabel, roleDesc))
                    cursor += 18
                }

                val macAddress = if (cursor + 6 <= len) {
                    (0 until 6).joinToString("-") { idx ->
                        String.format("%02X", recvBuf[cursor + idx].toInt() and 0xFF)
                    }
                } else {
                    "확인 불가"
                }

                buildString {
                    appendLine("NetBIOS 원격 컴퓨터 상태 (UDP/137)")
                    appendLine("대상 IP : $ip (${addr.hostName})")
                    appendLine("MAC 주소: $macAddress")
                    appendLine("---------------------------------------------------")
                    appendLine("NetBIOS 이름 테이블:")
                    if (nameEntries.isEmpty()) {
                        appendLine("  (이름 레코드 없음)")
                    } else {
                        nameEntries.forEach { appendLine(it) }
                    }
                }
            }
        } catch (_: SocketTimeoutException) {
            "대상 [$ip] UDP 137 포트 응답 시간 초과 (방화벽 차단, NetBIOS 비활성, 또는 동일 서브넷 외 호스트일 수 있습니다)."
        } catch (t: Throwable) {
            "nbtstat 실행 오류: ${t.localizedMessage}"
        }
    }

    suspend fun runPathPing(target: String, maxHops: Int, samplesPerHop: Int, onLine: (String) -> Unit) {
        val safeTarget = target.replace(Regex("[^a-zA-Z0-9.:-]"), "")
        val resolvedIp = try {
            InetAddress.getByName(safeTarget).hostAddress ?: safeTarget
        } catch (_: Throwable) {
            onLine("호스트를 찾을 수 없습니다: $safeTarget")
            return
        }

        onLine("대상: $safeTarget ($resolvedIp) | 최대 $maxHops 홉, 홉당 $samplesPerHop 회 샘플링")
        onLine("HOP  IP 주소          손실률     최소/평균/최대 RTT")
        onLine("----------------------------------------------------------")

        val ipRegex = Regex("(\d{1,3}(?:\.\d{1,3}){3})")
        val rttRegex = Regex("time[=<]?\s*(\d+(?:\.\d+)?)\s*ms")

        for (ttl in 1..maxHops) {
            if (!currentCoroutineContext().isActive) break
            var hopIp: String? = null
            val rtts = mutableListOf<Double>()
            var received = 0

            for (s in 1..samplesPerHop) {
                if (!currentCoroutineContext().isActive) break
                val startMs = System.currentTimeMillis()
                var proc: Process? = null
                val output = try {
                    proc = ProcessBuilder("ping", "-c", "1", "-t", ttl.toString(), "-W", "2", resolvedIp)
                        .redirectErrorStream(true)
                        .start()
                    proc.inputStream.bufferedReader().use { it.readText() }
                } catch (_: Throwable) {
                    ""
                } finally {
                    proc?.destroy()
                }
                val elapsed = (System.currentTimeMillis() - startMs).toDouble()
                val bodyLines = output.lines().drop(1).joinToString(" ")
                val foundIp = ipRegex.find(bodyLines)?.value
                if (foundIp != null) {
                    if (hopIp == null) hopIp = foundIp
                    received++
                    val parsedRtt = rttRegex.find(bodyLines)?.groupValues?.getOrNull(1)?.toDoubleOrNull() ?: elapsed
                    rtts.add(parsedRtt)
                }
            }

            val lossPercent = ((samplesPerHop - received) * 100.0) / samplesPerHop.toDouble()
            val statsText = if (rtts.isNotEmpty()) {
                val minRtt = rtts.minOrNull() ?: 0.0
                val avgRtt = rtts.average()
                val maxRtt = rtts.maxOrNull() ?: 0.0
                String.format("%.1f / %.1f / %.1f ms", minRtt, avgRtt, maxRtt)
            } else {
                "N/A (응답 없음)"
            }

            val displayIp = hopIp ?: "*.*.*.*"
            onLine(String.format("%2d   %-16s %5.1f%%     %s", ttl, displayIp, lossPercent, statsText))

            if (hopIp == resolvedIp) {
                onLine("----------------------------------------------------------")
                onLine("최종 목적지 도달 완료.")
                break
            }
        }
    }

    fun getNetshWlan(context: Context): String = try {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNet = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(activeNet)
        val hasWifiTransport = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true

        val appContext = context.applicationContext
        val wifiMgr = appContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        val wifiInfo: WifiInfo? = @Suppress("DEPRECATION") wifiMgr?.connectionInfo

        buildString {
            appendLine("=== 무선 LAN 인터페이스 상태 (netsh wlan show interfaces) ===")
            appendLine("Wi-Fi 활성화   : ${if (wifiMgr?.isWifiEnabled == true) "예 (Enabled)" else "아니오 (Disabled)"}")
            appendLine("현재 전송 경로 : ${if (hasWifiTransport) "Wi-Fi 연결됨" else "Wi-Fi 미연결 (모바일 데이터 또는 오프라인)"}")

            if (wifiInfo != null && (hasWifiTransport || wifiInfo.networkId != -1)) {
                val rawSsid = wifiInfo.ssid ?: ""
                val cleanSsid = rawSsid.removePrefix("\"").removeSuffix("\"")
                val displaySsid = if (cleanSsid.isBlank() || cleanSsid == "<unknown ssid>") {
                    "비공개/위치권한 필요 (<unknown ssid>)"
                } else {
                    cleanSsid
                }
                val bssid = wifiInfo.bssid?.takeIf { it.isNotBlank() && it != "02:00:00:00:00:00" } ?: "비공개/위치권한 필요"
                val rssi = wifiInfo.rssi
                val signalQuality = ((rssi + 100) * 2).coerceIn(0, 100)
                val linkSpeed = wifiInfo.linkSpeed
                val freqMhz = wifiInfo.frequency
                val bandLabel = when {
                    freqMhz >= 5925 -> "6 GHz (Wi-Fi 6E / 7)"
                    freqMhz in 5000..5924 -> "5 GHz"
                    freqMhz in 2400..2500 -> "2.4 GHz"
                    else -> "알 수 없음"
                }
                val ipInt = wifiInfo.ipAddress
                val ipStr = if (ipInt != 0) {
                    val bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()
                    InetAddress.getByAddress(bytes).hostAddress
                } else {
                    getPrimaryIp(context)
                }

                appendLine("SSID           : $displaySsid")
                appendLine("BSSID (AP MAC) : $bssid")
                appendLine("IPv4 주소      : $ipStr")
                appendLine("신호 강도      : $rssi dBm (품질 약 $signalQuality%)")
                appendLine("수신 링크 속도 : $linkSpeed Mbps")
                if (Build.VERSION.SDK_INT >= 29) {
                    val txSpeed = try { wifiInfo.txLinkSpeedMbps } catch (_: Throwable) { -1 }
                    val rxSpeed = try { wifiInfo.rxLinkSpeedMbps } catch (_: Throwable) { -1 }
                    if (txSpeed > 0 || rxSpeed > 0) {
                        appendLine("송/수신 속도   : TX \${txSpeed} Mbps / RX \${rxSpeed} Mbps")
                    }
                }
                appendLine("주파수 대역    : $freqMhz MHz ($bandLabel)")
                if (Build.VERSION.SDK_INT >= 30) {
                    val wifiStd = try {
                        when (wifiInfo.wifiStandard) {
                            1 -> "802.11a/b/g (Wi-Fi 1/2/3)"
                            4 -> "802.11n (Wi-Fi 4)"
                            5 -> "802.11ac (Wi-Fi 5)"
                            6 -> "802.11ax (Wi-Fi 6)"
                            7 -> "802.11be (Wi-Fi 7)"
                            else -> "표준 코드 \${wifiInfo.wifiStandard}"
                        }
                    } catch (_: Throwable) { "N/A" }
                    appendLine("무선 규격      : $wifiStd")
                }
            } else {
                appendLine("연결된 Wi-Fi 세션 정보가 없습니다.")
            }
        }
    } catch (t: Throwable) {
        "netsh wlan 조회 오류: ${t.localizedMessage}"
    }

    suspend fun streamPing(host: String, count: Int, onLine: (String) -> Unit) {
        val safeHost = host.replace(Regex("[^a-zA-Z0-9.:-]"), "")
        if (safeHost.isBlank()) {
            onLine("유효한 호스트 또는 IP를 입력하세요.")
            return
        }
        var process: Process? = null
        try {
            process = ProcessBuilder("ping", "-c", count.toString(), safeHost)
                .redirectErrorStream(true)
                .start()
            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String? = null
                while (currentCoroutineContext().isActive && reader.readLine().also { line = it } != null) {
                    line?.let { onLine(it) }
                }
            }
        } catch (t: Throwable) {
            onLine("Ping 실행 오류: ${t.localizedMessage}")
        } finally {
            process?.destroy()
        }
    }

    suspend fun runTraceroute(target: String, maxHops: Int, onHop: (TraceHop, String) -> Unit) {
        val safeTarget = target.replace(Regex("[^a-zA-Z0-9.:-]"), "")
        val resolvedIp = try {
            InetAddress.getByName(safeTarget).hostAddress ?: safeTarget
        } catch (_: Throwable) {
            onHop(TraceHop(0, "DNS 실패", 0), "호스트를 찾을 수 없습니다: $safeTarget")
            return
        }

        val ipRegex = Regex("(\d{1,3}(?:\.\d{1,3}){3})")
        for (ttl in 1..maxHops) {
            if (!currentCoroutineContext().isActive) break
            val startMs = System.currentTimeMillis()
            var proc: Process? = null
            val output = try {
                proc = ProcessBuilder("ping", "-c", "1", "-t", ttl.toString(), "-W", "2", resolvedIp)
                    .redirectErrorStream(true)
                    .start()
                proc.inputStream.bufferedReader().use { it.readText() }
            } catch (_: Throwable) {
                ""
            } finally {
                proc?.destroy()
            }
            val elapsed = System.currentTimeMillis() - startMs

            val responseLines = output.lines().drop(1).joinToString(" ")
            val hopIp = ipRegex.find(responseLines)?.value ?: "*"

            val hopObj = if (hopIp != "*") {
                fetchGeoForHop(ttl, hopIp, elapsed)
            } else {
                TraceHop(ttl, "* (응답 없음)", elapsed)
            }

            val locTag = listOf(hopObj.city, hopObj.country).filter { it.isNotBlank() }.joinToString(", ")
            val formatted = String.format("%2d  %-16s  %4dms  %s", ttl, hopIp, elapsed, locTag)
            onHop(hopObj, formatted)

            if (hopIp == resolvedIp) break
        }
    }

    fun fetchGeoForHop(hopNum: Int, ip: String, rttMs: Long): TraceHop {
        if (ip.isNotBlank() && isPrivateIp(ip)) {
            return TraceHop(hopNum, ip, rttMs, city = "로컬/통신사 사설 게이트웨이")
        }
        return try {
            val endpoint = if (ip.isBlank()) "https://ipwho.is/" else "https://ipwho.is/$ip"
            val obj = JSONObject(httpGet(endpoint))
            if (!obj.optBoolean("success", false)) return TraceHop(hopNum, ip, rttMs)
            TraceHop(
                hop = hopNum,
                ip = obj.optString("ip", ip),
                rttMs = rttMs,
                city = obj.optString("city", ""),
                country = obj.optString("country_code", ""),
                isp = obj.optJSONObject("connection")?.optString("isp", "") ?: "",
                lat = obj.optDouble("latitude").takeIf { !it.isNaN() && it.isFinite() },
                lon = obj.optDouble("longitude").takeIf { !it.isNaN() && it.isFinite() }
            )
        } catch (_: Throwable) {
            TraceHop(hopNum, ip, rttMs)
        }
    }

    fun lookupWhoisAndGeo(query: String): String = try {
        val targetIp = if (query.isBlank()) "" else InetAddress.getByName(query).hostAddress ?: query
        val url = if (targetIp.isBlank()) "https://ipwho.is/" else "https://ipwho.is/$targetIp"
        val obj = JSONObject(httpGet(url))

        if (!obj.optBoolean("success", false)) {
            "조회 실패: ${obj.optString("message", "유효하지 않은 주소")}"
        } else {
            val conn = obj.optJSONObject("connection")
            val tz = obj.optJSONObject("timezone")
            buildString {
                appendLine("입력 쿼리   : ${query.ifBlank { "(내 공인 IP)" }}")
                appendLine("대상 IP     : ${obj.optString("ip")} (${obj.optString("type")})")
                appendLine("대륙 / 국가 : ${obj.optString("continent")} / ${obj.optString("country")} (${obj.optString("country_code")})")
                appendLine("지역 / 도시 : ${obj.optString("region")} / ${obj.optString("city")}")
                appendLine("우편번호    : ${obj.optString("postal", "N/A")}")
                appendLine("위도 / 경도 : ${obj.optDouble("latitude")}, ${obj.optDouble("longitude")}")
                appendLine("타임존      : ${tz?.optString("id")} (UTC ${tz?.optString("utc")})")
                appendLine("----------------------------------------")
                appendLine("ASN 번호    : AS${conn?.optInt("asn")}")
                appendLine("ISP 사업자  : ${conn?.optString("isp")}")
                appendLine("소속 기관   : ${conn?.optString("org")}")
                appendLine("도메인      : ${conn?.optString("domain")}")
            }
        }
    } catch (t: Throwable) {
        "Whois / GeoIP 오류: ${t.localizedMessage}"
    }

    fun runNsLookup(domain: String): String = try {
        val addresses = InetAddress.getAllByName(domain)
        val dnsGoogle = httpGet("https://dns.google/resolve?name=$domain&type=ANY")
        val json = JSONObject(dnsGoogle)
        val answers = json.optJSONArray("Answer")

        buildString {
            appendLine("Server: System Resolver & Google DoH (8.8.8.8)")
            appendLine("Name  : $domain\n")
            addresses.forEach { addr ->
                appendLine("Address: ${addr.hostAddress}")
            }
            if (answers != null) {
                appendLine("\n=== 상세 DNS 레코드 ===")
                for (i in 0 until answers.length()) {
                    val rec = answers.getJSONObject(i)
                    appendLine("Type ${rec.optInt("type")} | TTL ${rec.optInt("TTL")}s -> ${rec.optString("data")}")
                }
            }
        }
    } catch (t: Throwable) {
        "nslookup 실패: ${t.localizedMessage}"
    }

    suspend fun runPortScan(host: String): String = coroutineScope {
        val ports = mapOf(
            21 to "FTP", 22 to "SSH", 23 to "Telnet", 25 to "SMTP",
            53 to "DNS", 80 to "HTTP", 110 to "POP3", 143 to "IMAP",
            443 to "HTTPS", 3306 to "MySQL", 3389 to "RDP", 8080 to "HTTP-Alt"
        )
        val ip = try {
            InetAddress.getByName(host).hostAddress ?: host
        } catch (_: Throwable) {
            return@coroutineScope "호스트 해석 실패: $host"
        }

        val results = ports.map { (port, name) ->
            async(Dispatchers.IO) {
                val open = try {
                    Socket().use { it.connect(InetSocketAddress(ip, port), 700) }
                    true
                } catch (_: Throwable) {
                    false
                }
                Triple(port, name, open)
            }
        }.awaitAll()

        buildString {
            appendLine("대상 호스트: $host ($ip) 주요 포트 상태")
            appendLine("PORT     SERVICE     STATE")
            appendLine("-------------------------------")
            results.forEach { (port, service, open) ->
                val state = if (open) "OPEN" else "CLOSED/FILTERED"
                appendLine(String.format("%-8d %-11s %s", port, service, state))
            }
        }
    }

    fun getRouteTable(context: Context): String = try {
        buildString {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val lp = cm.getLinkProperties(cm.activeNetwork)
            if (lp == null) {
                appendLine("활성 라우팅 테이블이 없습니다.")
            } else {
                appendLine("인터페이스: ${lp.interfaceName}")
                appendLine("대상 네트워크(Destination)       게이트웨이(Gateway)     기본경로")
                appendLine("-----------------------------------------------------------------")
                lp.routes.forEach { route ->
                    val dest = route.destination.toString()
                    val gw = route.gateway?.hostAddress ?: "On-link"
                    val isDef = try { route.isDefaultRoute } catch (_: Throwable) { false }
                    appendLine(String.format("%-30s %-22s %s", dest, gw, if (isDef) "YES" else "NO"))
                }
            }
        }
    } catch (t: Throwable) {
        "라우팅 테이블 조회 오류: ${t.localizedMessage}"
    }

    suspend fun runLocalSubnetScan(context: Context): String = coroutineScope {
        val myIp = getPrimaryIp(context)
        val parts = myIp.split(".")
        if (parts.size != 4) {
            return@coroutineScope "IPv4 사설망(Wi-Fi 등) 연결 시 서브넷 스캔이 가능합니다.\n현재 할당 IP: $myIp"
        }
        val prefix = "${parts[0]}.${parts[1]}.${parts[2]}"

        val reachableHosts = (1..254).map { lastOctet ->
            async(Dispatchers.IO) {
                val candidate = "$prefix.$lastOctet"
                val ok = try { InetAddress.getByName(candidate).isReachable(350) } catch (_: Throwable) { false }
                if (ok) candidate else null
            }
        }.awaitAll().filterNotNull()

        buildString {
            appendLine("로컬 서브넷 활성 호스트 탐색 ($prefix.0/24)")
            appendLine("--------------------------------------------")
            if (reachableHosts.isEmpty()) appendLine("응답한 기기가 없습니다.")
            reachableHosts.forEach { host ->
                val tag = if (host == myIp) " (내 기기)" else if (host.endsWith(".1")) " (게이트웨이 추정)" else ""
                appendLine("응답 확인 : $host$tag")
            }
        }
    }

    private fun prefixToMask(prefix: Int): String {
        val p = prefix.coerceIn(0, 32)
        val mask = if (p == 0) 0 else -1 shl (32 - p)
        return "${(mask ushr 24) and 0xFF}.${(mask ushr 16) and 0xFF}.${(mask ushr 8) and 0xFF}.${mask and 0xFF}"
    }

    private fun isPrivateIp(ip: String): Boolean =
        ip.startsWith("10.") || ip.startsWith("192.168.") || ip.startsWith("127.") ||
        ip.matches(Regex("^172\.(1[6-9]|2\d|3[0-1])\..*"))
}
