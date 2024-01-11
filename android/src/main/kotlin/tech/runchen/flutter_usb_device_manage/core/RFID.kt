package tech.runchen.flutter_usb_device_manage.core

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.annotation.NonNull
import com.android.usbserial.client.AndroidUsbSerialClient
import com.android.usbserial.client.OnUsbSerialDeviceListener
import com.gg.reader.api.dal.GClient
import com.gg.reader.api.dal.HandlerTagEpcLog
import com.gg.reader.api.dal.HandlerTagEpcOver
import com.gg.reader.api.protocol.gx.EnumG
import com.gg.reader.api.protocol.gx.LogBaseEpcInfo
import com.gg.reader.api.protocol.gx.MsgBaseInventoryEpc
import com.gg.reader.api.protocol.gx.MsgBaseStop
import com.google.gson.Gson
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import tech.runchen.flutter_usb_device_manage.enum.EventType
import tech.runchen.flutter_usb_device_manage.enum.UsbDeviceType
import tech.runchen.flutter_usb_device_manage.message.EventBus
import tech.runchen.flutter_usb_device_manage.message.EventMessage


object RFID {

    private const val TAG = "RFID"
    private var client: GClient? = null

    fun getInstance(): RFID {
        return this;
    }

    private val handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            if (msg.obj is EventMessage) {
                EventBus.getDefault().publish(msg.obj as EventMessage);
            }
        }
    }

    fun onMethodCall(
        @NonNull call: MethodCall,
        @NonNull result: MethodChannel.Result,
        context: Context
    ) {
        when {
            call.method.contains("openUsbSerial") -> {
                result.success(openAndroidUsbSerial(context));
            }

            call.method.contains("closeUsbSerial") -> {
                result.success(closeAndroidUsbSerial())
            }

            call.method.contains("startRead") -> {
                var mode = call.argument<Int>("mode")
                var timeout = call.argument<Int>("timeout")
                if (mode != null && timeout != null) {
                    result.success(startRead(mode, timeout))
                };
            }

            call.method.contains("stopRead") -> {
                result.success(stopRead())
            }
        }
    }

    private fun openAndroidUsbSerial(context: Context): Boolean {
        client = GClient();
        val usbDevicesList = AndroidUsbSerialClient.getUsbDevicesList(context)
        var usbClient: AndroidUsbSerialClient? = null
        for (device in usbDevicesList) {
            when {
                device.usbDevice.vendorId == UsbDeviceType.RFID.vendorId && device.usbDevice.productId == UsbDeviceType.RFID.productId -> {
                    usbClient = device
                }
            }
        }
        usbClient?.deviceListener = object : OnUsbSerialDeviceListener {
            override fun onDeviceConnected() {
                Log.i(TAG, "RFID设备连接成功")
                var eventMessage = EventMessage(EventType.RFID_MESSAGE, "RFID设备连接成功");
                EventBus.getDefault().publish(eventMessage)
            }

            override fun onDeviceConnectFailed() {
                Log.i(TAG, "RFID设备连接失败")
                var eventMessage = EventMessage(EventType.RFID_MESSAGE, "RFID设备连接失败");
                EventBus.getDefault().publish(eventMessage)
            }
        }
        if (client?.openAndroidUsbSerial(usbClient) == true) {
            client?.onTagEpcLog = HandlerTagEpcLog { _, info ->
                Log.i(TAG, "6C标签主动上报开始.")
                var eventMessage = EventMessage(EventType.RFID_RESULT, Gson().toJson(epcInfoToMap(info)));
                val msg = Message.obtain()
                msg.obj = eventMessage
                handler.sendMessage(msg)
            }
            client?.onTagEpcOver = HandlerTagEpcOver { _, _ ->
                Log.i(TAG, "6C标签主动上报结束.")
                var eventMessage = EventMessage(EventType.RFID_MESSAGE, "6C标签主动上报结束");
                val msg = Message.obtain()
                msg.obj = eventMessage
                handler.sendMessage(msg)
            }
            return true
        }
        return false;
    }

    private fun startRead(mode: Int, timeout: Int): Boolean {
        if (client != null) {
            val msgBaseInventoryEpc = MsgBaseInventoryEpc()
            msgBaseInventoryEpc.antennaEnable = EnumG.AntennaNo_1 or EnumG.AntennaNo_2
            msgBaseInventoryEpc.inventoryMode = mode
            msgBaseInventoryEpc.timeout = timeout
            client!!.sendSynMsg(msgBaseInventoryEpc)
            if (0 == msgBaseInventoryEpc.rtCode.toInt()) {
                return true
            }
        }
        return false
    }

    private fun stopRead(): Boolean {
        if (client != null) {
            val msgBaseStop = MsgBaseStop()
            client!!.sendSynMsg(msgBaseStop)
            if (0 == msgBaseStop.rtCode.toInt()) {
                return true
            }
        }
        return false
    }

    private fun closeAndroidUsbSerial(): Boolean {
        if (client != null) {
            stopRead()
            return client!!.close()
        }
        return false
    }

    private fun epcInfoToMap(info: LogBaseEpcInfo): Map<String, Any>? {
        val data: MutableMap<String, Any> = HashMap()
        data["Epc"] = info.epc
        data["Pc"] = info.pc
        data["AntId"] = info.antId
        data["Rssi"] = info.rssi
        data["Result"] = info.result
        return data
    }

}