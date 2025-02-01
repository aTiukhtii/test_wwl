package com.wwl.test.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

/**
 * Represents an item in the AspectGrid layout.
 *
 * @param aspectRatio The width to height ratio of the item
 * @param key Unique identifier for the item, used for efficient updates
 * @param contentType Type of content for recomposition optimization
 * @param content Composable content to be displayed
 * @param width Calculated width based on layout constraints
 * @param height Calculated height based on layout constraints
 */
@Stable
data class FlexLayoutInfo(
    val aspectRatio: Float,
    val key: Any? = null,
    val contentType: Any? = null,
    val content: @Composable () -> Unit,
    val width: Int = 0,
    val height: Int = 0
)

/**
 * Scope for building grid content with aspect ratio-based items.
 */
class CustomLayoutScope {
    private val _items = mutableListOf<FlexLayoutInfo>()
    val items: List<FlexLayoutInfo> = _items

    /**
     * Adds a single item to the grid.
     *
     * @param aspectRatio The width to height ratio of the item
     * @param key Unique identifier for the item (optional)
     * @param contentType Type of content for recomposition optimization (optional)
     * @param content Composable content to be displayed
     */
    fun item(
        aspectRatio: Float,
        key: Any? = null,
        contentType: Any? = null,
        content: @Composable () -> Unit
    ) {
        validate(aspectRatio)
        _items.add(
            FlexLayoutInfo(
                aspectRatio = aspectRatio,
                key = key,
                contentType = contentType,
                content = content
            )
        )
    }

    /**
     * Adds multiple items to the grid.
     *
     * @param T The type of items
     * @param items List of items to add
     * @param key Lambda to extract unique identifier from item (optional)
     * @param aspectRatio Lambda to extract aspect ratio from item
     * @param contentType Lambda to extract content type from item (optional)
     * @param itemContent Composable content for each item
     */
    fun <T> items(
        items: List<T>,
        key: ((item: T) -> Any)? = null,
        aspectRatio: (T) -> Float,
        contentType: ((item: T) -> Any)? = null,
        itemContent: @Composable (T) -> Unit
    ) {
        items.forEach { item ->
            validate(aspectRatio(item))
            _items.add(
                FlexLayoutInfo(
                    key = key?.invoke(item),
                    aspectRatio = aspectRatio(item),
                    contentType = contentType?.invoke(item),
                    content = { itemContent(item) }
                )
            )
        }
    }

    private fun validate(aspectRatio: Float, key: Any? = null) {
        require(aspectRatio > 0f) {
            buildString {
                append("Aspect ratio must be positive and greater than zero (got $aspectRatio)")
                if (key != null) append(" for key: $key")
            }
        }
    }
}