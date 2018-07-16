package domain.self.iotlauncher

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Process.THREAD_PRIORITY_BACKGROUND
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout


/**
 * Created by Sreejith on 15/7/18.
 */
class OverlayService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    private lateinit var wm: WindowManager
    private var homeButton: Button? = null
    private var launcherButton: Button? = null
    private var mainLayout: LinearLayout? = null
    private val handlerThread = HandlerThread("handler-thread", THREAD_PRIORITY_BACKGROUND)
    private var threadHandler: Handler

    init {
        handlerThread.start()
        threadHandler = Handler(handlerThread.looper)
    }

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val params = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT)
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        params.x = 0
        params.y = 0
        try {
            wm.addView(mainLayout, params)
        } catch (e: Exception) {
            e.printStackTrace()
            showShort(this, "Reboot device")
            return
        }

        homeButton = Button(this).apply {
            text = context.getString(R.string.home)
            setOnClickListener {
                val startMain = Intent(Intent.ACTION_MAIN)
                startMain.addCategory(Intent.CATEGORY_HOME)
                startActivity(startMain)
            }
        }

        launcherButton = Button(this).apply {
            text = context.getString(R.string.launcher)
            setOnClickListener {
                startActivity(Intent(this@OverlayService, MainActivity::class.java))
            }
        }

        mainLayout?.apply {
            addView(homeButton)
            addView(launcherButton)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quitSafely()
        if (mainLayout != null) {
            wm.removeView(mainLayout)
        }
    }
}