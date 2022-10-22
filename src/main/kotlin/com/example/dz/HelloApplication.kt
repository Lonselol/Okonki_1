package com.example.dz

import javafx.animation.FadeTransition
import javafx.animation.TranslateTransition
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.shape.Circle
import javafx.scene.shape.Rectangle
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration


enum class Position {
    RIGHT_BOTTOM,
    RIGHT_TOP,
    LEFT_BOTTOM,
    LEFT_TOP
}

enum class ImageStyle {
    CIRCLE, RECTANGLE
}

class Config {
    var alpha = 0.9
    var openTime = 17000.0
    var imageType = ImageStyle.RECTANGLE
    var title = "TITLE"
    var message = "MESSAGE"
    var appName = "APP NAME"
    var image = "https://avatars.mds.yandex.net/i?id=ee0a8cd0c69a411b7fee131fde2b4980-3732926-images-thumbs&n=13"
    var position = Position.RIGHT_BOTTOM
    var musicFile = "https://clck.ru/32ReV2"
    var bgColor = "#808080"
}

class Toast {
    private var config = Config()
    private val windows = Stage()
    private var root = BorderPane()
    private var box = HBox()


    class Builder {
        private var config = Config()

        fun setTitle(str: String): Builder {
            config.title = str
            return this
        }

        fun setMessage(str: String): Builder {
            config.message = str;
            return this
        }

        fun setAppName(str: String): Builder {
            config.appName = str
            return this
        }

        fun setPosition(pos: Position): Builder {
            config.position = pos
            return this
        }

        fun setStyle(style: ImageStyle): Builder {
            config.imageType = style
            return this
        }

        fun build(): Toast  {
            var toast = Toast()
            toast.config = config
            toast.build()

            return toast
        }
    }


    private fun build() {
        windows.initStyle(StageStyle.TRANSPARENT)

        val sound = Media(config.musicFile.toString())
        val mediaPlayer = MediaPlayer(sound)
        mediaPlayer.play()

        setImage()

        val vbox = VBox()

        val title = Label(config.title)
        val message = Label(config.message)
        val appName = Label(config.appName)

        val buttonClose = Button()
        box.children.add(buttonClose)
        buttonClose.addEventFilter(MouseEvent.MOUSE_PRESSED, EventHandler { me: MouseEvent ->
            closeAnimation()
        })

        vbox.children.addAll(title, message, appName, buttonClose)

        title.setStyle("-fx-font-family: Cochin; -fx-font-size: 20")
        message.setStyle("-fx-font-family: Didot; -fx-font-size: 15")
        appName.setStyle("-fx-font-family: Dialog; -fx-font-size: 10")
        box.children.add(vbox)
        root.center = box

        windows.isResizable = true
        val width = 400.0
        val height = windows.height

        box.setPrefSize(width, windows.height)
        box.setPadding(Insets(5.0, 5.0, 5.0, 5.0))
        box.spacing = 10.0
        box.style = "-fx-background-color:" + config.bgColor

        var screenRect = Screen.getPrimary().bounds

        windows.scene = Scene(root, width, windows.height)
        windows.scene.fill = Color.TRANSPARENT

        root.setPrefSize(width, windows.height)

        val shift = 10.0
        when (config.position) {
            Position.LEFT_BOTTOM -> {
                windows.x = shift
                windows.y = screenRect.height - 200
            }
            Position.LEFT_TOP -> {
                windows.x = shift
                windows.y = shift
            }
            Position.RIGHT_BOTTOM -> {
                windows.x = screenRect.width - width - shift
                windows.y = screenRect.height - 200
            }
            Position.RIGHT_TOP -> {
                windows.x = screenRect.width - width - shift
                windows.y = shift
            }
        }

    }

    private fun setImage() {
        if (config.image.isEmpty()) {
            return
        }

        val iconBorder = if (config.imageType == ImageStyle.RECTANGLE) {
            Rectangle(100.0, 100.0)
        }
        else {
            Circle(50.0, 50.0, 50.0)
        }
        iconBorder.setFill(ImagePattern(Image(config.image)))
        box.children.add(iconBorder)
    }

    private fun openAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = 0.0
        anim.toValue = config.alpha
        anim.cycleCount = 1
        anim.play()
    }

    private fun altOpenAnimation() {
        val anim = TranslateTransition(Duration.millis(1500.0), root, )
        var screenRect = Screen.getPrimary().bounds

        anim.cycleCount = 1
        anim.play()
    }

    private fun closeAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = config.alpha
        anim.toValue = 0.0
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }

    private fun altCloseAnimation() {
        val anim = FadeTransition(Duration.millis(1500.0), root)
        anim.fromValue = config.alpha
        anim.toValue = 0.0
        anim.cycleCount = 1
        anim.onFinished = EventHandler {
            Platform.exit()
            System.exit(0)
        }
        anim.play()
    }

    fun start() {
        windows.show()
        openAnimation();
        val thread = Thread {
            try {
                Thread.sleep(config.openTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            closeAnimation()
        }
        Thread(thread).start()
    }

}


class SomeClass: Application() {
    override fun start(p0: Stage?) {
        var toast = Toast.Builder()
            .setTitle("Title")
            .setMessage("AppMessage")
            .setAppName("AppName")
            .setPosition(Position.RIGHT_TOP)
            .setStyle(ImageStyle.CIRCLE)
            .build()
        toast.start()
    }
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(SomeClass::class.java)
        }
    }
}