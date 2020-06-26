package ex.kyj.myapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConfirmDB extends SQLiteOpenHelper {
    private Context ctx ;
    private String PACKAGE_DIR;

    private static final String DATABASE_NAME = "music.db";

    ConfirmDB(Context ctx){
        super(ctx,DATABASE_NAME,null,1);
        this.ctx = ctx;
        PACKAGE_DIR = ctx.getFilesDir().getPath();
    }

    void init(){
        try {
            boolean bResult = isCheckDB();
            // DB 확인
            Log.d("MiniApp", "1");
            if (!bResult) {
                // DB가 없으면 복사
                Log.d("MiniApp", "2");
                copyDB(ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isCheckDB() {

        String filePath = PACKAGE_DIR + "/databases/" + DATABASE_NAME;
        File file = new File(filePath);
        if (file.exists()) {
            Log.d("MiniApp", "1");
            return true;
        }
        Log.d("MiniApp", "2");
        return false;
    }
    //Asset에서 db파일을 가져와 databases 밑에 db 복사
    private void copyDB(Context mContext) {
        AssetManager manager = mContext.getAssets();
        String folderPath = PACKAGE_DIR + "/databases";
        String filePath = PACKAGE_DIR + "/databases/" + DATABASE_NAME;
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fos;
        BufferedOutputStream bos;
        try {
            InputStream is = manager.open("db/" + DATABASE_NAME);
            BufferedInputStream bis = new BufferedInputStream(is);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            int read;
            byte[] buffer = new byte[1024];
            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
