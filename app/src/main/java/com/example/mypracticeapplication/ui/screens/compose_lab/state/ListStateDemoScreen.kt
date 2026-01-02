package com.example.mypracticeapplication.ui.screens.compose_lab.state

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ListStateDemoScreen demonstrates the critical difference between using
 * standard Kotlin mutable collections vs. Compose's SnapshotStateList.
 *
 * KEY CONCEPT: Compose's recomposition system tracks state changes through
 * "Snapshot State" objects. Standard Kotlin collections (like MutableList)
 * are NOT observed by Compose, so mutations don't trigger recomposition.
 */
@Composable
fun ListStateDemoScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "List State Demo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Understanding why standard lists fail in Compose",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ══════════════════════════════════════════════════════════════
        // SECTION 1: THE TRAP - Standard MutableList (FAILS!)
        // ══════════════════════════════════════════════════════════════
        TrapSection()

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline
        )

        // ══════════════════════════════════════════════════════════════
        // SECTION 2: THE FIX - SnapshotStateList (WORKS!)
        // ══════════════════════════════════════════════════════════════
        FixSection()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * SECTION 1: Demonstrates the "Trap" of using standard MutableList.
 *
 * WHY IT FAILS:
 * - `remember { mutableListOf<String>() }` creates a regular Kotlin MutableList.
 * - The `remember` block preserves the SAME LIST REFERENCE across recompositions.
 * - When we call `list.add()`, the list contents change, but the REFERENCE stays the same.
 * - Compose uses "Referential Equality" (===) to detect changes in remembered values.
 * - Since the reference hasn't changed, Compose thinks nothing has changed → NO RECOMPOSITION!
 *
 * This is called the "REFERENTIAL EQUALITY TRAP".
 */
@Composable
private fun TrapSection() {
    // ⚠️ THE TRAP: This is a regular Kotlin MutableList.
    // It is NOT observable by Compose's snapshot system!
    val list = remember { mutableListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFFFEBEE), // Light red background
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFFE53935), // Red border
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Title
        Text(
            text = "⚠️ The Trap (Standard MutableList)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFC62828) // Dark red
        )

        // Explanation
        Text(
            text = "Using: val list = remember { mutableListOf<String>() }",
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF8B0000)
        )

        // Add Item Button
        Button(
            onClick = {
                // Adding to the list, but Compose won't see this change!
                // The list reference (memory address) stays the same.
                list.add("Item ${list.size + 1}")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item (Won't Update UI!)")
        }

        // Display current count (also won't update!)
        Text(
            text = "Items in list: ${list.size}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFC62828)
        )

        // List items display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (list.isEmpty()) {
                Text(
                    text = "(Click button - items won't appear here)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            list.forEach { item ->
                Text(
                    text = "• $item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFC62828)
                )
            }
        }

        // Why it fails explanation
        Text(
            text = "❌ WHY IT FAILS:\n" +
                    "• Compose tracks changes via 'Referential Equality' (===)\n" +
                    "• The list REFERENCE never changes, only its CONTENTS\n" +
                    "• Compose sees same reference → assumes no change → skips recomposition",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 18.sp,
            color = Color(0xFF8B0000)
        )
    }
}

/**
 * SECTION 2: Demonstrates the "Fix" using SnapshotStateList.
 *
 * WHY IT WORKS:
 * - `mutableStateListOf<String>()` creates a SnapshotStateList.
 * - SnapshotStateList is part of Compose's "Snapshot System".
 * - Every mutation (add, remove, set) is TRACKED by Compose.
 * - When you call `list.add()`, Compose is NOTIFIED of the change.
 * - This triggers recomposition of any Composable reading from this list.
 *
 * This is the "SNAPSHOT SYSTEM" in action - Compose's reactive state management.
 */
@Composable
private fun FixSection() {
    // ✅ THE FIX: This is a SnapshotStateList.
    // It IS observable by Compose's snapshot system!
    // Every mutation triggers recomposition.
    val list = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE8F5E9), // Light green background
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = Color(0xFF43A047), // Green border
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Section Title
        Text(
            text = "✅ The Fix (SnapshotStateList)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32) // Dark green
        )

        // Explanation
        Text(
            text = "Using: val list = remember { mutableStateListOf<String>() }",
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic,
            color = Color(0xFF1B5E20)
        )

        // Add Item Button
        Button(
            onClick = {
                // Adding to SnapshotStateList - Compose WILL see this!
                // The snapshot system tracks this mutation.
                list.add("Item ${list.size + 1}")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF43A047)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Item (Updates Instantly!)")
        }

        // Display current count (updates correctly!)
        Text(
            text = "Items in list: ${list.size}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2E7D32)
        )

        // List items display
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (list.isEmpty()) {
                Text(
                    text = "(Click button - items will appear here!)",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            list.forEach { item ->
                Text(
                    text = "• $item",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF2E7D32)
                )
            }
        }

        // Why it works explanation
        Text(
            text = "✅ WHY IT WORKS:\n" +
                    "• mutableStateListOf uses Compose's 'Snapshot System'\n" +
                    "• Every add/remove/set operation NOTIFIES Compose\n" +
                    "• Compose detects the change → triggers recomposition → UI updates!",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 18.sp,
            color = Color(0xFF1B5E20)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListStateDemoScreenPreview() {
    ListStateDemoScreen()
}
