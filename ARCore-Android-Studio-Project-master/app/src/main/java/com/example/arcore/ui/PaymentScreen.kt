// PaymentScreen.kt
package com.example.arcore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arcore.database.JsonDatabase

@Composable
fun PaymentScreen(
    onFreeContinue: () -> Unit,
    onUnlimitedSelected: () -> Unit
) {
    val prices = JsonDatabase.getPrices()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Choose a payment option:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onFreeContinue) {
            Text("Free - ${prices.firstOrNull { it.first == "free" }?.second ?: 0.0} USD")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onUnlimitedSelected) {
            Text("Unlimited - ${prices.firstOrNull { it.first == "unlimited" }?.second ?: 50.0} USD")
        }
    }
}
