package com.raywenderlich.placebook2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.raywenderlich.placebook2.db.BookmarkDao
import com.raywenderlich.placebook2.db.PlaceBookDatabase
import com.raywenderlich.placebook2.model.Bookmark

class BookmarkRepo(private val context: Context) {

    private val db: PlaceBookDatabase = PlaceBookDatabase.getInstance(context)
    private val bookmarkDao: BookmarkDao = db.bookmarkDao()

    fun addBookmark(bookmark: Bookmark): Long? {
        val newId = bookmarkDao.insertBookmark(bookmark)
        bookmark.id = newId
        return newId
    }

    fun createBookmark(): Bookmark {
        return Bookmark()
    }

    val allBookmarks: LiveData<List<Bookmark>>
        get() {
            return bookmarkDao.loadAll()
        }
}
