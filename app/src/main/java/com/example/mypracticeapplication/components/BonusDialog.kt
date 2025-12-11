package com.example.mypracticeapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.mypracticeapplication.R
import com.example.mypracticeapplication.ui.theme.MyPracticeApplicationTheme


@Composable
fun BonusDialog(onDismiss: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)

            ,
            shape = RoundedCornerShape(30.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface
                    )
            ) {

                Spacer(modifier = Modifier.height(38.dp))

                Image(
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.img_reward),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(9.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Congratulation!",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(9.dp))


                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "You have got 5 credits as a welcome bonus!",
                    style = MaterialTheme.typography.bodySmall
                    ,textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 31.dp, end = 31.dp)
                        ,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3366FF),
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )

                ) {
                    Text("Got it")
                }

                Spacer(modifier = Modifier.height(48.dp))


            }
        }
    }


}

@Preview(
    name = "Phone Preview",
    device = PIXEL_4,
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun BonusDialogPreview() {
    MyPracticeApplicationTheme {
        BonusDialog(onDismiss = {})
    }
}

