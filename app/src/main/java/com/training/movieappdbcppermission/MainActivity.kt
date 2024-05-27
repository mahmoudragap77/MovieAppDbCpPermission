package com.training.movieappdbcppermission

import AddMovieDialogFragment
import MovieAdapter
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.training.movieappdbcppermission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var movieDataBase: MovieDataBase
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieList: MutableList<Movie>

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        movieDataBase = MovieDataBase(this)
        movieList = mutableListOf()
        movieAdapter = MovieAdapter(movieList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = movieAdapter
        setContentView(binding.root)

        binding.fabAddMovie.setOnClickListener { showAddMovieDialog() }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_collection -> viewMovies()
                R.id.nav_import -> importMovies()
            }
            true
        }
    }

    private fun showAddMovieDialog() {
        val dialog = AddMovieDialogFragment { title, year, genre, rating ->
            addMovie(title, year, genre, rating)
        }
        dialog.show(supportFragmentManager, "AddMovieDialogFragment")
    }

    private fun addMovie(title: String, year: String, genre: String, rating: Float) {
        val db = movieDataBase.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("year", year)
            put("genre", genre)
            put("rating", rating)
        }
        db.insert(Constant.TABLE_NAME, null, values)
        Snackbar.make(binding.root, "Movie added", Snackbar.LENGTH_SHORT).show()
        viewMovies()
    }

    private fun deleteMovie(title: String) {
        val db = movieDataBase.writableDatabase
        val selection = "${Constant.TITLE} = ?"
        val selectionArgs = arrayOf(title)
        db.delete(Constant.TABLE_NAME, selection, selectionArgs)
        Snackbar.make(binding.root, "Movie deleted", Snackbar.LENGTH_SHORT).show()
        viewMovies()
    }

    @SuppressLint("Recycle", "NotifyDataSetChanged")
    private fun viewMovies() {
        movieList.clear()
        val db = movieDataBase.readableDatabase
        val projection = arrayOf(Constant.TITLE, Constant.YEAR, Constant.GENRE, Constant.RATING)
        val cursor = db.query(
            Constant.TABLE_NAME,
            projection,
            null, null, null, null, null
        )
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndexOrThrow(Constant.TITLE))
                val year = getString(getColumnIndexOrThrow(Constant.YEAR))
                val genre = getString(getColumnIndexOrThrow(Constant.GENRE))
                val rating = getFloat(getColumnIndexOrThrow(Constant.RATING))
                movieList.add(Movie(title, year, genre, rating))
            }
        }
        movieAdapter.notifyDataSetChanged()
    }

    private fun importMovies() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.INTERNET),
                PERMISSION_REQUEST_CODE
            )
        } else {
            fetchMoviesDetails()
        }

    }

    private fun fetchMoviesDetails() {
        val title = "Inception"
        val year = "2010"
        val genre = "Action"
        val rating = 8.8f
        addMovie(title, year, genre, rating)
        Snackbar.make(binding.root, "Movie imported", Snackbar.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchMoviesDetails()
                } else {
                    Snackbar.make(binding.root, "Permission denied", Snackbar.LENGTH_SHORT).show()
                }
            }
        }


    }

}