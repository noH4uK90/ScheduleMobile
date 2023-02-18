package com.example.schedulemobile.presentation.customComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schedulemobile.domain.models.discipline.Discipline

@Composable
fun DisciplineText(
    modifier: Modifier = Modifier,
    discipline: Discipline?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .border(width = 2.dp,
                brush = SolidColor(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = discipline?.name ?: "",
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 17.sp,
                fontWeight = FontWeight(1000),
                textAlign = TextAlign.Center
            )
        )
    }
}