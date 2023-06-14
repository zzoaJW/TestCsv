package com.example.testcsv

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.testcsv.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
import java.io.OutputStream
import java.time.Year

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val movies = readCsv(/*Open a stream to CSV file*/)

        FileOutputStream("filename.csv").apply { writeCsv(movies) }
    }

    fun readCsv(inputStream: InputStream): List<Movie> {
        val reader = inputStream.bufferedReader()
        val header = reader.readLine()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (year, rating, title) = it.split(',', ignoreCase = false, limit = 3)
                Movie(year.trim().toInt(), rating.trim().toInt(), title.trim().removeSurrounding("\""))
            }.toList()
    }

    fun OutputStream.writeCsv(movies: List<Movie>) {
        val writer = bufferedWriter()
        writer.write(""""Year", "Score", "Title"""")
        writer.newLine()
        movies.forEach {
            writer.write("${it.year}, ${it.score}, \"${it.title}\"")
            writer.newLine()
        }
        writer.flush()
    }
}