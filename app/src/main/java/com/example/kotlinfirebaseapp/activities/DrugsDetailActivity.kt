package com.example.kotlinfirebaseapp.activities

import android.adservices.adid.AdId
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinfirebaseapp.R
import com.example.kotlinfirebaseapp.models.DrugModel
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent as Intent

class DrugsDetailActivity : AppCompatActivity() {

    private lateinit var tvDrugId: TextView
    private lateinit var tvDrugName: TextView
    private lateinit var tvDoze: TextView
    private lateinit var tvDozeTimes: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drugs_detail)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("drugId").toString(),
                intent.getStringExtra("drugName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("drugId").toString(),
            )
        }


    }

    private fun deleteRecord(
        id:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Drugs").child(id)
        val dTask = dbRef.removeValue()

        dTask.addOnSuccessListener {
            Toast.makeText(this,"Drug data Deleted",Toast.LENGTH_LONG).show()
            val intent = Intent(this,FetchActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{error ->
            Toast.makeText(this,"Deleting error ${error.message}",Toast.LENGTH_LONG).show()
        }
    }

            private fun initView() {
        tvDrugId = findViewById(R.id.tvDrugId)
        tvDrugName = findViewById(R.id.tvDrugName)
        tvDoze = findViewById(R.id.tvDoze)
        tvDozeTimes = findViewById(R.id.tvDozeTimes)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {

        tvDrugId.text = intent.getStringExtra("drugId")
        tvDrugName.text = intent.getStringExtra("drugName")
        tvDoze.text = intent.getStringExtra("doze")
        tvDozeTimes.text = intent.getStringExtra("DozeTimes")


    }


    private fun openUpdateDialog(
        drugId: String,
        drugName: String
    ) {
        val dDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dDialogView = inflater.inflate(R.layout.update_dialog, null)

        dDialog.setView(dDialogView)

        val etDrugName = dDialogView.findViewById<EditText>(R.id.etDrugName)
        val etDrugCount = dDialogView.findViewById<EditText>(R.id.etDrugCount)
        val etDozeTimes = dDialogView.findViewById<EditText>(R.id.etDozeTimes)
        val btnUpdateData =
            dDialogView.findViewById<Button>(R.id.btnUpdateData) // Zmieniono EditText na Button

        etDrugName.setText(drugName)
        etDrugCount.setText(intent.getStringExtra("doze"))
        etDozeTimes.setText(intent.getStringExtra("dozeTimes"))

        dDialog.setTitle("Updating $drugName Record")
        val alertDialog = dDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateDrugData(
                drugId,
                etDrugName.text.toString(),
                etDrugCount.text.toString(),
                etDozeTimes.text.toString()
            )
            Toast.makeText(applicationContext, "Drug Data Updated!", Toast.LENGTH_LONG).show()

            tvDrugName.text = etDrugName.text.toString()
            tvDoze.text = etDrugCount.text.toString()
            tvDozeTimes.text = etDozeTimes.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateDrugData(
        id: String,
        name: String,
        doze: String,
        dozeTimes: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Drugs").child(id)
        val drugInfo = DrugModel(id, name, doze, dozeTimes)
        dbRef.setValue(drugInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Drug data updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update drug data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}