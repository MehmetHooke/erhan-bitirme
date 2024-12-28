package com.example.deneme


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.tensorflow.lite.Interpreter
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HastalikFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var textViewResult: TextView
    private lateinit var textViewSolution: TextView
    private var selectedImageBitmap: Bitmap? = null
    private lateinit var tflite: Interpreter

    private val classLabels = listOf(
        "Healthy",
        "Bacterial Spot",
        "Late Blight",
        "Leaf Mold",
        "Septoria Leaf Spot",
        "Spider Mites",
        "Target Spot",
        "Yellow Leaf Curl Virus",
        "Mosaic Virus",
        "Unknown"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_hastalik, container, false)

        imageView = view.findViewById(R.id.imageView)
        textViewResult = view.findViewById(R.id.textViewResult)
        textViewSolution = view.findViewById(R.id.textViewSolution)

        val buttonSelectImage: Button = view.findViewById(R.id.buttonSelectImage)

        // TensorFlow Lite modelini yükleyin
        val modelFile = requireContext().assets.openFd("tomato_disease_model.tflite")
        val inputStream = modelFile.createInputStream()
        val buffer = inputStream.channel.map(
            java.nio.channels.FileChannel.MapMode.READ_ONLY,
            modelFile.startOffset,
            modelFile.declaredLength
        )
        tflite = Interpreter(buffer)

        // Fotoğraf seçme butonuna tıklama
        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            uri?.let {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedImageBitmap = bitmap
                imageView.setImageBitmap(bitmap)

                processImageWithModel(bitmap)
            }
        }
    }

    private fun processImageWithModel(bitmap: Bitmap) {
        val inputBuffer = preprocessImage(bitmap)
        val predictions = runModelInference(inputBuffer)

        val maxIndex = predictions.indices.maxByOrNull { predictions[it] } ?: -1
        val predictedLabel = if (maxIndex in classLabels.indices) classLabels[maxIndex] else "Unknown"
        textViewResult.text = "Predicted: $predictedLabel"

        val solutions = getSolutionForPrediction(maxIndex)
        textViewSolution.text = solutions
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val inputBuffer = ByteBuffer.allocateDirect(1 * 150 * 150 * 3 * 4)
        inputBuffer.order(ByteOrder.nativeOrder())

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
        val intValues = IntArray(150 * 150)
        resizedBitmap.getPixels(intValues, 0, 150, 0, 0, 150, 150)

        for (pixelValue in intValues) {
            val r = (pixelValue shr 16 and 0xFF) / 255.0f
            val g = (pixelValue shr 8 and 0xFF) / 255.0f
            val b = (pixelValue and 0xFF) / 255.0f
            inputBuffer.putFloat(r)
            inputBuffer.putFloat(g)
            inputBuffer.putFloat(b)
        }

        return inputBuffer
    }

    private fun runModelInference(inputBuffer: ByteBuffer): FloatArray {
        val output = Array(1) { FloatArray(classLabels.size) }
        tflite.run(inputBuffer, output)
        return output[0]
    }

    private fun getSolutionForPrediction(index: Int): String {
        return when (index) {
            0 -> "Healthy: No solution needed."
            1 -> "Bacterial Spot: Use fungicide X."
            2 -> "Late Blight: Apply treatment Y."
            3 -> "Leaf Mold: Improve air circulation."
            4 -> "Septoria Leaf Spot: Use fungicide Z."
            5 -> "Spider Mites: Use insecticidal soap."
            6 -> "Target Spot: Use organic fungicide."
            7 -> "Yellow Leaf Curl Virus: Remove infected plants."
            8 -> "Mosaic Virus: Disinfect tools regularly."
            else -> "No solution available."
        }
    }
}