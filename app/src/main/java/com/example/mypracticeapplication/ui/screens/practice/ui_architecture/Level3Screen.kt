package com.example.mypracticeapplication.ui.screens.practice.ui_architecture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class PizzaSize(val displayName: String, val basePrice: Double) {
    SMALL("Small", 8.99),
    MEDIUM("Medium", 12.99),
    LARGE("Large", 16.99)
}

data class Topping(
    val name: String,
    val price: Double,
    val emoji: String
)

val availableToppings = listOf(
    Topping("Pepperoni", 1.50, "🍕"),
    Topping("Mushrooms", 1.00, "🍄"),
    Topping("Onions", 0.75, "🧅"),
    Topping("Sausage", 1.50, "🌭"),
    Topping("Bacon", 1.75, "🥓"),
    Topping("Extra Cheese", 1.25, "🧀"),
    Topping("Peppers", 0.75, "🫑"),
    Topping("Olives", 1.00, "🫒")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Level3Screen() {
    // State - interconnected!
    var selectedSize by remember { mutableStateOf(PizzaSize.MEDIUM) }
    val selectedToppings = remember { mutableStateListOf<Topping>() }
    var quantity by remember { mutableIntStateOf(1) }

    // Derived state - automatically updates when dependencies change
    val basePrice by remember(selectedSize) {
        derivedStateOf { selectedSize.basePrice }
    }
    
    val toppingsPrice by remember(selectedToppings.size) {
        derivedStateOf { selectedToppings.sumOf { it.price } }
    }
    
    val singlePizzaPrice by remember(basePrice, toppingsPrice) {
        derivedStateOf { basePrice + toppingsPrice }
    }
    
    val totalPrice by remember(singlePizzaPrice, quantity) {
        derivedStateOf { singlePizzaPrice * quantity }
    }
    
    val freeDelivery by remember(totalPrice) {
        derivedStateOf { totalPrice >= 25.0 }
    }
    
    val deliveryFee by remember(freeDelivery) {
        derivedStateOf { if (freeDelivery) 0.0 else 4.99 }
    }
    
    val grandTotal by remember(totalPrice, deliveryFee) {
        derivedStateOf { totalPrice + deliveryFee }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Header()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1️⃣ Size Selection - affects base price
            SizeSelector(
                selectedSize = selectedSize,
                onSizeSelected = { selectedSize = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2️⃣ Toppings - affects toppings price
            ToppingsSection(
                selectedToppings = selectedToppings,
                onToppingToggle = { topping ->
                    if (selectedToppings.contains(topping)) {
                        selectedToppings.remove(topping)
                    } else {
                        selectedToppings.add(topping)
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3️⃣ Quantity - affects total
            QuantitySelector(
                quantity = quantity,
                onQuantityChange = { quantity = it.coerceIn(1, 10) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4️⃣ Price Breakdown - derived from above
            PriceBreakdown(
                basePrice = basePrice,
                toppingsPrice = toppingsPrice,
                quantity = quantity,
                singlePizzaPrice = singlePizzaPrice,
                subtotal = totalPrice,
                freeDelivery = freeDelivery,
                deliveryFee = deliveryFee,
                grandTotal = grandTotal
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 5️⃣ Order Button - shows grand total
            OrderButton(
                grandTotal = grandTotal,
                enabled = quantity > 0
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE65100))
            .padding(horizontal = 4.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Text(
            text = "🍕 Pizza Order",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SizeSelector(
    selectedSize: PizzaSize,
    onSizeSelected: (PizzaSize) -> Unit
) {
    Column {
        Text(
            text = "Choose Size",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PizzaSize.entries.forEach { size ->
                val isSelected = selectedSize == size
                val bgColor by animateColorAsState(
                    if (isSelected) Color(0xFFE65100) else Color.White,
                    label = "sizeBg"
                )
                
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSizeSelected(size) },
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (size) {
                                PizzaSize.SMALL -> "🍕"
                                PizzaSize.MEDIUM -> "🍕🍕"
                                PizzaSize.LARGE -> "🍕🍕🍕"
                            },
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = size.displayName,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else Color.Black
                        )
                        Text(
                            text = "$${String.format("%.2f", size.basePrice)}",
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToppingsSection(
    selectedToppings: List<Topping>,
    onToppingToggle: (Topping) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Add Toppings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${selectedToppings.size} selected",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        // Toppings in a flow layout
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            availableToppings.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { topping ->
                        val isSelected = selectedToppings.contains(topping)
                        FilterChip(
                            selected = isSelected,
                            onClick = { onToppingToggle(topping) },
                            modifier = Modifier.weight(1f),
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(topping.emoji)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(topping.name, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "+$${String.format("%.2f", topping.price)}",
                                        fontSize = 10.sp,
                                        color = if (isSelected) Color.White.copy(alpha = 0.7f) else Color.Gray
                                    )
                                }
                            },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFE65100),
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = { onQuantityChange(quantity - 1) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE65100))
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = Color.White
                    )
                }
                
                Text(
                    text = quantity.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
                
                IconButton(
                    onClick = { onQuantityChange(quantity + 1) },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE65100))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceBreakdown(
    basePrice: Double,
    toppingsPrice: Double,
    quantity: Int,
    singlePizzaPrice: Double,
    subtotal: Double,
    freeDelivery: Boolean,
    deliveryFee: Double,
    grandTotal: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Price Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            PriceRow("Base price", basePrice)
            if (toppingsPrice > 0) {
                PriceRow("Toppings", toppingsPrice)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            PriceRow("Per pizza", singlePizzaPrice)
            PriceRow("× $quantity pizzas", subtotal, isBold = true)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Delivery fee with animation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Text("Delivery", fontSize = 14.sp)
                    if (freeDelivery) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "FREE! 🎉",
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = if (freeDelivery) "$0.00" else "$${String.format("%.2f", deliveryFee)}",
                    fontSize = 14.sp,
                    color = if (freeDelivery) Color(0xFF4CAF50) else Color.Black
                )
            }
            
            AnimatedVisibility(!freeDelivery) {
                Text(
                    text = "Add $${String.format("%.2f", 25.0 - subtotal)} more for FREE delivery!",
                    fontSize = 11.sp,
                    color = Color(0xFFE65100),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0xFFE65100))
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", grandTotal)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100)
                )
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, price: Double, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = "$${String.format("%.2f", price)}",
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun OrderButton(
    grandTotal: Double,
    enabled: Boolean
) {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE65100)
        )
    ) {
        Text(
            text = "Place Order • $${String.format("%.2f", grandTotal)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Level3ScreenPreview() {
    Level3Screen()
}
