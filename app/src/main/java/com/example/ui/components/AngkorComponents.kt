package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AngkorWatSilhouette(
    modifier: Modifier = Modifier,
    primaryColor: Color = AngkorGold,
    glowColor: Color = AngkorGoldLight
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Background Golden Sun Aura behind center tower
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glowColor.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(w * 0.5f, h * 0.45f),
                radius = w * 0.45f
            ),
            radius = w * 0.45f,
            center = Offset(w * 0.5f, h * 0.45f)
        )

        val path = Path()
        val baseY = h * 0.88f

        // Base sanctuary platform line
        path.moveTo(w * 0.05f, baseY)
        path.lineTo(w * 0.95f, baseY)

        // Tier 1 Base
        path.lineTo(w * 0.92f, baseY - h * 0.08f)
        path.lineTo(w * 0.88f, baseY - h * 0.08f)

        // Outer Right Tower 5
        path.lineTo(w * 0.85f, baseY - h * 0.22f)
        path.lineTo(w * 0.83f, baseY - h * 0.48f)
        path.lineTo(w * 0.81f, baseY - h * 0.56f) // Peak T5
        path.lineTo(w * 0.79f, baseY - h * 0.48f)
        path.lineTo(w * 0.77f, baseY - h * 0.22f)

        // Connecting gallery
        path.lineTo(w * 0.74f, baseY - h * 0.22f)

        // Inner Right Tower 3
        path.lineTo(w * 0.72f, baseY - h * 0.35f)
        path.lineTo(w * 0.69f, baseY - h * 0.70f)
        path.lineTo(w * 0.67f, baseY - h * 0.78f) // Peak T3
        path.lineTo(w * 0.65f, baseY - h * 0.70f)
        path.lineTo(w * 0.62f, baseY - h * 0.35f)

        // Central Gallery Roof
        path.lineTo(w * 0.58f, baseY - h * 0.35f)

        // Center Main Tower 1 (Highest Prang)
        path.lineTo(w * 0.56f, baseY - h * 0.55f)
        path.lineTo(w * 0.53f, baseY - h * 0.88f)
        path.lineTo(w * 0.50f, baseY - h * 0.98f) // Peak Center T1
        path.lineTo(w * 0.47f, baseY - h * 0.88f)
        path.lineTo(w * 0.44f, baseY - h * 0.55f)

        // Central Gallery Roof Left
        path.lineTo(w * 0.38f, baseY - h * 0.35f)

        // Inner Left Tower 2
        path.lineTo(w * 0.35f, baseY - h * 0.35f)
        path.lineTo(w * 0.33f, baseY - h * 0.70f)
        path.lineTo(w * 0.31f, baseY - h * 0.78f) // Peak T2
        path.lineTo(w * 0.29f, baseY - h * 0.70f)
        path.lineTo(w * 0.26f, baseY - h * 0.35f)

        // Connecting gallery Left
        path.lineTo(w * 0.23f, baseY - h * 0.22f)

        // Outer Left Tower 4
        path.lineTo(w * 0.21f, baseY - h * 0.22f)
        path.lineTo(w * 0.19f, baseY - h * 0.48f)
        path.lineTo(w * 0.17f, baseY - h * 0.56f) // Peak T4
        path.lineTo(w * 0.15f, baseY - h * 0.48f)
        path.lineTo(w * 0.13f, baseY - h * 0.22f)

        // Tier 1 Base Left
        path.lineTo(w * 0.10f, baseY - h * 0.08f)
        path.lineTo(w * 0.05f, baseY)

        path.close()

        // Draw filled silhouette with golden gradient
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colors = listOf(primaryColor, primaryColor.copy(alpha = 0.6f), primaryColor.copy(alpha = 0.15f))
            )
        )

        // Draw crisp golden outline
        drawPath(
            path = path,
            color = glowColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}

@Composable
fun AngkorWatBannerCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AngkorIndigoNight,
                        AngkorTempleSurface,
                        AngkorTerracotta.copy(alpha = 0.5f)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(AngkorGold, AngkorSandstone, AngkorGoldLight, AngkorGold)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row with Kingdom Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AngkorGold.copy(alpha = 0.2f))
                            .border(1.dp, AngkorGold, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🇰🇭", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ANGKOR EDITION", color = AngkorGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = AngkorGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "VIP PRO",
                        color = AngkorGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Angkor Wat Silhouette Drawing Canvas
            AngkorWatSilhouette(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Khmer Title Text
            Text(
                text = "ព្រះរាជាណាចក្រកម្ពុជា",
                color = AngkorGold,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "VIPV2RAY • Secure High-Speed Proxy Engine",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}
