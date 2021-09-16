package com.raywenderlich.placebook2.ui

import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.raywenderlich.placebook2.R
import com.raywenderlich.placebook2.viewmodel.BookmarkDetailsViewModel
import com.raywenderlich.placebook2.databinding.ActivityBookmarkDetailsBinding

class BookmarkDetailsActivity : AppCompatActivity() {

    private val bookmarkDetailsViewModel by viewModels<BookmarkDetailsViewModel>()

    private var bookmarkDetailsView: BookmarkDetailsViewModel.BookmarkDetailsView? = null

    private lateinit var  databinding:
            ActivityBookmarkDetailsBinding

    override fun onCreate(savedInstanceState: android.os.Bundle?)
    {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this,
        R.layout.activity_bookmark_details)
        setupToolbar()
        getIntentData()
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu):
            Boolean {
        menuInflater.inflate(R.menu.menu_bookmark_details, menu)
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(databinding.toolbar)
    }

    private fun populateImageView() {
        bookmarkDetailsView?.let { bookmarkView ->
            val placeImage = bookmarkView.getImage(this)
            placeImage?.let {
                databinding.imageViewPlace.setImageBitmap(placeImage)
            }
        }
    }

    private fun getIntentData() {
        //1
        val bookmarkId = intent.getLongExtra(
            MapsActivity.Companion.EXTRA_BOOKMARK_ID, 0)

        //2
        bookmarkDetailsViewModel.getBookmark(bookmarkId)?.observe(this,
            {
                //3
                it?.let {
                    bookmarkDetailsView = it
                    //4
                    databinding.bookmarkDetailsView = it
                    populateImageView()
                }
            })
    }

    private fun saveChanges() {
        val name = databinding.editTextName.text.toString()
        if (name.isEmpty()) {
            return
        }
        bookmarkDetailsView?.let { bookmarkView ->
            bookmarkView.name = databinding.editTextName.text.toString()
            bookmarkView.notes = databinding.editTextNotes.text.toString()
            bookmarkView.address = databinding.editTextAddress.text.toString()
            bookmarkView.phone = databinding.editTextPhone.text.toString()
            bookmarkDetailsViewModel.updateBookmark(bookmarkView)
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_save -> {
                saveChanges()
                true
            }
            else -> super.onOptionsItemSelected(item)
    }
}