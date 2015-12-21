package com.zachklipp.intentsendertool

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

/**
 * A way to launch an intent. Map to the {@code context.start*} methods.
 */
public enum class LaunchAction(val launchName: String) {

  ACTIVITY("Activity") {

    public override fun resolve(intent: Intent, packageManager: PackageManager): IntentTargets {
      return IntentTargets(packageManager.resolveActivity(intent, 0),
          packageManager.queryIntentActivities(intent, 0))
    }

    public override fun launch(intent: Intent, context: Context) {
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

  public abstract fun resolve(intent: Intent, packageManager: PackageManager): IntentTargets
  public abstract fun launch(intent: Intent, context: Context)

  override fun toString(): String {
    return launchName
  }
}
