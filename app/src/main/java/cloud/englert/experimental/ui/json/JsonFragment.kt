package cloud.englert.experimental.ui.json

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.IOException
import java.util.Calendar

import kotlin.random.Random

import cloud.englert.experimental.data.Data
import cloud.englert.experimental.data.Entity
import cloud.englert.experimental.databinding.FragmentJsonBinding

class JsonFragment : Fragment() {
    private var _binding: FragmentJsonBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var gson: Gson? = null

    val entities = ArrayList<Entity>()

    init {
        gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJsonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonImport.setOnClickListener {
            import()
        }
        binding.buttonExport.setOnClickListener {
            export()
        }
        binding.buttonGenerate.setOnClickListener {
            generate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun import() {
        openFileManager()
    }

    private fun export() {
        if (isStoragePermissionGranted()) {
            writeTextToFile(convertClassToJson())
        }
    }

    private fun generate() {
        val random = Random(System.currentTimeMillis())
        (0..3).forEach { i ->
            entities.add(Entity(random.nextInt(100),
                (System.currentTimeMillis() % 100).toString()))
        }
    }

    private fun convertClassToJson(): String? {
        val allData = Data(entities)
        return gson?.toJson(allData)
    }

    private fun getRandomFileName(): String {
        return Calendar.getInstance().timeInMillis.toString() + ".json"
    }

    private fun isStoragePermissionGranted(): Boolean {
        return true // TODO
    }

    private fun writeTextToFile(jsonResponse: String?) {
        if (jsonResponse != null) {
            // create file object
            val dir = File("") // TODO
            val file = File(dir, getRandomFileName())
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                fos.write(jsonResponse.toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Toast.makeText(requireContext(), "File saved successfully!",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun readTextFromUri(uri: Uri): String {
        var inputStream: InputStream? = null
        val stringBuilder = StringBuilder()
        try {
            val contentResolver = requireContext().contentResolver
            inputStream = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private var getDataFromFile = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            val fileContents = readTextFromUri(uri!!)
            Toast.makeText(requireContext(), fileContents,
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFileManager() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        getDataFromFile.launch(intent)
    }
}