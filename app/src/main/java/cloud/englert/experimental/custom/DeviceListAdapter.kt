package cloud.englert.experimental.custom

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView

import cloud.englert.experimental.R

class DeviceListAdapter(
    context: Context,
    data: List<WifiP2pDevice>,
    private val listenersDetails: Array<View.OnClickListener?>
) : ArrayAdapter<WifiP2pDevice?>(context, R.layout.list_device, R.id.deviceListTitle, data) {
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView: View = super.getView(position, view, parent)

        val device = getItem(position) ?: return rowView

        (rowView.findViewById<View>(R.id.deviceListTitle) as TextView).text =
            device.deviceName
        (rowView.findViewById<View>(R.id.deviceListSubtitle) as TextView).text =
            device.deviceAddress
        val buttonDetails: ImageButton = rowView.findViewById(R.id.deviceListButtonDetails)
        buttonDetails.setOnClickListener(listenersDetails[position])

        return rowView
    }
}