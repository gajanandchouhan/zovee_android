package com.zoho.app.utils;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.zoho.app.R;
import com.zoho.app.custom.CustomProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class PdfCreater {

    public static void createPdf(final Context mContext, final String htmlString, final String fileName) {
        final CustomProgressDialog dialogHandler = new CustomProgressDialog(mContext);
        dialogHandler.show();
        new AsyncTask<Object, Object, File>() {
            @Override
            protected File doInBackground(Object... objects) {
                File vellopayDirs = null;
                File outputFile = null;
                try {
                    vellopayDirs = new File(ConstantLib.FILE_PATH);
                    if (!vellopayDirs.exists()) {
                        vellopayDirs.mkdir();
                    }
                    outputFile = new File(vellopayDirs, fileName);
                    OutputStream file = new FileOutputStream(outputFile);
                    Document document = new Document();
                    // step 2
                    PdfWriter.getInstance(document, file);
                    // step 3
                    document.open();
                    // step 4
                    document.add(new Paragraph(htmlString));
                    // step 5
                    document.close();
                    return outputFile;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                dialogHandler.dismiss();
                if (file != null) {
                    Utils.showToast(mContext, String.format(mContext.getString(R.string.file_saved), ConstantLib.FILE_PATH));
                    //shareFile(file, mContext);
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.can_not_saved));
                }
            }
        }.execute();
    }


    private static void shareFile(File fileWithinMyDir, Context mContext) {
        if (fileWithinMyDir.exists()) {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileWithinMyDir));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "VPReport");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Please find attached File...");
            mContext.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

}
