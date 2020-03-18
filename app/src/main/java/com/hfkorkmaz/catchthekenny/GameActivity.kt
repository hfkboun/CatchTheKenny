package com.hfkorkmaz.catchthekenny

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    var score = 0
    var runnable = Runnable {  }
    var handler = Handler()
    var time:Int = 120000
    var difficulty:Int = 200
    var usersname=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val myDatabase = this.openOrCreateDatabase("High_Scores", Context.MODE_PRIVATE,null)


        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS highScores (id INTEGER PRIMARY KEY, username VARCHAR,difficulty INTEGER, time INTEGER, score INTEGER)")


        val intent = intent
        val difficultyString = intent.getStringExtra("difficulty")
        val timeString = intent.getStringExtra("time")

        formatDifficultyToMiliseconds(difficultyString)
        formatTimeStringToMilliseconds(timeString)


        runnable = object : Runnable {
            override fun run() {
                changeImagePosition()

                handler.postDelayed(runnable,difficulty.toLong())
            }

        }

        handler.post(runnable)

        object : CountDownTimer(time.toLong(),1000){
            override fun onFinish() {

                timeText.text = "Time: 0"

                handler.removeCallbacks(runnable)

                kenny.visibility=View.INVISIBLE

                handleHighScore(myDatabase)


            }

            override fun onTick(millisUntilFinished: Long) {
                timeText.text = "Time: " + millisUntilFinished/1000
            }

        }.start()


    }


    fun handleHighScore(myDatabase:SQLiteDatabase) {
        //get existing high scores
        val existingHighScores = myDatabase.rawQuery(
            "SELECT score FROM highScores where time = $time and difficulty = $difficulty ORDER BY score DESC LIMIT 10",
            null
        )
        if(score>0){

            if (existingHighScores.count<10 ){
                val alertDialogBuilder = AlertDialog.Builder(this@GameActivity)

                alertDialogBuilder.setTitle("New High Score")
                alertDialogBuilder.setMessage("What's your name?")
                val userInput = EditText(applicationContext);

                alertDialogBuilder.setView(userInput)


                // set dialog message
                alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton(
                        "OK"
                    ) { dialog, id -> // get user input and set it to result
                        usersname= userInput.text.toString()
                        myDatabase.execSQL("INSERT INTO highScores (username, difficulty, time, score) VALUES ('$usersname', $difficulty, $time, $score)")
                        val highScoresIntent = Intent(applicationContext, HighScoresActivity::class.java)
                        highScoresIntent.putExtra("time", formatTimeMilliSecondsToString(time.toInt()))
                        highScoresIntent.putExtra("difficulty", formatDifficultyMillisecondsToString(difficulty.toInt()))
                        startActivity(highScoresIntent)
                        finish()
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, id ->
                        Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG).show()
                        dialog.cancel()
                        val alert = AlertDialog.Builder(this@GameActivity)

                        alert.setTitle("Game Over")
                        alert.setMessage("Restart The Game?")
                        alert.setPositiveButton("Yes") { dialog, which ->
                            //Restart
                            val intent = intent
                            finish()
                            startActivity(intent)


                        }

                        alert.setNegativeButton("No") { dialog, which ->
                            Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG).show()
                        }

                        alert.show()
                    }


                // create alert dialog
                val alertDialog = alertDialogBuilder.create()

                // show it
                alertDialog.show()

            }

            else if (existingHighScores.count >= 10 ) {

                //check if the user's score is eligible to the list
                while (existingHighScores.moveToLast()) {
                    if (existingHighScores.getInt(existingHighScores.getColumnIndex("score")) <= score) {
                        val alertDialogBuilder = AlertDialog.Builder(this@GameActivity)

                        alertDialogBuilder.setTitle("New High Score")
                        alertDialogBuilder.setMessage("What's your name?")
                        val userInput = EditText(applicationContext);

                        alertDialogBuilder.setView(userInput)


                        // set dialog message
                        alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(
                                "OK"
                            ) { dialog, id -> // get user input and set it to result
                                usersname= userInput.text.toString()
                                myDatabase.execSQL("INSERT INTO highScores (username, difficulty, time, score) VALUES ('$usersname', $difficulty, $time, $score)")
                                val highScoresIntent = Intent(applicationContext, HighScoresActivity::class.java)
                                highScoresIntent.putExtra("time", formatTimeMilliSecondsToString(time.toInt()))
                                highScoresIntent.putExtra("difficulty", formatTimeMilliSecondsToString(difficulty.toInt()))
                                println("++++++++++++++++++++++++++++++++++++++++++++++")
                                startActivity(highScoresIntent)
                                finish()
                            }
                            .setNegativeButton(
                                "Cancel"
                            ) { dialog, id ->
                                Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG).show()
                                dialog.cancel()
                                val alert = AlertDialog.Builder(this@GameActivity)

                                alert.setTitle("Game Over")
                                alert.setMessage("Restart The Game?")
                                alert.setPositiveButton("Yes") { dialog, which ->
                                    //Restart
                                    val intent = intent
                                    finish()
                                    startActivity(intent)


                                }

                                alert.setNegativeButton("No") { dialog, which ->
                                    Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG).show()
                                }

                                alert.show()
                            }


                        // create alert dialog
                        val alertDialog = alertDialogBuilder.create()

                        // show it
                        alertDialog.show()
                    } else {
                        //Alert
                        val alert = AlertDialog.Builder(this@GameActivity)

                        alert.setTitle("Game Over")
                        alert.setMessage("Restart The Game?")
                        alert.setPositiveButton("Yes") { dialog, which ->
                            //Restart
                            val intent = intent
                            finish()
                            startActivity(intent)


                        }

                        alert.setNegativeButton("No") { dialog, which ->
                            Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG).show()
                        }

                        alert.show()
                    }

                }
            }

        }
        else{
            //Alert
            val alert = AlertDialog.Builder(this@GameActivity)

            alert.setTitle("Game Over")
            alert.setMessage("Restart The Game?")
            alert.setPositiveButton("Yes") { dialog, which ->
                //Restart
                val intent = intent
                finish()
                startActivity(intent)


            }

            alert.setNegativeButton("No") { dialog, which ->
                Toast.makeText(this@GameActivity, "Game Over", Toast.LENGTH_LONG).show()
            }

            alert.show()
        }

    }




    fun increaseScore(view: View){
        score += 1
        scoreText.text = "Score: $score"

        //change image position to disable gaining multiple points by clicking too fast on an image
        changeImagePosition()

    }

    fun changeImagePosition(){

        var maxWidth = imageConstraintLayout.width-kenny.width
        var maxHeight = imageConstraintLayout.height-kenny.height
        val randomX = (0..maxWidth).random().toFloat()
        val randomY = (0..maxHeight).random().toFloat()

        kenny.x = randomX
        kenny.y = randomY


    }

    fun formatTimeMilliSecondsToString(timeMilliseconds: Int?): String {
        when (timeMilliseconds) {
            120000 ->  return "2 Minutes"
            60000 ->   return "1 Minute"
            30000 -> return "30 Seconds"
            15000 -> return "15 Seconds"

            else -> { // Note the block
                return "2 Minutes"
            }
        }
    }

    fun formatDifficultyMillisecondsToString(difficultyMilliseconds: Int?): String{
        when (difficultyMilliseconds) {
            200 ->  return "Hard"
            500 -> return "Normal"
            1000 -> return  "Easy"

            else -> { // Note the block
                return "Hard"
            }
        }


    }


    fun formatTimeStringToMilliseconds(timeString: String?){
        when (timeString) {
            "2 Minutes" -> time=120000
            "1 Minute" ->  time=60000
            "30 Seconds" -> time=30000
            "15 Seconds" -> time=15000

            else -> { // Note the block

            }
        }
    }

    fun formatDifficultyToMiliseconds(difficultyString: String?){
        when (difficultyString) {
            "Hard" ->    difficulty=200
            "Normal" ->  difficulty=500
            "Easy" ->    difficulty=1000

            else -> { // Note the block

            }
        }
    }


}