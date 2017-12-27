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
    data class Vector(var x:Float,var y:Float) {
        fun add(v:Vector) {
            x+=v.x
            y+=v.y
        }
    }
    data class PhysicsBody(var position:Vector,var velocity:Vector = Vector(0f,0f)) {
        fun update(acceleration:Vector) {
            velocity.add(acceleration)
            position.add(acceleration)
        }
        fun update_velocity(v:Vector) {
            this.velocity = v
        }
    }
    data class Bubble(var x:Float,var y:Float,var size:Float = 0f) {
        var body = PhysicsBody(Vector(x,y))
        fun update() {
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
}