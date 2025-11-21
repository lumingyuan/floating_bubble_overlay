package com.example.floating_bubble_overlay

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.example.floating_bubble_overlay.src.BroadcastListener
import com.example.floating_bubble_overlay.src.BubbleManager
import com.example.floating_bubble_overlay.src.BubbleOptions
import com.example.floating_bubble_overlay.src.Constants
import com.example.floating_bubble_overlay.src.Helpers
import com.example.floating_bubble_overlay.src.NotificationOptions

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class FloatingBubbleOverlayPlugin : 
    FlutterPlugin, 
    MethodChannel.MethodCallHandler, 
    ActivityAware,
    PluginRegistry.ActivityResultListener, 
    PluginRegistry.RequestPermissionsResultListener {

    private var activityBinding: ActivityPluginBinding? = null
    private lateinit var mActivity: Activity
    private lateinit var channel: MethodChannel
    private lateinit var delayedResultHandler: MethodChannel.Result
    private lateinit var broadcastListener: BroadcastListener
    private lateinit var bubbleManager: BubbleManager

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(binding.binaryMessenger, Constants.METHOD_CHANNEL)
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        try {
            when (call.method) {

                Constants.REQUEST_OVERLAY_PERMISSION -> {
                    if (bubbleManager.requestOverlayPermission() == true) {
                        result.success(true)
                        return
                    }
                    delayedResultHandler = result
                }

                Constants.HAS_OVERLAY_PERMISSION ->
                    result.success(bubbleManager.hasOverlayPermission())

                Constants.REQUEST_POST_NOTIFICATIONS_PERMISSION -> {
                    if (bubbleManager.requestPostNotificationsPermission() == true) {
                        result.success(true)
                        return
                    }
                    delayedResultHandler = result
                }

                Constants.HAS_POST_NOTIFICATIONS_PERMISSION ->
                    result.success(bubbleManager.hasPostNotificationsPermission())

                Constants.IS_RUNNING ->
                    result.success(bubbleManager.isRunning())

                Constants.START_BUBBLE -> result.success(
                    bubbleManager.startBubble(
                        BubbleOptions.fromMethodCall(call),
                        NotificationOptions.fromMethodCall(call)
                    )
                )

                Constants.STOP_BUBBLE ->
                    result.success(bubbleManager.stopBubble())

                else -> result.notImplemented()
            }
        } catch (e: Exception) {
            result.error(Constants.ERROR_TAG, e.message, null)
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        mActivity = binding.activity

        binding.addActivityResultListener(this)
        binding.addRequestPermissionsResultListener(this)

        bubbleManager = BubbleManager(mActivity)
        broadcastListener = BroadcastListener(channel)

        val intentFilter = IntentFilter().apply {
            addAction(Constants.ON_TAP)
            addAction(Constants.ON_TAP_DOWN)
            addAction(Constants.ON_TAP_UP)
            addAction(Constants.ON_MOVE)
        }

        LocalBroadcastManager.getInstance(mActivity)
            .registerReceiver(broadcastListener, intentFilter)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activityBinding?.removeActivityResultListener(this)
        activityBinding?.removeRequestPermissionsResultListener(this)
        activityBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activityBinding = binding
        binding.addActivityResultListener(this)
        binding.addRequestPermissionsResultListener(this)
    }

    override fun onDetachedFromActivity() {
        activityBinding?.removeActivityResultListener(this)
        activityBinding?.removeRequestPermissionsResultListener(this)
        activityBinding = null

        LocalBroadcastManager.getInstance(mActivity)
            .unregisterReceiver(broadcastListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == Constants.OVERLAY_PERMISSION_REQUEST_CODE) {
            delayedResultHandler.success(bubbleManager.hasOverlayPermission())
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == Constants.POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE) {
            delayedResultHandler.success(bubbleManager.hasPostNotificationsPermission())
            return true
        }
        return false
    }
}