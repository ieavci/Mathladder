package com.example.arcore.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import coil.compose.rememberImagePainter

@Composable
fun GameStartScreen(onStartGame: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Blurred background image
        Image(
            painter = rememberImagePainter(
                data = "https://images.pexels.com/photos/7111514/pexels-photo-7111514.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(16.dp)
        )

        // Foreground content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Game Rules",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "1. Oyuna bir işlem ile başlanır.\n2. Her işlemin sonucu ile yeni bir 4 işlem yapılır.\n3. Eğer 5 kere üst üste doğru cevap verilirse oyun kazanılır.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 16.sp,
                        color = Color.Black,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = onStartGame, modifier = Modifier.fillMaxWidth()) {
                        Text("Start Game")
                    }
                }
            }
        }
    }
}