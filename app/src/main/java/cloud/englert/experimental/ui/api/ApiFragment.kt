package cloud.englert.experimental.ui.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import cloud.englert.experimental.databinding.FragmentApiBinding

class ApiFragment : Fragment() {
    private var _binding: FragmentApiBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val apiViewModel = ViewModelProvider(this)[ApiViewModel::class.java]

        _binding = FragmentApiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textJoke
        apiViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.buttonFetch.setOnClickListener {
            apiViewModel.update()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}