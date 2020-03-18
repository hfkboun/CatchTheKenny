package com.hfkorkmaz.catchthekenny

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    fun openDifficultyMenu(view: View){
        val intent =  Intent(applicationContext, DifficultyMenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun openHighScores(view: View){
        val intent = Intent(applicationContext, HighScoresActivity::class.java)
        intent.putExtra("time", "1 Minute")
        intent.putExtra("difficulty", "Easy")
        startActivity(intent)
        finish()
    }
}