package cloud.englert.experimental.textrecognition

import android.content.Context
import android.net.Uri

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object TextRecognitionHelper {
    fun recognizeTextFromUri(context: Context, uri: Uri, onResult: (success: Boolean, result: String) -> Unit) {
        val inputImage = InputImage.fromFilePath(context, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                onResult(true, visionText.text)
            }
            .addOnFailureListener { exception ->
                onResult(false, "Recognition failed: ${exception.localizedMessage}")
            }
    }
}