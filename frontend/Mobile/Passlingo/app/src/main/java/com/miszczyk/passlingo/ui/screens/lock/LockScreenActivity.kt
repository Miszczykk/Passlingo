package com.miszczyk.passlingo.ui.screens.lock

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.miszczyk.passlingo.MainActivity
import com.miszczyk.passlingo.R
import com.miszczyk.passlingo.ui.theme.Dimens.cornerRadiusDefault
import com.miszczyk.passlingo.ui.theme.Dimens.iconSuperGiant
import com.miszczyk.passlingo.ui.theme.Dimens.spaceDefault
import com.miszczyk.passlingo.ui.theme.Dimens.spaceExtraLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceHuge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceLarge
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMedium
import com.miszczyk.passlingo.ui.theme.Dimens.spaceMediumLarge
import com.miszczyk.passlingo.ui.theme.PasslingoTheme
import com.miszczyk.passlingo.ui.theme.TextSize.headlineLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleLarge
import com.miszczyk.passlingo.ui.theme.TextSize.titleMedium
import com.miszczyk.passlingo.ui.theme.vagRoundedBold
import com.miszczyk.passlingo.ui.theme.vagRoundedLight

class LockScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed(){}
        })

        setContent {
            PasslingoTheme {
                Screen(onBack = { navigateToHome() })
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

}

@Composable
fun Screen(onBack: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = spaceExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))

        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = stringResource(id = R.string.content_desc_lock),
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .size(size = iconSuperGiant)
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    shape = CircleShape
                )
                .padding(all = spaceMediumLarge)
        )

        Spacer(modifier = Modifier.height(height = spaceLarge))

        Text(
            text = stringResource(id = R.string.title_access_locked),
            fontSize = headlineLarge,
            fontFamily = vagRoundedBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(height = spaceMedium))

        Text(
            text = stringResource(id = R.string.message_out_of_time),
            fontSize = titleMedium,
            textAlign = TextAlign.Center,
            fontFamily = vagRoundedLight,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.weight(weight = 1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(size = cornerRadiusDefault),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                onBack()
            }
        ) {
            Text(
                text = stringResource(id = R.string.action_earn_study_time),
                fontSize = titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = vagRoundedBold,
                modifier = Modifier.padding(vertical = spaceDefault)
            )
        }
        Spacer(modifier = Modifier.height(height = spaceHuge))
    }
}