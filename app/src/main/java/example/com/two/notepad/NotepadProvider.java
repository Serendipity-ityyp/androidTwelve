package example.com.two.notepad;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import example.com.two.utils.SQLiteHelper;


public class NotepadProvider extends ContentProvider {
    private static UriMatcher mUrimathers = new UriMatcher(-1);
    private static final int SUCCESS = 1;
    private SQLiteHelper helper;
    static {
        mUrimathers.addURI("example.com.two.notepad","Note",SUCCESS);
    }

    public NotepadProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        System.out.println("开始删除。。。");
        int code  = mUrimathers.match(uri);
        if (code == SUCCESS){
            SQLiteDatabase db = helper.getWritableDatabase();
            int count = db.delete("Note",selection,selectionArgs);
            if (count > 0){
                getContext().getContentResolver().notifyChange(uri,null);
            }
            db.close();
            return count;
        }else
            throw new UnsupportedOperationException("路径不正确，无法随便删除数据");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        System.out.println("开始插入");
        int code  = mUrimathers.match(uri);
        if (code == SUCCESS){
            SQLiteDatabase db = helper.getReadableDatabase();
            long rowId = db.insert("Note",null,values);
            if (rowId>0){
                Uri insertdUri = ContentUris.withAppendedId(uri,rowId);
                getContext().getContentResolver().notifyChange(insertdUri,null);
                return insertdUri;
            }
            db.close();
            return uri;
        }else
            throw new UnsupportedOperationException("路径不正确，无法插入数据");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        helper = new SQLiteHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        System.out.println("开始查询。。。");
        int code = mUrimathers.match(uri);
        if (code == SUCCESS){
            SQLiteDatabase db = helper.getReadableDatabase();
            System.out.println(db.query("Note", projection, selection, selectionArgs, null, null, sortOrder).toString());
            return db.query("Note",projection,selection,selectionArgs,null,null,sortOrder);
        }else {
            throw new UnsupportedOperationException("路径不匹配，查询失败");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        System.out.println("开始修改");
        int code  = mUrimathers.match(uri);
        if (code == SUCCESS){
            SQLiteDatabase db = helper.getWritableDatabase();
            int count = db.update("Note",values,selection,selectionArgs);
            if (count > 0){
                getContext().getContentResolver().notifyChange(uri,null);
            }
            db.close();
            return count;
        }else
            throw new UnsupportedOperationException("路径不正确，无法更新数据");
    }
}
