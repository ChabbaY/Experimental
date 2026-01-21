package cloud.englert.experimental.ui.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView

import androidx.fragment.app.Fragment

import cloud.englert.experimental.R
import cloud.englert.experimental.databinding.FragmentQrBinding
import cloud.englert.experimental.qrgenerator.QRCodeGenerator
import cloud.englert.experimental.qrgenerator.Version

class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    var errorCorrection: Version.ErrorCorrection = Version.ErrorCorrection.MEDIUM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerErrorCorrection.setSelection(1)
        binding.spinnerErrorCorrection.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val spinnerItems = resources.getStringArray(R.array.error_correction_entries)
                val selectedItem = spinnerItems[position]
                errorCorrection = when (selectedItem) {
                    getString(R.string.low) -> Version.ErrorCorrection.LOW
                    getString(R.string.quartile) -> Version.ErrorCorrection.QUARTILE
                    getString(R.string.high) -> Version.ErrorCorrection.HIGH
                    else -> Version.ErrorCorrection.MEDIUM
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }

        binding.buttonGenerate.setOnClickListener {
            val input = binding.editTextInput.text.toString()
            generateCode(input)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateCode(value: String) {
        val bitmap = QRCodeGenerator().generate(value, errorCorrection)
        binding.imageViewQr.setImageBitmap(bitmap)
    }
}