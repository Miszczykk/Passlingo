package com.miszczyk.passlingo.ui.screens.decks.manageDeck.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.components.ThemedDivider
import com.miszczyk.passlingo.ui.screens.decks.manageDeck.model.Flashcard
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.TextSize.bodySmall
import com.miszczyk.passlingo.ui.theme.vagRoundedLight
import com.miszczyk.passlingo.ui.util.DeckIcons

@Composable
fun DeckFormContent(
    modifier: Modifier = Modifier,
    headerTitle: String,

    deckNameState: TextFieldState,
    deckIcon: DeckIcons,

    frontCardState: TextFieldState,
    backCardState: TextFieldState,

    cards: List<Flashcard>,

    onBackClicked: () -> Unit,
    onSelectIconClicked: () -> Unit,
    onAddToDeckClicked: () -> Unit,
    onEditCardClicked: (Flashcard) -> Unit,
    onDeleteCardClicked: (Flashcard) -> Unit,
    onSaveDeckClicked: () -> Unit
){
    BackHandler {
        onBackClicked()
    }

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(height = spaceLarge))
        DeckFormTopBar(title = headerTitle, onClick = { onBackClicked() })
        Spacer(modifier = Modifier.height(height = spaceExtraLarge))

        LazyColumn(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(horizontal = spaceExtraLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                DeckDetailsSection(
                    state = deckNameState,
                    onSelectIconClicked = { onSelectIconClicked() },
                    icon = deckIcon.resId
                )

                Spacer(modifier = Modifier.height(height = spaceExtraHuge))
                ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)
                Spacer(modifier = Modifier.height(height = spaceHuge))

                AddCardSection(
                    addedCards = cards.size,
                    stateFront = frontCardState,
                    stateBack = backCardState,
                    onAddToDeckClicked = { onAddToDeckClicked() }
                )

                Spacer(modifier = Modifier.height(height = spaceExtraHuge))
            }

            if(cards.isEmpty()) {
                item{
                    Text(
                        text = stringResource(R.string.prompt_no_cards_added),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = vagRoundedLight,
                        fontSize = bodySmall,
                    )
                }
            }else {
                items(items = cards, key = {it.id}) { card ->
                    FlashcardItem(
                        frontText = card.front,
                        backText = card.back,
                        onEditClicked = { onEditCardClicked(card) },
                        onDeleteClicked = { onDeleteCardClicked(card) }
                    )
                    Spacer(modifier = Modifier.height(height = spaceLarge))
                }
            }

            item {
                Spacer(modifier = Modifier.height(height = spaceHuge))
            }
        }

        ThemedDivider(colorLine = MaterialTheme.colorScheme.onSecondary)
        Spacer(modifier = Modifier.height(height = spaceExtraLarge))
        DeckFormBottomBar(
            deckName = deckNameState.text.toString(),
            addedCards = cards.size,
            onSaveDeckClicked = { onSaveDeckClicked() }
        )
        Spacer(modifier = Modifier.height(height = spaceLarge))
    }
}