package com.wwl.test.ui.custom

import kotlin.math.abs

/**
 * Calculator for optimizing row layouts in AspectGrid.
 * 
 * This class handles:
 * - Row height and width calculations
 * - Item distribution across rows
 * - Height constraints enforcement
 * - Incremental layout updates
 *
 * @param maxRowHeight Maximum allowed height for any row
 * @param horizontalPadding Padding between items in a row
 */
internal class CustomRowCalculator(
    private val maxRowHeight: Int = DEFAULT_MAX_ROW_HEIGHT,
    private val horizontalPadding: Int = 0
) {
    companion object {
        /** Default maximum row height in pixels */
        const val DEFAULT_MAX_ROW_HEIGHT = 600
    }

    private val minRowHeight = (maxRowHeight * 0.5f).toInt()
    private var availableWidth = 0
    private val rows = mutableListOf<CustomRow>()
    private var lastProcessedItems = listOf<FlexLayoutInfo>()

    fun setMaxRowWidth(maxWidth: Int) {
        availableWidth = maxWidth
    }

    fun addItems(items: List<FlexLayoutInfo>) {
        if (items == lastProcessedItems) return

        val firstDiffIndex = findFirstDifferenceIndex(items, lastProcessedItems)

        if (rows.isNotEmpty() && firstDiffIndex > 0) {
            var itemCount = 0
            val rowsToKeep = rows.takeWhile { row ->
                itemCount += row.items.size
                itemCount <= firstDiffIndex
            }

            rows.clear()
            rows.addAll(rowsToKeep)

            processItems(items.subList(itemCount, items.size))
        } else {
            rows.clear()
            processItems(items)
        }

        lastProcessedItems = items
    }

    private fun findFirstDifferenceIndex(
        newItems: List<FlexLayoutInfo>,
        oldItems: List<FlexLayoutInfo>
    ): Int  {
        val minLength = minOf(oldItems.size, newItems.size)

        // Compare elements up to the length of the shorter list
        for (i in 0 until minLength) {
            if (oldItems[i] != newItems[i]) {
                return i
            }
        }

        // If no difference was found, check for length mismatch
        return if (oldItems.size != newItems.size) minLength else -1
    }

    private fun calculateEffectiveWidth(itemCount: Int): Int {
        return availableWidth - (horizontalPadding * (itemCount - 1))
    }

    private fun processItems(items: List<FlexLayoutInfo>) {
        var currentIndex = 0

        while (currentIndex < items.size) {
            // Find best configuration for next row
            val rowConfig = findBestRowConfiguration(
                items = items,
                startIndex = currentIndex
            )

            // Calculate and add row with adjusted dimensions
            val rowItems = adjustItemDimensions(
                items = items,
                startIndex = rowConfig.startIndex,
                endIndex = rowConfig.endIndex,
                effectiveWidth = rowConfig.effectiveWidth,
                rowHeight = rowConfig.rowHeight
            )
            
            rows.add(CustomRow(rowItems))
            currentIndex = rowConfig.endIndex
        }
    }

    private data class RowConfiguration(
        val startIndex: Int,
        val endIndex: Int,
        val effectiveWidth: Int,
        val rowHeight: Float
    )

    private fun findBestRowConfiguration(
        items: List<FlexLayoutInfo>,
        startIndex: Int
    ): RowConfiguration {
        var bestStartIndex = startIndex
        var bestEndIndex = startIndex + 1
        var bestScore = Float.POSITIVE_INFINITY
        var bestEffectiveWidth = 0
        var bestRowHeight = 0f

        for (numItems in 1..items.size - startIndex) {
            val endIndex = startIndex + numItems
            val effectiveWidth = calculateEffectiveWidth(numItems)
            val aspectRatioSum = calculateAspectRatioSum(items, startIndex, endIndex)
            val rowHeight = calculateRowHeight(effectiveWidth, aspectRatioSum)
                .coerceIn(minRowHeight.toFloat(), maxRowHeight.toFloat())
            
            val score = calculateRowScore(items, startIndex, endIndex, effectiveWidth, rowHeight)

            if (score > bestScore) break

            bestScore = score
            bestStartIndex = startIndex
            bestEndIndex = endIndex
            bestEffectiveWidth = effectiveWidth
            bestRowHeight = rowHeight
        }

        return RowConfiguration(
            startIndex = bestStartIndex,
            endIndex = bestEndIndex,
            effectiveWidth = bestEffectiveWidth,
            rowHeight = bestRowHeight
        )
    }

    private fun adjustItemDimensions(
        items: List<FlexLayoutInfo>,
        startIndex: Int,
        endIndex: Int,
        effectiveWidth: Int,
        rowHeight: Float
    ): List<FlexLayoutInfo> {
        return List(endIndex - startIndex) { index ->
            val item = items[startIndex + index]
            val itemWidth = (rowHeight * item.aspectRatio).toInt()
                .coerceAtMost(effectiveWidth)
            item.copy(
                width = itemWidth,
                height = rowHeight.toInt()
            )
        }
    }

    private fun calculateAspectRatioSum(
        items: List<FlexLayoutInfo>,
        startIndex: Int,
        endIndex: Int
    ): Float {
        var sum = 0f
        for (i in startIndex until endIndex) {
            sum += items[i].aspectRatio
        }
        return sum
    }

    private fun calculateRowScore(
        items: List<FlexLayoutInfo>,
        startIndex: Int,
        endIndex: Int,
        effectiveWidth: Int,
        rowHeight: Float
    ): Float {
        var totalWidth = 0
        for (i in startIndex until endIndex) {
            val itemWidth = (rowHeight * items[i].aspectRatio).toInt()
                .coerceAtMost(effectiveWidth)
            totalWidth += itemWidth
        }

        return abs(totalWidth - effectiveWidth) / (endIndex - startIndex).toFloat()
    }

    fun getRows(): List<CustomRow> = rows

    private fun calculateRowHeight(width: Int, aspectRatioSum: Float): Float {
        if (aspectRatioSum == 0f) return minRowHeight.toFloat()

        // Calculate the base row height
        val rowHeight = width / aspectRatioSum

        // If there's only one item and it would result in a very tall row,
        // limit it to 75% of maxRowHeight
        if (rowHeight > maxRowHeight) {
            return (maxRowHeight * 0.75f)
        }

        return rowHeight.coerceIn(minRowHeight.toFloat(), maxRowHeight.toFloat())
    }
}