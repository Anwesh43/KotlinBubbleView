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
            body.update(Vector(0f,3f),stopcb)
            size = body.velocity.y
        }
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            canvas.drawCircle(body.position.x,body.position.y,size/2,paint)
        }
        fun increaseSize() {
            size+=3
            body.update_velocity(Vector(0f,-size))
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
        fun startBubbling(x:Float,y:Float) {
            currBubble = Bubble()
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
    data class BubblesRenderer(var view:BubblesView,var time:Int = 0) {
        var container:BubbleContainer?=null
        var mode = 0
        var animated = false
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = BubbleContainer(w,h)
            }
            container?.draw(canvas,paint)
            time++
        }
        fun startBubbling(x:Float,y:Float) {
            container?.startBubbling(x,y)
        }
        private fun update() {
            when(mode) {
                0 -> container?.update {
                    animated = false
                }
                1 -> container?.increaseSize()
            }
        }
        fun updateMode(mode:Int) {
            this.mode = mode
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
}