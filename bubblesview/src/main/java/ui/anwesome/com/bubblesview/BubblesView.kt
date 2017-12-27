package ui.anwesome.com.bubblesview

/**
 * Created by anweshmishra on 27/12/17.
 */
import android.view.*
import android.graphics.*
import android.content.*
import java.util.concurrent.ConcurrentLinkedQueue

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
    data class Vector(var x:Float,var y:Float) {
        fun add(v:Vector) {
            x+=v.x
            y+=v.y
        }
    }
    data class PhysicsBody(var position:Vector,var velocity:Vector = Vector(0f,0f)) {
        fun update(acceleration:Vector,stopcb: () -> Unit) {
            velocity.add(acceleration)
            position.add(acceleration)
            if(velocity.x == 0f && velocity.y == 0f) {
                stopcb()
            }
        }
        fun update_velocity(v:Vector) {
            this.velocity = v
        }
    }
    data class Bubble(var x:Float,var y:Float,var size:Float = 0f) {
        var body = PhysicsBody(Vector(x,y))
        fun update(stopcb: () -> Unit) {
            size--
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            canvas.drawCircle(body.position.x,body.position.y,size/2,paint)
        }
        fun increaseSize() {
            size++
            body.update_velocity(Vector(0f,size))
        }
    }
    data class BubbleContainer(var w:Float,var h:Float) {
        var bubbles:ConcurrentLinkedQueue<Bubble> = ConcurrentLinkedQueue()
        var currBubble:Bubble ?= null
        fun increaseSize() {
            currBubble?.increaseSize()
        }
        fun update(stopcb:()->Unit) {
            bubbles.forEach {
                it.update {
                    bubbles.remove(it)
                    if(bubbles.size == 0) {
                        stopcb()
                    }
                }
            }
        }
        fun startUpdating(startcb:()->Unit) {
            bubbles.add(currBubble)
            currBubble = null
            startcb()
        }
        fun draw(canvas:Canvas,paint:Paint) {
            bubbles.forEach { it ->
                it.draw(canvas, paint)
            }
        }
    }
}