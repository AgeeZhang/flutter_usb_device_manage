package tech.runchen.flutter_usb_device_manage.permission

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import tech.runchen.flutter_usb_device_manage.enum.UsbDeviceType

object UsbDevicePermission {

    private const val TAG = "UsbDevicePermission"
    private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private var usbDeviceList: ArrayList<UsbDevice> = ArrayList();
    private var usbDeviceIndex: Int = 0
    private var mPermissionIntent: PendingIntent? = null
    private var usbManager: UsbManager? = null

    fun getInstance(context: Context): UsbDevicePermission {
        resumeRegister(context)
        return this;
    }

    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            Log.i(TAG, "设备就绪")
                            usbDeviceIndex++
                            hasPermission();
                        } else {
                            Log.i(TAG, "没有找到设备")
                        }
                    } else {
                        Log.i(TAG, "打开设备失败")
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
                Log.i(TAG, "USB设备插入")
                if (context != null) {
                    requestPermission(context)
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                Log.i(TAG, "USB设备拔出")
            }
        }
    }

    fun requestPermission(context: Context) {
        usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        for (device in usbManager!!.deviceList.values) {
            val productId = device.productId
            val vendorId = device.vendorId
            when {
                vendorId == UsbDeviceType.FINGERPRINT.vendorId && productId == UsbDeviceType.FINGERPRINT.productId -> {
                    usbDeviceList.add(device)
                }

                vendorId == UsbDeviceType.RFID.vendorId && productId == UsbDeviceType.RFID.productId -> {
                    usbDeviceList.add(device)
                }

                vendorId == UsbDeviceType.MHD.vendorId && productId == UsbDeviceType.MHD.productId -> {
                    usbDeviceList.add(device)
                }
            }
        }
        hasPermission()
    }

    private fun hasPermission() {
        if (usbDeviceList != null && usbDeviceList.size > 0) {
            if (!usbManager!!.hasPermission(usbDeviceList.getOrNull(usbDeviceIndex))) {
                usbManager!!.requestPermission(
                    usbDeviceList.getOrNull(usbDeviceIndex),
                    mPermissionIntent
                )
            } else {
                Log.i(TAG, "设备就绪")
            }
        }
    }

    private fun resumeRegister(context: Context) {
        mPermissionIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(ACTION_USB_PERMISSION),
            PendingIntent.FLAG_IMMUTABLE
        )
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        context.registerReceiver(mUsbReceiver, filter)
    }
}