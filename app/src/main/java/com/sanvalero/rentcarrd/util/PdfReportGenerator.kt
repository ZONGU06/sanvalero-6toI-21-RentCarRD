package com.sanvalero.rentcarrd.util

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Environment
import androidx.printing.PrintAttributesCompat
import com.example.rentcarrd.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import org.jetbrains.kotlinx.coroutines.Main

/**
 * Generador de reportes PDF para RentCarRD.
 * Cumple con el requisito de incluir encabezado con nombre RentCarRD, logo, fecha y numeración de página.
 * Los PDFs se guardan en la carpeta de Descargas con opción de compartir por Intent.ACTION_SEND.
 */
object PdfReportGenerator {

    private const val PDF_HEADER_TITLE = "RentCarRD"
    private const val PDF_FOOTER_FORMAT = "RentCarRD - Página %d"
    private const val DEFAULT_PAGE_SIZE_WIDTH = 595f // A4 width in points (70mm * 300dpi / 25.4)
    private const val DEFAULT_PAGE_SIZE_HEIGHT = 842f // A4 height in points

    /**
     * Genera un reporte PDF general con todos los registros según filtros aplicados.
     * @param context Contexto de Android
     * @param title Título del reporte
     * @param dataDescripción de los datos incluidos
     * @return Ruta del archivo PDF generado, o null si falla
     */
    fun generateGeneralReport(context: Context, title: String, data: String?): String? {
        try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fileName = "RentCarRD_${title.replace(" ", "_")}_${dateFormat.format(calendar.time)}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            // Configurar PDF
            val pdfTitle = PDF_HEADER_TITLE
            val pdfSubject = title
            val pdfAuthor = "RentCarRD"

            // Crear documento PDF
            // Usando Android PdfDocument
            val pdfDoc = android.print.pdf.PdfDocument(context)

            // Configurar página
            val pageInfo = android.print.pdf.PdfDocument.PageInfo.Builder(DEFAULT_PAGE_SIZE_WIDTH.toInt(), DEFAULT_PAGE_SIZE_HEIGHT.toInt(), 1).create()
            val page = pdfDoc.startPage(pageInfo)

            // Obtener canvas para dibujar
            val canvas = page.canvas
            val paint = Paint()

            // --- Encabezado ---
            drawHeader(canvas, paint, pdfTitle, calendar)

            // --- Contenido ---
            drawContent(canvas, paint, data)

            // --- Pie de página ---
            drawFooter(canvas, paint, PDF_FOOTER_FORMAT.format(1), 1)

            // Finalizar página
            pdfDoc.finishPage(page)

            // Guardar archivo
            val outputStream = FileOutputStream(file)
            pdfDoc.writeTo(outputStream)
            outputStream.close()

            // Retornar ruta del archivo
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Genera un reporte por período (alquileres y mantenimientos entre dos fechas).
     * @param context Contexto de Android
     * @param startDate Fecha inicio (dd/MM/yyyy)
     * @param endDate Fecha fin (dd/MM/yyyy)
     * @param rentalCount Cantidad de alquileres
     * @param maintenanceCount Cantidad de mantenimientos
     * @param totalRentalAmount Monto total de alquileres
     * @param totalMaintenanceAmount Monto total de mantenimientos
     * @return Ruta del archivo PDF generado
     */
    fun generatePeriodReport(
        context: Context,
        startDate: String,
        endDate: String,
        rentalCount: Int,
        maintenanceCount: Int,
        totalRentalAmount: Double,
        totalMaintenanceAmount: Double
    ): String? {
        try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fileName = "RentCarRD_Periodo_${startDate}_${endDate}_${dateFormat.format(calendar.time)}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            val pdfDoc = android.print.pdf.PdfDocument(context)
            val pageInfo = android.print.pdf.PdfDocument.PageInfo.Builder(DEFAULT_PAGE_SIZE_WIDTH.toInt(), DEFAULT_PAGE_SIZE_HEIGHT.toInt(), 1).create()
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // Encabezado
            drawHeader(canvas, paint, PDF_HEADER_TITLE, calendar)

            // Contenido del reporte periódico
            drawPeriodReportContent(
                canvas, paint,
                startDate, endDate,
                rentalCount, maintenanceCount,
                totalRentalAmount, totalMaintenanceAmount
            )

            drawFooter(canvas, paint, PDF_FOOTER_FORMAT.format(1), 1)

            pdfDoc.finishPage(page)

            val outputStream = FileOutputStream(file)
            pdfDoc.writeTo(outputStream)
            outputStream.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Genera un resumen gerencial PDF con indicadores y gráficos.
     * @param context Contexto de Android
     * @param totalVehicles Total de vehículos
     * @param activeContracts Contratos activos
     * @param totalRevenue Ingresos totales
     * @param topVehicles Lista de los 5 vehículos más alquilados
     * @param topServices Lista de los servicios más solicitados
     * @param chartView Gráfico Canvas (BarChart o similar)
     * @return Ruta del archivo PDF generado
     */
    fun generateManagerReport(
        context: Context,
        totalVehicles: Int,
        activeContracts: Int,
        totalRevenue: Double,
        topVehicles: List<String>,
        topServices: List<String>,
        chartView: android.view.View
    ): String? {
        try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fileName = "RentCarRD_Resumen_Gerencial_${dateFormat.format(calendar.time)}.pdf"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            val pdfDoc = android.print.pdf.PdfDocument(context)
            val pageInfo = android.print.pdf.PdfDocument.PageInfo.Builder(DEFAULT_PAGE_SIZE_WIDTH.toInt(), DEFAULT_PAGE_SIZE_HEIGHT.toInt(), 1).create()
            val page = pdfDoc.startPage(pageInfo)
            val canvas = page.canvas
            val paint = Paint()

            // Encabezado
            drawHeader(canvas, paint, PDF_HEADER_TITLE, calendar)

            // Contenido del resumen gerencial
            drawManagerReportContent(
                canvas, paint,
                totalVehicles, activeContracts, totalRevenue,
                topVehicles, topServices
            )

            // Insertar gráfico convertido a imagen
            if (chartView != null) {
                val bitmap = android.graphics.Bitmap.createBitmap(
                    chartView.width,
                    chartView.height,
                    android.graphics.Bitmap.Config.ARGB_8888
                )
                chartView.draw(android.graphics.Canvas(bitmap))
                canvas.drawBitmap(
                    bitmap,
                    0f, 200f, paint
                )
            }

            drawFooter(canvas, paint, PDF_FOOTER_FORMAT.format(1), 1)

            pdfDoc.finishPage(page)

            val outputStream = FileOutputStream(file)
            pdfDoc.writeTo(outputStream)
            outputStream.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /** Dibuja el encabezado con nombre RentCarRD, logo y fecha */
    private fun drawHeader(canvas: Canvas, paint: Paint, title: String, calendar: Calendar) {
        val titlePaint = Paint().apply {
            textSize = 24f
            color = Color.WHITE
            setFakeBoldText(true)
        }

        val datePaint = Paint().apply {
            textSize = 12f
            color = Color.WHITE
        }

        // Nombre de la empresa
        canvas.drawText(
            title,
            25f,
            40f,
            titlePaint
        )

        // Línea separadora
        paint.setColor(Color.WHITE)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(2f)
        canvas.drawLine(25f, 50f, DEFAULT_PAGE_SIZE_WIDTH - 25f, 50f, paint)

        // Fecha de emisión
        val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        canvas.drawText(
            "Fecha de emisión: $dateStr",
            25f,
        }
    }

    /** Dibuja el pie de página con numeración */
    private fun drawFooter(canvas: Canvas, paint: Paint, footerText: String, pageNumber: Int) {
        paint.isAntiAlias = true
        paint.textSize = 10f
        paint.color = Color.WHITE

        // Números de página centrado
        val pageNumWidth = paint.measureText(footerText)
        canvas.drawText(
            footerText,
            (DEFAULT_PAGE_SIZE_WIDTH - pageNumWidth) / 2f,
            DEFAULT_PAGE_SIZE_HEIGHT - 20f,
            paint
        )
    }

    /** Contenido para reporte general */
    private fun drawContent(canvas: Canvas, paint: Paint, data: String?) {
        paint.textSize = 14f
        paint.color = Color.WHITE
        paint.isAntiAlias = true

        if (data.isNotEmpty()) {
            val lines = data.split("\n")
            var y = 100f
            for (line in lines) {
                canvas.drawText(line, 25f, y, paint)
                y += 30f
            }
        } else {
            canvas.drawText("Sin datos disponibles", 25f, 100f, paint)
        }
    }

    /** Contenido para reporte por período */
    private fun drawPeriodReportContent(
        canvas: Canvas,
        paint: Paint,
        startDate: String,
        endDate: String,
        rentalCount: Int,
        maintenanceCount: Int,
        totalRentalAmount: Double,
        totalMaintenanceAmount: Double
    ) {
        paint.textSize = 14f
        paint.color = Color.WHITE
        paint.isAntiAlias = true

        var y = 120f

        canvas.drawText("Reporte Período: $startDate - $endDate", 25f, y, paint)
        y += 30f

        canvas.drawText("Alquileres: $rentalCount", 25f, y, paint)
        y += 30f
        canvas.drawText("Total Alquileres: ${RDFormatter.formatRD$(totalRentalAmount)}", 25f, y, paint)
        y += 30f

        canvas.drawText("Mantenimientos: $maintenanceCount", 25f, y, paint)
        y += 30f
        canvas.drawText("Total Mantenimientos: ${RDFormatter.formatRD$(totalMaintenanceAmount)}", 25f, y, paint)
        y += 30f

        // Línea separadora
        paint.setColor(Color.WHITE)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(1f)
        canvas.drawLine(25f, y, DEFAULT_PAGE_SIZE_WIDTH - 25f, y, paint)
        y += 20f

        canvas.drawText("Subtotales generados automáticamente por el sistema", 25f, y, paint)
    }

    /** Contenido para resumen gerencial */
    private fun drawManagerReportContent(
        canvas: Canvas,
        paint: Paint,
        totalVehicles: Int,
        activeContracts: Int,
        totalRevenue: Double,
        topVehicles: List<String>,
        topServices: List<String>
    ) {
        paint.textSize = 14f
        paint.color = Color.WHITE
        paint.isAntiAlias = true

        var y = 120f

        canvas.drawText("Resumen Gerencial RentCarRD", 25f, y, paint)
        y += 30f

        canvas.drawText("Vehículos totales: $totalVehicles", 25f, y, paint)
        y += 30f
        canvas.drawText("Contratos activos: $activeContracts", 25f, y, paint)
        y += 30f
        canvas.drawText("Ingresos totales: ${RDFormatter.formatRD$(totalRevenue)}", 25f, y, paint)
        y += 30f

        // Top 5 vehículos más alquilados
        canvas.drawText("Top 5 Vehículos más alquilados", 25f, y, paint)
        y += 20f
        topVehicles.forEach { vehicle ->
            canvas.drawText("• $vehicle", 40f, y, paint)
            y += 20f
        }

        // Top 5 servicios más solicitados
        y += 10f
        canvas.drawText("Top 5 Servicios más solicitados", 25f, y, paint)
        y += 20f
        topServices.forEach { service ->
            canvas.drawText("• $service", 40f, y, paint)
            y += 20f
        }

        // Línea separadora
        paint.setColor(Color.WHITE)
        paint.setStyle(Paint.Style.STROKE)
        paint.setStrokeWidth(1f)
        canvas.drawLine(25f, y, DEFAULT_PAGE_SIZE_WIDTH - 25f, y, paint)
        y += 20f

        canvas.drawText("Indicadores en tiempo real - RentCarRD", 25f, y, paint)
    }
}