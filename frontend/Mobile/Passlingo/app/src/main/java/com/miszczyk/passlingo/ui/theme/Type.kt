package com.miszczyk.passlingo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.TextSize.bodyLarge
import com.miszczyk.passlingo.ui.theme.TextSize.letterSpacingDefault
import com.miszczyk.passlingo.ui.theme.TextSize.lineHeightDefault

// Set of Material typography styles to start with
val vagRoundedBlack = FontFamily(
    Font(R.font.vag_rounded_black)
)
val vagRoundedBold = FontFamily(
    Font(R.font.vag_rounded_bold)
)
val vagRoundedLight = FontFamily(
    Font(R.font.vag_rounded_light)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = bodyLarge,
        lineHeight = lineHeightDefault,
        letterSpacing = letterSpacingDefault
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)