package com.miszczyk.passlingo.ui.screens.createDeck.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.BottomSheetHeader
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.TextSize.body
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectBottomSheet(
    sheetState: SheetState,
    currentIcon: Int,
    onIconClicked: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    deckIconsList: List<Int>,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val coroutineScope = rememberCoroutineScope()
            BottomSheetHeader(
                label = stringResource(id = R.string.label_select_cover_icon)
            ) {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismissRequest()
                }
            }

            Spacer(modifier = Modifier.height(height = spaceMediumLarge))

            Text(
                text = stringResource(id = R.string.prompt_select_icon_description),
                fontSize = body,
                color = MaterialTheme.colorScheme.onSecondary,
                fontFamily = vagRoundedLight,
                modifier = Modifier.padding(horizontal = spaceExtraLarge)
            )

            Spacer(modifier = Modifier.height(height = spaceExtraLarge))

            ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)

            IconListContent(
                icons = deckIconsList, currentIcon = currentIcon, onIconClicked = onIconClicked
            )
        }
    }
}

@Composable
private fun IconListContent(icons: List<Int>, currentIcon: Int, onIconClicked: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 4),
        contentPadding = PaddingValues(all = spaceExtraLarge),
        horizontalArrangement = Arrangement.spacedBy(spaceExtraLarge),
        verticalArrangement = Arrangement.spacedBy(spaceExtraLarge)
    ) {
        items(items = icons) { iconRes ->
            IconItem(
                iconResId = iconRes, isSelected = iconRes == currentIcon, onClick = {
                    onIconClicked(iconRes)
                }
            )
        }
    }
}