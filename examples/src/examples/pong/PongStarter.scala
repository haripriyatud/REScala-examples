package examples.pong

import react.events.ImperativeEvent
import react.SignalSynt
import react.Var
import react.Signal
import macro.SignalMacro.{ SignalM => Signal }
import swing.{ Panel, MainFrame, SimpleSwingApplication }
import java.awt.{ Color, Graphics2D, Dimension }
import java.awt.Point
import scala.swing.Swing
import scala.swing.event._

object PongStarter {
  def main(args: Array[String]) {
    val app = new PongWindow
    app.main(args)
    while (true) {
      Thread sleep 30
      app.tick()
    }
  }
}

class PongWindow extends SimpleSwingApplication {
  val Size = 20
  val Max_X = 800
  val Max_Y = 400

  val tick = new ImperativeEvent[Unit]
  tick += { _: Unit => frame.repaint() }

  val mouse = new Mouse

  val ball = new Pong(tick, mouse)

  // drawing code
  def top = frame
  val frame: MainFrame = new MainFrame {
    title = "Pong"
    resizable = false
    contents = new Panel() {
      listenTo(mouse.moves, mouse.clicks)

      /** forward mouse events to EScala wrapper class. Should be replaced once reactive GUI lib is complete */
      reactions += {
        case e: MouseMoved => { PongWindow.this.mouse.mouseMovedE(e.point) }
        case e: MousePressed => PongWindow.this.mouse.mousePressedE(e.point)
        case e: MouseDragged => { PongWindow.this.mouse.mouseDraggedE(e.point) }
        case e: MouseReleased => PongWindow.this.mouse.mouseReleasedE(e.point)
      }

      preferredSize = new Dimension(Max_X, Max_Y)
      override def paintComponent(g: Graphics2D) {
        g.fillOval(ball.x.getVal, ball.y.getVal, Size, Size)
        
        g.fillRect(ball.leftRacket.area.getVal.x, 
        		   ball.leftRacket.area.getVal.y,
        		   ball.leftRacket.area.getVal.width,
        		   ball.leftRacket.area.getVal.height
            )
        g.fillRect(ball.rightRacket.area.getVal.x, 
        		   ball.rightRacket.area.getVal.y,
        		   ball.rightRacket.area.getVal.width,
        		   ball.rightRacket.area.getVal.height
            )        
      }
    }
  }
}