package ui.anwesome.com.bubblesview

/**
 * Created by anweshmishra on 27/12/17.
 */
import android.view.*
import android.graphics.*
import android.content.*
class BubblesView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}