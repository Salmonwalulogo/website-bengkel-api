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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.bengkel.app.data.local.entity.TransactionDetailEntity;
import com.bengkel.app.data.local.entity.TransactionEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionEntity> __insertionAdapterOfTransactionEntity;

  private final EntityInsertionAdapter<TransactionDetailEntity> __insertionAdapterOfTransactionDetailEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __updateAdapterOfTransactionEntity;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionEntity = new EntityInsertionAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`localId`,`serverId`,`userId`,`customerId`,`customerName`,`subtotal`,`discount`,`tax`,`totalPayment`,`paidAmount`,`changeAmount`,`status`,`paymentMethod`,`createdAt`,`isSynced`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindString(1, entity.getLocalId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getServerId());
        }
        statement.bindLong(3, entity.getUserId());
        if (entity.getCustomerId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCustomerId());
        }
        if (entity.getCustomerName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCustomerName());
        }
        statement.bindDouble(6, entity.getSubtotal());
        statement.bindDouble(7, entity.getDiscount());
        statement.bindDouble(8, entity.getTax());
        statement.bindDouble(9, entity.getTotalPayment());
        statement.bindDouble(10, entity.getPaidAmount());
        statement.bindDouble(11, entity.getChangeAmount());
        statement.bindString(12, entity.getStatus());
        statement.bindString(13, entity.getPaymentMethod());
        statement.bindString(14, entity.getCreatedAt());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(15, _tmp);
      }
    };
    this.__insertionAdapterOfTransactionDetailEntity = new EntityInsertionAdapter<TransactionDetailEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transaction_details` (`localId`,`transactionLocalId`,`productId`,`itemName`,`quantity`,`price`,`subtotal`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionDetailEntity entity) {
        statement.bindString(1, entity.getLocalId());
        statement.bindString(2, entity.getTransactionLocalId());
        if (entity.getProductId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getProductId());
        }
        statement.bindString(4, entity.getItemName());
        statement.bindLong(5, entity.getQuantity());
        statement.bindDouble(6, entity.getPrice());
        statement.bindDouble(7, entity.getSubtotal());
      }
    };
    this.__updateAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `localId` = ?,`serverId` = ?,`userId` = ?,`customerId` = ?,`customerName` = ?,`subtotal` = ?,`discount` = ?,`tax` = ?,`totalPayment` = ?,`paidAmount` = ?,`changeAmount` = ?,`status` = ?,`paymentMethod` = ?,`createdAt` = ?,`isSynced` = ? WHERE `localId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindString(1, entity.getLocalId());
        if (entity.getServerId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getServerId());
        }
        statement.bindLong(3, entity.getUserId());
        if (entity.getCustomerId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getCustomerId());
        }
        if (entity.getCustomerName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCustomerName());
        }
        statement.bindDouble(6, entity.getSubtotal());
        statement.bindDouble(7, entity.getDiscount());
        statement.bindDouble(8, entity.getTax());
        statement.bindDouble(9, entity.getTotalPayment());
        statement.bindDouble(10, entity.getPaidAmount());
        statement.bindDouble(11, entity.getChangeAmount());
        statement.bindString(12, entity.getStatus());
        statement.bindString(13, entity.getPaymentMethod());
        statement.bindString(14, entity.getCreatedAt());
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(15, _tmp);
        statement.bindString(16, entity.getLocalId());
      }
    };
  }

  @Override
  public Object insertTransaction(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionEntity.insert(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTransactions(final List<TransactionEntity> transactions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionEntity.insert(transactions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTransactionDetails(final List<TransactionDetailEntity> details,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTransactionDetailEntity.insert(details);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransaction(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransactionEntity>> getAllTransactionsFlow() {
    final String _sql = "SELECT * FROM transactions ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPayment");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "changeAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
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
            final Long _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpDiscount;
            _tmpDiscount = _cursor.getDouble(_cursorIndexOfDiscount);
            final double _tmpTax;
            _tmpTax = _cursor.getDouble(_cursorIndexOfTax);
            final double _tmpTotalPayment;
            _tmpTotalPayment = _cursor.getDouble(_cursorIndexOfTotalPayment);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final double _tmpChangeAmount;
            _tmpChangeAmount = _cursor.getDouble(_cursorIndexOfChangeAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new TransactionEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpCustomerId,_tmpCustomerName,_tmpSubtotal,_tmpDiscount,_tmpTax,_tmpTotalPayment,_tmpPaidAmount,_tmpChangeAmount,_tmpStatus,_tmpPaymentMethod,_tmpCreatedAt,_tmpIsSynced);
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
  public Flow<List<TransactionEntity>> getTransactionsByCustomerIdFlow(final long customerId) {
    final String _sql = "SELECT * FROM transactions WHERE customerId = ? OR userId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, customerId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, customerId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPayment");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "changeAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
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
            final Long _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpDiscount;
            _tmpDiscount = _cursor.getDouble(_cursorIndexOfDiscount);
            final double _tmpTax;
            _tmpTax = _cursor.getDouble(_cursorIndexOfTax);
            final double _tmpTotalPayment;
            _tmpTotalPayment = _cursor.getDouble(_cursorIndexOfTotalPayment);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final double _tmpChangeAmount;
            _tmpChangeAmount = _cursor.getDouble(_cursorIndexOfChangeAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new TransactionEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpCustomerId,_tmpCustomerName,_tmpSubtotal,_tmpDiscount,_tmpTax,_tmpTotalPayment,_tmpPaidAmount,_tmpChangeAmount,_tmpStatus,_tmpPaymentMethod,_tmpCreatedAt,_tmpIsSynced);
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
  public Object getTransactionByLocalId(final String localId,
      final Continuation<? super TransactionEntity> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE localId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, localId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransactionEntity>() {
      @Override
      @Nullable
      public TransactionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPayment");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "changeAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final TransactionEntity _result;
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
            final Long _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpDiscount;
            _tmpDiscount = _cursor.getDouble(_cursorIndexOfDiscount);
            final double _tmpTax;
            _tmpTax = _cursor.getDouble(_cursorIndexOfTax);
            final double _tmpTotalPayment;
            _tmpTotalPayment = _cursor.getDouble(_cursorIndexOfTotalPayment);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final double _tmpChangeAmount;
            _tmpChangeAmount = _cursor.getDouble(_cursorIndexOfChangeAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _result = new TransactionEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpCustomerId,_tmpCustomerName,_tmpSubtotal,_tmpDiscount,_tmpTax,_tmpTotalPayment,_tmpPaidAmount,_tmpChangeAmount,_tmpStatus,_tmpPaymentMethod,_tmpCreatedAt,_tmpIsSynced);
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
  public Object getDetailsForTransaction(final String transactionLocalId,
      final Continuation<? super List<TransactionDetailEntity>> $completion) {
    final String _sql = "SELECT * FROM transaction_details WHERE transactionLocalId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, transactionLocalId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TransactionDetailEntity>>() {
      @Override
      @NonNull
      public List<TransactionDetailEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfTransactionLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionLocalId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final List<TransactionDetailEntity> _result = new ArrayList<TransactionDetailEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionDetailEntity _item;
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpTransactionLocalId;
            _tmpTransactionLocalId = _cursor.getString(_cursorIndexOfTransactionLocalId);
            final Long _tmpProductId;
            if (_cursor.isNull(_cursorIndexOfProductId)) {
              _tmpProductId = null;
            } else {
              _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            }
            final String _tmpItemName;
            _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            _item = new TransactionDetailEntity(_tmpLocalId,_tmpTransactionLocalId,_tmpProductId,_tmpItemName,_tmpQuantity,_tmpPrice,_tmpSubtotal);
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
  public Flow<List<TransactionDetailEntity>> getDetailsForTransactionFlow(
      final String transactionLocalId) {
    final String _sql = "SELECT * FROM transaction_details WHERE transactionLocalId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, transactionLocalId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transaction_details"}, new Callable<List<TransactionDetailEntity>>() {
      @Override
      @NonNull
      public List<TransactionDetailEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfTransactionLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "transactionLocalId");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfItemName = CursorUtil.getColumnIndexOrThrow(_cursor, "itemName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final List<TransactionDetailEntity> _result = new ArrayList<TransactionDetailEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionDetailEntity _item;
            final String _tmpLocalId;
            _tmpLocalId = _cursor.getString(_cursorIndexOfLocalId);
            final String _tmpTransactionLocalId;
            _tmpTransactionLocalId = _cursor.getString(_cursorIndexOfTransactionLocalId);
            final Long _tmpProductId;
            if (_cursor.isNull(_cursorIndexOfProductId)) {
              _tmpProductId = null;
            } else {
              _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            }
            final String _tmpItemName;
            _tmpItemName = _cursor.getString(_cursorIndexOfItemName);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            _item = new TransactionDetailEntity(_tmpLocalId,_tmpTransactionLocalId,_tmpProductId,_tmpItemName,_tmpQuantity,_tmpPrice,_tmpSubtotal);
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
  public Flow<Integer> getTransactionCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM transactions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Integer>() {
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
  public Flow<Double> getTotalIncomeFlow() {
    final String _sql = "SELECT SUM(totalPayment) FROM transactions WHERE status = 'completed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
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
  public Object getUnsyncedTransactions(
      final Continuation<? super List<TransactionEntity>> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalId = CursorUtil.getColumnIndexOrThrow(_cursor, "localId");
          final int _cursorIndexOfServerId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfCustomerId = CursorUtil.getColumnIndexOrThrow(_cursor, "customerId");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalPayment = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPayment");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paidAmount");
          final int _cursorIndexOfChangeAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "changeAmount");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
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
            final Long _tmpCustomerId;
            if (_cursor.isNull(_cursorIndexOfCustomerId)) {
              _tmpCustomerId = null;
            } else {
              _tmpCustomerId = _cursor.getLong(_cursorIndexOfCustomerId);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final double _tmpSubtotal;
            _tmpSubtotal = _cursor.getDouble(_cursorIndexOfSubtotal);
            final double _tmpDiscount;
            _tmpDiscount = _cursor.getDouble(_cursorIndexOfDiscount);
            final double _tmpTax;
            _tmpTax = _cursor.getDouble(_cursorIndexOfTax);
            final double _tmpTotalPayment;
            _tmpTotalPayment = _cursor.getDouble(_cursorIndexOfTotalPayment);
            final double _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getDouble(_cursorIndexOfPaidAmount);
            final double _tmpChangeAmount;
            _tmpChangeAmount = _cursor.getDouble(_cursorIndexOfChangeAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            _item = new TransactionEntity(_tmpLocalId,_tmpServerId,_tmpUserId,_tmpCustomerId,_tmpCustomerName,_tmpSubtotal,_tmpDiscount,_tmpTax,_tmpTotalPayment,_tmpPaidAmount,_tmpChangeAmount,_tmpStatus,_tmpPaymentMethod,_tmpCreatedAt,_tmpIsSynced);
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
