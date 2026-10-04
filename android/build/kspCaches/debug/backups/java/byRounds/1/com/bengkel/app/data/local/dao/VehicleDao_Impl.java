package com.bengkel.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.bengkel.app.data.local.entity.VehicleEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VehicleDao_Impl implements VehicleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<VehicleEntity> __insertionAdapterOfVehicleEntity;

  private final EntityDeletionOrUpdateAdapter<VehicleEntity> __updateAdapterOfVehicleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteVehicleByLocalId;

  public VehicleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVehicleEntity = new EntityInsertionAdapter<VehicleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vehicles` (`localId`,`serverId`,`userId`,`ownerName`,`plateNumber`,`brand`,`model`,`year`,`color`,`photoPath`,`isSynced`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VehicleEntity entity) {
        statement.bindString(1, entity.getLocalId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getServerId());
        }
        statement.bindLong(3, entity.getUserId());
        if (entity.getOwnerName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getOwnerName());
        }
        statement.bindString(5, entity.getPlateNumber());
        statement.bindString(6, entity.getBrand());
        statement.bindString(7, entity.getModel());
        statement.bindLong(8, entity.getYear());
        statement.bindString(9, entity.getColor());
        if (entity.getPhotoPath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPhotoPath());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(11, _tmp);
      }
    };
    this.__updateAdapterOfVehicleEntity = new EntityDeletionOrUpdateAdapter<VehicleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vehicles` SET `localId` = ?,`serverId` = ?,`userId` = ?,`ownerName` = ?,`plateNumber` = ?,`brand` = ?,`model` = ?,`year` = ?,`color` = ?,`photoPath` = ?,`isSynced` = ? WHERE `localId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VehicleEntity entity) {
        statement.bindString(1, entity.getLocalId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getServerId());
        }
        statement.bindLong(3, entity.getUserId());
        if (entity.getOwnerName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getOwnerName());
        }
        statement.bindString(5, entity.getPlateNumber());
        statement.bindString(6, entity.getBrand());
        statement.bindString(7, entity.getModel());
        statement.bindLong(8, entity.getYear());
        statement.bindString(9, entity.getColor());
        if (entity.getPhotoPath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPhotoPath());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindString(12, entity.getLocalId());
      }
    };
    this.__preparedStmtOfDeleteVehicleByLocalId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM vehicles WHERE localId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertVehicle(final VehicleEntity vehicle,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVehicleEntity.insert(vehicle);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertVehicles(final List<VehicleEntity> vehicles,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVehicleEntity.insert(vehicles);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateVehicle(final VehicleEntity vehicle,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVehicleEntity.handle(vehicle);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteVehicleByLocalId(final String localId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteVehicleByLocalId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, localId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteVehicleByLocalId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getVehicleByLocalId(final String localId,
      final Continuation<? super VehicleEntity> $completion) {
    final String _sql = "SELECT * FROM vehicles WHERE localId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, localId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VehicleEntity>() {
      @Override
      @Nullable
      public VehicleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPlateNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "plateNumber");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final VehicleEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpPlateNumber;
            _tmpPlateNumber = _cursor.getString(_cursorIndexOfPlateNumber);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpModel;
            _tmpModel = _cursor.getString(_cursorIndexOfModel);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpPhotoPath;
            if (_cursor.isNull(_cursorIndexOfPhotoPath)) {
              _tmpPhotoPath = null;
            } else {
              _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _result = new VehicleEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpOwnerName,_tmpPlateNumber,_tmpBrand,_tmpModel,_tmpYear,_tmpColor,_tmpPhotoPath,_tmpIsSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getVehicleByServerId(final long serverId,
      final Continuation<? super VehicleEntity> $completion) {
    final String _sql = "SELECT * FROM vehicles WHERE serverId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, serverId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<VehicleEntity>() {
      @Override
      @Nullable
      public VehicleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPlateNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "plateNumber");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final VehicleEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpPlateNumber;
            _tmpPlateNumber = _cursor.getString(_cursorIndexOfPlateNumber);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpModel;
            _tmpModel = _cursor.getString(_cursorIndexOfModel);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpPhotoPath;
            if (_cursor.isNull(_cursorIndexOfPhotoPath)) {
              _tmpPhotoPath = null;
            } else {
              _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _result = new VehicleEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpOwnerName,_tmpPlateNumber,_tmpBrand,_tmpModel,_tmpYear,_tmpColor,_tmpPhotoPath,_tmpIsSynced);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<VehicleEntity>> getAllVehiclesFlow() {
    final String _sql = "SELECT * FROM vehicles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vehicles"}, new Callable<List<VehicleEntity>>() {
      @Override
      @NonNull
      public List<VehicleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPlateNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "plateNumber");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<VehicleEntity> _result = new ArrayList<VehicleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VehicleEntity _item;
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpPlateNumber;
            _tmpPlateNumber = _cursor.getString(_cursorIndexOfPlateNumber);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpModel;
            _tmpModel = _cursor.getString(_cursorIndexOfModel);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpPhotoPath;
            if (_cursor.isNull(_cursorIndexOfPhotoPath)) {
              _tmpPhotoPath = null;
            } else {
              _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new VehicleEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpOwnerName,_tmpPlateNumber,_tmpBrand,_tmpModel,_tmpYear,_tmpColor,_tmpPhotoPath,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<VehicleEntity>> getVehiclesByUserIdFlow(final long userId) {
    final String _sql = "SELECT * FROM vehicles WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vehicles"}, new Callable<List<VehicleEntity>>() {
      @Override
      @NonNull
      public List<VehicleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPlateNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "plateNumber");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<VehicleEntity> _result = new ArrayList<VehicleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VehicleEntity _item;
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpPlateNumber;
            _tmpPlateNumber = _cursor.getString(_cursorIndexOfPlateNumber);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpModel;
            _tmpModel = _cursor.getString(_cursorIndexOfModel);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpPhotoPath;
            if (_cursor.isNull(_cursorIndexOfPhotoPath)) {
              _tmpPhotoPath = null;
            } else {
              _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new VehicleEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpOwnerName,_tmpPlateNumber,_tmpBrand,_tmpModel,_tmpYear,_tmpColor,_tmpPhotoPath,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getVehiclesByUserId(final long userId,
      final Continuation<? super List<VehicleEntity>> $completion) {
    final String _sql = "SELECT * FROM vehicles WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VehicleEntity>>() {
      @Override
      @NonNull
      public List<VehicleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPlateNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "plateNumber");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<VehicleEntity> _result = new ArrayList<VehicleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VehicleEntity _item;
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpPlateNumber;
            _tmpPlateNumber = _cursor.getString(_cursorIndexOfPlateNumber);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpModel;
            _tmpModel = _cursor.getString(_cursorIndexOfModel);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpPhotoPath;
            if (_cursor.isNull(_cursorIndexOfPhotoPath)) {
              _tmpPhotoPath = null;
            } else {
              _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new VehicleEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpOwnerName,_tmpPlateNumber,_tmpBrand,_tmpModel,_tmpYear,_tmpColor,_tmpPhotoPath,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> getVehicleCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM vehicles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vehicles"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUnsyncedVehicles(final Continuation<? super List<VehicleEntity>> $completion) {
    final String _sql = "SELECT * FROM vehicles WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VehicleEntity>>() {
      @Override
      @NonNull
      public List<VehicleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfPlateNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "plateNumber");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfPhotoPath = CursorUtil.getColumnIndexOrThrow(_cursor, "photoPath");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<VehicleEntity> _result = new ArrayList<VehicleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VehicleEntity _item;
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final Long _tmpServerId;
            if (_cursor.isNull(_cursorIndexOfServerId)) {
              _tmpServerId = null;
            } else {
              _tmpServerId = _cursor.getLong(_cursorIndexOfServerId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpPlateNumber;
            _tmpPlateNumber = _cursor.getString(_cursorIndexOfPlateNumber);
            final String _tmpBrand;
            _tmpBrand = _cursor.getString(_cursorIndexOfBrand);
            final String _tmpModel;
            _tmpModel = _cursor.getString(_cursorIndexOfModel);
            final int _tmpYear;
            _tmpYear = _cursor.getInt(_cursorIndexOfYear);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpPhotoPath;
            if (_cursor.isNull(_cursorIndexOfPhotoPath)) {
              _tmpPhotoPath = null;
            } else {
              _tmpPhotoPath = _cursor.getString(_cursorIndexOfPhotoPath);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new VehicleEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpOwnerName,_tmpPlateNumber,_tmpBrand,_tmpModel,_tmpYear,_tmpColor,_tmpPhotoPath,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
