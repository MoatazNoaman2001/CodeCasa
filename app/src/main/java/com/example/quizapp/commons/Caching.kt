package com.example.quizapp.commons

import android.content.Context
import com.example.quizapp.domian.model.Result
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun CachingResultForFutureShow(context: Context , result: Result){
    val file = context.filesDir
    if (!file.exists())
        file.mkdirs()
    val f = FileOutputStream(File(file , "QuizAppData.txt"))
    val obj = ObjectOutputStream(f)

    obj.writeObject(result)
    obj.flush()
    obj.close()

}

fun getCachedObj(context: Context): Result?{
    val file =context.filesDir
    if (!file.exists())
        return null
    val f:FileInputStream = FileInputStream(File(file , "QuizAppData.txt"))
    val obj: ObjectInputStream = ObjectInputStream(f)

    val result : Result = obj.readObject() as Result

    return result
}