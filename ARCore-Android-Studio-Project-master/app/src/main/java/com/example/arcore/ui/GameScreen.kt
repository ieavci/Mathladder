package com.example.arcore.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arcore.ARGameScreen

@Composable
fun GameScreen(
    onPaymentRequired: () -> Unit,
    onPlayAgain: () -> Unit
) {
    var isGameFinished by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween, // Boşlukları düzenler
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(1.dp), // Çerçeve daha ince
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (!isGameFinished) "✨ MathLadder ✨" else "🎉 Game Over! 🎉",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F),
                        fontSize = 26.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isGameFinished) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize() // ARGameScreen tam boyut
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            ARGameScreen(onPaymentRequired = onPaymentRequired)
                        }
                    }
                }
            }

            // Bottom Button
            Button(
                onClick = { isGameFinished = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Finish Game",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
