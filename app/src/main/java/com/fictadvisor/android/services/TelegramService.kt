package com.fictadvisor.android.services

import android.content.Context
import android.content.Intent
import android.net.Uri

class TelegramService(private val context: Context) {

    private val TELEGRAM_BOT_NAME = "fictadvisordevbot"

    fun openTelegramBot() {
        try {
            val telegramIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://t.me/$TELEGRAM_BOT_NAME")
            )
            context.startActivity(telegramIntent)
        } catch (e: Exception) {
            val telegramWebIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://www.telegram.me/$TELEGRAM_BOT_NAME")
            )
            context.startActivity(telegramWebIntent)
        }
    }
}


