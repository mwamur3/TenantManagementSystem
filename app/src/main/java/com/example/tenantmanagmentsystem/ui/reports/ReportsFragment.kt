package com.example.tenantmanagmentsystem.ui.reports

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tenantmanagmentsystem.databinding.FragmentReportsBinding
import com.example.tenantmanagmentsystem.ui.viewmodel.TenantViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private val tenantViewModel: TenantViewModel by activityViewModels()

    private var latestReportData = ""

    private val createPdfLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/pdf")) { uri: Uri? ->

            if (uri != null) {
                generatePdf(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tenantViewModel.tenants.observe(viewLifecycleOwner) { tenants ->

            val totalTenants = tenants.size

            val totalRent = tenants.sumOf { tenant ->
                tenant.rent.replace(",", "").trim().toDoubleOrNull() ?: 0.0
            }

            val averageRent =
                if (totalTenants > 0) totalRent / totalTenants else 0.0

            binding.totalTenantsTextView.text = totalTenants.toString()

            binding.totalRentTextView.text =
                String.format(Locale.US, "KSh %,.2f", totalRent)

            binding.averageRentTextView.text =
                String.format(Locale.US, "KSh %,.2f", averageRent)

            binding.tenantReportTextView.text =
                if (tenants.isEmpty()) {
                    "No tenant records available."
                } else {
                    tenants.joinToString("\n\n") { tenant ->
                        """
                        Tenant ID: ${tenant.id}
                        Name: ${tenant.name}
                        Phone: ${tenant.phone}
                        Monthly Rent: KSh ${tenant.rent}
                        """.trimIndent()
                    }
                }

            latestReportData = buildString {
                appendLine("TENANT MANAGEMENT SYSTEM")
                appendLine("Generated: ${getCurrentDate()}")
                appendLine()
                appendLine("RENTAL SUMMARY")
                appendLine("Total Tenants: $totalTenants")
                appendLine("Total Monthly Rent: KSh ${String.format(Locale.US, "%,.2f", totalRent)}")
                appendLine("Average Rent: KSh ${String.format(Locale.US, "%,.2f", averageRent)}")
                appendLine()
                appendLine("TENANT DIRECTORY")
                appendLine()

                if (tenants.isEmpty()) {
                    appendLine("No tenant records available.")
                } else {
                    tenants.forEach { tenant ->
                        appendLine("Tenant ID: ${tenant.id}")
                        appendLine("Name: ${tenant.name}")
                        appendLine("Phone: ${tenant.phone}")
                        appendLine("Monthly Rent: KSh ${tenant.rent}")
                        appendLine("----------------------------------------")
                    }
                }
            }
        }

        binding.exportPdfButton.setOnClickListener {
            createPdfLauncher.launch(
                "Tenant_Report_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.US).format(Date())}.pdf"
            )
        }
    }

    private fun generatePdf(uri: Uri) {

        val pdfDocument = PdfDocument()

        try {
            val pageWidth = 595
            val pageHeight = 842
            val leftMargin = 45f
            val rightMargin = 550f
            val bottomMargin = 790f

            val titlePaint = Paint().apply {
                textSize = 20f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            val headingPaint = Paint().apply {
                textSize = 14f
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                isAntiAlias = true
            }

            val bodyPaint = Paint().apply {
                textSize = 11f
                typeface = Typeface.DEFAULT
                isAntiAlias = true
            }

            var pageNumber = 1
            var yPosition = 55f

            var page = pdfDocument.startPage(
                PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            )

            var canvas: Canvas = page.canvas

            canvas.drawText(
                "TENANT MANAGEMENT SYSTEM",
                leftMargin,
                yPosition,
                titlePaint
            )

            yPosition += 35f

            val lines = latestReportData.lines()

            for (line in lines) {

                if (yPosition > bottomMargin) {
                    pdfDocument.finishPage(page)

                    pageNumber++

                    page = pdfDocument.startPage(
                        PdfDocument.PageInfo.Builder(
                            pageWidth,
                            pageHeight,
                            pageNumber
                        ).create()
                    )

                    canvas = page.canvas
                    yPosition = 55f

                    canvas.drawText(
                        "TENANT MANAGEMENT SYSTEM - CONTINUED",
                        leftMargin,
                        yPosition,
                        headingPaint
                    )

                    yPosition += 35f
                }

                val paint = when {
                    line == "RENTAL SUMMARY" ||
                            line == "TENANT DIRECTORY" ||
                            line == "TENANT MANAGEMENT SYSTEM" -> headingPaint

                    else -> bodyPaint
                }

                canvas.drawText(
                    line.take(85),
                    leftMargin,
                    yPosition,
                    paint
                )

                yPosition += 19f
            }

            pdfDocument.finishPage(page)

            requireNotNull(context).contentResolver.openOutputStream(uri).use { outputStream ->
                checkNotNull(outputStream) { "Unable to open the selected file." }
                pdfDocument.writeTo(outputStream)
            }

            Toast.makeText(
                requireContext(),
                "PDF report saved successfully!",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {

            Toast.makeText(
                requireContext(),
                "PDF export failed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()

        } finally {
            pdfDocument.close()
        }
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat(
            "dd MMMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}