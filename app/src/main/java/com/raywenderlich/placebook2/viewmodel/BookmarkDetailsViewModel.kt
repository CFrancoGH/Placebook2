package com.raywenderlich.placebook2.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.raywenderlich.placebook2.model.Bookmark
import com.raywenderlich.placebook2.repository.BookmarkRepo
import com.raywenderlich.placebook2.util.ImageUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookmarkDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private var bookmarkDetailsView: LiveData<BookmarkDetailsView>?
    = null

    private val bookmarkRepo = BookmarkRepo(getApplication())

    private fun bookmarkToBookmarkView(bookmark: Bookmark):
            BookmarkDetailsView{
        return BookmarkDetailsView(
            bookmark.id,
            bookmark.name,
            bookmark.phone,
            bookmark.address,
            bookmark.notes
        )
    }

    private fun bookmarkViewToBookmark(bookmarkView:
    BookmarkDetailsView): Bookmark? {
        val bookmark = bookmarkView.id?.let {
            bookmarkRepo.getBookmark(it)
        }
        if (bookmark != null) {
            bookmark.id = bookmarkView.id
            bookmark.name = bookmarkView.name
            bookmark.phone = bookmarkView.phone
            bookmark.address = bookmarkView.address
            bookmark.notes = bookmarkView.notes
        }
        return bookmark
    }

    private fun mapBookmarkToBookmarkView(bookmarkId: Long) {
        val bookmark = bookmarkRepo.getLiveBookmark(bookmarkId)
        bookmarkDetailsView = Transformations.map(bookmark)
        { repoBookmark ->
            bookmarkToBookmarkView(repoBookmark)
        }
    }

    fun getBookmark(bookmarkId: Long):
            LiveData<BookmarkDetailsView>? {
        if (bookmarkDetailsView == null) {
            mapBookmarkToBookmarkView(bookmarkId)
        }
        return bookmarkDetailsView
    }

    fun updateBookmark(bookmarkView: BookmarkDetailsView) {
        //1
        GlobalScope.launch {
            //2
            val bookmark = bookmarkViewToBookmark(bookmarkView)
            //3
            bookmark?.let { bookmarkRepo.updateBookmark(it) }
        }
    }

    data class BookmarkDetailsView(
        var id: Long? = null,
        var name: String = "",
        var phone: String = "",
        var address: String = "",
        var notes: String = ""
    ) {
        fun getImage(context: Context) = id?.let {
            ImageUtils.loadBitmapFromFile(context, Bookmark.generateImageFilename(it))
        }

        fun setImage(context: Context, image: Bitmap) {
            id?.let {
                ImageUtils.saveBitmapToFile(context, image, Bookmark.generateImageFilename(it))
            }
        }
    }
}