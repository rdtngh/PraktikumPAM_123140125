package com.example.tugaspam2.data

import com.example.tugaspam2.model.News
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository {

    private val categories = listOf("Health", "Education", "Travel", "Science")

    fun getNewsStream(): Flow<News> = flow {

        var counter = 1

        while (true) {
            delay(2000)

            emit(
                News(
                    id = counter,
                    title = "Update Berita #$counter",
                    category = categories.random(),
                    content = "Isi detail berita ke-$counter"
                )
            )

            counter++
        }
    }
}
