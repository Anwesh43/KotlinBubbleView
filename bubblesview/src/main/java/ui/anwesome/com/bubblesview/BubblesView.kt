package ui.anwesome.com.bubblesview

/**
 * Created by anweshmishra on 27/12/17.
 */
import android.app.Activity
import android.view.*
import android.graphics.*
import android.content.*
import java.util.concurrent.ConcurrentLinkedQueue

class BubblesView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = BubblesRenderer(this)
    val touchHandler = BubbleTouchHandler(renderer)
    override fun onDraw(canvas:Canvas) {
        canvas.drawColor(Color.parseColor("#212121"))
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        touchHandler.handleTouch(event)
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
            position.add(velocity)
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
            size = Math.abs(body.velocity.y)
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
            currBubble = Bubble(x,y)
        }
        fun startUpdating(startcb:()->Unit) {
            bubbles.add(currBubble)
            currBubble = null
            startcb()
        }
        fun draw(canvas:Canvas,paint:Paint) {
            bubbles.forEach {
                it.draw(canvas, paint)
            }
            currBubble?.draw(canvas,paint)
        }
    }
    data class BubblesRenderer(var view:BubblesView,var time:Int = 0) {
        var container:BubbleContainer?=null
        var mode = 0
        val animator = BubbleAnimator(view)
        fun render(canvas: Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                container = BubbleContainer(w,h)
            }
            container?.draw(canvas,paint)
            update()
            time++
        }
        fun startBubbling(x:Float,y:Float) {
            container?.startBubbling(x,y)
            animator?.startUpdating()
        }
        private fun update() {
            animator.update {
                when(mode) {
                    1 -> container?.update {
                        animator.stop()
                    }
                    0 -> container?.increaseSize()
                }
            }
        }
        fun startUpdating() {
            container?.startUpdating {
            }
        }
        fun updateMode(mode:Int) {
            this.mode = mode
        }
    }
    data class BubbleAnimator(var view:BubblesView,var animated:Boolean = false) {
        fun update(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
        fun startUpdating() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
    }
    data class BubbleTouchHandler(var renderer:BubblesRenderer) {
        fun handleTouch(event:MotionEvent) {
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    renderer.startBubbling(event.x,event.y)
                    renderer.updateMode(0)
                }
                MotionEvent.ACTION_UP -> {
                    renderer.updateMode(1)
                    renderer.startUpdating()
                }
            }
        }
    }
    companion object {
        fun create(activity:Activity) {
            val view = BubblesView(activity)
            activity.setContentView(view)
        }
    }
}