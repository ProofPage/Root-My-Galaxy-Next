package dev.busung.s25uroot.ui.theme

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme
import dev.busung.s25uroot.AccentColor
import dev.busung.s25uroot.AppThemeMode
import dev.busung.s25uroot.HelperTint
import dev.busung.s25uroot.R
import dev.busung.s25uroot.VerdictTint

private val Pretendard = FontFamily(
    Font(R.font.pretendard_variable),
)

private val AppTypography = Typography(
    displaySmall = TextStyle(fontFamily = Pretendard, fontSize = 38.sp, lineHeight = 44.sp, fontWeight = FontWeight.Normal),
    headlineLarge = TextStyle(fontFamily = Pretendard, fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = TextStyle(fontFamily = Pretendard, fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.SemiBold),
    headlineSmall = TextStyle(fontFamily = Pretendard, fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontFamily = Pretendard, fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontFamily = Pretendard, fontSize = 17.sp, lineHeight = 23.sp, fontWeight = FontWeight.Medium),
    titleSmall = TextStyle(fontFamily = Pretendard, fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontFamily = Pretendard, fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontFamily = Pretendard, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontFamily = Pretendard, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontFamily = Pretendard, fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontFamily = Pretendard, fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

private fun accentSeed(context: Context, accentColor: AccentColor): Color = when (accentColor) {
    AccentColor.Dynamic -> Color(context.getColor(android.R.color.system_accent1_500))
    AccentColor.Blue -> Color(0xFF3182F6) // Toss Blue
    AccentColor.Violet -> Color(0xFF7C4DFF)
    AccentColor.Green -> Color(0xFF2E7D32)
    AccentColor.Orange -> Color(0xFFE65100)
}

@Composable
fun RootMyGalaxyTheme(
    accentColor: AccentColor,
    themeMode: AppThemeMode,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val systemDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        AppThemeMode.System -> systemDarkTheme
        AppThemeMode.Light -> false
        AppThemeMode.Dark -> true
    }
    val colors = if (accentColor == AccentColor.Dynamic) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        val generatedColors = rememberDynamicColorScheme(
            seedColor = accentSeed(context, accentColor),
            isDark = darkTheme,
            style = PaletteStyle.TonalSpot,
            specVersion = ColorSpec.SpecVersion.SPEC_2025,
        )
        if (darkTheme) {
            generatedColors.copy(
                background = Color(0xFF101012),
                surface = Color(0xFF101012),
                surfaceContainer = Color(0xFF101012),
                surfaceContainerLow = Color(0xFF17171C),
                surfaceContainerLowest = Color(0xFF101012),
                surfaceContainerHigh = Color(0xFF1C1C22),
                surfaceContainerHighest = Color(0xFF222228),
                onSurface = Color(0xFFF2F4F6),
                onSurfaceVariant = Color(0xFF8B95A1),
                outline = Color(0xFF333D4B),
                outlineVariant = Color(0xFF222228),
            )
        } else {
            generatedColors.copy(
                background = Color(0xFFF2F4F6),
                surface = Color(0xFFFFFFFF),
                surfaceContainer = Color(0xFFF2F4F6),
                surfaceContainerLow = Color(0xFFF9FAFB),
                surfaceContainerLowest = Color(0xFFFFFFFF),
                surfaceContainerHigh = Color(0xFFE5E8EB),
                surfaceContainerHighest = Color(0xFFFFFFFF),
                onSurface = Color(0xFF191F28),
                onSurfaceVariant = Color(0xFF6B7684),
            )
        }
    }

    SideEffect {
        val window = (context as Activity).window
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = !darkTheme
            isAppearanceLightNavigationBars = !darkTheme
        }
        // The notification is drawn by the system and cannot read any of this, so the verdict colours are
        // handed over here - the one place in the app that has them as plain values.
        VerdictTint.update(colors)
        // The same for the helper's screen, which is a different APK drawn in the platform's theme: it
        // gets the window it is being asked to look like, not a palette of its own to keep in step.
        HelperTint.update(colors, darkTheme)
    }

    MaterialExpressiveTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        motionScheme = MotionScheme.expressive(),
        content = content,
    )
}
