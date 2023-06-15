package com.example.timetrekaccelerateyourknowledge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var strikeDay: TextView
    private lateinit var startStreakText: TextView
    private lateinit var seeAllTextView: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var collectionRecyclerView: RecyclerView
    private lateinit var infoRecyclerView: RecyclerView
    private lateinit var newReleaseRecyclerView: RecyclerView
    private lateinit var seeAllNewReleaseTextView: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        strikeDay = findViewById(R.id.strikeday)
        startStreakText = findViewById(R.id.textView)
        seeAllTextView = findViewById(R.id.seealltv)
        recyclerView = findViewById(R.id.recyclerview)
        collectionRecyclerView = findViewById(R.id.collectionrec)
        infoRecyclerView = findViewById(R.id.inforec)
        newReleaseRecyclerView = findViewById(R.id.newreleaserec)
        seeAllNewReleaseTextView = findViewById(R.id.newrelseealltv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainscreen,menu)
        return super.onCreateOptionsMenu(menu)
    }
}