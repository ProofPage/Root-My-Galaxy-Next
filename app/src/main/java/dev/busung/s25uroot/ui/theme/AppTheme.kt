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
import dev.busung.s25uroot.VerdictTint

private val AppTypography = Typography(
    displaySmall = TextStyle(fontSize = 38.sp, lineHeight = 44.sp, fontWeight = FontWeight.Normal),
    headlineLarge = TextStyle(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.SemiBold),
    headlineSmall = TextStyle(fontSize = 24.sp, lineHeight = 30.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 17.sp, lineHeight = 23.sp, fontWeight = FontWeight.Medium),
    titleSmall = TextStyle(fontSize = 15.sp, lineHeight = 21.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
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
    AccentColor.Blue -> Color(0xFF3860F4)
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
            generatedColors
        } else {
            generatedColors.copy(
                onSurfaceVariant = lerp(generatedColors.surface, generatedColors.onSurface, 0.8f),
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
