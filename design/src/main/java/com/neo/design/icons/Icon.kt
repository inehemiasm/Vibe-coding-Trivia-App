package com.neo.design.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppIcon(
    icon: ImageVector,
    contentDescription: String? = null,
    size: IconSize = IconSize.Medium,
    tint: Color? = null
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = Modifier.size(size.value),
        tint = tint ?: MaterialTheme.colorScheme.onSurface
    )
}