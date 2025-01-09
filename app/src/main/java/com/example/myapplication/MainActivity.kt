package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer


    private var timeSelected : Int = 0
    private var timeCountDown: CountDownTimer? = null
    private var timeProgress = 0
    private var pauseOffSet: Long = 0
    private var isStart = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnIntent= findViewById<Button>(R.id.button3)
        btnIntent.setOnClickListener {
            intent = Intent(applicationContext, data::class.java)
            startActivity(intent)
        }

         mediaPlayer = MediaPlayer.create(this, R.raw.mm)


        val addBtn: ImageButton = findViewById(R.id.btnAdd)
        addBtn.setOnClickListener {
            setTimeFunction()
        }
        val startBtn: Button = findViewById(R.id.btnPlayPause)
        startBtn.setOnClickListener {
            startTimerSetup()



        }
        val stopBtn: Button = findViewById(R.id.button)
        stopBtn.setOnClickListener{
            mediaPlayer.pause()
        }
        val startBtnn: Button = findViewById(R.id.button2)
        startBtnn.setOnClickListener{
            mediaPlayer.start()
        }





        val resetBtn:ImageButton = findViewById(R.id.ib_reset)
        resetBtn.setOnClickListener {
            resetTime()
        }

        val addTimeTv:TextView = findViewById(R.id.tv_addTime)
        addTimeTv.setOnClickListener {
            addExtraTime()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    private fun addExtraTime()
    {
        val progressBar : ProgressBar = findViewById(R.id.pbTimer)
        if (timeSelected!=0)
        {
            timeSelected+=15
            progressBar.max = timeSelected
            timePause()
            startTimer(pauseOffSet)
            Toast.makeText(this,"15 sec added",Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun resetTime()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
            timeProgress=0
            timeSelected=0
            pauseOffSet=0
            timeCountDown=null
            val startBtn:Button = findViewById(R.id.btnPlayPause)
            startBtn.text ="Start"
            isStart = true
            val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
            progressBar.progress = 0
            val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
            timeLeftTv.text = "0"
        }
    }

    private fun timePause()
    {
        if (timeCountDown!=null)
        {
            timeCountDown!!.cancel()
        }
    }



    @SuppressLint("SetTextI18n")
    private fun startTimerSetup()
    {
        val startBtn: Button = findViewById(R.id.btnPlayPause)
        if (timeSelected>timeProgress)
        {
            if (isStart)
            {
                startBtn.text = "Pause"
                startTimer(pauseOffSet)
                isStart = false


            }
            else
            {
                isStart =true
                startBtn.text = "Resume"
                timePause()



            }

        }
        else
        {

            Toast.makeText(this,"Enter Time",Toast.LENGTH_SHORT).show()
        }
    }



    private fun startTimer(pauseOffSetL: Long)
    {
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        progressBar.progress = timeProgress
        timeCountDown = object :CountDownTimer(
            (timeSelected*1000).toLong() - pauseOffSetL*1000, 1000)
        {
            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {
                timeProgress++
                pauseOffSet = timeSelected.toLong()- p0/1000
                progressBar.progress = timeSelected-timeProgress
                val timeLeftTv:TextView = findViewById(R.id.tvTimeLeft)
                timeLeftTv.text = (timeSelected - timeProgress).toString()
            }

            override fun onFinish() {
                resetTime()
                mediaPlayer.stop()
                Toast.makeText(this@MainActivity,"Times Up!", Toast.LENGTH_SHORT).show()
            }

        }.start()
    }



    @SuppressLint("SetTextI18n")
    private fun setTimeFunction()
    {
        val timeDialog = Dialog(this)
        timeDialog.setContentView(R.layout.add_dialog)
        val timeSet = timeDialog.findViewById<EditText>(R.id.etGetTime)
        val timeLeftTv: TextView = findViewById(R.id.tvTimeLeft)
        val btnStart: Button = findViewById(R.id.btnPlayPause)
        val progressBar = findViewById<ProgressBar>(R.id.pbTimer)
        timeDialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
            if (timeSet.text.isEmpty())
            {
                Toast.makeText(this,"Enter Time Duration",Toast.LENGTH_SHORT).show()
            }
            else
            {
                resetTime()
                timeLeftTv.text = timeSet.text
                btnStart.text = "Start"
                timeSelected = timeSet.text.toString().toInt()
                progressBar.max = timeSelected
            }
            timeDialog.dismiss()
        }
        timeDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        if(timeCountDown!=null)
        {
            timeCountDown?.cancel()
            timeProgress=0
        }
    }
}


