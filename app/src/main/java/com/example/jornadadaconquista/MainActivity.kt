package com.example.jornadadaconquista

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var buttonDislike: Button
    private lateinit var buttonLike: Button
    private lateinit var buttonYes: Button
    private lateinit var buttonNo: Button

    private var clicks = 0
    private var targetClicks = (0..10).random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.parseColor("#F5F5F5"))
            setPadding(16, 16, 16, 16)
        }

        imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            setImageResource(R.drawable.fundo)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        textView = TextView(this).apply {
            text = "Clique para avançar na sua jornada!"
            textSize = 26f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setTypeface(typeface, Typeface.BOLD)
            setPadding(20, 20, 20, 20)
            setShadowLayer(4f, 2f, 2f, Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(Color.parseColor("#80000000"))
        }

        buttonDislike = Button(this).apply {
            text = "×"
            textSize = 36f
            setBackgroundColor(Color.TRANSPARENT)
            setTextColor(Color.RED)
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            setOnClickListener {
                textView.text = "Você desistiu! Deseja tentar novamente?"
                imageView.setImageResource(R.drawable._4)
                showRestartOptions()
            }
        }

        buttonLike = Button(this).apply {
            text = "♥"
            textSize = 36f
            setBackgroundColor(Color.TRANSPARENT)
            setTextColor(Color.GREEN)
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            setOnClickListener {
                clicks++
                handleLikeClick()
            }
        }

        buttonYes = Button(this).apply {
            text = "Sim"
            textSize = 18f
            setBackgroundColor(Color.GREEN)
            setTextColor(Color.WHITE)
            visibility = Button.GONE
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            setOnClickListener {
                resetGame()
            }
        }

        buttonNo = Button(this).apply {
            text = "Não"
            textSize = 18f
            setBackgroundColor(Color.RED)
            setTextColor(Color.WHITE)
            visibility = Button.GONE
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(16, 16, 16, 16)
            }
            setOnClickListener {
                finish()
            }
        }

        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(buttonDislike)
            addView(buttonLike)
        }

        val restartButtonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(buttonYes)
            addView(buttonNo)
        }

        mainLayout.addView(imageView)
        mainLayout.addView(textView)
        mainLayout.addView(buttonRow)
        mainLayout.addView(restartButtonRow)

        setContentView(mainLayout)
    }

    private fun handleLikeClick() {
        when {
            clicks >= targetClicks -> {
                textView.text = "Parabéns! Você conquistou a Gal Gadot!"
                imageView.setImageResource(R.drawable._4)
            }
            clicks > targetClicks * 0.66 -> {
                textView.text = "Ela está gostando de você!"
                imageView.setImageResource(R.drawable._3)
            }
            clicks > targetClicks * 0.33 -> {
                textView.text = "Você a chamou pra sair!"
                imageView.setImageResource(R.drawable._2)
            }
            else -> {
                textView.text = "Você conheceu a Gal Gadot!"
                imageView.setImageResource(R.drawable._1)
            }
        }
    }

    private fun showRestartOptions() {
        buttonYes.visibility = Button.VISIBLE
        buttonNo.visibility = Button.VISIBLE

        buttonDislike.visibility = Button.GONE
        buttonLike.visibility = Button.GONE
    }

    private fun resetGame() {
        clicks = 0
        targetClicks = (0..10).random()
        textView.text = "Clique para avançar na sua jornada!"
        imageView.setImageResource(R.drawable.fundo)

        buttonDislike.visibility = Button.VISIBLE
        buttonLike.visibility = Button.VISIBLE

        buttonYes.visibility = Button.GONE
        buttonNo.visibility = Button.GONE
    }
}
