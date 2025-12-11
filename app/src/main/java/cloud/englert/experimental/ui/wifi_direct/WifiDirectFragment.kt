package cloud.englert.experimental.ui.wifi_direct

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import cloud.englert.experimental.MainActivity
import cloud.englert.experimental.custom.DeviceListAdapter
import cloud.englert.experimental.databinding.FragmentWifiDirectBinding

class WifiDirectFragment : Fragment() {
    private var _binding: FragmentWifiDirectBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var activity: MainActivity

    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var intentFilter: IntentFilter
    private lateinit var receiver: WifiReceiver
    private var peers = mutableListOf<WifiP2pDevice>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWifiDirectBinding.inflate(inflater, container,
            false)
        val root: View = binding.root

        activity = requireActivity() as MainActivity

        manager = activity.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(activity, activity.mainLooper, null)

        intentFilter = IntentFilter().apply {
            // Indicates a change in the Wi-Fi Direct status.
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            // Indicates a change in the list of available peers.
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            // Indicates the state of Wi-Fi Direct connectivity has changed.
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            // Indicates this device's details have changed.
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }

        if (!allPermissionsGranted()) {
            requestPermissions()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        receiver = WifiReceiver(activity, manager, channel, peerListListener)
        activity.registerReceiver(receiver, intentFilter)

        try {
            discoverPeers()
        } catch (e: SecurityException) {
            Log.d(TAG, "Discovery failed: ${e.message}, requesting permissions again")
            requestPermissions()
        }
    }

    override fun onPause() {
        super.onPause()
        activity.unregisterReceiver(receiver)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES])
    private fun discoverPeers() {
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank. Code for peer discovery goes in the
                // onReceive method, detailed below.
                Log.d(TAG, "Discovery started")
            }

            override fun onFailure(reason: Int) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.
                Log.d(TAG, "Discovery failed: $reason")
            }
        })
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES])
    private fun connect(device: WifiP2pDevice) {
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // WiFiDirectBroadcastReceiver notifies us. Ignore for now.
            }

            override fun onFailure(reason: Int) {
                Toast.makeText(
                    activity,
                    "Connect failed. Retry.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList
        if (refreshedPeers != peers) {
            peers.clear()
            peers.addAll(refreshedPeers)

            // Perform any updates needed based on the new list of
            // peers connected to the Wi-Fi P2P network.
            Log.d(TAG, "Found ${peers.size} devices, updating list")
            updateListView()
        }

        if (peers.isEmpty()) {
            Log.d(TAG, "No devices found")
            return@PeerListListener
        }
    }

    private class WifiReceiver(
        var activity: MainActivity,
        var manager: WifiP2pManager,
        var channel: WifiP2pManager.Channel,
        var peerListListener: WifiP2pManager.PeerListListener
    ) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                    // Determine if Wi-Fi Direct mode is enabled or not, alert the Activity.
                    val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                    activity.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                }
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                    // Request available peers from the wifi p2p manager. This is an
                    // asynchronous call and the calling activity is notified with a
                    // callback on PeerListListener.onPeersAvailable()
                    try {
                        manager.requestPeers(channel, peerListListener)
                        Log.d(TAG, "P2P peers changed")
                    } catch (e: SecurityException) {
                        Log.d(TAG, "Requesting peers failed: ${e.message}")
                    }
                }
                WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                    // Connection state changed! We should probably do something about that.
                }
                WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {

                }
            }
        }
    }

    private fun createListView(view: ListView, data: List<WifiP2pDevice>) {
        val listenersDetails = arrayOfNulls<View.OnClickListener>(data.size)
        for ((index, device) in data.withIndex()) {
            listenersDetails[index] = View.OnClickListener { _: View? ->
                try {
                    connect(device)
                } catch (e: SecurityException) {
                    Log.d(TAG, "Connect failed: ${e.message}, requesting permissions again")
                    requestPermissions()
                }
            }
        }

        val dataAdapter = DeviceListAdapter(activity, data, listenersDetails)
        view.adapter = dataAdapter
    }

    private fun updateListView() {
        createListView(binding.listViewDevices, peers)
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireContext(),
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val TAG = WifiDirectFragment::class.java.simpleName
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        ).apply { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.plus(Manifest.permission.NEARBY_WIFI_DEVICES)
        } }
    }
}