package tech.runchen.flutter_usb_device_manage.message

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.flutter.plugin.common.EventChannel

object EventBus {

    private var eventSink: EventChannel.EventSink? = null

    fun getDefault(): EventBus {
        return this;
    }

    private val handler = object : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            if (msg.obj is EventMessage) {
                eventSink?.success(GsonBuilder().disableHtmlEscaping().create().toJson(msg.obj));
            }
        }
    }

    fun register(eventSink: EventChannel.EventSink) {
        this.eventSink = eventSink
    }

    fun unregister() {
        this.eventSink = null
    }

    fun publish(eventMessage: EventMessage) {
        val message = Message.obtain()
        message.obj = eventMessage
        handler.handleMessage(message)
    }
}