package com.example.cycletracker.ui.statistics

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import com.example.cycletracker.R
import com.example.cycletracker.data.Cycle
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

object StatisticsExporter {

    data class ExportResult(val file: File, val uri: android.net.Uri)

    fun export(context: Context, cycles: List<Cycle>, stats: CycleStatistics): ExportResult {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 16f
            isAntiAlias = true
        }

        var y = 80f

        paint.textSize = 24f
        canvas.drawText(context.getString(R.string.app_name), 40f, y, paint)
        y += 30f

        paint.textSize = 16f
        canvas.drawText("Дата экспорта: " + java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru"))), 40f, y, paint)
        y += 40f

        canvas.drawText("Сводка цикла", 40f, y, paint)
        y += 24f

        canvas.drawText("Средний цикл: ${stats.avgCycleLength ?: "—"} дней", 40f, y, paint)
        y += 20f

        canvas.drawText("Циклов отслежено: ${stats.totalCycles}", 40f, y, paint)
        y += 20f

        canvas.drawText("Средняя длина периода: ${stats.avgPeriodLength ?: "—"}", 40f, y, paint)
        y += 20f

        canvas.drawText("Регулярность: ${stats.regularity}", 40f, y, paint)
        y += 30f

        canvas.drawText("Последние циклы", 40f, y, paint)
        y += 24f
        val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
        cycles.take(6).forEach { cycle ->
            val start = cycle.startDate.format(formatter)
            val end = cycle.endDate?.format(formatter) ?: "—"
            canvas.drawText("$start – $end (${cycle.cycleLength ?: "?"} дней)", 40f, y, paint)
            y += 20f
        }

        pdfDocument.finishPage(page)

        val exportsDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "exports").apply {
            if (!exists()) mkdirs()
        }

        val fileName = "cycle_statistics_${System.currentTimeMillis()}.pdf"
        val pdfFile = File(exportsDir, fileName)
        FileOutputStream(pdfFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        val authority = context.packageName + ".fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, pdfFile)

        return ExportResult(file = pdfFile, uri = uri)
    }
}

