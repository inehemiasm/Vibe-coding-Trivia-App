package com.neo.design.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = CardDefaults.shape,
    border: BorderStroke? = null,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 16.dp,
    clickable: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val elevationModifier = if (clickable) {
        CardDefaults.outlinedCardColors()
    } else {
        colors()
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            elevation = CardDefaults.elevatedCardElevation(),
//        colors = if (clickable && onClick != null) elevationModifier else colors,
            border = border
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontalPadding, verticalPadding),
                content = content
            )
        }
    }
}