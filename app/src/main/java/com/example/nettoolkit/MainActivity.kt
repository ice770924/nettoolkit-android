package com.example.nettoolkit

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.LinkProperties
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
import androidx.compose.foundation.gestures.detectTransformGestures
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
                title = { Text("NetToolkit Pro", fontWeight = FontWeight.Bold) },
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

// 1. ipconfig /all + NAT IP + Release / Renew
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

// 2. Ping 화면 (입력창을 상단에 배치하여 화상 키보드 가림 완벽 차단)
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

// 3. Tracert + Visual Traceroute 화면
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
            ) { Text("시각화 맵 (Visual Route)") }
        }

        if (viewMode == 0) {
            TerminalBox(title = "Traceroute 로그 (최대 20 Hops)", content = textLog)
        } else {
            VisualTracerouteMap(hops = hops, isRunning = isRunning)
        }
    }
}

@Composable
fun VisualTracerouteMap(hops: List<TraceHop>, isRunning: Boolean) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val geoHops = hops.filter { it.lat != null && it.lon != null && it.lat.isFinite() && it.lon.isFinite() }

    val labelPaint = remember {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = 28f
            isFakeBoldText = true
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0A1628))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 4f)
                        offset = if (scale == 1f) Offset.Zero else offset + pan
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                if (w <= 0f || h <= 0f) return@Canvas

                fun project(lat: Double, lon: Double): Offset {
                    val x = ((lon + 180.0) / 360.0 * w).toFloat()
                    val y = ((90.0 - lat) / 180.0 * h).toFloat()
                    return Offset(
                        x = (x - w / 2f) * scale + w / 2f + offset.x,
                        y = (y - h / 2f) * scale + h / 2f + offset.y
                    )
                }

                val gridColor = Color(0xFF1A3152)
                for (lon in -150..150 step 30) {
                    val p1 = project(90.0, lon.toDouble())
                    val p2 = project(-90.0, lon.toDouble())
                    drawLine(gridColor, p1, p2, strokeWidth = 1f)
                }
                for (lat in -60..60 step 30) {
                    val p1 = project(lat.toDouble(), -180.0)
                    val p2 = project(lat.toDouble(), 180.0)
                    drawLine(gridColor, p1, p2, strokeWidth = 1f)
                }

                for (i in 0 until geoHops.size - 1) {
                    val start = project(geoHops[i].lat!!, geoHops[i].lon!!)
                    val end = project(geoHops[i + 1].lat!!, geoHops[i + 1].lon!!)
                    val mid = Offset((start.x + end.x) / 2f, minOf(start.y, end.y) - 35f * scale)
                    val path = Path().apply {
                        moveTo(start.x, start.y)
                        quadraticTo(mid.x, mid.y, end.x, end.y)
                    }
                    drawPath(
                        path = path,
                        color = Color(0xFF38BDF8),
                        style = Stroke(
                            width = 3.5f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f), 0f)
                        )
                    )
                }

                geoHops.forEach { hop ->
                    val pt = project(hop.lat!!, hop.lon!!)
                    val nodeColor = when {
                        hop.hop == 0 -> Color(0xFF22C55E)
                        hop.rttMs < 50 -> Color(0xFF38BDF8)
                        hop.rttMs < 150 -> Color(0xFFFACC15)
                        else -> Color(0xFFF87171)
                    }
                    drawCircle(color = nodeColor.copy(alpha = 0.28f), radius = 14f, center = pt)
                    drawCircle(color = nodeColor, radius = 6.5f, center = pt)

                    val label = if (hop.hop == 0) "내 위치 (${hop.city})" else "#${hop.hop} ${hop.city.ifBlank { hop.country }}"
                    drawContext.canvas.nativeCanvas.drawText(label, pt.x + 10f, pt.y - 10f, labelPaint)
                }
            }

            Text(
                text = if (isRunning) "실시간 경로 지오코딩 중... (두 손가락 줌/드래그)" else "두 손가락 줌/드래그 지원 | 탐색 홉: ${hops.size}",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
            )
        }

        if (hops.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                hops.forEach { hop ->
                    AssistChip(
                        onClick = {},
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

// 4. Whois 화면
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

// 5. 기타 CMD 네트워크 명령어 화면
@Composable
fun CmdToolsScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var selectedCmd by remember { mutableStateOf("nslookup") }
    var targetArg by remember { mutableStateOf("naver.com") }
    var cmdOutput by remember { mutableStateOf("실행할 네트워크 명령어를 선택하고 실행을 누르세요.\n") }
    var running by remember { mutableStateOf(false) }

    val commands = listOf("nslookup", "portscan (netstat 대용)", "route print", "subnet scan (arp 대용)")

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
                    onClick = { selectedCmd = cmd },
                    label = { Text(cmd) }
                )
            }
        }

        if (selectedCmd.startsWith("nslookup") || selectedCmd.startsWith("portscan")) {
            OutlinedTextField(
                value = targetArg,
                onValueChange = { targetArg = it.trim() },
                label = { Text("대상 도메인 또는 IP 입력") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }

        Button(
            onClick = {
                focusManager.clearFocus()
                running = true
                cmdOutput = "> $selectedCmd 실행 중...\n"
                scope.launch {
                    cmdOutput = withContext(Dispatchers.IO) {
                        when {
                            selectedCmd.startsWith("nslookup") -> NetworkHelper.runNsLookup(targetArg)
                            selectedCmd.startsWith("portscan") -> NetworkHelper.runPortScan(targetArg)
                            selectedCmd.startsWith("route") -> NetworkHelper.getRouteTable(context)
                            else -> NetworkHelper.runLocalSubnetScan(context)
                        }
                    }
                    running = false
                }
            },
            enabled = !running,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (running) "명령 처리 중..." else "$selectedCmd 실행")
        }

        TerminalBox(title = "CMD 실행 결과 [$selectedCmd]", content = cmdOutput)
    }
}

// 중첩 세로 스크롤 크래시를 방지하기 위해 단일 컨테이너 + 가로 스크롤/텍스트 복사 지원
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
            setRequestProperty("User-Agent", "NetToolkitPro/1.1")
        }
        return try {
            conn.inputStream.bufferedReader().use { it.readText() }
        } finally {
            conn.disconnect()
        }
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
                appendLine("Android 11+ 보안 정책으로 로우레벨 인터페이스 열람이 제한되었습니다 (상단 활성 어댑터 정보를 참고하세요).")
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

        val ipRegex = Regex("""(\d{1,3}(?:\.\d{1,3}){3})""")
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
        ip.matches(Regex("""^172\.(1[6-9]|2\d|3[0-1])\..*"""))
}
