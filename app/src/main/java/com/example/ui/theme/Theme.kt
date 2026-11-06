package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BcnsCyan,
    onPrimary = Color(0xFF00344A),
    primaryContainer = BcnsNightPanel,
    onPrimaryContainer = Color(0xFFBCE9FF),
    secondary = BcnsGold,
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5B4300),
    onSecondaryContainer = Color(0xFFFFDF9E),
    tertiary = BcnsCyanDark,
    onTertiary = Color.White,
    background = BcnsNightBg,
    onBackground = BcnsNightText,
    surface = BcnsNightSurface,
    onSurface = BcnsNightText,
    surfaceVariant = BcnsNightPanel,
    onSurfaceVariant = BcnsNightTextMuted,
    outline = BcnsNightBorder,
    error = BcnsDanger
)

private val LightColorScheme = lightColorScheme(
    primary = BcnsCyanDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCE8F8),
    onPrimaryContainer = Color(0xFF001F2E),
    secondary = BcnsGoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFECC2),
    onSecondaryContainer = Color(0xFF261900),
    tertiary = BcnsCyan,
    onTertiary = Color.White,
    background = BcnsLightBg,
    onBackground = BcnsLightText,
    surface = BcnsLightSurface,
    onSurface = BcnsLightText,
    surfaceVariant = BcnsLightPanel,
    onSurfaceVariant = BcnsLightTextMuted,
    outline = BcnsLightBorder,
    error = BcnsDanger
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand identity by default
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
