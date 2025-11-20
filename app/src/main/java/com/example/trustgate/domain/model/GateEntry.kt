package com.example.trustgate.domain.model

data class GateEntry(
    val id: String,
    val visitorUid: String,
    val photoUrl: String,
    val timestampMillis: Long?
)