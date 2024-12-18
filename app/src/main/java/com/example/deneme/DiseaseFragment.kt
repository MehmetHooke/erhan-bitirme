package com.example.deneme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import org.tensorflow.lite.Interpreter
import android.content.res.AssetFileDescriptor
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import android.util.Log
import android.widget.TextView


class DiseaseFragment : Fragment() {

    private lateinit var interpreter: Interpreter
    private lateinit var resultTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment layout'unu inflate et
        val view = inflater.inflate(R.layout.fragment_disease, container, false)

        // TextView'i XML'den bağla
        resultTextView = view.findViewById(R.id.textView5)

        // TensorFlow Lite modelini yükle
        loadTFLiteModel()

        // Modeli çalıştır ve sonucu göster
        runModelInference()

        return view
    }

    private fun loadTFLiteModel() {
        // assets klasöründeki TFLite modelini yükle
        val modelPath = "tomato_disease_model.tflite"
        val tfliteModel = loadModelFile(requireActivity().assets, modelPath)
        interpreter = Interpreter(tfliteModel)
        Log.d("DiseaseFragment", "TFLite Model Yüklendi!")
    }

    private fun runModelInference() {
        // Örnek input tensorü: 150x150x3 boyutunda rastgele veriler
        val input = Array(1) { Array(150) { Array(150) { FloatArray(3) } } }
        val output = Array(1) { FloatArray(10) } // Çıktı tensorü: 10 sınıf

        // Modeli çalıştır
        interpreter.run(input, output)

        // En yüksek olasılığa sahip sınıfı bul ve TextView'e yazdır
        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        val resultText = "Tahmin Edilen Sınıf: $maxIndex\nGüven Skoru: ${output[0][maxIndex]}"

        // Sonucu ekrana yazdır
        resultTextView.text = resultText
        Log.d("DiseaseFragment", resultText)
    }

    private fun loadModelFile(assetManager: android.content.res.AssetManager, modelPath: String): ByteBuffer {
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset: Long = fileDescriptor.startOffset
        val declaredLength: Long = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
