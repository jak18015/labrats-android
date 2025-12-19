package com.example.biolab.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

// Fancy formatted text for concentration readout
@Composable
fun NiceConcentrationText(concentration: Double) {
    val formatter = DecimalFormat("0.00E0")
    val parts = formatter.format(concentration).split("E")
    val base = parts[0]
    val exponent = parts[1].replace("+", "")

    Text(buildAnnotatedString {
        append("Concentration: $base × 10")
        withStyle(style = SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = 12.sp // Slightly smaller for the exponent
        )
        ) {
            append(exponent)
        }
        append(" cells/mL")
    })
}