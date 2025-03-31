package com.example.emergen_app.data.models

import com.example.emergen_app.R

data class SignLanguageSymbol(
    val id: Int,
    val imageRes: Int,
    val meaning: String
)

val symbolsList = listOf(
    SignLanguageSymbol(1, R.drawable.applause, "applause"),
    SignLanguageSymbol(2, R.drawable.hello, "hello"),
    SignLanguageSymbol(3, R.drawable.surprise, "surprise"),
    SignLanguageSymbol(4, R.drawable.thumbs_up, "thumbs up"),
    SignLanguageSymbol(5, R.drawable.thumbs_down, "thumbs down"),
    SignLanguageSymbol(6, R.drawable.thumbs_up, "thumbs up"),
    SignLanguageSymbol(7, R.drawable.call_me, "call me"),
    SignLanguageSymbol(8, R.drawable.agreement, "agreement"),
    SignLanguageSymbol(9, R.drawable.fight, "fight"),
    SignLanguageSymbol(10, R.drawable.good_luck, "good luck"),
    SignLanguageSymbol(11, R.drawable.heart, "heart"),
    SignLanguageSymbol(12, R.drawable.gratitude, "gratitude"),
    SignLanguageSymbol(13, R.drawable.hug, "hug"),
    SignLanguageSymbol(14, R.drawable.handshake, "handshake"),
    SignLanguageSymbol(15, R.drawable.love_you, "love you"),
    SignLanguageSymbol(16, R.drawable.pointing_up, "pointing up"),
    SignLanguageSymbol(17, R.drawable.pointing_down, "pointing down"),
    SignLanguageSymbol(18, R.drawable.pointing_left, "pointing left"),
    SignLanguageSymbol(19, R.drawable.pointing_right, "pointing right"),
    SignLanguageSymbol(20, R.drawable.ok, "ok"),
    SignLanguageSymbol(21, R.drawable.picking_up, "picking up"),
    SignLanguageSymbol(22, R.drawable.pinch, "pinching"),
    SignLanguageSymbol(23, R.drawable.power, "power"),
    SignLanguageSymbol(24, R.drawable.support, "support"),
    SignLanguageSymbol(25, R.drawable.greetings, "greetings"),
    SignLanguageSymbol(26, R.drawable.love, "love"),
    SignLanguageSymbol(27, R.drawable.praying, "praying"),
    SignLanguageSymbol(28, R.drawable.question, "question"),
    SignLanguageSymbol(29, R.drawable.raised_hand, "raised hand"),
    SignLanguageSymbol(30, R.drawable.receiving, "receiving"),
    SignLanguageSymbol(31, R.drawable.rock_on, "rock on"),
    SignLanguageSymbol(32, R.drawable.small_amount, "small amount"),
    SignLanguageSymbol(33, R.drawable.stop, "stop"),
    SignLanguageSymbol(34, R.drawable.strength, "strength"),
    SignLanguageSymbol(35, R.drawable.vectory, "victory"),
)

