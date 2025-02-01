package com.wwl.test.ui.custom

internal data class CustomRow(
    val items: List<FlexLayoutInfo> = emptyList()
) {
    /** Unique identifier for the row, derived from item keys */
    val key: String
        get() = items.joinToString("-") { it.key?.toString() ?: it.hashCode().toString() }
}