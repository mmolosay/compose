package com.ordolabs.compose.util

inline fun <reified E : Enum<E>> getFrom(ordinal: Int): E {
    return enumValues<E>()[ordinal]
}