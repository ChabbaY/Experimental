package cloud.englert.experimental.ui.textrecognition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import cloud.englert.experimental.databinding.FragmentTextrecognitionBinding

class TextRecognitionFragment : Fragment() {
    private var _binding: FragmentTextrecognitionBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextrecognitionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonScan.setOnClickListener { _: View ->

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}