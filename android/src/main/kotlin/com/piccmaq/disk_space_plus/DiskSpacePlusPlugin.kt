package com.piccmaq.disk_space_plus

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import android.os.Environment
import android.os.StatFs

class DiskSpacePlusPlugin: FlutterPlugin, MethodCallHandler {
  private lateinit var channel : MethodChannel

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "disk_space_plus")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(call: MethodCall, result: Result) {
    when(call.method) {
        "getFreeDiskSpace" -> result.success(getFreeDiskSpace())
        "getTotalDiskSpace" -> result.success(getTotalDiskSpace())
        "getFreeDiskSpaceForPath" -> result.success(getFreeDiskSpaceForPath(call.argument<String>("path")!!))
        else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  private fun getFreeDiskSpace(): Double {
      val stat = StatFs(Environment.getExternalStorageDirectory().path)

      val bytesAvailable: Long
      bytesAvailable = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
          stat.blockSizeLong * stat.availableBlocksLong
      else
          stat.blockSize.toLong() * stat.availableBlocks.toLong()
      return (bytesAvailable / (1024f * 1024f)).toDouble()
  }

  private fun getFreeDiskSpaceForPath(path: String): Double {
      val stat = StatFs(path)
  
      val bytesAvailable: Long
      bytesAvailable = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
          stat.blockSizeLong * stat.availableBlocksLong
      else
          stat.blockSize.toLong() * stat.availableBlocks.toLong()
      return (bytesAvailable / (1024f * 1024f)).toDouble()
  }

  private fun getTotalDiskSpace(): Double {
      val stat = StatFs(Environment.getExternalStorageDirectory().path)

      val bytesAvailable: Long
      bytesAvailable = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
          stat.blockSizeLong * stat.blockCountLong
      else
          stat.blockSize.toLong() * stat.blockCount.toLong()
      return (bytesAvailable / (1024f * 1024f)).toDouble()
  }
}