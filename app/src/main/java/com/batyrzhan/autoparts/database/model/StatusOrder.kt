package com.batyrzhan.autoparts.database.model

enum class StatusOrder(val status:String) {
    PROCESSED("Обработан"),
    CANCELED("Отменен"),
    WAITING("В ожидании")
}