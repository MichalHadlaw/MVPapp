package com.example.kotlinfirebaseapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinfirebaseapp.models.DrugModel
import com.example.kotlinfirebaseapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etDrugName: EditText
    private lateinit var etDrugCount: EditText
    private lateinit var etDozeTimes:EditText
    private lateinit var btnSaveData: Button


    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etDrugName = findViewById(R.id.etDrugName)
        etDrugCount = findViewById(R.id.etDrugCount)
        etDozeTimes = findViewById(R.id.etDozeTimes)
        btnSaveData = findViewById(R.id.btnSaveData)

        dbRef = FirebaseDatabase.getInstance().getReference("Drugs")

        btnSaveData.setOnClickListener {
        saveDrugData()

        }


    }

    //geting values
    private fun saveDrugData(){
        val DrugName = etDrugName.text.toString()
        val DrugCount = etDrugCount.text.toString()
        val Doze = etDozeTimes.text.toString()

        if (DrugName.isEmpty()){
            etDrugName.error = "Wpisz nazwę leku!"
        }

        if (DrugCount.isEmpty()){
            etDrugCount.error = "Wpisz Ilośc leku!"
        }

        if (Doze.isEmpty()){
            etDozeTimes.error = "Wpisz dawkowanie!"
        }

        val drugId = dbRef.push().key!!

        val drugs = DrugModel(drugId,DrugName,DrugCount,Doze)

        dbRef.child(drugId).setValue(drugs)
            .addOnCompleteListener {
                Toast.makeText(this,"Data inserted succesfuly", Toast.LENGTH_LONG).show()

                etDrugName.text.clear()
                etDrugCount.text.clear()
                etDozeTimes.text.clear()

            }.addOnFailureListener{err ->
                Toast.makeText(this,"Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}