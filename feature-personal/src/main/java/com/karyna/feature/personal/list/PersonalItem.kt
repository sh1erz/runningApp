package com.karyna.feature.personal.list

data class PersonalItem(val type: PersonalItemType, val data: Any)
enum class PersonalItemType { USER, LIST_TITLE, RUN_ITEM }