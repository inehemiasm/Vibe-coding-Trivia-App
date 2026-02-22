package com.neo.trivia.domain.model

enum class Category(val id: Int, val displayName: String) {
    ALL(9, "Any Category"),
    GENERAL_KNOWLEDGE(9, "General Knowledge"),
    ENTERTAINMENT(11, "Entertainment: Movies"),
    HISTORY(23, "History"),
    SCIENCE(18, "Science: Mathematics"),
    SPORTS(21, "Sports"),
    GEOGRAPHY(22, "Geography"),
    SCIENCE_NATURE(17, "Science & Nature"),
    COMPUTER_TECHNOLOGY(30, "Computer Technology"),
    ASTRONOMY(18, "Science: Astronomy")
}