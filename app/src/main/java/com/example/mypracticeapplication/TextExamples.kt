package com.example.mypracticeapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextComponentsModifierDemo() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {

        // 1. Normal Text
        Text(
            text = "1. Simple Text with padding",
            modifier = Modifier.padding(8.dp)
        )

        // 2. Styled Text
        Text(
            text = "2. Styled Text (fontSize, weight)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color(0xFFFFF9C4))
                .padding(12.dp)
        )

        // 3. Text with overflow + maxLines
        Text(
            text = "3. This is long text long text long text long text long text",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFBBDEFB))
                .padding(10.dp)
        )

        // 4. Text Alignment
        Text(
            text = "4. Center Aligned Text",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(10.dp)
        )

        // 5. Selectable Text
        SelectionContainer {
            Text(
                "5. This text is selectable",
                modifier = Modifier
                    .background(Color(0xFFE1BEE7))
                    .padding(10.dp)
            )
        }

        // 6. ClickableText
        ClickableText(
            text = AnnotatedString("6. ClickableText example"),
            onClick = { println("ClickableText tapped!") },
            modifier = Modifier
                .background(Color(0xFFC8E6C9))
                .padding(10.dp)
        )

        // 7. BasicText for lower-level control
        BasicText(
            text = "7. BasicText (lower-level Text primitive)",
            modifier = Modifier
                .background(Color(0xFFFFCDD2))
                .padding(10.dp)
        )
    }
}
