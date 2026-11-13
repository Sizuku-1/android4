package cn.edu.sicnu.cs.txp.android4

import android.content.Context
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.nio.file.Files
import java.nio.file.StandardCopyOption

internal object ObjectStore {
    fun <T : Serializable> read(context: Context, name: String, type: Class<T>): T? {
        return try {
            context.openFileInput(name).use { file ->
                ObjectInputStream(file).use { input -> type.cast(input.readObject()) }
            }
        } catch (_: FileNotFoundException) {
            null
        } catch (error: IOException) {
            Log.w("ObjectStore", "Could not read $name", error)
            null
        } catch (error: ClassNotFoundException) {
            Log.w("ObjectStore", "Unknown object in $name", error)
            null
        } catch (error: ClassCastException) {
            Log.w("ObjectStore", "Unexpected object in $name", error)
            null
        }
    }

    fun write(context: Context, name: String, value: Serializable): Boolean {
        val temporaryName = "$name.tmp"
        val temporary = context.getFileStreamPath(temporaryName)
        return try {
            context.openFileOutput(temporaryName, Context.MODE_PRIVATE).use { file ->
                ObjectOutputStream(file).use { output ->
                    output.writeObject(value)
                    output.flush()
                    file.fd.sync()
                }
            }
            Files.move(
                temporary.toPath(), context.getFileStreamPath(name).toPath(),
                StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING
            )
            true
        } catch (error: IOException) {
            Log.e("ObjectStore", "Could not save $name", error)
            temporary.delete()
            false
        }
    }
}
