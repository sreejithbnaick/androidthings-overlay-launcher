package domain.self.iotlauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Sreejith on 15/7/18.
 */
class BootCompletedIntentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent?.action) {
            context?.apply {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}
