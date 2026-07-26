package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LiveSpeedGraph(
    uploadDataPoints: List<Float>,
    downloadDataPoints: List<Float>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .glassCard(cornerRadius = 16.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Live Speed Chart",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).glassCard(cornerRadius = 4.dp, backgroundColor = NeonCyan))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("DL", color = TextSecondary, fontSize = 11.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).glassCard(cornerRadius = 4.dp, backgroundColor = ElectricPurple))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("UL", color = TextSecondary, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
        ) {
            val width = size.width
            val height = size.height

            // Draw grid lines
            val gridStepY = height / 4
            for (i in 0..4) {
                val y = i * gridStepY
                drawLine(
                    color = GlassBorder.copy(alpha = 0.2f),
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
            }

            fun drawSeries(points: List<Float>, color: Color) {
                if (points.isEmpty()) return
                val maxVal = (points.maxOrNull() ?: 100f).coerceAtLeast(10f)
                val stepX = width / (points.size - 1).coerceAtLeast(1)

                val path = Path()
                points.forEachIndexed { index, valPoint ->
                    val x = index * stepX
                    val y = height - ((valPoint / maxVal) * (height - 10f))
                    if (index == 0) {
                        path.moveTo(x, y)
                    } else {
                        val prevX = (index - 1) * stepX
                        val prevY = height - ((points[index - 1] / maxVal) * (height - 10f))
                        val controlX1 = prevX + (stepX / 2)
                        val controlY1 = prevY
                        val controlX2 = prevX + (stepX / 2)
                        val controlY2 = y
                        path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                    }
                }

                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Fill area below path
                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.35f), Color.Transparent)
                    )
                )
            }

            drawSeries(downloadDataPoints, NeonCyan)
            drawSeries(uploadDataPoints, ElectricPurple)
        }
    }
}
