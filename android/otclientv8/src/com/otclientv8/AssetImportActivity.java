package com.otclientv8;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** Imports user-provided game assets through SAF into app-private storage. */
public class AssetImportActivity extends Activity {
    private static final String[] NAMES = {"Tibia.dat", "Tibia.spr"};
    private static final int[] SIGNATURES = {0x4c28b721, 0x4c220594};
    private File destination(int i) { return new File(getFilesDir(), "data/things/860/" + NAMES[i]); }
    @Override public void onCreate(Bundle state) { super.onCreate(state); next(); }
    private void next() {
        for (int i = 0; i < NAMES.length; ++i) {
            if (!destination(i).isFile()) { explain(i, "Selecciona tu archivo " + NAMES[i] + " de Tibia 8.60."); return; }
        }
        startActivity(new Intent(this, OTClientV8.class)); finish();
    }
    private void explain(final int i, String text) {
        new AlertDialog.Builder(this).setTitle("Tibia 8.60 — archivos de juego").setMessage(text)
            .setPositiveButton("Seleccionar", (dialog, which) -> {
                Intent picker = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                picker.setType("*/*"); picker.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(picker, 860 + i);
            }).setNegativeButton("Salir", (dialog, which) -> finish()).setCancelable(false).show();
    }
    @Override protected void onActivityResult(int code, int result, Intent data) {
        super.onActivityResult(code, result, data);
        int i = code - 860;
        if (i < 0 || i >= NAMES.length) return;
        if (result != RESULT_OK || data == null) { next(); return; }
        final File file = destination(i); file.getParentFile().mkdirs();
        final File temp = new File(file.getPath() + ".partial");
        try (InputStream in = getContentResolver().openInputStream(data.getData()); FileOutputStream out = new FileOutputStream(temp)) {
            byte[] header = new byte[4]; int pos = 0;
            while (pos < 4) { int n = in.read(header, pos, 4-pos); if (n < 0) throw new Exception("Archivo incompleto"); pos += n; }
            if (ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt() != SIGNATURES[i])
                throw new Exception("La cabecera no coincide con los assets 8.60 verificados");
            out.write(header); byte[] buffer = new byte[65536]; int n; long total = 4;
            while ((n = in.read(buffer)) != -1) { total += n; if (total > 128L*1024*1024) throw new Exception("Archivo demasiado grande"); out.write(buffer,0,n); }
            out.getFD().sync();
        } catch (Exception error) { temp.delete(); explain(i,error.getMessage()); return; }
        if (!temp.renameTo(file)) { temp.delete(); explain(i,"No se pudo guardar el archivo"); return; }
        next();
    }
}
