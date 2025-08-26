package cloud.englert.experimental.ui.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import cloud.englert.experimental.databinding.FragmentQrBinding
import cloud.englert.experimental.qrgenerator.QRCodeGenerator

class QRFragment : Fragment() {
    private var _binding: FragmentQrBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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
        val bitmap = QRCodeGenerator().generate(value)
        binding.imageViewQr.setImageBitmap(bitmap)
    }
}