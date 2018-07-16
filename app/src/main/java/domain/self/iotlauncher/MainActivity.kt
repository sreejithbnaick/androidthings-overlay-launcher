package domain.self.iotlauncher

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val appsService = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
        val userHandle = android.os.Process.myUserHandle()
        val allApps = appsService.getActivityList(null, userHandle).filter {
            it.componentName.packageName != "domain.self.iotlauncher"
        }
        val allAppNames = allApps.map { it.label }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, allAppNames)
        lv_all_apps.adapter = adapter
        lv_all_apps.setOnItemClickListener { _, _, position, _ ->
            val launcherInfo = allApps[position]
            appsService.startMainActivity(launcherInfo.componentName, userHandle, null, null)
        }

        startService(Intent(this, OverlayService::class.java))
    }
}

fun showShort(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
