package com.example.tasklist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasklist.R  // Asegúrate de ajustar el nombre de tu paquete
import com.example.tasklist.data.daos.CategoryDAO
import com.example.tasklist.data.entities.Category

class CreateCategoryActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        nameEditText = findViewById(R.id.nameEditTextView)
        saveButton = findViewById(R.id.saveButton)

        categoryDAO = CategoryDAO(this)

        saveButton.setOnClickListener {
            saveCategory()
        }
    }

    fun saveCategory() {
        val name = nameEditText.text.toString()
        // Obtén otros datos como color, icono, etc., según tu diseño

        if (name.isNotEmpty()) {
            val newCategory = Category(-1, name, "default_color", R.drawable.ic_delete)
            val insertedCategory = categoryDAO.insert(newCategory)

            // Devolver el ID de la nueva categoría a la actividad anterior
            val resultIntent = Intent()
            resultIntent.putExtra("categoryId", insertedCategory.id)
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "Please enter a category name", Toast.LENGTH_SHORT).show()
        }
    }
}