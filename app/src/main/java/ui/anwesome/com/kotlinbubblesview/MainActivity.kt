package ui.anwesome.com.kotlinbubblesview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.bubblesview.BubblesView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BubblesView.create(this)
    }
}
