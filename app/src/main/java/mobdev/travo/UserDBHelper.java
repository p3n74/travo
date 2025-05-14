package mobdev.travo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;
import java.util.List;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TravoDB.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "_id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";

    public static final String COL_NAME = "name";
    public static final String COL_COUNTRY = "country";
    public static final String COL_PHONE = "phone";
    public static final String COL_GENDER = "gender";

    // DESTINATIONS
    public static final String TABLE_DESTINATIONS = "destinations";
    public static final String DEST_COL_ID = "_id";
    public static final String DEST_COL_NAME = "name";
    public static final String DEST_COL_DESC = "description";
    public static final String DEST_COL_LOCATION = "location";
    public static final String DEST_COL_CATEGORY = "category";
    public static final String DEST_COL_GALLERY = "gallery_url";
    public static final String DEST_COL_AVGRATING = "avg_rating";

    // REVIEWS
    public static final String TABLE_REVIEWS = "reviews";
    public static final String REV_COL_ID = "_id";
    public static final String REV_COL_USERID = "user_id";
    public static final String REV_COL_DESTID = "dest_id";
    public static final String REV_COL_CONTENT = "content";
    public static final String REV_COL_RATING = "rating";
    public static final String REV_COL_DATE = "date";
    public static final String REV_COL_PHOTO = "photo_path";

    // ROUTES
    public static final String TABLE_ROUTES = "routes";
    public static final String ROUTE_COL_ID = "_id";
    public static final String ROUTE_COL_USERID = "user_id";
    public static final String ROUTE_COL_DESTID = "dest_id";
    public static final String ROUTE_COL_START = "start_location";
    public static final String ROUTE_COL_TRANSPORT = "transport_mode";
    public static final String ROUTE_COL_DURATION = "duration";
    public static final String ROUTE_COL_COST = "cost";
    public static final String ROUTE_COL_TIPS = "tips";
    public static final String ROUTE_COL_PHOTO = "photo_path";
    public static final String ROUTE_COL_DATE = "date";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT, " +
                        COL_EMAIL + " TEXT UNIQUE, " +
                        COL_PASSWORD + " TEXT, " +
                        COL_COUNTRY + " TEXT, " +
                        COL_PHONE + " TEXT, " +
                        COL_GENDER + " TEXT)"
        );

        // Destinations
        db.execSQL(
                "CREATE TABLE " + TABLE_DESTINATIONS + " (" +
                        DEST_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DEST_COL_NAME + " TEXT, " +
                        DEST_COL_DESC + " TEXT, " +
                        DEST_COL_LOCATION + " TEXT, " +
                        DEST_COL_CATEGORY + " TEXT, " +
                        DEST_COL_GALLERY + " TEXT, " +
                        DEST_COL_AVGRATING + " REAL DEFAULT 0.0)"
        );

        // Reviews
        db.execSQL(
                "CREATE TABLE " + TABLE_REVIEWS + " (" +
                        REV_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        REV_COL_USERID + " INTEGER, " +
                        REV_COL_DESTID + " INTEGER, " +
                        REV_COL_CONTENT + " TEXT, " +
                        REV_COL_RATING + " INTEGER, " +
                        REV_COL_DATE + " TEXT, " +
                        REV_COL_PHOTO + " TEXT, " +
                        "FOREIGN KEY(" + REV_COL_USERID + ") REFERENCES " + TABLE_NAME + "(" + COL_ID + ")," +
                        "FOREIGN KEY(" + REV_COL_DESTID + ") REFERENCES " + TABLE_DESTINATIONS + "(" + DEST_COL_ID + "))"
        );

        // Routes
        db.execSQL(
                "CREATE TABLE " + TABLE_ROUTES + " (" +
                        ROUTE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ROUTE_COL_USERID + " INTEGER, " +
                        ROUTE_COL_DESTID + " INTEGER, " +
                        ROUTE_COL_START + " TEXT, " +
                        ROUTE_COL_TRANSPORT + " TEXT, " +
                        ROUTE_COL_DURATION + " TEXT, " +
                        ROUTE_COL_COST + " TEXT, " +
                        ROUTE_COL_TIPS + " TEXT, " +
                        ROUTE_COL_PHOTO + " TEXT, " +
                        ROUTE_COL_DATE + " TEXT, " +
                        "FOREIGN KEY(" + ROUTE_COL_USERID + ") REFERENCES " + TABLE_NAME + "(" + COL_ID + ")," +
                        "FOREIGN KEY(" + ROUTE_COL_DESTID + ") REFERENCES " + TABLE_DESTINATIONS + "(" + DEST_COL_ID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESTINATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        onCreate(db);
    }

    public long insertUser(String name, String email, String password, String country, String phone, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_COUNTRY, country);
        values.put(COL_PHONE, phone);
        values.put(COL_GENDER, gender);
        return db.insert(TABLE_NAME, null, values);
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?",
                new String[]{email, password},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null,
                COL_EMAIL + "=?",
                new String[]{email},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public long insertDestination(String name, String description, String location,
                                  String category, String galleryUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEST_COL_NAME, name);
        values.put(DEST_COL_DESC, description);
        values.put(DEST_COL_LOCATION, location);
        values.put(DEST_COL_CATEGORY, category);
        values.put(DEST_COL_GALLERY, galleryUrl);
        return db.insert(TABLE_DESTINATIONS, null, values);
    }

    public Cursor getAllDestinations() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DESTINATIONS, null, null, null, null, null, DEST_COL_NAME + " ASC");
    }

    public Cursor getDestinationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DESTINATIONS, null, DEST_COL_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
    }

    public long insertReview(long userId, long destId, String content, int rating,
                             String date, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REV_COL_USERID, userId);
        values.put(REV_COL_DESTID, destId);
        values.put(REV_COL_CONTENT, content);
        values.put(REV_COL_RATING, rating);
        values.put(REV_COL_DATE, date);
        values.put(REV_COL_PHOTO, photoPath);
        return db.insert(TABLE_REVIEWS, null, values);
    }

    public Cursor getReviewsForDestination(long destId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REVIEWS, null, REV_COL_DESTID + "=?",
                new String[]{String.valueOf(destId)}, null, null, REV_COL_DATE + " DESC");
    }

    public Cursor getReviewsByUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_REVIEWS, null, REV_COL_USERID + "=?",
                new String[]{String.valueOf(userId)}, null, null, REV_COL_DATE + " DESC");
    }

    public long insertRoute(long userId, long destId, String startLocation, String transportMode,
                            String duration, String cost, String tips, String photoPath, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROUTE_COL_USERID, userId);
        values.put(ROUTE_COL_DESTID, destId);
        values.put(ROUTE_COL_START, startLocation);
        values.put(ROUTE_COL_TRANSPORT, transportMode);
        values.put(ROUTE_COL_DURATION, duration);
        values.put(ROUTE_COL_COST, cost);
        values.put(ROUTE_COL_TIPS, tips);
        values.put(ROUTE_COL_PHOTO, photoPath);
        values.put(ROUTE_COL_DATE, date);
        return db.insert(TABLE_ROUTES, null, values);
    }

    public Cursor getRoutesForDestination(long destId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ROUTES, null, ROUTE_COL_DESTID + "=?",
                new String[]{String.valueOf(destId)}, null, null, ROUTE_COL_DATE + " DESC");
    }

    public Cursor getRoutesByUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ROUTES, null, ROUTE_COL_USERID + "=?",
                new String[]{String.valueOf(userId)}, null, null, ROUTE_COL_DATE + " DESC");
    }

    public void insertSampleDestinations() {
        insertDestination("Temple of Leah", "Roman-inspired, 'Taj Mahal of Cebu'",
                "Cebu Transcentral Hwy, Cebu City", "Landmark", "temple_of_leah");
        insertDestination("Sirao Flower Garden", "'Little Amsterdam' IG-worthy snap spot",
                "Brgy. Sirao, Cebu City", "Nature Park", "sirao_garden");
        insertDestination("Magellan's Cross", "Famous cross planted by Magellan in 1521",
                "Magallanes St, Cebu City", "Historical", "magellans_cross");
        insertDestination("IT Park", "Live, work, play district",
                "Lahug, Cebu City", "Lifestyle", "itpark");
        insertDestination("Colon Street", "Oldest street in the Philippines",
                "Downtown Cebu City", "Historical", "colon");
        insertDestination("Tops Lookout", "Mountain viewpoint over Cebu City",
                "Busay, Cebu City", "Viewpoint", "tops");
        insertDestination("Basilica del Sto. Niño", "16th-century Catholic church",
                "Osmeña Blvd, Cebu City", "Church", "sto_nino");
        insertDestination("Cebu Ocean Park", "Largest oceanarium in the VisMin region",
                "SRP, Cebu City", "Attraction", "ocean_park");
    }


    // Return cursor for a user, searching by their primary key (_id)
    public Cursor getUserByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,
                null, // all columns
                COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
    }

    // Get a user's data by their email address
    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,
                null, // all columns
                COL_EMAIL + " = ?",
                new String[]{email},
                null, null, null);
    }

    public void clearDestinations() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("destinations", null, null); // deletes all rows
        db.close();
    }

    public List<DestinationModel> getAllDestinationsAsList(Context context) {
        List<DestinationModel> destinationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_DESTINATIONS,
                null,
                null,
                null,
                null,
                null,
                DEST_COL_NAME + " ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DEST_COL_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(DEST_COL_LOCATION));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DEST_COL_DESC));
                String imgName = cursor.getString(cursor.getColumnIndexOrThrow(DEST_COL_GALLERY));

                // Just store the filename directly
                destinationList.add(new DestinationModel(name, location, description, imgName));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return destinationList;
    }

    public boolean isDestinationsTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DESTINATIONS, null);

        boolean isEmpty = true;
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            isEmpty = (count == 0);
            cursor.close();
        }

        return isEmpty;
    }


    public boolean updateUserProfile(String oldEmail, String newName, String newPhone, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newName);
        values.put(COL_PHONE, newPhone);
        values.put(COL_EMAIL, newEmail);

        // Update the user's data in the database where the email matches
        int rowsAffected = db.update(TABLE_NAME, values, COL_EMAIL + " = ?", new String[]{oldEmail});
        return rowsAffected > 0;  // Return true if the profile was successfully updated
    }

}
