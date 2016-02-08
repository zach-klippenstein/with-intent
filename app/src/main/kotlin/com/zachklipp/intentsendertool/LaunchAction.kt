package com.zachklipp.intentsendertool

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * A way to launch an intent. Map to the {@code context.start*} methods.
 */
enum class LaunchAction(val launchName: String) {

  ACTIVITY("Activity") {

    override fun resolve(intent: Intent, packageManager: PackageManager): IntentTargets {
      return IntentTargets(packageManager.resolveActivity(intent, 0),
          packageManager.queryIntentActivities(intent, 0))
    }

    override fun launch(intent: Intent, context: Context) {
      context.startActivity(intent)
    }
  },

  BROADCAST("Broadcast") {

    override fun resolve(intent: Intent, packageManager: PackageManager): IntentTargets {
      return IntentTargets(otherResults = packageManager.queryBroadcastReceivers(intent, 0))
    }

    override fun launch(intent: Intent, context: Context) {
      context.sendBroadcast(intent)
    }
  },

  SERVICE("Service") {

    override fun resolve(intent: Intent, packageManager: PackageManager): IntentTargets {
      return IntentTargets(packageManager.resolveService(intent, 0),
          packageManager.queryIntentServices(intent, 0))
    }

    override fun launch(intent: Intent, context: Context) {
      context.startService(intent)
    }

  };

  abstract fun resolve(intent: Intent, packageManager: PackageManager): IntentTargets
  abstract fun launch(intent: Intent, context: Context)

  override fun toString(): String {
    return launchName
  }

  companion object {
    fun getLaunchTypeNames(): List<String> = LaunchAction.values().map { it.launchName }
  }
}
