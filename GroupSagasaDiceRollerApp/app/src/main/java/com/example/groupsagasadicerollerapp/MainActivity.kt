package com.example.groupsagasadicerollerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableIntStateOf(1) }

    Box(modifier = Modifier.fillMaxSize()) {
        Y2KBackground()

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Scaled up 3D Isometric Die
            IsometricDice3D(value = result, modifier = Modifier.size(320.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Prominent Roll Button
            Button(
                onClick = { result = (1..6).random() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF007F)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Roll",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun IsometricDice3D(value: Int, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Scaled geometry for 320dp container
        val topVertex = Offset(cx, cy - 100f)
        val centerVertex = Offset(cx, cy + 15f)
        val rightVertex = Offset(cx + 120f, cy - 40f)
        val leftVertex = Offset(cx - 120f, cy - 40f)
        val bottomVertex = Offset(cx, cy + 130f)
        val bottomRight = Offset(cx + 120f, cy + 75f)
        val bottomLeft = Offset(cx - 120f, cy + 75f)

        // 1. Top Face
        val topFacePath = Path().apply {
            moveTo(topVertex.x, topVertex.y)
            lineTo(rightVertex.x, rightVertex.y)
            lineTo(centerVertex.x, centerVertex.y)
            lineTo(leftVertex.x, leftVertex.y)
            close()
        }
        drawPath(path = topFacePath, color = Color(0xFF2A1B4E))
        drawPath(path = topFacePath, color = Color(0xFF00FFFF), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f))

        // 2. Left Face
        val leftFacePath = Path().apply {
            moveTo(leftVertex.x, leftVertex.y)
            lineTo(centerVertex.x, centerVertex.y)
            lineTo(bottomVertex.x, bottomVertex.y)
            lineTo(bottomLeft.x, bottomLeft.y)
            close()
        }
        drawPath(path = leftFacePath, color = Color(0xFF160C2E))
        drawPath(path = leftFacePath, color = Color(0xFF00FFFF), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f))

        // 3. Right Face
        val rightFacePath = Path().apply {
            moveTo(centerVertex.x, centerVertex.y)
            lineTo(rightVertex.x, rightVertex.y)
            lineTo(bottomRight.x, bottomRight.y)
            lineTo(bottomVertex.x, bottomVertex.y)
            close()
        }
        drawPath(path = rightFacePath, color = Color(0xFF1E0F3D))
        drawPath(path = rightFacePath, color = Color(0xFF00FFFF), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 5f))

        // Top Face Static Center Pip
        drawCircle(color = Color(0xFF00FFFF), radius = 10f, center = Offset(cx, cy - 42f))

        // Top Face Dynamic Pips
        val pips = when (value) {
            1 -> listOf(Offset(cx, cy - 42f))
            2 -> listOf(Offset(cx - 45f, cy - 58f), Offset(cx + 45f, cy - 26f))
            3 -> listOf(Offset(cx - 45f, cy - 58f), Offset(cx, cy - 42f), Offset(cx + 45f, cy - 26f))
            4 -> listOf(Offset(cx - 50f, cy - 60f), Offset(cx + 20f, cy - 72f), Offset(cx - 20f, cy - 12f), Offset(cx + 50f, cy - 24f))
            5 -> listOf(Offset(cx - 50f, cy - 60f), Offset(cx + 20f, cy - 72f), Offset(cx, cy - 42f), Offset(cx - 20f, cy - 12f), Offset(cx + 50f, cy - 24f))
            6 -> listOf(Offset(cx - 50f, cy - 60f), Offset(cx + 20f, cy - 72f), Offset(cx - 35f, cy - 36f), Offset(cx + 35f, cy - 48f), Offset(cx - 20f, cy - 12f), Offset(cx + 50f, cy - 24f))
            else -> emptyList()
        }

        for (pip in pips) {
            drawCircle(color = Color(0xFFFF007F), radius = 12f, center = pip)
        }
    }
}

@Composable
fun Y2KBackground(modifier: Modifier = Modifier) {
    val darkBackground = Color(0xFF0A0A12)
    val glowColor = Color(0xFF2A004E)
    val gridLineColor = Color(0xFF00FFFF).copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor, darkBackground),
                    radius = 1200f
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSpacing = 40.dp.toPx()
            val width = size.width
            val height = size.height

            var x = 0f
            while (x < width) {
                drawLine(
                    color = gridLineColor,
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1f
                )
                x += gridSpacing
            }

            var y = 0f
            while (y < height) {
                drawLine(
                    color = gridLineColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
                y += gridSpacing
            }
        }
    }
}