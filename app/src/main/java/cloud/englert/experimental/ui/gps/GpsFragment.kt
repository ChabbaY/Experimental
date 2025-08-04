package cloud.englert.experimental.ui.gps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import cloud.englert.experimental.databinding.FragmentGpsBinding

class GpsFragment : Fragment() {
    private var _binding: FragmentGpsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGpsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Request location permissions
        if (allPermissionsGranted()) {
            getLocation()
        } else {
            requestPermissions()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLocation() {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener {  location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val locationString = "Location: (Lat: $latitude / Lon: $longitude)"
                    Log.d(TAG, locationString)
                    Toast.makeText(requireContext(), locationString,
                        Toast.LENGTH_SHORT).show()
                    binding.textLocation.text = locationString
                }
            }
        }
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
            } else {
                getLocation()
            }
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "GpsFragment"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).toTypedArray()
    }
}