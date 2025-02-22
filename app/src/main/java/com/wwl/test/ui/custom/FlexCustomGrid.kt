package com.wwl.test.ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * A composable that arranges items in a grid layout with varying row heights based on item aspect ratios.
 * Each row is optimized to best utilize the available width while maintaining the aspect ratios of its items.
 *
 * @param modifier The modifier to be applied to the grid
 * @param state The state object to be used to control or observe the list's state
 * @param contentPadding The padding around the content
 * @param maxRowHeight Maximum height allowed for any row
 * @param itemPadding Padding between items in a row
 * @param content The grid content using [CustomLayoutScope]
 */
@Composable
fun FlexCustomGrid(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    maxRowHeight: Dp = CustomRowCalculator.DEFAULT_MAX_ROW_HEIGHT.dp,
    itemPadding: PaddingValues = PaddingValues(0.dp),
    content: CustomLayoutScope.() -> Unit
) {
    val scope = CustomLayoutScope().apply(content)

    val density = LocalDensity.current

    val layoutInfo = remember(maxRowHeight, itemPadding) {
        CustomRowCalculator(
            maxRowHeight = with(density) { maxRowHeight.toPx().toInt() },
            horizontalPadding = with(density) {
                itemPadding.calculateStartPadding(LayoutDirection.Ltr).toPx().toInt()
            }
        )
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val rows by remember(scope.items, constraints.maxWidth) {
            mutableStateOf(
                calculateRows(
                    layoutInfo = layoutInfo,
                    items = scope.items,
                    availableWidth = constraints.maxWidth,
                    contentPadding = contentPadding,
                    density = density
                )
            )
        }

        LazyColumn(
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(itemPadding.calculateTopPadding())
        ) {
            items(
                items = rows,
                key = { row -> row.key }
            ) { row ->
                FlexRow(
                    row = row.items,
                    density = density,
                    itemPadding = itemPadding
                )
            }
        }
    }
}

private fun calculateRows(
    layoutInfo: CustomRowCalculator,
    items: List<FlexLayoutInfo>,
    availableWidth: Int,
    contentPadding: PaddingValues,
    density: Density
): List<CustomRow> {
    val width = availableWidth - with(density) {
        (contentPadding.calculateStartPadding(LayoutDirection.Ltr) +
                contentPadding.calculateEndPadding(LayoutDirection.Ltr)).toPx()
    }.toInt()

    return with(layoutInfo) {
        setMaxRowWidth(width)
        addItems(items)
        getRows()
    }
}

@Composable
private fun FlexRow(
    row: List<FlexLayoutInfo>,
    density: Density,
    itemPadding: PaddingValues
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(density) { row.first().height.toDp() }),
        horizontalArrangement = Arrangement.spacedBy(
            itemPadding.calculateStartPadding(LayoutDirection.Ltr)
        )
    ) {
        for (item in row) {
            Box(
                modifier = Modifier
                    .width(with(density) { item.width.toDp() })
                    .fillMaxHeight()
            ) {
                item.content()
            }
        }
    }
}