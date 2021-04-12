package id.ac.unpas.sab.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    lateinit var scope: CoroutineScope
    private var JOB_TIME1 = 3000
    private var JOB_TIME2 = 4000
    private val PROGRESS_START = 0
    private val PROGRESS_MAX = 100

    lateinit var progressBar1:ProgressBar
    lateinit var progressBar2:ProgressBar
    lateinit var btn_start:Button
    lateinit var textResult:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar1 = findViewById(R.id.progressBar1)
        progressBar2 = findViewById(R.id.progressBar2)
        btn_start = findViewById(R.id.btn_start)
        textResult= findViewById(R.id.textResult)

        progressBar1.scaleY = 5f
        progressBar2.scaleY = 5f

        btn_start.setOnClickListener {
            scope = CoroutineScope(Dispatchers.Main)
            startTask()
        }

    }

    private fun startTask() {
        scope.launch {
            var  time = measureTimeMillis {
                val result1 :Deferred<String> = async {
                    println("Debug async 1 : ${Thread.currentThread().name}")
                    getDataFromNetwork1()
                }

                val result2 :Deferred<String> = async {
                    println("Debug async 2 : ${Thread.currentThread().name}")
                    getDataFromNetwork2()
                }

                updateUI(result1.await())
                updateUI(result2.await())
            }
            println("Debug Total Elapsed Time ${time}")
        }
    }

    private suspend fun getDataFromNetwork1(): String {
        withContext(Dispatchers.IO){
            for (i in PROGRESS_START..PROGRESS_MAX){
                delay((JOB_TIME1/PROGRESS_MAX).toLong())
                println("Debug get data from network 1 ${i}:${Thread.currentThread().name}")
                showProgressBar1(i)
            }
        }

        return "Progress Bar #1 Completed"
    }

    private suspend fun showProgressBar1(i: Int) {
        withContext(Dispatchers.Main){
            progressBar1.progress=i
        }
    }
    private suspend fun showProgressBar2(i: Int) {
        withContext(Dispatchers.Main){
            progressBar2.progress=i
        }
    }

    private suspend fun getDataFromNetwork2(): String {

        withContext(Dispatchers.IO) {
            for (i in PROGRESS_START..PROGRESS_MAX) {
                delay((JOB_TIME2 / PROGRESS_MAX).toLong())
                println("Debug get data from network 2 ${i}:${Thread.currentThread().name}")
                showProgressBar2(i)
            }
        }

        return "Progress Bar #1 Completed"
    }

    private suspend fun updateUI(message:String) {
        withContext(Dispatchers.Main){
            println("Debug update UI ${Thread.currentThread().name}")
                   textResult.text = textResult.text.toString() + "\n"+message
        }
    }

}

