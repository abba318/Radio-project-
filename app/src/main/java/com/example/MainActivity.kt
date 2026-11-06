package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.playback.PlaybackState
import com.example.playback.RadioManager
import com.example.ui.components.StationHeader
import com.example.ui.components.VisualizerBars
import com.example.ui.screens.AdvertRatesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NewsroomScreen
import com.example.ui.screens.ScheduleScreen
import com.example.ui.screens.StationInfoScreen
import com.example.ui.theme.BcnsCyan
import com.example.ui.theme.BcnsGold
import com.example.ui.theme.BcnsNightBg
import com.example.ui.theme.BcnsSuccess
import com.example.ui.theme.MyApplicationTheme

enum class MainTab {
    RADIO,
    SCHEDULE,
    NEWSROOM,
    INFO
}

class MainActivity : ComponentActivity() {

    private lateinit var radioManager: RadioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        radioManager = RadioManager.getInstance(applicationContext)

        setContent {
            MyApplicationTheme {
                MainApp(radioManager = radioManager)
            }
        }
    }
}

@Composable
fun MainApp(radioManager: RadioManager) {
    var currentTab by remember { mutableStateOf(MainTab.RADIO) }
    var infoSubTabIndex by remember { mutableIntStateOf(0) }

    val playbackState by radioManager.state.collectAsStateWithLifecycle()
    val volume by radioManager.volume.collectAsStateWithLifecycle()
    val statusDetail by radioManager.statusDetail.collectAsStateWithLifecycle()
    val sleepTimerMinutes by radioManager.sleepTimerMinutes.collectAsStateWithLifecycle()
    val visualizerAmplitudes by radioManager.visualizerAmplitudes.collectAsStateWithLifecycle()
    val currentStreamUrl by radioManager.streamUrl.collectAsStateWithLifecycle()
    val errorMessage by radioManager.errorMessage.collectAsStateWithLifecycle()

    BackHandler(enabled = currentTab != MainTab.RADIO) {
        currentTab = MainTab.RADIO
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BcnsNightBg),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            StationHeader()
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Persistent Mini Player when browsing other screens and audio is active
                AnimatedVisibility(
                    visible = (currentTab != MainTab.RADIO) && (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.CONNECTING),
                    enter = slideInVertically { it },
                    exit = slideOutVertically { it }
                ) {
                    MiniPlayerBar(
                        playbackState = playbackState,
                        visualizerAmplitudes = visualizerAmplitudes,
                        onTogglePlay = { radioManager.togglePlayPause() },
                        onBarClick = { currentTab = MainTab.RADIO }
                    )
                }

                // Adaptive Navigation Bar (4 high-contrast tabs)
                NavigationBar(
                    containerColor = Color(0xFF040E16),
                    contentColor = Color(0xFF90AFC5),
                    tonalElevation = 6.dp,
                    modifier = Modifier.border(
                        1.dp,
                        Color(0x2297B1C7),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                ) {
                    NavigationBarItem(
                        selected = currentTab == MainTab.RADIO,
                        onClick = { currentTab = MainTab.RADIO },
                        icon = { Icon(Icons.Default.Radio, contentDescription = "Radio") },
                        label = { Text("Radio", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.SCHEDULE,
                        onClick = { currentTab = MainTab.SCHEDULE },
                        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Schedule") },
                        label = { Text("Schedule", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_schedule")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.NEWSROOM,
                        onClick = { currentTab = MainTab.NEWSROOM },
                        icon = { Icon(Icons.Default.Newspaper, contentDescription = "Newsroom") },
                        label = { Text("News", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_newsroom")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.INFO,
                        onClick = { currentTab = MainTab.INFO },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Station Info") },
                        label = { Text("Rates & Info", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = navItemColors(),
                        modifier = Modifier.testTag("nav_station_info")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BcnsNightBg)
        ) {
            when (currentTab) {
                MainTab.RADIO -> {
                    HomeScreen(
                        playbackState = playbackState,
                        volume = volume,
                        statusDetail = statusDetail,
                        sleepTimerMinutes = sleepTimerMinutes,
                        visualizerAmplitudes = visualizerAmplitudes,
                        currentStreamUrl = currentStreamUrl,
                        primaryStreamUrl = radioManager.primaryStreamUrl,
                        backupStreamUrl = radioManager.backupStreamUrl,
                        errorMessage = errorMessage,
                        onTogglePlay = { radioManager.togglePlayPause() },
                        onVolumeChange = { radioManager.setVolume(it) },
                        onSetSleepTimer = { radioManager.setSleepTimer(it) },
                        onCancelSleepTimer = { radioManager.cancelSleepTimer() },
                        onSwitchStream = { radioManager.switchStream(it) },
                        onNavigateToSchedule = { currentTab = MainTab.SCHEDULE },
                        onNavigateToNews = { currentTab = MainTab.NEWSROOM },
                        onNavigateToRates = {
                            infoSubTabIndex = 0
                            currentTab = MainTab.INFO
                        }
                    )
                }

                MainTab.SCHEDULE -> {
                    ScheduleScreen(
                        playbackState = playbackState,
                        onListenLiveClick = {
                            radioManager.play()
                            currentTab = MainTab.RADIO
                        }
                    )
                }

                MainTab.NEWSROOM -> {
                    NewsroomScreen()
                }

                MainTab.INFO -> {
                    // Sub-tab switcher between Advert Rates and About Station
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TabRow(
                            selectedTabIndex = infoSubTabIndex,
                            containerColor = Color(0xFF04111B),
                            contentColor = BcnsCyan,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[infoSubTabIndex]),
                                    color = BcnsCyan
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 680.dp)
                        ) {
                            Tab(
                                selected = infoSubTabIndex == 0,
                                onClick = { infoSubTabIndex = 0 },
                                text = {
                                    Text(
                                        "Advert Rate Card",
                                        fontWeight = if (infoSubTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            )
                            Tab(
                                selected = infoSubTabIndex == 1,
                                onClick = { infoSubTabIndex = 1 },
                                text = {
                                    Text(
                                        "About & Studio Contacts",
                                        fontWeight = if (infoSubTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            )
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            if (infoSubTabIndex == 0) {
                                AdvertRatesScreen()
                            } else {
                                StationInfoScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniPlayerBar(
    playbackState: PlaybackState,
    visualizerAmplitudes: List<Float>,
    onTogglePlay: () -> Unit,
    onBarClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A2234))
            .border(1.dp, Color(0x3397B1C7))
            .clickable { onBarClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (playbackState == PlaybackState.PLAYING) BcnsSuccess else BcnsGold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Crystal FM 91.3 Minna Live",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = if (playbackState == PlaybackState.PLAYING) "Streaming live feed • Tap to view" else "Connecting...",
                        color = BcnsCyan,
                        fontSize = 10.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                VisualizerBars(
                    amplitudes = visualizerAmplitudes,
                    playbackState = playbackState,
                    height = 20.dp,
                    barWidth = 3.dp,
                    spacing = 2.dp,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(BcnsCyan)
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    if (playbackState == PlaybackState.CONNECTING) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (playbackState == PlaybackState.PLAYING) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Toggle stream",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = BcnsCyan,
    selectedTextColor = BcnsCyan,
    indicatorColor = Color(0xFF0F324C),
    unselectedIconColor = Color(0xFF7597AF),
    unselectedTextColor = Color(0xFF7597AF)
)
