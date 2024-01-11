package tech.runchen.flutter_usb_device_manage.core

import android.content.Context
import android.util.Base64
import androidx.annotation.NonNull
import com.fpreader.fpdevice.AsyncUsbReader
import com.fpreader.fpdevice.UseManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import tech.runchen.flutter_usb_device_manage.enum.EventType
import tech.runchen.flutter_usb_device_manage.message.EventBus
import tech.runchen.flutter_usb_device_manage.message.EventMessage

object Fingerprint {

    private lateinit var vFingerprint: AsyncUsbReader
    private var workType: Int = 1
    private var enrolCount: Int = 1
    private var maxEnrolCount: Int = 1

    fun getInstance(): Fingerprint {
        init()
        return this;
    }

    fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result, context: Context) {
        when {
            call.method.contains("openDevice") -> {
                result.success(openDevice(context));
            }

            call.method.contains("closeDevice") -> {
                result.success(closeDevice());
            }

            call.method.contains("registration") -> {
                var enrolCount: Int? = call.argument("enrolCount")
                if (enrolCount != null) {
                    registration(enrolCount)
                };
            }

            call.method.contains("collecting") -> {
                collecting();
            }
        }
    }

    private fun init() {
        vFingerprint = UseManager.getInstance().asyncUsbReader;
        vFingerprint.setOnGetImageListener(object : AsyncUsbReader.OnGetImageListener {
            override fun onGetImageSuccess() {
                if (workType == 1) {
                    if (enrolCount < maxEnrolCount) {
                        if (0 == vFingerprint.usbReader.FPGenChar(-0x1, enrolCount)) {
                            var eventMessage = EventMessage(
                                EventType.FINGERPRINT_MESSAGE,
                                "第" + enrolCount + "次指纹登记成功"
                            );
                            EventBus.getDefault().publish(eventMessage)
                            enrolCount ++
                            vFingerprint.GetImage()
                        }
                    } else {
                        if (0 == vFingerprint.usbReader.FPRegModule(-0x1)) {
                            val tpSize = IntArray(1)
                            val tpData = ByteArray(512)
                            if (vFingerprint.usbReader.FPUpChar(-0x1, 0x01, tpData, tpSize) == 0) {
                                var str = Base64.encodeToString(tpData, Base64.DEFAULT).replace("\n","")
                                var eventMessage = EventMessage(EventType.FINGERPRINT_RESULT, str);
                                EventBus.getDefault().publish(eventMessage)
                            }
                        }
                    }
                } else if (workType == 2) {
                    if (0 == vFingerprint.usbReader.FPGenChar(-0x1, enrolCount)) {
                        val tpSize = IntArray(1)
                        val tpData = ByteArray(512)
                        if (vFingerprint.usbReader.FPUpChar(-0x1, 0x01, tpData, tpSize) == 0) {
                            var str = Base64.encodeToString(tpData, Base64.DEFAULT).replace("\n","")
                            var eventMessage = EventMessage(EventType.FINGERPRINT_RESULT, str);
                            EventBus.getDefault().publish(eventMessage)
                        }
                    }
                }
            }

            override fun onGetImageFail() {
                vFingerprint.GetImage()
            }
        })
    }

    private fun openDevice(context: Context): Boolean {
        return vFingerprint.OpenDevice(context) == 0
    }

    private fun closeDevice(): Boolean {
        return vFingerprint.CloseDevice() == 0
    }

    private fun registration(enrolCount: Int) {
        this.maxEnrolCount = enrolCount
        this.enrolCount = 1
        this.workType = 1
        vFingerprint.usbReader.FPEmpty(-0x1)
        vFingerprint.GetImage()
    }

    private fun collecting() {
        this.workType = 2
        vFingerprint.usbReader.FPEmpty(-0x1)
        vFingerprint.GetImage()
    }
}