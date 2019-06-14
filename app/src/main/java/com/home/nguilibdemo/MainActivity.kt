package com.home.nguilibdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeCentralTractionButton()
        mushroomCentralTractionButton.post {
            mushroomCentralTractionButton.performClick()
        }
    }

    private fun initializeCentralTractionButton() {
        mushroomCentralTractionButton.setOnClickListener {
            starCentralTractionButton.isChecked = false
            wingCentralTractionButton.isChecked = false
        }
        starCentralTractionButton.setOnClickListener {
            mushroomCentralTractionButton.isChecked = false
            wingCentralTractionButton.isChecked = false
        }
        wingCentralTractionButton.setOnClickListener {
            mushroomCentralTractionButton.isChecked = false
            starCentralTractionButton.isChecked = false
        }
    }
}
