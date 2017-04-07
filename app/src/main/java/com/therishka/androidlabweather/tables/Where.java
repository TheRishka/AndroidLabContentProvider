package com.therishka.androidlabweather.tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rishad Mustafaev
 */
public class Where {

    private final StringBuilder mWhereBuilder;

    private final List<Object> mBindValues;

    private String mLimit;
    private String mOffset;

    private Where() {
        mWhereBuilder = new StringBuilder();
        mBindValues = new ArrayList<>();
    }

    @NonNull
    public static Where create() {
        return new Where();
    }

    @NonNull
    public Where where(@NonNull String where, Object... values) {
        mWhereBuilder.append(where);
        Collections.addAll(mBindValues, values);
        return this;
    }

    @NonNull
    public Where equalTo(@NonNull String column, @NonNull Object value) {
        return where(column, " = ?", value);
    }

    @NonNull
    public Where notEqualTo(@NonNull String column, @NonNull Object value) {
        return where(column, " <> ?", value);
    }

    @NonNull
    public Where lessThan(@NonNull String column, @NonNull Object value) {
        return where(column, " < ?", value);
    }

    @NonNull
    public Where lessThanOrEqualTo(@NonNull String column, @NonNull Object value) {
        return where(column, " <= ?", value);
    }

    @NonNull
    public Where greaterThan(@NonNull String column, @NonNull Object value) {
        return where(column, " > ?", value);
    }

    @NonNull
    public Where greaterThanOrEqualTo(@NonNull String column, @NonNull Object value) {
        return where(column, " >= ?", value);
    }

    @NonNull
    public Where like(@NonNull String column, @NonNull Object value) {
        return where(column, " LIKE ?", value);
    }

    @NonNull
    public Where between(@NonNull String column, @NonNull Object first, @NonNull Object second) {
        return where(column, " BETWEEN ? AND ?", first, second);
    }

    @NonNull
    public Where isNull(@NonNull String column) {
        return where(column, " IS NULL");
    }

    @NonNull
    public Where notNull(@NonNull String column) {
        return where(column, " NOT NULL");
    }

    @NonNull
    public Where in(@NonNull String column, @NonNull Object... values) {
        final int last = values.length - 1;
        mWhereBuilder.append(column).append(" IN(");
        for (int i = 0; i < values.length; ++i) {
            mWhereBuilder.append("?");
            if (i < last) {
                mWhereBuilder.append(", ");
            }
        }
        mWhereBuilder.append(")");
        Collections.addAll(mBindValues, values);
        return this;
    }

    @NonNull
    public Where beginGroup() {
        mWhereBuilder.append("(");
        return this;
    }

    @NonNull
    public Where endGroup() {
        mWhereBuilder.append(")");
        return this;
    }

    @NonNull
    public Where and() {
        mWhereBuilder.append(" AND ");
        return this;
    }

    @NonNull
    public Where or() {
        mWhereBuilder.append(" OR ");
        return this;
    }

    @NonNull
    public Where limit(int limit) {
        mLimit = String.valueOf(limit);
        return this;
    }

    @NonNull
    public Where offset(int offset) {
        mOffset = String.valueOf(offset);
        return this;
    }

    @Nullable
    public String where() {
        if (mWhereBuilder.length() == 0) {
            return null;
        }
        return mWhereBuilder.toString();
    }

    @Nullable
    public String[] whereArgs() {
        if (mBindValues.isEmpty()) {
            return null;
        }
        String[] args = new String[mBindValues.size()];
        for (int i = 0; i < mBindValues.size(); i++) {
            args[i] = String.valueOf(mBindValues.get(i));
        }
        return args;
    }

    @Nullable
    public String limit() {
        String result = "";
        if (mLimit != null && !mLimit.isEmpty()) {
            result += " LIMIT " + mLimit;
        }
        if (mOffset != null && !mOffset.isEmpty()) {
            result += " OFFSET " + mOffset;
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    @NonNull
    private Where where(@NonNull String column, @NonNull String operand, @NonNull Object... values) {
        mWhereBuilder.append(column).append(operand);
        Collections.addAll(mBindValues, values);
        return this;
    }
}
