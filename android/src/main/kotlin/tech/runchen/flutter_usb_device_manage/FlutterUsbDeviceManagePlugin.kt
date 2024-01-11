package tech.runchen.flutter_usb_device_manage

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import tech.runchen.flutter_usb_device_manage.core.Fingerprint
import tech.runchen.flutter_usb_device_manage.core.RFID
import tech.runchen.flutter_usb_device_manage.message.EventBus
import tech.runchen.flutter_usb_device_manage.permission.UsbDevicePermission

/** FlutterUsbDeviceManagePlugin */
class FlutterUsbDeviceManagePlugin : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private lateinit var flutterPluginBinding: FlutterPlugin.FlutterPluginBinding

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding

        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_usb_device_manage")
        channel.setMethodCallHandler(this)

        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "flutter_usb_device_manage.event_channel");
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                if (events != null) {
                    EventBus.getDefault().register(events)
                };
            }

            override fun onCancel(arguments: Any?) {
                EventBus.getDefault().unregister()
            }
        })
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "requestPermission") {
            UsbDevicePermission.getInstance(flutterPluginBinding.applicationContext).requestPermission(flutterPluginBinding.applicationContext);
        } else if(call.method.contains("Fingerprint")) {
            Fingerprint.getInstance().onMethodCall(call, result, flutterPluginBinding.applicationContext);
        } else if(call.method.contains("RFID")) {
            RFID.getInstance().onMethodCall(call, result, flutterPluginBinding.applicationContext);
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }
}
