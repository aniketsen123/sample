package com.example.ui.adapter.convertpdf

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*

import android.util.Log
import android.view.MenuItem
import android.view.View

import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ui.R
import com.example.ui.adapter.convertpdf.constant.REQUEST_CODE
import com.example.ui.adapter.convertpdf.viewmodel.viewmodel
import com.example.ui.databinding.ActivityConvertBinding
import com.example.ui.databinding.DialogProgressBinding
import com.google.android.material.dialog.MaterialDialogs
import com.itextpdf.text.Document

import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfDocument
import com.itextpdf.text.pdf.PdfWriter
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList



class Convert : AppCompatActivity() {
    lateinit var binding: ActivityConvertBinding
    lateinit var  viewModel: viewmodel
    private lateinit var mProgressDialog : Dialog
     var uri: MutableList<Uri> =ArrayList()
    lateinit var model: Model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this).get(viewmodel::class.java)
        binding = ActivityConvertBinding.inflate(layoutInflater)
        setContentView(binding.root)
         if(savedInstanceState!=null)
         {
            if(viewModel.getImages()!!.isEmpty())
                requestPermissions()
             else
            {
                viewModel.getImages()?.let {
                    uri.addAll(it) }
            }
         }
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        val adapter = Image_Adapter(this)
        binding.recyclerView.adapter = adapter
        viewModel.list.observe(this) {
            uri.clear()
            uri.addAll(it)
            adapter.upatelist(it)
        }
        Toast.makeText(this,uri.size.toString(),Toast.LENGTH_SHORT).show()
        if(uri.isEmpty())
        requestPermissions()
 binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)


        binding.floatingActionButton.setOnClickListener {
          requestPermissions()
        }

          binding.toolbar.inflateMenu(R.menu.menu)

        binding.toolbar.setOnMenuItemClickListener(object: Toolbar.OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if(item!!.itemId==R.id.pdf)
                {
                    if(uri.size==0)
                    {

                        Toast.makeText(this@Convert,"No images Selected",Toast.LENGTH_SHORT).show()
                    }
                    else
                        createpdf()
                }
                return false
            }

        })


        }




    private fun pickimage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, constant.IMAGE_PICK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == constant.IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until (count) step 1) {
                        uri.add(data.clipData!!.getItemAt(i).uri)
                    }
                } else {
                    uri.add(data.data!!)
                }
            }

            if (uri.isNotEmpty()) {
                viewModel.setPictureList(uri)
            }

        }
    }

     fun createpdf()
     {



             showProgressDialog()
       convertImageToPdf(uri)


     }
    private fun convertImageToPdf(myImageList: MutableList<Uri>){
        val pdfFile = File(baseContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() +
                File.separator + "Doc_Scanner_pdf" + System.currentTimeMillis()/1000 + ".pdf"
        )

        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        // Create a new document and a PDF writer
        val document = Document()
        val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))

        executor.execute {
            //Background work here

            // Open the document
            document.open()

            // Loop through the image URIs and add each image to the PDF document
            for (uri in myImageList) {
                // Load the image from the URI
                Log.e("myTag inLoop",uri.toString())
                val inputStream: InputStream = contentResolver.openInputStream(uri) ?: continue
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                val imageBytes: ByteArray = stream.toByteArray()
                val image = Image.getInstance(imageBytes)

                // Add the image to the document
                document.pageSize = image
                document.newPage()
                image.setAbsolutePosition(0f, 0f)
                document.add(image)
                // Close the input stream
                inputStream.close()
            }

            // Close the document and the PDF writer
            document.close()
            writer.close()
            hideProgressDialog()
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(this, "com.example.ui.adapter.convertpdf", pdfFile)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
        handler.post(Runnable {

        })
    }
    private fun showProgressDialog(){
        mProgressDialog = Dialog(this)
        var binding: DialogProgressBinding?=null
        binding= DialogProgressBinding.inflate(layoutInflater)
        binding.root.let { mProgressDialog.setContentView(it) }
        binding.tvProgressText.text = "Converting Please Wait"
        binding.tvStateOfProgress.text = ""
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
    }
    private fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
    private fun requestPermissions() {

      try{
          if(  android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
              if (ActivityCompat.checkSelfPermission(
                      this,
                      Manifest.permission.READ_EXTERNAL_STORAGE
                  ) != PackageManager.PERMISSION_GRANTED
              ) {
                  ActivityCompat.requestPermissions(
                      this,
                      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                      REQUEST_CODE
                  )
              } else {
                  pickimage()
              }
          }
          else
          {
              pickimage()
          }
      }catch (e:Exception)
      {
          e.printStackTrace()
      }

    }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if(requestCode== REQUEST_CODE)
            {
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    pickimage()
                }
                else
                {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }


}


